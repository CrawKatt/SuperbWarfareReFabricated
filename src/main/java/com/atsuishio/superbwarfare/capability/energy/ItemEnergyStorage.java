package com.atsuishio.superbwarfare.capability.energy;

import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

public class ItemEnergyStorage extends DynamicEnergyStorage {

    private static final String NBT_ENERGY = "Energy";

    private final ItemStack stack;

    public ItemEnergyStorage(ItemStack stack, long capacity) {
        this(stack, capacity, capacity, capacity);
    }

    public ItemEnergyStorage(ItemStack stack, long capacity, long maxInsert, long maxExtract) {
        this(stack, s -> capacity, s -> maxInsert, s -> maxExtract);
    }

    public ItemEnergyStorage(ItemStack stack, Function<ItemStack, Long> capacityGetter, Function<ItemStack, Long> maxInsertGetter, Function<ItemStack, Long> maxExtractGetter) {
        super(() -> capacityGetter.apply(stack), () -> maxInsertGetter.apply(stack), () -> maxExtractGetter.apply(stack));

        this.stack = stack;
        if (stack.getTag() != null) {
            this.amount = stack.hasTag() && stack.getTag().contains(NBT_ENERGY) ? stack.getTag().getLong(NBT_ENERGY) : 0;
        }
    }

    @Override
    protected void onFinalCommit() {
        stack.getOrCreateTag().putLong(NBT_ENERGY, getAmount());
    }
}
