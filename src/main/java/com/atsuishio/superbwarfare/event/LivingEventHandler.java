package com.atsuishio.superbwarfare.event;

import com.atsuishio.superbwarfare.api.event.PreKillEvent;
import com.atsuishio.superbwarfare.capability.LaserCapability;
import com.atsuishio.superbwarfare.event.custom.PreKillCallback;
import com.atsuishio.superbwarfare.capability.ModCapabilities;
import com.atsuishio.superbwarfare.capability.player.PlayerVariable;
import com.atsuishio.superbwarfare.config.common.GameplayConfig;
import com.atsuishio.superbwarfare.config.server.MiscConfig;
import com.atsuishio.superbwarfare.config.server.VehicleConfig;
import com.atsuishio.superbwarfare.data.gun.Ammo;
import com.atsuishio.superbwarfare.data.gun.GunData;
import com.atsuishio.superbwarfare.data.gun.value.ReloadState;
import com.atsuishio.superbwarfare.entity.TargetEntity;
import com.atsuishio.superbwarfare.entity.mixin.ExplosionAccess;
import com.atsuishio.superbwarfare.entity.mixin.ICustomKnockback;
import com.atsuishio.superbwarfare.entity.vehicle.base.AutoAimableEntity;
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.atsuishio.superbwarfare.event.custom.*;
import com.atsuishio.superbwarfare.init.*;
import com.atsuishio.superbwarfare.item.gun.GunItem;
import com.atsuishio.superbwarfare.network.NetworkRegistry;
import com.atsuishio.superbwarfare.network.message.receive.ClientIndicatorMessage;
import com.atsuishio.superbwarfare.network.message.receive.DrawClientMessage;
import com.atsuishio.superbwarfare.network.message.receive.LivingGunKillMessage;
import com.atsuishio.superbwarfare.perk.Perk;
import com.atsuishio.superbwarfare.tools.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

import static com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity.AI_PASSENGER_WEAPON_TARGET_UUID;
import static com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity.AI_TURRET_TARGET_UUID;

public class LivingEventHandler {

    public static void registerEvents() {
        LivingAttackCallback.EVENT.register(LivingEventHandler::onEntityAttacked);

        LivingHurtCallback.EVENT.register(event -> {
            float modified = onEntityHurt(event.getEntity(), event.getSource(), event.getAmount());
            event.setAmount(modified);
        });

        LivingDeathCallback.EVENT.register(event -> {
            onEntityDeath(event.getEntity(), event.getSource());
        });

        LivingDropsCallback.EVENT.register(event -> {
            onLivingDrops(event.getEntity(), event.getSource(), event.getDrops());
        });

        LivingExperienceDropCallback.EVENT.register(event -> {
            if (event.getAttackingPlayer() != null) {
                boolean handled = onLivingExperienceDrop(event.getAttackingPlayer(), event.getDroppedExperience());
                if (handled) {
                    event.setCanceled(true);
                }
            }
        });

        PreKillCallback.EVENT.register(event -> {
            if (event instanceof PreKillEvent.SendKillMessage sendKillMessage
                    && onPreSendKillMessage(sendKillMessage)) {
                event.setCanceled(true);
            } else if (event instanceof PreKillEvent.Indicator indicator
                    && onPreIndicator(indicator)) {
                event.setCanceled(true);
            }
        });
    }

    public static void onLivingChangeTargetEvent(Mob mob, LivingEntity newTarget) {
        if (mob.getVehicle() instanceof VehicleEntity vehicle) {
            if (mob == vehicle.getNthEntity(vehicle.getTurretControllerIndex())) {
                if (newTarget != null) {
                    vehicle.getEntityData().set(AI_TURRET_TARGET_UUID, newTarget.getStringUUID());
                } else {
                    vehicle.getEntityData().set(AI_TURRET_TARGET_UUID, "undefined");
                }
            }

            if (mob == vehicle.getNthEntity(vehicle.getPassengerWeaponStationControllerIndex())) {
                if (newTarget != null) {
                    vehicle.getEntityData().set(AI_PASSENGER_WEAPON_TARGET_UUID, newTarget.getStringUUID());
                } else {
                    vehicle.getEntityData().set(AI_PASSENGER_WEAPON_TARGET_UUID, "undefined");
                }
            }
        }
    }

