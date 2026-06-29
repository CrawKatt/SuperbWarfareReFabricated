package com.atsuishio.superbwarfare.capability.api;

import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;

public class EnergyStorage implements IEnergyStorage, team.reborn.energy.api.EnergyStorage {

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
        this.energy = energy;
    }

    public Tag serializeNBT(HolderLookup.Provider provider) {
        return LongTag.valueOf(this.energy);
    }

    public void deserializeNBT(HolderLookup.Provider provider, @NotNull Tag nbt) {
        if (nbt instanceof LongTag longTag) {
            this.energy = longTag.getAsLong();
        } else if (nbt instanceof IntTag intTag) {
            this.energy = intTag.getAsInt();
        }
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (simulate) {
            return (int) Math.min(capacity - energy, Math.min(this.maxReceive, (long) maxReceive));
        }
        try (var t = net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()) {
            long received = insert(maxReceive, t);
            t.commit();
            return (int) received;
        }
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (simulate) {
            return (int) Math.min(energy, Math.min(this.maxExtract, (long) maxExtract));
        }
        try (var t = net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()) {
            long extracted = extract(maxExtract, t);
            t.commit();
            return (int) extracted;
        }
    }

    @Override
    public int getEnergyStored() {
        return (int) energy;
    }

    @Override
    public int getMaxEnergyStored() {
        return (int) capacity;
    }

    @Override
    public boolean canExtract() {
        return maxExtract > 0;
    }

    @Override
    public boolean canReceive() {
        return maxReceive > 0;
    }

    // TeamReborn EnergyStorage implementation

    @Override
    public long insert(long maxAmount, TransactionContext transaction) {
        if (!supportsInsertion()) return 0;
        if (transaction == null) {
            try (var t = net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()) {
                long inserted = insert(maxAmount, t);
                t.commit();
                return inserted;
            }
        }

        long energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxAmount));
        if (energyReceived > 0) {
            final long prevEnergy = this.energy;
            this.energy += energyReceived;
            transaction.addCloseCallback((t, result) -> {
                if (result != TransactionContext.Result.COMMITTED) {
                    this.energy = prevEnergy;
                }
            });
        }
        return energyReceived;
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        if (!supportsExtraction()) return 0;
        if (transaction == null) {
            try (var t = net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()) {
                long extracted = extract(maxAmount, t);
                t.commit();
                return extracted;
            }
        }

        long energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxAmount));
        if (energyExtracted > 0) {
            final long prevEnergy = this.energy;
            this.energy -= energyExtracted;
            transaction.addCloseCallback((t, result) -> {
                if (result != TransactionContext.Result.COMMITTED) {
                    this.energy = prevEnergy;
                }
            });
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
