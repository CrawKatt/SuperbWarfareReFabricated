package com.atsuishio.superbwarfare.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface ItemDamageExtension {
    default int getMaxDamage(ItemStack stack) {
        return stack.getOrDefault(DataComponents.MAX_DAMAGE, 0);
    }

    default boolean isDamageable(ItemStack stack) {
        return stack.has(DataComponents.MAX_DAMAGE) && !stack.has(DataComponents.UNBREAKABLE) && stack.has(DataComponents.DAMAGE);
    }

    default <T extends LivingEntity> int damageItem(ItemStack stack, int amount, @Nullable T entity, Consumer<Item> onBroken) {
        return amount;
    }
}
