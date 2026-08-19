package com.atsuishio.superbwarfare.capability.api;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class InvWrapper implements IItemHandler {
    protected final Container container;

    public InvWrapper(Container container) {
        this.container = container;
    }

    @Override
    public int getSlots() {
        return container.getContainerSize();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        return container.getItem(slot);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) return ItemStack.EMPTY;

        ItemStack existing = container.getItem(slot);
        if (!existing.isEmpty()) {
            if (existing.getCount() >= Math.min(existing.getMaxStackSize(), getSlotLimit(slot))) return stack;
            if (!ItemStack.isSameItemSameTags(existing, stack)) return stack;
            if (!container.canPlaceItem(slot, stack)) return stack;

            int space = Math.min(stack.getMaxStackSize(), getSlotLimit(slot)) - existing.getCount();
            if (stack.getCount() <= space) {
                if (!simulate) {
                    ItemStack copy = stack.copy();
                    copy.grow(existing.getCount());
                    container.setItem(slot, copy);
                    container.setChanged();
                }
                return ItemStack.EMPTY;
            } else {
                ItemStack remainder = stack.copy();
                if (!simulate) {
                    ItemStack copy = remainder.split(space);
                    copy.grow(existing.getCount());
                    container.setItem(slot, copy);
                    container.setChanged();
                } else {
                    remainder.shrink(space);
                }
                return remainder;
            }
        }

        if (!container.canPlaceItem(slot, stack)) return stack;

        int limit = Math.min(stack.getMaxStackSize(), getSlotLimit(slot));
        if (limit < stack.getCount()) {
            ItemStack remainder = stack.copy();
            if (!simulate) {
                container.setItem(slot, remainder.split(limit));
                container.setChanged();
            } else {
                remainder.shrink(limit);
            }
            return remainder;
        }

        if (!simulate) {
            container.setItem(slot, stack);
            container.setChanged();
        }
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount <= 0) return ItemStack.EMPTY;
        ItemStack existing = container.getItem(slot);
        if (existing.isEmpty()) return ItemStack.EMPTY;

        int extractedCount = Math.min(amount, existing.getCount());
        ItemStack extracted = existing.copy();
        extracted.setCount(extractedCount);
        if (!simulate) {
            extracted = container.removeItem(slot, extractedCount);
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
