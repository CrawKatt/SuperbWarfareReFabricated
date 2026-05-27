package com.atsuishio.superbwarfare.capability.energy;

import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import team.reborn.energy.api.EnergyStorage;

import java.util.function.Supplier;

public class DynamicEnergyStorage extends SnapshotParticipant<Long> implements EnergyStorage {

    protected long amount;
    protected Supplier<Long> capacityGetter;
    protected Supplier<Long> maxInsertGetter;
    protected Supplier<Long> maxExtractGetter;

    public DynamicEnergyStorage(Supplier<Long> capacityGetter) {
        this(capacityGetter, capacityGetter, capacityGetter);
    }

    public DynamicEnergyStorage(Supplier<Long> capacityGetter, Supplier<Long> maxInsertGetter, Supplier<Long> maxExtractGetter) {
        this.capacityGetter = capacityGetter;
        this.maxInsertGetter = maxInsertGetter;
        this.maxExtractGetter = maxExtractGetter;
    }

    @Override
    protected Long createSnapshot() {
        return amount;
    }

    @Override
    protected void readSnapshot(Long snapshot) {
        amount = snapshot;
    }

    @Override
    public long insert(long maxAmount, TransactionContext transaction) {
        if (maxInsertGetter.get() <= 0 || !supportsInsertion()) return 0;
        long inserted = Math.min(maxInsertGetter.get(), Math.min(maxAmount, capacityGetter.get() - amount));
        if (inserted > 0) {
            updateSnapshots(transaction);
            amount += inserted;
            return inserted;
        }
        return 0;
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        if (maxExtractGetter.get() <= 0 || !supportsExtraction()) return 0;
        long extracted = Math.min(maxExtractGetter.get(), Math.min(maxAmount, amount));
        if (extracted > 0) {
            updateSnapshots(transaction);
            amount -= extracted;
            return extracted;
        }
        return 0;
    }

    @Override
    public long getAmount() {
        return amount;
    }

    @Override
    public long getCapacity() {
        return capacityGetter.get();
    }

    @Override
    public boolean supportsInsertion() {
        return maxInsertGetter.get() > 0;
    }

    @Override
    public boolean supportsExtraction() {
        return maxExtractGetter.get() > 0;
    }
}
