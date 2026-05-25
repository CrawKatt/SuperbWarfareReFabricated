package com.atsuishio.superbwarfare.capability.energy;

import com.atsuishio.superbwarfare.capability.api.EnergyStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import org.jetbrains.annotations.NotNull;

/**
 * 自动同步的实体能量存储能力，会和客户端自动同步实体的当前能量值
 */
public class SyncedEntityEnergyStorage extends EnergyStorage {

    protected SynchedEntityData entityData;
    protected EntityDataAccessor<Integer> energyDataAccessor;

    /**
     * 自动同步的实体能量存储能力
     *
     * @param capacity           能量上限
     * @param data               实体的entityData
     * @param energyDataAccessor 能量的EntityDataAccessor
     */
    public SyncedEntityEnergyStorage(long capacity, SynchedEntityData data, EntityDataAccessor<Integer> energyDataAccessor) {
        this(capacity, capacity, capacity, data, energyDataAccessor);
    }

    public SyncedEntityEnergyStorage(long capacity, long maxReceive, long maxExtract, SynchedEntityData data, EntityDataAccessor<Integer> energyDataAccessor) {
        super(capacity, maxReceive, maxExtract, 0);

        this.entityData = data;
        this.energyDataAccessor = energyDataAccessor;
    }

    public void setEnergy(long energy) {
        this.energy = energy;
        entityData.set(energyDataAccessor, (int) energy);
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }

    public void setMaxExtract(long maxExtract) {
        this.maxExtract = maxExtract;
    }

    public void setMaxReceive(long maxReceive) {
        this.maxReceive = maxReceive;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        var received = super.receiveEnergy(maxReceive, simulate);

        if (!simulate) {
            entityData.set(energyDataAccessor, getEnergyStored());
        }

        return received;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        var extracted = super.extractEnergy(maxExtract, simulate);

        if (!simulate) {
            entityData.set(energyDataAccessor, getEnergyStored());
        }

        return extracted;
    }

    @Override
    public long insert(long maxAmount, TransactionContext transaction) {
        long inserted = super.insert(maxAmount, transaction);
        if (inserted > 0) {
            transaction.addCloseCallback((t, result) -> {
                if (result == TransactionContext.Result.COMMITTED) {
                    entityData.set(energyDataAccessor, (int) this.energy);
                }
            });
        }
        return inserted;
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        long extracted = super.extract(maxAmount, transaction);
        if (extracted > 0) {
            transaction.addCloseCallback((t, result) -> {
                if (result == TransactionContext.Result.COMMITTED) {
                    entityData.set(energyDataAccessor, (int) this.energy);
                }
            });
        }
        return extracted;
    }

    @Override
    public int getEnergyStored() {
        // 获取同步数据，保证客户端能正确获得能量值
        return entityData.get(energyDataAccessor);
    }

    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, @NotNull Tag nbt) {
        if (nbt instanceof IntTag intTag) {
            this.energy = intTag.getAsInt();
        } else {
            super.deserializeNBT(provider, nbt);
        }
        entityData.set(energyDataAccessor, (int) this.energy);
    }
}
