package com.atsuishio.superbwarfare.capability.energy;

import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.world.item.ItemStack;
import team.reborn.energy.api.EnergyStorage;

import java.util.function.Function;

public class ItemEnergyStorage extends DynamicEnergyStorage {

    private final ItemStack stack;

    public ItemEnergyStorage(ItemStack stack, long capacity) {
        this(stack, capacity, capacity, capacity);
    }

    public ItemEnergyStorage(ItemStack stack, long capacity, long maxReceive, long maxExtract) {
        this(stack, s -> capacity, s -> maxReceive, s -> maxExtract);
    }

    public ItemEnergyStorage(ItemStack stack, Function<ItemStack, Long> capacityGetter, Function<ItemStack, Long> maxReceiveGetter, Function<ItemStack, Long> maxExtractGetter) {
        super(() -> capacityGetter.apply(stack), () -> maxReceiveGetter.apply(stack), () -> maxExtractGetter.apply(stack));

        this.stack = stack;
        var component = stack.get(EnergyStorage.ENERGY_COMPONENT);
        this.energy = component == null ? 0 : component;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int received = super.receiveEnergy(maxReceive, simulate);

        if (received > 0 && !simulate) {
            stack.set(EnergyStorage.ENERGY_COMPONENT, (long) getEnergyStored());
        }

        return received;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int extracted = super.extractEnergy(maxExtract, simulate);

        if (extracted > 0 && !simulate) {
            stack.set(EnergyStorage.ENERGY_COMPONENT, (long) getEnergyStored());
        }

        return extracted;
    }

    @Override
    public long insert(long maxAmount, TransactionContext transaction) {
        if (transaction == null) {
            try (var t = net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()) {
                long inserted = insert(maxAmount, t);
                t.commit();
                return inserted;
            }
        }

        long inserted = super.insert(maxAmount, transaction);
        if (inserted > 0) {
            transaction.addCloseCallback((t, result) -> {
                if (result == TransactionContext.Result.COMMITTED) {
                    stack.set(EnergyStorage.ENERGY_COMPONENT, this.energy);
                }
            });
        }
        return inserted;
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        if (transaction == null) {
            try (var t = net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()) {
                long extracted = extract(maxAmount, t);
                t.commit();
                return extracted;
            }
        }

        long extracted = super.extract(maxAmount, transaction);
        if (extracted > 0) {
            transaction.addCloseCallback((t, result) -> {
                if (result == TransactionContext.Result.COMMITTED) {
                    stack.set(EnergyStorage.ENERGY_COMPONENT, this.energy);
                }
            });
        }
        return extracted;
    }
}
