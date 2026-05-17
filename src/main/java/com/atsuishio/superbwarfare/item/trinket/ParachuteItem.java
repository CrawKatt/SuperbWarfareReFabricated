package com.atsuishio.superbwarfare.item.trinket;

import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.tools.NBTTool;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class ParachuteItem extends TrinketItem {

    public static final String TAG_OPEN = "Open";

    public ParachuteItem() {
        super(new Properties().stacksTo(1).durability(600));
    }

    @Override
    public boolean isValidRepairItem(@NotNull ItemStack pStack, ItemStack pRepairCandidate) {
        return pRepairCandidate.is(Items.PHANTOM_MEMBRANE);
    }

    @Override
    public boolean canEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        return TrinketsApi.getTrinketComponent(entity)
                .map(comp -> !comp.isEquipped(this))
                .orElse(false);
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        var tag = NBTTool.getTag(stack);
        if (tag.getBoolean(TAG_OPEN)) {
            if (entity.onGround() || entity.isInWater() || entity.isFallFlying() || entity.getVehicle() != null || (entity instanceof Player player && player.getAbilities().flying)) {
                tag.putBoolean(TAG_OPEN, false);
                NBTTool.saveTag(stack, tag);
                entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), ModSounds.PARACHUTE_CLOSE, SoundSource.PLAYERS, 1f, 1);
            }
            if (entity instanceof Player player) {
                if (player.level().isClientSide) {
                    player.addDeltaMovement(new Vec3(player.getLookAngle().x, 0, player.getLookAngle().z).normalize().scale(0.05));
                    player.setDeltaMovement(player.getDeltaMovement().multiply(1.03, 0.75, 1.03));
                }
            }
            if (entity.tickCount % 40 == 0 && entity.level() instanceof ServerLevel) {
                stack.hurtAndBreak(1, entity, EquipmentSlot.CHEST);
            }
            entity.resetFallDistance();
        }
    }

    public static boolean isParachuteOpen(LivingEntity entity) {
        return TrinketsApi.getTrinketComponent(entity)
                .map(comp -> comp.getEquipped(ModItems.PARACHUTE).stream()
                        .findFirst()
                        .map(tuple -> NBTTool.getTag(tuple.getB()).getBoolean(ParachuteItem.TAG_OPEN))
                        .orElse(false))
                .orElse(false);
    }

    public static boolean isParachuteVisible(LivingEntity entity) {
        return TrinketsApi.getTrinketComponent(entity)
                .map(comp -> !comp.getEquipped(ModItems.PARACHUTE).isEmpty())
                .orElse(false);
    }
}