    // TODO: Register in Mod.java using Fabric event API
    public static boolean onEntityAttacked(LivingEntity entity, DamageSource source, float amount) {
        if (!source.is(ModDamageTypes.VEHICLE_EXPLOSION)
                && entity.getVehicle() instanceof VehicleEntity vehicle
                && vehicle.isEnclosed(entity)
        ) {
            if (!source.is(ModTags.DamageTypes.VEHICLE_NOT_ABSORB)) {
                vehicle.hurt(source, amount);
            }
            return false;
        }
        return true;
    }

    // TODO: Register in Mod.java using Fabric event API
    public static float onEntityHurt(LivingEntity entity, DamageSource source, float amount) {
        if (entity == null) {
            return amount;
        }

        amount = handleVehicleHurt(entity, source, amount);
        amount = handleGunPerksWhenHurt(entity, source, amount);
        renderDamageIndicator(source, entity);
        amount = reduceDamage(entity, source, amount);
        giveExpToWeapon(entity, source, amount);
        handleGunLevels(entity, source, amount);
        return amount;
    }

    // TODO: Register in Mod.java using Fabric event API
    public static void onEntityDeath(LivingEntity entity, DamageSource source) {
        if (entity == null) {
            return;
        }

        killIndication(entity, source);
        handleGunPerksWhenDeath(entity, source);
        handlePlayerKillEntity(entity, source);
        giveKillExpToWeapon(entity, source);

        if (entity instanceof Player player) {
            handlePlayerBeamReset(player);
        }
    }

    public static float handleVehicleHurt(LivingEntity passenger, DamageSource source, float amount) {
        var vehicleEntity = passenger.getVehicle();
        if (vehicleEntity instanceof VehicleEntity vehicle) {
            if (source.is(ModTags.DamageTypes.VEHICLE_IGNORE)) return amount;

            if (vehicle.isEnclosed(passenger)) {
                if (!source.is(ModDamageTypes.VEHICLE_EXPLOSION)) {
                    return 0;
                }
            } else {
                if (!source.is(ModTags.DamageTypes.VEHICLE_NOT_ABSORB)) {
                    vehicleEntity.hurt(source, 0.7f * amount);
                }
                return 0.3f * amount;
            }
        }
        return amount;
    }

    /**
     * 计算伤害减免
     */
    private static float reduceDamage(LivingEntity entity, DamageSource source, float amount) {
        if (entity == null) return amount;
        Entity sourceEntity = source.getEntity();
        if (sourceEntity == null) return amount;
        if (sourceEntity.level().isClientSide) return amount;

        double damage = amount;

        ItemStack stack = sourceEntity instanceof LivingEntity living ? living.getMainHandItem() : ItemStack.EMPTY;

        if (DamageTypeTool.isGunDamage(source) && stack.getItem() instanceof GunItem) {
            var data = GunData.from(stack);
            double distance = entity.position().distanceTo(sourceEntity.position());
            damage = reduceDamageByDistance(amount, distance, data.getDamageReduceRate(), data.getDamageReduceMinDistance());
        }

        ItemStack armor = entity.getItemBySlot(EquipmentSlot.CHEST);

        if (armor != ItemStack.EMPTY && armor.getTag() != null && armor.getTag().contains("ArmorPlate")) {
            double armorValue = armor.getOrCreateTag().getDouble("ArmorPlate");
            armor.getOrCreateTag().putDouble("ArmorPlate", Math.max(armorValue - damage, 0));
            damage = Math.max(damage - armorValue, 0);
        }

        var bulletResistance = entity.getAttribute(ModAttributes.BULLET_RESISTANCE.get());
        double bulletResistVal = bulletResistance != null ? bulletResistance.getValue() : 0;

        if (source.is(ModTags.DamageTypes.PROJECTILE) || source.is(DamageTypes.MOB_PROJECTILE)) {
            damage *= 1 - 0.8 * Mth.clamp(bulletResistVal, 0, 1);
        }

        if (source.is(ModTags.DamageTypes.PROJECTILE_ABSOLUTE)) {
            damage *= 1 - 0.2 * Mth.clamp(bulletResistVal, 0, 1);
        }

        if (source.is(ModDamageTypes.PROJECTILE_EXPLOSION) || source.is(ModDamageTypes.MINE) || source.is(ModDamageTypes.PROJECTILE_HIT) || source.is(ModDamageTypes.CUSTOM_EXPLOSION)
                || source.is(DamageTypes.EXPLOSION) || source.is(DamageTypes.PLAYER_EXPLOSION)) {
            damage *= 1 - 0.3 * Mth.clamp(bulletResistVal, 0, 1);
        }

        if (entity instanceof TargetEntity && sourceEntity instanceof Player player) {
            if (source.is(ModDamageTypes.BEAST)) {
                damage = Float.POSITIVE_INFINITY;
            }

            player.displayClientMessage(Component.translatable("tips.superbwarfare.target.damage",
                    FormatTool.format2D(damage),
                    FormatTool.format1D(entity.position().distanceTo(sourceEntity.position()), "m")), false);
        }

        return (float) damage;
    }

