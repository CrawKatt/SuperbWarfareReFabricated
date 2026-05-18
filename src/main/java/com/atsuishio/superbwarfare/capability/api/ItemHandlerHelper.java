package com.atsuishio.superbwarfare.capability.api;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemHandlerHelper {

    public static void giveItemToPlayer(Player player, @NotNull ItemStack stack) {
        if (!player.addItem(stack)) {
            player.drop(stack, false);
        }
    }

    @NotNull
    public static ItemStack insertItemStacked(@NotNull IItemHandler handler, @NotNull ItemStack stack, boolean simulate) {
        for (int i = 0; i < handler.getSlots(); i++) {
            stack = handler.insertItem(i, stack, simulate);
            if (stack.isEmpty()) break;
        }
        return stack;
    }
}
