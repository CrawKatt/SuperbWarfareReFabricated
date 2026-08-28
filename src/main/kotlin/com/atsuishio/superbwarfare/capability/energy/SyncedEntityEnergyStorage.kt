package com.atsuishio.superbwarfare.capability.energy

import com.atsuishio.superbwarfare.capability.api.EnergyStorage
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.IntTag
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
) : EnergyStorage(capacity.toLong(), maxReceive.toLong(), maxExtract.toLong(), 0L) {

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

    init {
        // 从entityData同步初始能量值，避免reviveCaps()后内部energy字段与entityData不一致
        this.energy = entityData.get(energyDataAccessor).toLong()
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

    override fun onFinalCommit() {
        entityData.set(energyDataAccessor, this.energy.toInt())
    }

    override fun getAmount(): Long {
        // 获取同步数据，保证客户端能正确获得能量值
        return entityData.get(energyDataAccessor).toLong()
    }

    override fun deserializeNBT(provider: HolderLookup.Provider, nbt: Tag) {
        if (nbt is IntTag) {
            this.energy = nbt.asInt.toLong()
        } else {
            super.deserializeNBT(provider, nbt)
        }

        entityData.set(energyDataAccessor, this.energy.toInt())
    }
}
