package com.atsuishio.superbwarfare.event;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import com.atsuishio.superbwarfare.api.event.PreKillEvent;
import com.atsuishio.superbwarfare.component.ModDataComponents;
import com.atsuishio.superbwarfare.config.common.GameplayConfig;
import com.atsuishio.superbwarfare.config.server.MiscConfig;
import com.atsuishio.superbwarfare.config.server.VehicleConfig;
import com.atsuishio.superbwarfare.data.gun.Ammo;
import com.atsuishio.superbwarfare.data.gun.GunData;
import com.atsuishio.superbwarfare.data.gun.value.ReloadState;
import com.atsuishio.superbwarfare.entity.TargetEntity;
import com.atsuishio.superbwarfare.entity.mixin.ICustomKnockback;
import com.atsuishio.superbwarfare.entity.vehicle.base.AutoAimableEntity;
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.atsuishio.superbwarfare.init.*;
import com.atsuishio.superbwarfare.item.common.ammo.box.AmmoBoxInfo;
import com.atsuishio.superbwarfare.item.gun.GunItem;
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
import net.minecraft.world.level.block.entity.HopperBlockEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

import static com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity.AI_PASSENGER_WEAPON_TARGET_UUID;
import static com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity.AI_TURRET_TARGET_UUID;

public class LivingEventHandler {

    public static void onLivingChangeTargetEvent(Mob mob, LivingEntity target) {
        if (mob.getVehicle() instanceof VehicleEntity vehicle) {
            if (mob == vehicle.getNthEntity(vehicle.getTurretControllerIndex())) {
                if (target != null) {
                    vehicle.getEntityData().set(AI_TURRET_TARGET_UUID, target.getStringUUID());
                } else {
                    vehicle.getEntityData().set(AI_TURRET_TARGET_UUID, "undefined");
                }
            }

            if (mob == vehicle.getNthEntity(vehicle.getPassengerWeaponStationControllerIndex())) {
                if (target != null) {
                    vehicle.getEntityData().set(AI_PASSENGER_WEAPON_TARGET_UUID, target.getStringUUID());
                } else {
                    vehicle.getEntityData().set(AI_PASSENGER_WEAPON_TARGET_UUID, "undefined");
                }
            }
        }
    }

    public static boolean onEntityAttacked(LivingEntity entity, DamageSource source, float amount) {
        if (!source.is(ModDamageTypes.VEHICLE_EXPLOSION)
                && entity.getVehicle() instanceof VehicleEntity vehicle
                && vehicle.isEnclosed(entity)
        ) {
            if (!source.is(ModTags.DamageTypes.VEHICLE_NOT_ABSORB)) {
                vehicle.hurt(source, amount);
            }
            return true;
        }
        return false;
    }

    public static float onEntityHurt(LivingEntity entity, DamageSource source, float amount) {
        amount = handleVehicleHurt(entity, source, amount);
        if (amount == 0f) return 0f;

        amount = handleGunPerksWhenHurt(entity, source, amount);
        renderDamageIndicator(entity, source, amount);
        amount = reduceDamage(entity, source, amount);
        giveExpToWeapon(entity, source, amount);
        handleGunLevels(entity, source, amount);
        return amount;
    }

    public static void onEntityDeath(LivingEntity entity, DamageSource source) {
        killIndication(entity, source);
        handleGunPerksWhenDeath(entity, source);
        handlePlayerKillEntity(entity, source);
        giveKillExpToWeapon(entity, source);

        if (entity instanceof Player player) {
            handlePlayerBeamReset(player);
        }
    }

