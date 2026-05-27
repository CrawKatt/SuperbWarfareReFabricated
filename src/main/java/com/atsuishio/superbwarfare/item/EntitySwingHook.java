package com.atsuishio.superbwarfare.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface EntitySwingHook {
    boolean onEntitySwing(ItemStack stack, LivingEntity entity);
}