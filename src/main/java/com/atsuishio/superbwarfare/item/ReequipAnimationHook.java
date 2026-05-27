package com.atsuishio.superbwarfare.item;

import net.minecraft.world.item.ItemStack;

public interface ReequipAnimationHook {
    boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged);
}