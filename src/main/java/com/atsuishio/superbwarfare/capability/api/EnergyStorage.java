package com.atsuishio.superbwarfare.capability.api;

import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;

public class EnergyStorage extends SnapshotParticipant<Long>
        implements IEnergyStorage, team.reborn.energy.api.EnergyStorage {

    protected long energy;
    protected long capacity;
    protected long maxReceive;
    protected long maxExtract;

    public EnergyStorage(long capacity) {
        this(capacity, capacity, capacity);
    }

    public EnergyStorage(long capacity, long maxReceive, long maxExtract) {
        this(capacity, maxReceive, maxExtract, 0);
    }

    public EnergyStorage(long capacity, long maxReceive, long maxExtract, long energy) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.energy = Math.max(0, Math.min(capacity, energy));
    }

    public Tag serializeNBT() {
        return IntTag.valueOf(getEnergyStored());
    }

    public void deserializeNBT(@NotNull Tag nbt) {
        if (nbt instanceof NumericTag numericTag) {
            energy = Math.max(0, Math.min(capacity, numericTag.getAsLong()));
        }
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!canReceive() || maxReceive <= 0) return 0;
        if (simulate) {
            return (int) Math.max(0, Math.min(capacity - energy, Math.min(this.maxReceive, (long) maxReceive)));
        }

        try (Transaction transaction = Transaction.openOuter()) {
            long received = insert(maxReceive, transaction);
            transaction.commit();
            return (int) received;
        }
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!canExtract() || maxExtract <= 0) return 0;
        if (simulate) {
            return (int) Math.min(energy, Math.min(this.maxExtract, (long) maxExtract));
        }

        try (Transaction transaction = Transaction.openOuter()) {
            long extracted = extract(maxExtract, transaction);
            transaction.commit();
            return (int) extracted;
        }
    }

    @Override
    public int getEnergyStored() {
        return (int) Math.min(Integer.MAX_VALUE, energy);
    }

    @Override
    public int getMaxEnergyStored() {
        return (int) Math.min(Integer.MAX_VALUE, capacity);
    }

    @Override
    public boolean canExtract() {
        return maxExtract > 0;
    }

    @Override
    public boolean canReceive() {
        return maxReceive > 0;
    }

    @Override
    protected Long createSnapshot() {
        return energy;
    }

    @Override
    protected void readSnapshot(Long snapshot) {
        energy = snapshot;
    }

    @Override
    public long insert(long maxAmount, TransactionContext transaction) {
        if (!supportsInsertion() || maxAmount <= 0) return 0;

        long inserted = Math.max(0, Math.min(capacity - energy, Math.min(maxReceive, maxAmount)));
        if (inserted > 0) {
            updateSnapshots(transaction);
            energy += inserted;
        }
        return inserted;
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        if (!supportsExtraction() || maxAmount <= 0) return 0;

        long extracted = Math.min(energy, Math.min(maxExtract, maxAmount));
        if (extracted > 0) {
            updateSnapshots(transaction);
            energy -= extracted;
        }
        return extracted;
    }

    @Override
    public long getAmount() {
        return energy;
    }

    @Override
    public long getCapacity() {
        return capacity;
    }

    @Override
    public boolean supportsInsertion() {
        return canReceive();
    }

    @Override
    public boolean supportsExtraction() {
        return canExtract();
    }
}
