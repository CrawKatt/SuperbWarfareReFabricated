package com.atsuishio.superbwarfare.event;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.capability.ModCapabilities;
import com.atsuishio.superbwarfare.config.common.GameplayConfig;
import com.atsuishio.superbwarfare.config.server.MiscConfig;
import com.atsuishio.superbwarfare.data.gun.GunData;
import com.atsuishio.superbwarfare.data.vehicle.VehicleDataTool;
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModParticleTypes;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.init.ModTags;
import com.atsuishio.superbwarfare.item.Beast;
import com.atsuishio.superbwarfare.item.gun.GunItem;
import com.atsuishio.superbwarfare.tools.GunsTool;
import com.atsuishio.superbwarfare.tools.InventoryTool;
import com.atsuishio.superbwarfare.tools.PlayerReachTool;
import com.atsuishio.superbwarfare.tools.TraceTool;
import com.atsuishio.superbwarfare.world.TDMSavedData;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

import static com.atsuishio.superbwarfare.tools.ParticleTool.sendParticle;

public class PlayerEventHandler {

    public static final UUID TACTICAL_SPRINT_UUID = UUID.fromString("fe8a1213-cf3d-4ec2-8ea8-29acca64b301");

    public static void registerEvents() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            onPlayerLoggedIn(handler.player);
            GunsTool.onPlayerLogin(handler.player);
            VehicleDataTool.onPlayerLogin(handler.player);
            TDMSavedData.onPlayerLoggedIn(handler.player);
        });
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, serverResourceManager, success) -> {
            if (success) {
                GunsTool.onDataPackSync(server.getPlayerList());
                VehicleDataTool.onDataPackSync(server.getPlayerList());
            }
        });
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> onPlayerRespawned(newPlayer));

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            boolean isEnd = true;
            for (var player : server.getPlayerList().getPlayers()) {
                onPlayerTick(player, isEnd);
            }
        });

        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            onAttackEntity(player, entity);
            return InteractionResult.PASS;
        });
    }

    public static void onPlayerLoggedIn(Player player) {
        ItemStack stack = player.getMainHandItem();
        if (stack.is(ModItems.MONITOR.get()) && stack.getOrCreateTag().getBoolean("Using")) {
            stack.getOrCreateTag().putBoolean("Using", false);
        }
    }

    // TODO: Register in Mod.java using Fabric event API
    public static void onPlayerRespawned(Player player) {
        if (player == null) {
            return;
        }

        handleRespawnReload(player);
        handleRespawnAutoArmor(player);
    }

    // TODO: Register in Mod.java using Fabric event API
    public static void onPlayerTick(Player player, boolean isEnd) {
        if (player == null) {
            return;
        }

        ItemStack stack = player.getMainHandItem();

        if (isEnd) {
            if (stack.getItem() instanceof GunItem) {
                handleSpecialWeaponAmmo(player);
            }

            if (!player.level().isClientSide) {
                handleTacticalAttribute(player);
            }
        }
    }

    private static void handleSpecialWeaponAmmo(Player player) {
        ItemStack stack = player.getMainHandItem();
        var data = GunData.from(stack);

        if ((stack.is(ModItems.RPG.get()) || stack.is(ModItems.BOCEK.get())) && data.hasEnoughAmmoToShoot(player)) {
            data.isEmpty.set(false);
        }
    }

    private static void handleRespawnReload(Player player) {
        if (!GameplayConfig.RESPAWN_RELOAD.get()) return;

        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() instanceof GunItem) {
                var data = GunData.from(stack);
                if (!InventoryTool.hasCreativeAmmoBox(player)) {
                    data.reloadAmmo(player);
                } else {
                    data.ammo.set(data.compute().magazine);
                }
                data.holdOpen.set(false);
                data.save();
            }
        }
    }

    private static void handleRespawnAutoArmor(Player player) {
        if (!GameplayConfig.RESPAWN_AUTO_ARMOR.get()) return;

        ItemStack armor = player.getItemBySlot(EquipmentSlot.CHEST);
        if (armor == ItemStack.EMPTY) return;

        double armorPlate = armor.getOrCreateTag().getDouble("ArmorPlate");

        int armorLevel = MiscConfig.DEFAULT_ARMOR_LEVEL.get();
        if (armor.is(ModTags.Items.MILITARY_ARMOR)) {
            armorLevel = MiscConfig.MILITARY_ARMOR_LEVEL.get();
        } else if (armor.is(ModTags.Items.MILITARY_ARMOR_HEAVY)) {
            armorLevel = MiscConfig.HEAVY_MILITARY_ARMOR_LEVEL.get();
        }

        if (armorPlate < armorLevel * MiscConfig.ARMOR_POINT_PER_LEVEL.get()) {
            for (var stack : player.getInventory().items) {
                if (stack.is(ModItems.ARMOR_PLATE.get())) {
                    if (stack.getTag() != null && stack.getTag().getBoolean("Infinite")) {
                        armor.getOrCreateTag().putDouble("ArmorPlate", armorLevel * MiscConfig.ARMOR_POINT_PER_LEVEL.get());

                        if (player instanceof ServerPlayer serverPlayer) {
                            serverPlayer.level().playSound(null, serverPlayer.getOnPos(), SoundEvents.ARMOR_EQUIP_IRON, SoundSource.PLAYERS, 0.5f, 1);
                        }
                    } else {
                        for (int index0 = 0; index0 < Math.ceil(((armorLevel * MiscConfig.ARMOR_POINT_PER_LEVEL.get()) - armorPlate) / MiscConfig.ARMOR_POINT_PER_LEVEL.get()); index0++) {
                            stack.finishUsingItem(player.level(), player);
                        }
                    }
                }
            }
        }
    }

    public static void handleTacticalAttribute(Player player) {
        if (player == null) {
            return;
        }
        var attr = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attr == null) return;
        if (attr.getModifier(TACTICAL_SPRINT_UUID) != null) {
            attr.removeModifier(TACTICAL_SPRINT_UUID);
        }

        if (MiscConfig.ALLOW_TACTICAL_SPRINT.get() && ModCapabilities.PLAYER_VARIABLE.get(player).tacticalSprint) {
            player.setSprinting(true);
            attr.addTransientModifier(new AttributeModifier(TACTICAL_SPRINT_UUID, Mod.ATTRIBUTE_MODIFIER,
                    0.25, AttributeModifier.Operation.MULTIPLY_BASE));
        }
    }

    // TODO: Register in Mod.java using Fabric event API
    public static void onAnvilUpdate(ItemStack left, ItemStack right, AnvilUpdateCallback callback) {
        if (left.getItem() instanceof GunItem && right.getItem() == ModItems.SHORTCUT_PACK.get()) {
            ItemStack output = left.copy();

            var data = GunData.from(output);
            data.level.add(1);
            data.save();

            callback.accept(output, 10, 1);
        }
    }

    @FunctionalInterface
    public interface AnvilUpdateCallback {
        void accept(ItemStack output, int cost, int materialCost);
    }

    // TODO: Register in Mod.java using Fabric event API
    public static boolean onAttackEntity(Player player, Entity target) {
        boolean vehicleTarget = target instanceof VehicleEntity;
        if (target instanceof VehicleEntity vehicle) {
            double reach = PlayerReachTool.getEntityReach(player);

            Vec3 position = TraceTool.playerFindLookingPos(player, vehicle, reach);

            if (position != null) {
                if (vehicle.shouldSendHitSounds()) {
                    vehicle.level().playSound(null, BlockPos.containing(position), ModSounds.HIT.get(), SoundSource.PLAYERS, 1, 1);
                }

                if (vehicle.shouldSendHitParticles() && vehicle.level() instanceof ServerLevel serverLevel) {
                    sendParticle(serverLevel, ModParticleTypes.FIRE_STAR.get(), position.x, position.y, position.z,
                            2, 0, 0, 0, 0.2, false);
                }
            }
        }

        if (player.getMainHandItem().getItem() instanceof Beast) {
            Beast.beastKill(player, target);
        }

        return vehicleTarget;
    }
}