    private static double reduceDamageByDistance(double amount, double distance, double rate, double minDistance) {
        return amount / (1 + rate * Math.max(0, distance - minDistance));
    }

    private static void giveExpToWeapon(LivingEntity entity, DamageSource source, float amount) {
        if (source == null) return;
        Entity sourceEntity = source.getEntity();
        if (!(sourceEntity instanceof Player player)) return;
        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof GunItem)) return;
        if (entity.getType().is(ModTags.EntityTypes.NO_EXPERIENCE)) return;

        var data = GunData.from(stack);
        double expAmount = Math.min(0.125 * amount, entity.getMaxHealth());

        if (source.is(ModDamageTypes.PROJECTILE_EXPLOSION)) {
            if (data.compute().explosionDamage > 0 || GunData.from(stack).perk.getLevel(ModPerks.HE_BULLET) > 0) {
                data.exp.set(data.exp.get() + expAmount);
            }
        }

        if (!DamageTypeTool.isGunDamage(source)) return;

        data.exp.set(data.exp.get() + expAmount);
        data.save();
    }

    private static void giveKillExpToWeapon(LivingEntity entity, DamageSource source) {
        if (source == null) return;
        Entity sourceEntity = source.getEntity();
        if (!(sourceEntity instanceof Player player)) return;
        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof GunItem)) return;
        if (entity.getType().is(ModTags.EntityTypes.NO_EXPERIENCE)) return;

        var data = GunData.from(stack);
        double amount = 20 + 2 * entity.getMaxHealth();

        if (source.is(ModDamageTypes.PROJECTILE_EXPLOSION)) {
            if (data.compute().explosionDamage > 0 || GunData.from(stack).perk.getLevel(ModPerks.HE_BULLET) > 0) {
                data.exp.add(amount);
            }
        }

        if (DamageTypeTool.isGunDamage(source)) {
            data.exp.add(amount);
        }

        int level = data.level.get();
        double exp = data.exp.get();
        double upgradeExpNeeded = 20 * Math.pow(level, 2) + 160 * level + 20;

        while (exp >= upgradeExpNeeded) {
            exp -= upgradeExpNeeded;
            level = data.level.get() + 1;
            upgradeExpNeeded = 20 * Math.pow(level, 2) + 160 * level + 20;
            data.exp.set(exp);
            data.level.set(level);
        }
        data.save();
    }

    private static void handleGunLevels(LivingEntity entity, DamageSource source, float amount) {
        if (source == null) return;
        Entity sourceEntity = source.getEntity();
        if (!(sourceEntity instanceof Player player)) return;
        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof GunItem)) return;
        if (entity.getType().is(ModTags.EntityTypes.NO_EXPERIENCE)) return;

        var data = GunData.from(stack);
        int level = data.level.get();
        double exp = data.exp.get();
        double upgradeExpNeeded = 20 * Math.pow(level, 2) + 160 * level + 20;

        while (exp >= upgradeExpNeeded) {
            exp -= upgradeExpNeeded;
            level = data.level.get() + 1;
            upgradeExpNeeded = 20 * Math.pow(level, 2) + 160 * level + 20;
            data.exp.set(exp);
            data.level.set(level);
        }
        data.save();
    }

    private static void killIndication(LivingEntity killedEntity, DamageSource source) {
        if (!MiscConfig.SEND_KILL_FEEDBACK.get()) return;

        var sourceEntity = source.getEntity();
        if (sourceEntity == null) {
            return;
        }

        if (!GameplayConfig.GLOBAL_INDICATION.get() && !DamageTypeTool.isModDamage(source)) {
            return;
        }

        if (!sourceEntity.level().isClientSide() && sourceEntity instanceof ServerPlayer player) {
            if (PreKillCallback.post(new PreKillEvent.Indicator(player, source, killedEntity))) {
                return;
            }

            SoundTool.playLocalSound(player, ModSounds.TARGET_DOWN.get(), 3f, 1f);

            NetworkRegistry.sendToPlayer(player, new ClientIndicatorMessage(2, 8));
        }
    }

    private static void renderDamageIndicator(DamageSource damagesource, LivingEntity entity) {
        if (entity == null) return;

        var sourceEntity = damagesource.getEntity();

        if (sourceEntity == null) {
            return;
        }

        if (sourceEntity instanceof ServerPlayer player && (damagesource.is(DamageTypes.EXPLOSION) || damagesource.is(DamageTypes.PLAYER_EXPLOSION)
                || damagesource.is(ModDamageTypes.MINE) || damagesource.is(ModDamageTypes.PROJECTILE_EXPLOSION))) {
            SoundTool.playLocalSound(player, ModSounds.INDICATION.get(), 1f, 1f);

            NetworkRegistry.sendToPlayer(player, new ClientIndicatorMessage(0, 5));
        }
    }

    // TODO: Register in Mod.java using Fabric event API
    public static void handleChangeSlot(Player player, EquipmentSlot slot, ItemStack from, ItemStack to) {
        if (slot != EquipmentSlot.MAINHAND || player.level().isClientSide) {
            return;
        }

        ItemStack oldStack = from;
        ItemStack newStack = to;

        ModCapabilities.LASER_CAPABILITY.maybeGet(player).ifPresent(LaserCapability.ILaserCapability::stop);

        if (player instanceof ServerPlayer serverPlayer) {
            if (newStack.getItem() instanceof GunItem) {
                checkCopyGuns(newStack, player);
            }

            if (newStack.getItem() != oldStack.getItem()
                    || newStack.getTag() == null || oldStack.getTag() == null
                    || (newStack.getItem() instanceof GunItem && !GunData.from(newStack).initialized())
                    || (oldStack.getItem() instanceof GunItem && !GunData.from(oldStack).initialized())
                    || (newStack.getItem() instanceof GunItem && oldStack.getItem() instanceof GunItem && !Objects.equals(GunsTool.getGunUUID(newStack), GunsTool.getGunUUID(oldStack)))
            ) {
                NetworkRegistry.sendToPlayer(serverPlayer, DrawClientMessage.INSTANCE);

                if (oldStack.getItem() instanceof GunItem oldGun) {
                    var oldData = GunData.from(oldStack);

                    stopGunReloadSound(serverPlayer, oldData);

                    if (oldData.compute().boltActionTime > 0) {
                        oldData.bolt.actionTimer.reset();
                    }

                    oldData.reload.setTime(0);

                    oldData.reload.setState(ReloadState.NOT_RELOADING);

                    if (oldData.compute().iterativeTime != 0) {
                        oldData.stopped.set(false);
                        oldData.forceStop.set(false);
                        oldData.reload.setStage(0);
                        oldData.reload.prepareTimer.reset();
                        oldData.reload.prepareLoadTimer.reset();
                        oldData.reload.iterativeLoadTimer.reset();
                        oldData.reload.finishTimer.reset();
                    }

                    if (oldStack.is(ModItems.SENTINEL.get())) {
                        oldData.charge.timer.reset();
                    }

                    oldGun.onChangeSlot(oldData, player);
                    oldData.save();
                }

                if (newStack.getItem() instanceof GunItem) {
                    var newData = GunData.from(newStack);

                    if (newData.compute().boltActionTime > 0) {
                        newData.bolt.actionTimer.reset();
                    }

                    newData.reload.setState(ReloadState.NOT_RELOADING);
                    newData.reload.reloadTimer.reset();

                    if (newData.compute().iterativeTime != 0) {
                        newData.forceStop.set(false);
                        newData.stopped.set(false);
                        newData.reload.setStage(0);
                        newData.reload.prepareTimer.reset();
                        newData.reload.prepareLoadTimer.reset();
                        newData.reload.iterativeLoadTimer.reset();
                        newData.reload.finishTimer.reset();
                    }

                    if (newStack.is(ModItems.SENTINEL.get())) {
                        newData.charge.timer.reset();
                    }

                    for (Perk.Type type : Perk.Type.values()) {
                        var instance = newData.perk.getInstance(type);
                        if (instance != null) {
                            instance.perk().onChangeSlot(newData, instance, player);
                        }
                    }

                    newData.save();
                }
            }
        }
    }

    private static void checkCopyGuns(ItemStack stack, Player player) {
        var data = GunData.from(stack);
        if (!data.initialized()) return;
        if (data.gunDataTag == null) return;
        var uuid = data.gunDataTag.getUUID("UUID");

        for (var item : player.getInventory().items) {
            if (item.equals(stack)) continue;
            if (item.getItem() instanceof GunItem) {
                var itemData = GunData.from(item);
                var dataTag = itemData.gunDataTag;
                if (dataTag == null) continue;
                if (!dataTag.hasUUID("UUID")) continue;
                if (dataTag.getUUID("UUID").equals(uuid)) {
                    data.gunDataTag.putUUID("UUID", UUID.randomUUID());
                    return;
                }
            }
        }
    }

    public static void stopGunReloadSound(ServerPlayer player, GunData data) {
        var soundInfo = data.compute().soundInfo;
        soundInfo.cancellableSounds.list
                .forEach(str -> {
                    var location = ResourceLocation.tryParse(str);
                    if (location != null) {
                        player.connection.send(new ClientboundStopSoundPacket(location, SoundSource.PLAYERS));
                    }
                });
    }

    private static void handlePlayerKillEntity(LivingEntity entity, DamageSource source) {
        ResourceKey<DamageType> damageTypeResourceKey = source.typeHolder().unwrapKey().isPresent() ? source.typeHolder().unwrapKey().get() : DamageTypes.GENERIC;

        LivingEntity attacker = null;
        if (source.getEntity() instanceof LivingEntity living) {
            if (living instanceof ServerPlayer player) {
                attacker = player;
            } else {
                attacker = living;
            }
        }
        if (source.getDirectEntity() instanceof Projectile projectile && projectile.getOwner() instanceof LivingEntity living) {
            if (living instanceof ServerPlayer player) {
                attacker = player;
            } else if (living instanceof OwnableEntity ownableEntity && ownableEntity.getOwner() instanceof ServerPlayer) {
                attacker = living;
            }
        }

        if (attacker != null && PreKillCallback.post(new PreKillEvent.SendKillMessage(attacker, source, entity))) {
            return;
        }

        if (attacker != null && MiscConfig.SEND_KILL_FEEDBACK.get()) {
            if (DamageTypeTool.isHeadshotDamage(source)) {
                NetworkRegistry.sendToAll(new LivingGunKillMessage(attacker.getId(), entity.getId(), true, damageTypeResourceKey));
            } else {
                NetworkRegistry.sendToAll(new LivingGunKillMessage(attacker.getId(), entity.getId(), false, damageTypeResourceKey));
            }
        }
    }

    // TODO: Register in Mod.java using Fabric event API
    public static float handleGunPerksWhenHurt(LivingEntity target, DamageSource source, float amount) {
        if (!DamageTypeTool.isGunDamage(source) && !source.is(DamageTypes.PLAYER_ATTACK)) return amount;

        LivingEntity attacker = null;
        if (source.getEntity() instanceof LivingEntity living) {
            attacker = living;
        }
        if (source.getDirectEntity() instanceof Projectile projectile && projectile.getOwner() instanceof LivingEntity living) {
            attacker = living;
        }
        if (attacker == null) {
            return amount;
        }

        ItemStack stack = attacker.getMainHandItem();
        if (!(stack.getItem() instanceof GunItem)) {
            return amount;
        }

        float damage = amount;

        GunData data = GunData.from(stack);
        for (Perk.Type type : Perk.Type.values()) {
            var instance = data.perk.getInstance(type);
            if (instance != null) {
                if (DamageTypeTool.isGunDamage(source)) {
                    damage = instance.perk().getModifiedDamage(damage, data, instance, target, source);
                    instance.perk().onHurtEntity(damage, data, instance, target, source);
                } else if (source.is(DamageTypes.PLAYER_ATTACK)) {
                    instance.perk().onMeleeAttack(data, instance, target);
                }
            }
        }

        return damage;
    }

    private static void handleGunPerksWhenDeath(LivingEntity deadEntity, DamageSource source) {
        if (!DamageTypeTool.isGunDamage(source)) return;

        LivingEntity attacker = null;
        if (source.getEntity() instanceof LivingEntity living) {
            attacker = living;
        }
        if (source.getDirectEntity() instanceof Projectile projectile && projectile.getOwner() instanceof LivingEntity living) {
            attacker = living;
        }
        if (attacker == null) {
            return;
        }

        ItemStack stack = attacker.getMainHandItem();
        if (!(stack.getItem() instanceof GunItem)) {
            return;
        }

        GunData data = GunData.from(stack);
        for (Perk.Type type : Perk.Type.values()) {
            var instance = data.perk.getInstance(type);
            if (instance != null) {
                instance.perk().onKill(data, instance, deadEntity, source);
            }
        }
    }

    // TODO: Register in Mod.java using Fabric event API
    public static boolean onPickup(Player player, ItemEntity item) {
        if (!VehicleConfig.VEHICLE_ITEM_PICKUP.get()) return false;
        if (player.getVehicle() instanceof VehicleEntity vehicleEntity) {
            if (!vehicleEntity.level().isClientSide) {
                HopperBlockEntity.addItem(vehicleEntity, item);
            }
            return true;
        }
        return false;
    }

    // TODO: Register in Mod.java using Fabric event API
    public static void onLivingDrops(LivingEntity entity, DamageSource source, Collection<ItemEntity> drops) {
        playerDropAmmoBox(entity, drops);
        vehicleCollectDrops(entity, source, drops);
    }

    private static void playerDropAmmoBox(LivingEntity entity, Collection<ItemEntity> drops) {
        if (!(entity instanceof Player player)) return;
        if (!MiscConfig.DROP_AMMO_BOX.get()) return;

        var cap = ModCapabilities.PLAYER_VARIABLE.maybeGet(player).orElse(new PlayerVariable());
        cap.watch();

        boolean drop = Stream.of(Ammo.values())
                .mapToInt(type -> type.get(cap))
                .sum() > 0;
        if (!drop) return;

        var stack = new ItemStack(ModItems.AMMO_BOX.get());

        for (var type : Ammo.values()) {
            type.set(stack, type.get(cap));
            type.set(cap, 0);
        }

        stack.getOrCreateTag().putBoolean("All", true);
        stack.getOrCreateTag().putBoolean("IsDrop", true);

        ModCapabilities.PLAYER_VARIABLE.sync(player);
        drops.add(new ItemEntity(player.level(), player.getX(), player.getY() + 1, player.getZ(), stack));
    }

    private static void vehicleCollectDrops(LivingEntity entity, DamageSource source, Collection<ItemEntity> drops) {
        if (!VehicleConfig.COLLECT_DROPS_BY_CRASHING.get()) return;

        if (source == null) return;
        if (!source.is(ModDamageTypes.VEHICLE_STRIKE)) return;

        Entity sourceEntity = source.getEntity();
        if (!(sourceEntity instanceof Player player)) return;

        if (player.getVehicle() instanceof VehicleEntity vehicle) {
            var dropsList = new ArrayList<>(drops);
            var removed = new ArrayList<ItemEntity>();

            dropsList.forEach(itemEntity -> {
                ItemStack stack = itemEntity.getItem();

                InventoryTool.insertItem(vehicle.getItemStacks(), stack);

                if (stack.getCount() <= 0) {
                    player.drop(stack, false);
                    removed.add(itemEntity);
                }
            });

            drops.removeAll(removed);
        }
    }

    // TODO: Register in Mod.java using Fabric event API
    public static boolean onLivingExperienceDrop(Player player, int droppedExperience) {
        if (player == null) return false;

        if (player.getVehicle() instanceof VehicleEntity) {
            player.giveExperiencePoints(droppedExperience);
            return true;
        }
        return false;
    }

    public static void handlePlayerBeamReset(Player player) {
        ModCapabilities.LASER_CAPABILITY.maybeGet(player).ifPresent(LaserCapability.ILaserCapability::end);
    }

    // TODO: Register in Mod.java using Fabric event API
    public static float onKnockback(LivingEntity entity, float strength) {
        ICustomKnockback knockback = ICustomKnockback.getInstance(entity);
        if (knockback.superbWarfare$getKnockbackStrength() >= 0) {
            return (float) knockback.superbWarfare$getKnockbackStrength();
        }
        return strength;
    }

    // TODO: Register in Mod.java using Fabric event API
    public static boolean onEntityFall(LivingEntity entity) {
        return entity.getVehicle() instanceof VehicleEntity;
    }

    // TODO: Register in Mod.java using Fabric event API
    public static boolean onPreSendKillMessage(PreKillEvent.SendKillMessage event) {
        if (event.getSource().getDirectEntity() instanceof AutoAimableEntity && !(event.getTarget() instanceof Player)) {
            return true;
        }
        return false;
    }

    // TODO: Register in Mod.java using Fabric event API
    public static boolean onPreIndicator(PreKillEvent.Indicator event) {
        if (event.getSource().getDirectEntity() instanceof AutoAimableEntity && !(event.getTarget() instanceof Player)) {
            return true;
        }
        return false;
    }

    // TODO: Register in Mod.java using Fabric event API
    public static boolean onEffectApply(MobEffectInstance effect, LivingEntity entity) {
        if (effect.getEffect().getCategory() == MobEffectCategory.HARMFUL
                && entity.getVehicle() instanceof VehicleEntity vehicle
                && vehicle.isEnclosed(vehicle.getSeatIndex(entity))
        ) {
            return true;
        }
        return false;
    }

    /**
     * 取消原版爆炸对载具的影响，改为单独计算
     * Code based on YWZJ-Vehicle
     */
    // TODO: Register in Mod.java using Fabric event API
    public static void onExplosionDetonate(Explosion explosion, java.util.List<Entity> affectedEntities) {
        if (explosion instanceof CustomExplosion) return;

        Iterator<Entity> iterator = affectedEntities.iterator();
        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            if (entity instanceof VehicleEntity) {
                iterator.remove();

                var explosionAccess = ExplosionAccess.of(explosion);
                Vec3 explosionPos = new Vec3(explosionAccess.superbwarfare$getX(), explosionAccess.superbwarfare$getY(), explosionAccess.superbwarfare$getZ());
                float explosionRadius = ((ExplosionAccess) explosion).superbwarfare$getRadius() * 2.0F;
                if (!entity.ignoreExplosion()) {
                    double distanceRatio = Math.sqrt(entity.distanceToSqr(explosionPos)) / explosionRadius;
                    if (distanceRatio <= 1.0D) {
                        double dx = entity.getX() - explosionPos.x;
                        double dy = entity.getEyeY() - explosionPos.y;
                        double dz = entity.getZ() - explosionPos.z;
                        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
                        if (distance != 0.0D) {
                            double visibilityFactor = Explosion.getSeenPercent(explosionPos, entity);
                            double impactStrength = (1.0D - distanceRatio) * visibilityFactor;
                            float damage = (float) ((int) ((impactStrength * impactStrength + impactStrength) / 2.0D * 7.0D * explosionRadius + 1.0D));
                            entity.hurt(explosion.getDamageSource(), damage);
                        }
                    }
                }
            }
        }
    }
}
