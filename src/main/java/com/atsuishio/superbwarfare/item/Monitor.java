package com.atsuishio.superbwarfare.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import com.atsuishio.superbwarfare.entity.vehicle.DroneEntity;
import com.atsuishio.superbwarfare.client.item.MonitorClient;
import com.atsuishio.superbwarfare.network.NetworkRegistry;
import com.atsuishio.superbwarfare.network.message.receive.ResetCameraTypeMessage;
import com.atsuishio.superbwarfare.tools.EntityFindUtil;
import com.atsuishio.superbwarfare.tools.ItemNBTTool;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;


import java.util.List;

public class Monitor extends Item implements ReequipAnimationHook {

    public static final String LINKED = "Linked";
    public static final String LINKED_DRONE = "LinkedDrone";
//    public static final String DRONE_UUID = "DroneUUID";

    public Monitor() {
        super(new Properties().stacksTo(1));
    }

    public static void link(ItemStack itemstack, String id) {
        ItemNBTTool.setBoolean(itemstack, LINKED, true);
        itemstack.getOrCreateTag().putString(LINKED_DRONE, id);
    }

    public static void disLink(ItemStack itemstack, Player player) {
        ItemNBTTool.setBoolean(itemstack, LINKED, false);
        itemstack.getOrCreateTag().putString(LINKED_DRONE, "none");
        if (player instanceof ServerPlayer serverPlayer) {
            NetworkRegistry.sendToPlayer(serverPlayer, ResetCameraTypeMessage.INSTANCE);
        }
    }

    private void resetDroneData(DroneEntity drone) {
        if (drone == null) return;

        drone.setLeftInputDown(false);
        drone.setRightInputDown(false);
        drone.setForwardInputDown(false);
        drone.setBackInputDown(false);
        drone.setUpInputDown(false);
        drone.setDownInputDown(false);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getMainHandItem();

        if (!ItemNBTTool.getBoolean(stack, LINKED, false)) {
            return super.use(level, player, hand);
        }

        if (stack.getOrCreateTag().getBoolean("Using")) {
            stack.getOrCreateTag().putBoolean("Using", false);
            if (level.isClientSide) {
                MonitorClient.stopUsing();
            }
        } else {
            stack.getOrCreateTag().putBoolean("Using", true);
            if (level.isClientSide) {
                MonitorClient.startUsing();
            }
        }

        DroneEntity drone = EntityFindUtil.findDrone(player.level(), stack.getOrCreateTag().getString(LINKED_DRONE));
        this.resetDroneData(drone);

        return super.use(level, player, hand);
//        ItemStack stack = player.getItemInHand(hand);
//        if (!player.isShiftKeyDown()) {
//            CompoundTag tag = stack.getOrCreateTag();
//            if (DronesTool.hasInstanceOf(player)) return InteractionResultHolder.fail(stack);
//            if (!tag.contains(DRONE_UUID)) return InteractionResultHolder.fail(stack);
//            player.startUsingItem(hand);
//        }
//        return InteractionResultHolder.fail(stack);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(super.getAttributeModifiers(stack, slot));
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Item modifier", 2d, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Item modifier", -2.4, AttributeModifier.Operation.ADDITION));
            return builder.build();
        }

        return super.getAttributeModifiers(stack, slot);
    }

//    @Override
//    public int getUseDuration(ItemStack stack) {
//        return 72000;
//    }

    public static void getDronePos(ItemStack itemstack, Vec3 vec3) {
        itemstack.getOrCreateTag().putDouble("PosX", vec3.x);
        itemstack.getOrCreateTag().putDouble("PosY", vec3.y);
        itemstack.getOrCreateTag().putDouble("PosZ", vec3.z);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag) {
        if (!stack.getOrCreateTag().contains(LINKED_DRONE) || stack.getOrCreateTag().getString(LINKED_DRONE).equals("none"))
            return;

        MonitorClient.appendHoverText(stack, list);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(itemstack, world, entity, slot, selected);
        DroneEntity drone = EntityFindUtil.findDrone(entity.level(), itemstack.getOrCreateTag().getString(LINKED_DRONE));

        if (!selected) {
            if (itemstack.getOrCreateTag().getBoolean("Using")) {
                itemstack.getOrCreateTag().putBoolean("Using", false);
                if (entity.level().isClientSide) {
                    MonitorClient.stopUsing();
                }
            }
            this.resetDroneData(drone);
        } else if (drone == null) {
            if (itemstack.getOrCreateTag().getBoolean("Using")) {
                itemstack.getOrCreateTag().putBoolean("Using", false);
                if (entity.level().isClientSide) {
                    MonitorClient.stopUsing();
                }
            }
        }
    }

//    @Nullable
//    public static UUID getDroneUUID(Player player) {
//        if (player == null) return null;
//        if (player.getMainHandItem().is(ModItems.MONITOR.get())) {
//            CompoundTag tag = player.getMainHandItem().getOrCreateTag();
//            if (tag.contains(DRONE_UUID)) {
//                return tag.getUUID(DRONE_UUID);
//            }
//        }
//        return null;
//    }
}
