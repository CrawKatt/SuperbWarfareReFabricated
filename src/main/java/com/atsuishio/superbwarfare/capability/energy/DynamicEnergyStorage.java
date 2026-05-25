package com.atsuishio.superbwarfare.capability.energy;

import com.atsuishio.superbwarfare.capability.api.EnergyStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

import java.util.function.Supplier;

public class DynamicEnergyStorage extends EnergyStorage {

    protected final Supplier<Long> maxStorageGetter;
    protected final Supplier<Long> maxReceiveGetter;
    protected final Supplier<Long> maxExtractGetter;

    public DynamicEnergyStorage(Supplier<Long> maxStorageGetter) {
        this(maxStorageGetter, maxStorageGetter, maxStorageGetter);
    }

    public DynamicEnergyStorage(Supplier<Long> maxStorageGetter, Supplier<Long> maxReceiveGetter, Supplier<Long> maxExtractGetter) {
        super(Integer.MAX_VALUE);

        this.maxStorageGetter = maxStorageGetter;
        this.maxReceiveGetter = maxReceiveGetter;
        this.maxExtractGetter = maxExtractGetter;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        updateProps();
        return super.extractEnergy(maxExtract, simulate);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        updateProps();
        return super.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public long insert(long maxAmount, TransactionContext transaction) {
        updateProps();
        return super.insert(maxAmount, transaction);
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        updateProps();
        return super.extract(maxAmount, transaction);
    }

    @Override
    public boolean canReceive() {
        updateProps();
        return super.canReceive();
    }

    @Override
    public boolean canExtract() {
        updateProps();
        return super.canExtract();
    }

    @Override
    public int getMaxEnergyStored() {
        updateProps();
        return super.getMaxEnergyStored();
    }

    @Override
    public long getCapacity() {
        updateProps();
        return super.getCapacity();
    }

    protected void updateProps() {
        this.capacity = maxStorageGetter.get();
        this.maxExtract = maxExtractGetter.get();
        this.maxReceive = maxReceiveGetter.get();
    }
}