    private static float handleVehicleHurt(LivingEntity livingEntity, DamageSource source, float amount) {
        var vehicleEntity = livingEntity.getVehicle();
        if (vehicleEntity instanceof VehicleEntity vehicle) {
            if (source.is(ModTags.DamageTypes.VEHICLE_IGNORE)) return amount;

            if (vehicle.isEnclosed(livingEntity)) {
                if (!source.is(ModDamageTypes.VEHICLE_EXPLOSION)) {
                    return 0f;
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
        Entity sourceEntity = source.getEntity();
        if (sourceEntity == null) return amount;
        if (sourceEntity.level().isClientSide) return amount;

        double dmg = amount;
        double damage = dmg;

        ItemStack stack = sourceEntity instanceof LivingEntity living ? living.getMainHandItem() : ItemStack.EMPTY;

        if (DamageTypeTool.isGunDamage(source) && stack.getItem() instanceof GunItem) {
            var data = GunData.from(stack);
            double distance = entity.position().distanceTo(sourceEntity.position());
            damage = reduceDamageByDistance(dmg, distance, data.getDamageReduceRate(), data.getDamageReduceMinDistance());
        }

        ItemStack armor = entity.getItemBySlot(EquipmentSlot.CHEST);

        var tag = NBTTool.getTag(armor);
        if (armor != ItemStack.EMPTY && tag.contains("ArmorPlate")) {
            double armorValue = tag.getDouble("ArmorPlate");
            tag.putDouble("ArmorPlate", Math.max(armorValue - damage, 0));
            NBTTool.saveTag(armor, tag);
            damage = Math.max(damage - armorValue, 0);
        }

        if (source.is(ModTags.DamageTypes.PROJECTILE) || source.is(DamageTypes.MOB_PROJECTILE)) {
            damage *= 1 - 0.8 * Mth.clamp(entity.getAttributeValue(ModAttributes.bulletResistanceHolder()), 0, 1);
        }

        if (source.is(ModTags.DamageTypes.PROJECTILE_ABSOLUTE)) {
            damage *= 1 - 0.2 * Mth.clamp(entity.getAttributeValue(ModAttributes.bulletResistanceHolder()), 0, 1);
        }

        if (source.is(ModDamageTypes.PROJECTILE_EXPLOSION) || source.is(ModDamageTypes.MINE) || source.is(ModDamageTypes.PROJECTILE_HIT) || source.is(ModDamageTypes.CUSTOM_EXPLOSION)
                || source.is(DamageTypes.EXPLOSION) || source.is(DamageTypes.PLAYER_EXPLOSION)) {
            damage *= 1 - 0.3 * Mth.clamp(entity.getAttributeValue(ModAttributes.bulletResistanceHolder()), 0, 1);
        }

        float result = (float) damage;

        if (entity instanceof TargetEntity && sourceEntity instanceof Player player) {
            float display = result;
            if (source.is(ModDamageTypes.BEAST)) {
                display = Float.POSITIVE_INFINITY;
            }

            player.displayClientMessage(Component.translatable("tips.superbwarfare.target.damage",
                    FormatTool.format2D(display),
                    FormatTool.format1D(entity.position().distanceTo(sourceEntity.position()), "m")), false);
        }

        return result;
    }

    private static double reduceDamageByDistance(double amount, double distance, double rate, double minDistance) {
        return amount / (1 + rate * Math.max(0, distance - minDistance));
    }

    /**
     * 根据造成的伤害，提供武器经验
     */
    private static void giveExpToWeapon(LivingEntity entity, DamageSource source, float amount) {
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

    private static void killIndication(LivingEntity entity, DamageSource source) {
        if (!MiscConfig.SEND_KILL_FEEDBACK) return;

        var sourceEntity = source.getEntity();
        if (sourceEntity == null) return;

        if (!GameplayConfig.GLOBAL_INDICATION && !DamageTypeTool.isModDamage(source)) return;

        if (!sourceEntity.level().isClientSide() && sourceEntity instanceof ServerPlayer player) {
            var preEvent = new PreKillEvent.Indicator(player, source, entity);
            onPreIndicator(preEvent);
            if (preEvent.isCanceled()) return;

            SoundTool.playLocalSound(player, ModSounds.TARGET_DOWN, 3f, 1f);
            ServerPlayNetworking.send(player, new ClientIndicatorMessage(2, 8));
        }
    }

    private static void renderDamageIndicator(LivingEntity entity, DamageSource source, float amount) {
        var sourceEntity = source.getEntity();
        if (sourceEntity == null) return;

        if (sourceEntity instanceof ServerPlayer player && (source.is(DamageTypes.EXPLOSION) || source.is(DamageTypes.PLAYER_EXPLOSION)
                || source.is(ModDamageTypes.MINE) || source.is(ModDamageTypes.PROJECTILE_EXPLOSION))) {
            SoundTool.playLocalSound(player, ModSounds.INDICATION, 1f, 1f);
            ServerPlayNetworking.send(player, new ClientIndicatorMessage(0, 5));
        }
    }

    /**
     * 换弹时切换枪械，取消换弹音效播放
     */
    public static void handleChangeSlot(LivingEntity entity, EquipmentSlot slot, ItemStack before, ItemStack after) {
        if (entity instanceof Player player && slot == EquipmentSlot.MAINHAND) {
            if (player.level().isClientSide) return;

            var laserCap = ModCapabilities.LASER_CAPABILITY.find(player, null);
            if (laserCap != null) laserCap.stop();

            if (player instanceof ServerPlayer serverPlayer) {
                if (after.getItem() instanceof GunItem) {
                    checkCopyGuns(after, player);
                }

                if (after.getItem() != before.getItem()
                        || (after.getItem() instanceof GunItem && !GunData.from(after).initialized())
                        || (before.getItem() instanceof GunItem && !GunData.from(before).initialized())
                        || (after.getItem() instanceof GunItem && before.getItem() instanceof GunItem && !Objects.equals(GunsTool.getGunUUID(NBTTool.getTag(after)), GunsTool.getGunUUID(NBTTool.getTag(before))))
                ) {
                    ServerPlayNetworking.send(serverPlayer, DrawClientMessage.INSTANCE);

                    if (before.getItem() instanceof GunItem oldGun) {
                        var oldData = GunData.from(before);

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

                        if (before.is(ModItems.SENTINEL)) {
                            oldData.charge.timer.reset();
                        }

                        oldGun.onChangeSlot(oldData, player);
                        oldData.save();
                    }

                    if (after.getItem() instanceof GunItem) {
                        var newData = GunData.from(after);

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

                        if (after.is(ModItems.SENTINEL)) {
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

    /**
     * 发送击杀消息
     */
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

        var preEvent = new PreKillEvent.SendKillMessage(attacker, source, entity);
        onPreSendKillMessage(preEvent);
        if (preEvent.isCanceled()) return;

        if (attacker != null && MiscConfig.SEND_KILL_FEEDBACK) {
            if (DamageTypeTool.isHeadshotDamage(source)) {
                // FIXME: sendToAllPlayers(new LivingGunKillMessage(attacker.getId(), entity.getId(), true, damageTypeResourceKey));
            } else {
                // FIXME: sendToAllPlayers(new LivingGunKillMessage(attacker.getId(), entity.getId(), false, damageTypeResourceKey));
            }
        }
    }

    private static float handleGunPerksWhenHurt(LivingEntity entity, DamageSource source, float amount) {
        if (!DamageTypeTool.isGunDamage(source) && !source.is(DamageTypes.PLAYER_ATTACK)) return amount;

        LivingEntity attacker = null;
        if (source.getEntity() instanceof LivingEntity living) {
            attacker = living;
        }
        if (source.getDirectEntity() instanceof Projectile projectile && projectile.getOwner() instanceof LivingEntity living) {
            attacker = living;
        }
        if (attacker == null) return amount;

        ItemStack stack = attacker.getMainHandItem();
        if (!(stack.getItem() instanceof GunItem)) return amount;

        var data = GunData.from(stack);
        float damage = amount;

        for (Perk.Type type : Perk.Type.values()) {
            var instance = data.perk.getInstance(type);
            if (instance != null) {
                if (DamageTypeTool.isGunDamage(source)) {
                    damage = instance.perk().getModifiedDamage(damage, data, instance, entity, source);
                    instance.perk().onHurtEntity(damage, data, instance, entity, source);
                } else if (source.is(DamageTypes.PLAYER_ATTACK)) {
                    instance.perk().onMeleeAttack(data, instance, entity);
                }
            }
        }

        return damage;
    }

    private static void handleGunPerksWhenDeath(LivingEntity entity, DamageSource source) {
        if (!DamageTypeTool.isGunDamage(source)) return;

        LivingEntity attacker = null;
        if (source.getEntity() instanceof LivingEntity living) {
            attacker = living;
        }
        if (source.getDirectEntity() instanceof Projectile projectile && projectile.getOwner() instanceof LivingEntity living) {
            attacker = living;
        }
        if (attacker == null) return;

        ItemStack stack = attacker.getMainHandItem();
        if (!(stack.getItem() instanceof GunItem)) return;

        GunData data = GunData.from(stack);
        for (Perk.Type type : Perk.Type.values()) {
            var instance = data.perk.getInstance(type);
            if (instance != null) {
                instance.perk().onKill(data, instance, entity, source);
            }
        }
    }

    public static boolean onPickup(ItemEntity itemEntity, Player player) {
        if (!VehicleConfig.VEHICLE_ITEM_PICKUP) return true;
        if (player.getVehicle() instanceof VehicleEntity vehicleEntity) {
            if (!vehicleEntity.level().isClientSide) {
                HopperBlockEntity.addItem(vehicleEntity, itemEntity);
            }
            return false;
        }
        return true;
    }

    public static void onLivingDrops(LivingEntity entity, DamageSource source, Collection<ItemEntity> drops) {
        playerDropAmmoBox(entity, source, drops);
        vehicleCollectDrops(entity, source, drops);
    }

    /**
     * 开启死亡掉落 & 保留武器弹药时，玩家死亡会掉落一个弹药盒
     */
    private static void playerDropAmmoBox(LivingEntity entity, DamageSource source, Collection<ItemEntity> drops) {
        if (!(entity instanceof Player player)) return;
        if (!MiscConfig.DROP_AMMO_BOX) return;

        var cap = player.getAttached(ModAttachments.PLAYER_VARIABLE).watch();

        boolean drop = Stream.of(Ammo.values())
                .mapToInt(type -> type.get(cap))
                .sum() > 0;
        if (!drop) return;

        var stack = new ItemStack(ModItems.AMMO_BOX);

        for (var type : Ammo.values()) {
            type.set(stack, type.get(cap));
            type.set(cap, 0);
        }

        var info = new AmmoBoxInfo("All", true);
        stack.set(ModDataComponents.AMMO_BOX_INFO, info);

        player.setAttached(ModAttachments.PLAYER_VARIABLE, cap);
        cap.sync(player);

        drops.add(new ItemEntity(player.level(), player.getX(), player.getY() + 1, player.getZ(), stack));
    }

    /**
     * 载具撞死生物时自动收集掉落物
     */
    private static void vehicleCollectDrops(LivingEntity entity, DamageSource source, Collection<ItemEntity> drops) {
        if (!VehicleConfig.COLLECT_DROPS_BY_CRASHING) return;

        if (!source.is(ModDamageTypes.VEHICLE_STRIKE)) return;

        Entity sourceEntity = source.getEntity();
        if (!(sourceEntity instanceof Player player)) return;

        if (player.getVehicle() instanceof VehicleEntity vehicle) {
            var removed = new ArrayList<ItemEntity>();

            drops.forEach(itemEntity -> {
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

    public static boolean onLivingExperienceDrop(LivingEntity entity, Player attackingPlayer, int originalXp) {
        if (attackingPlayer == null) return false;

        if (attackingPlayer.getVehicle() instanceof VehicleEntity) {
            attackingPlayer.giveExperiencePoints(originalXp);
            return true;
        }
        return false;
    }

    public static void handlePlayerBeamReset(Player player) {
        var cap = ModCapabilities.LASER_CAPABILITY.find(player, null);
        if (cap != null) {
            cap.end();
        }
    }

    public static float onKnockback(LivingEntity entity) {
        ICustomKnockback knockback = ICustomKnockback.getInstance(entity);
        if (knockback.superbWarfare$getKnockbackStrength() >= 0) {
            return (float) knockback.superbWarfare$getKnockbackStrength();
        }
        return -1f;
    }

    public static boolean onEntityFall(LivingEntity entity, float fallDistance, float damageMultiplier) {
        if (entity.getVehicle() instanceof VehicleEntity) {
            return true;
        }
        return false;
    }

    public static void onPreSendKillMessage(PreKillEvent.SendKillMessage event) {
        if (event.getSource().getDirectEntity() instanceof AutoAimableEntity && !(event.getTarget() instanceof Player)) {
            event.setCanceled(true);
        }
    }

    public static void onPreIndicator(PreKillEvent.Indicator event) {
        if (event.getSource().getDirectEntity() instanceof AutoAimableEntity && !(event.getTarget() instanceof Player)) {
            event.setCanceled(true);
        }
    }

    public static boolean onEffectApply(LivingEntity entity, MobEffectInstance effectInstance) {
        if (effectInstance.getEffect().value().getCategory() == MobEffectCategory.HARMFUL
                && entity.getVehicle() instanceof VehicleEntity vehicle
                && vehicle.isEnclosed(vehicle.getSeatIndex(entity))
        ) {
            return true;
        }
        return false;
    }
}
