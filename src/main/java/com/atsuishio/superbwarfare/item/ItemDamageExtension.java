package com.atsuishio.superbwarfare.item;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface ItemDamageExtension {
    int getMaxDamage(ItemStack stack);

    boolean isDamageable(@Nullable ItemStack stack);
}
