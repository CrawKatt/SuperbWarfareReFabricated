package com.atsuishio.superbwarfare.capability.api;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class InvWrapper implements IItemHandler {

    private final Container container;

    public InvWrapper(Container container) {
        this.container = container;
    }

    @Override
    public int getSlots() {
        return container.getContainerSize();
    }

    @Override
    @NotNull
    public ItemStack getStackInSlot(int slot) {
        return container.getItem(slot);
    }

    @Override
    @NotNull
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (!container.canPlaceItem(slot, stack)) return stack;

        ItemStack existing = container.getItem(slot);
        int limit = Math.min(container.getMaxStackSize(), stack.getMaxStackSize());

        if (!existing.isEmpty()) {
            if (!ItemStack.isSameItemSameComponents(existing, stack)) return stack;

            int space = limit - existing.getCount();
            if (space <= 0) return stack;

            int toInsert = Math.min(space, stack.getCount());
            if (!simulate) {
                existing.grow(toInsert);
                container.setChanged();
            }
            ItemStack remainder = stack.copy();
            remainder.shrink(toInsert);
            return remainder;
        }

        int toInsert = Math.min(limit, stack.getCount());
        if (!simulate) {
            ItemStack inserted = stack.copy();
            inserted.setCount(toInsert);
            container.setItem(slot, inserted);
            container.setChanged();
        }
        ItemStack remainder = stack.copy();
        remainder.shrink(toInsert);
        return remainder;
    }

    @Override
    @NotNull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack existing = container.getItem(slot);
        if (existing.isEmpty()) return ItemStack.EMPTY;

        int toExtract = Math.min(amount, existing.getCount());
        ItemStack extracted = existing.copy();
        extracted.setCount(toExtract);

        if (!simulate) {
            existing.shrink(toExtract);
            container.setChanged();
        }

        return extracted;
    }

    @Override
    public int getSlotLimit(int slot) {
        return container.getMaxStackSize();
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return container.canPlaceItem(slot, stack);
    }
}
