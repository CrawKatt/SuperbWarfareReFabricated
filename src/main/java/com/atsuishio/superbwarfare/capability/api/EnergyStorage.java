package com.atsuishio.superbwarfare.capability.api;

import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;

public class EnergyStorage extends SnapshotParticipant<Long> implements team.reborn.energy.api.EnergyStorage {

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

    public Tag serializeNBT(HolderLookup.Provider provider) {
        return IntTag.valueOf((int) energy);
    }

    public void deserializeNBT(HolderLookup.Provider provider, @NotNull Tag nbt) {
        if (nbt instanceof NumericTag numericTag) {
            this.energy = Math.max(0, Math.min(capacity, numericTag.getAsLong()));
        }
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

        long energyReceived = Math.max(0, Math.min(capacity - energy, Math.min(this.maxReceive, maxAmount)));
        if (energyReceived > 0) {
            updateSnapshots(transaction);
            this.energy += energyReceived;
        }
        return energyReceived;
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        if (!supportsExtraction() || maxAmount <= 0) return 0;

        long energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxAmount));
        if (energyExtracted > 0) {
            updateSnapshots(transaction);
            this.energy -= energyExtracted;
        }
        return energyExtracted;
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
        return maxReceive > 0;
    }

    @Override
    public boolean supportsExtraction() {
        return maxExtract > 0;
    }
}
