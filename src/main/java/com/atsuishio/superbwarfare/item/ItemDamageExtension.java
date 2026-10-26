package com.atsuishio.superbwarfare.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface ItemDamageExtension {
    int getMaxDamage(ItemStack stack);

    boolean isDamageable(@Nullable ItemStack stack);

    default <T extends LivingEntity> int damageItem(ItemStack stack, int amount, @Nullable T entity, Consumer<Item> onBroken) {
        return amount;
    }
}
