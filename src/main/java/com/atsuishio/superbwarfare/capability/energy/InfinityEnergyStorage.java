package com.atsuishio.superbwarfare.capability.energy;

import com.atsuishio.superbwarfare.capability.api.IEnergyStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import team.reborn.energy.api.EnergyStorage;

/**
 * 无限供电能力，纯逆天
 */
public class InfinityEnergyStorage implements IEnergyStorage, EnergyStorage {
    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return 0;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return maxExtract;
    }

    @Override
    public int getEnergyStored() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getMaxEnergyStored() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return false;
    }

    // TeamReborn EnergyStorage

    @Override
    public long insert(long maxAmount, TransactionContext transaction) {
        return 0;
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        return maxAmount;
    }

    @Override
    public long getAmount() {
        return Long.MAX_VALUE;
    }

    @Override
    public long getCapacity() {
        return Long.MAX_VALUE;
    }

    @Override
    public boolean supportsInsertion() {
        return false;
    }

    @Override
    public boolean supportsExtraction() {
        return true;
    }
}
