package com.atsuishio.superbwarfare.entity.mixin;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface LivingDropsCapture {

    boolean superbwarfare$isCapturingDrops();

    @Nullable
    ItemEntity superbwarfare$captureDrop(ItemStack stack, float yOffset);
}
