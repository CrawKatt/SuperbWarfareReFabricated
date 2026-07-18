package com.atsuishio.superbwarfare.capability.energy;

import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;

public class SyncedEntityEnergyStorage extends DynamicEnergyStorage {

    protected SynchedEntityData entityData;
    protected EntityDataAccessor<Integer> energyDataAccessor;

    public SyncedEntityEnergyStorage(long capacity, SynchedEntityData data, EntityDataAccessor<Integer> energyDataAccessor) {
        this(capacity, capacity, capacity, data, energyDataAccessor);
    }

    public SyncedEntityEnergyStorage(long capacity, long maxInsert, long maxExtract, SynchedEntityData data, EntityDataAccessor<Integer> energyDataAccessor) {
        super(() -> capacity, () -> maxInsert, () -> maxExtract);
        this.entityData = data;
        this.energyDataAccessor = energyDataAccessor;
    }

    @Override
    public long insert(long maxAmount, TransactionContext transaction) {
        this.amount = entityData.get(energyDataAccessor);
        long inserted = super.insert(maxAmount, transaction);
        if (inserted > 0) {
            entityData.set(energyDataAccessor, (int) this.amount);
        }
        return inserted;
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        this.amount = entityData.get(energyDataAccessor);
        long extracted = super.extract(maxAmount, transaction);
        if (extracted > 0) {
            entityData.set(energyDataAccessor, (int) amount);
        }
        return extracted;
    }

    @Override
    public long getAmount() {
        return entityData.get(energyDataAccessor);
    }

    @Override
    protected void readSnapshot(Long snapshot) {
        super.readSnapshot(snapshot);
        entityData.set(energyDataAccessor, snapshot.intValue());
    }

    public void deserializeNBT(Tag nbt) {
        if (nbt instanceof NumericTag numericTag) {
            setEnergy(numericTag.getAsInt());
        }
    }

    public void setEnergy(int energy) {
        this.amount = energy;
        entityData.set(energyDataAccessor, energy);
    }

    public void setCapacity(long capacity) {
        this.capacityGetter = () -> capacity;
    }

    public void setMaxExtract(long maxExtract) {
        this.maxExtractGetter = () -> maxExtract;
    }

    public void setMaxReceive(long maxReceive) {
        this.maxInsertGetter = () -> maxReceive;
    }
}
