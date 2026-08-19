package com.atsuishio.superbwarfare.capability.energy

import com.atsuishio.superbwarfare.capability.api.EnergyStorage
import net.minecraft.nbt.Tag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.SynchedEntityData

/**
 * 自动同步的实体能量存储能力，会和客户端自动同步实体的当前能量值
 */
open class SyncedEntityEnergyStorage(
    capacity: Int,
    maxReceive: Int,
    maxExtract: Int,
    protected var entityData: SynchedEntityData,
    protected var energyDataAccessor: EntityDataAccessor<Int>
) : EnergyStorage(capacity.toLong(), maxReceive.toLong(), maxExtract.toLong(), 0) {

    constructor(
        capacity: Int,
        data: SynchedEntityData,
        energyDataAccessor: EntityDataAccessor<Int>
    ) : this(
        capacity,
        capacity,
        capacity,
        data,
        energyDataAccessor
    )

    override fun onFinalCommit() {
        entityData.set(energyDataAccessor, energy.toInt())
    }

    override fun getEnergyStored(): Int {
        // 获取同步数据，保证客户端能正确获得能量值
        return entityData.get(energyDataAccessor)
    }

    override fun getAmount(): Long = entityData.get(energyDataAccessor).toLong()

    override fun deserializeNBT(nbt: Tag) {
        super.deserializeNBT(nbt)
        entityData.set(energyDataAccessor, energy.toInt())
    }

    fun setEnergy(energy: Int) {
        this.energy = energy.toLong()
        entityData.set(energyDataAccessor, energy)
    }

    fun setCapacity(capacity: Int) {
        this.capacity = capacity.toLong()
    }

    fun setMaxExtract(maxExtract: Int) {
        this.maxExtract = maxExtract.toLong()
    }

    fun setMaxReceive(maxReceive: Int) {
        this.maxReceive = maxReceive.toLong()
    }
}
