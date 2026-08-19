package com.atsuishio.superbwarfare.capability.api;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class ItemHandlerHelper {
    private ItemHandlerHelper() {
    }

    public static void giveItemToPlayer(Player player, @NotNull ItemStack stack) {
        if (stack.isEmpty()) return;

        int originalCount = stack.getCount();
        ItemStack remainder = insertItemStacked(new PlayerInvWrapper(player.getInventory(), true), stack, false);

        if (remainder.isEmpty() || remainder.getCount() != originalCount) {
            var level = player.level();
            level.playSound(
                    null,
                    player.getX(),
                    player.getY() + 0.5,
                    player.getZ(),
                    SoundEvents.ITEM_PICKUP,
                    SoundSource.PLAYERS,
                    0.2F,
                    ((level.random.nextFloat() - level.random.nextFloat()) * 0.7F + 1.0F) * 2.0F
            );
        }

        if (!remainder.isEmpty() && !player.level().isClientSide) {
            ItemEntity item = new ItemEntity(
                    player.level(),
                    player.getX(),
                    player.getY() + 0.5,
                    player.getZ(),
                    remainder
            );
            item.setPickUpDelay(40);
            item.setDeltaMovement(item.getDeltaMovement().multiply(0, 1, 0));
            player.level().addFreshEntity(item);
        }
    }

    public static @NotNull ItemStack insertItemStacked(
            @NotNull IItemHandler handler,
            @NotNull ItemStack stack,
            boolean simulate
    ) {
        ItemStack remainder = stack;

        if (remainder.isStackable()) {
            for (int slot = 0; slot < handler.getSlots() && !remainder.isEmpty(); slot++) {
                ItemStack existing = handler.getStackInSlot(slot);
                if (!existing.isEmpty() && ItemStack.isSameItemSameTags(existing, remainder)) {
                    remainder = handler.insertItem(slot, remainder, simulate);
                }
            }
        }

        for (int slot = 0; slot < handler.getSlots() && !remainder.isEmpty(); slot++) {
            if (handler.getStackInSlot(slot).isEmpty()) {
                remainder = handler.insertItem(slot, remainder, simulate);
            }
        }

        return remainder;
    }
}
