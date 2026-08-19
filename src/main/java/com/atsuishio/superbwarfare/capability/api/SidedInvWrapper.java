package com.atsuishio.superbwarfare.capability.api;

import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class SidedInvWrapper extends InvWrapper {
    private final WorldlyContainer sidedContainer;
    private final Direction side;

    public SidedInvWrapper(WorldlyContainer container, Direction side) {
        super(container);
        this.sidedContainer = container;
        this.side = side;
    }

    public static IItemHandler[] create(WorldlyContainer container, Direction... sides) {
        return Arrays.stream(sides).map(side -> new SidedInvWrapper(container, side)).toArray(IItemHandler[]::new);
    }

    private int getContainerSlot(int slot) {
        int[] slots = sidedContainer.getSlotsForFace(side);
        return slot >= 0 && slot < slots.length ? slots[slot] : -1;
    }

    @Override
    public int getSlots() {
        return sidedContainer.getSlotsForFace(side).length;
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        int containerSlot = getContainerSlot(slot);
        return containerSlot == -1 ? ItemStack.EMPTY : sidedContainer.getItem(containerSlot);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        int containerSlot = getContainerSlot(slot);
        if (containerSlot == -1 || !sidedContainer.canPlaceItemThroughFace(containerSlot, stack, side)) return stack;
        return super.insertItem(containerSlot, stack, simulate);
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        int containerSlot = getContainerSlot(slot);
        if (containerSlot == -1) return ItemStack.EMPTY;

        ItemStack stack = sidedContainer.getItem(containerSlot);
        if (!sidedContainer.canTakeItemThroughFace(containerSlot, stack, side)) return ItemStack.EMPTY;
        return super.extractItem(containerSlot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return sidedContainer.getMaxStackSize();
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        int containerSlot = getContainerSlot(slot);
        return containerSlot != -1 && sidedContainer.canPlaceItem(containerSlot, stack);
    }
}
