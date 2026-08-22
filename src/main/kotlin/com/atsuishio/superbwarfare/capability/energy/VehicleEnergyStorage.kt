package com.atsuishio.superbwarfare.capability.energy

import com.atsuishio.superbwarfare.data.vehicle.VehicleData
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext

open class VehicleEnergyStorage(protected var vehicle: VehicleEntity) :
    SyncedEntityEnergyStorage(
        Int.MAX_VALUE,
        vehicle.getEntityData(),
        vehicle.getEnergyDataAccessor()
    ) {

    override fun extractEnergy(maxExtract: Int, simulate: Boolean): Int {
        if (VehicleData.getDefault(vehicle).isDefaultData) return 0

        this.capacity = maxEnergyStored.toLong()
        this.maxExtract = maxEnergyStored.toLong()
        return super.extractEnergy(maxExtract, simulate)
    }

    override fun receiveEnergy(maxReceive: Int, simulate: Boolean): Int {
        if (VehicleData.getDefault(vehicle).isDefaultData) return 0

        this.capacity = maxEnergyStored.toLong()
        this.maxReceive = maxEnergyStored.toLong()
        return super.receiveEnergy(maxReceive, simulate)
    }

    override fun insert(maxAmount: Long, transaction: TransactionContext): Long {
        if (VehicleData.getDefault(vehicle).isDefaultData) return 0

        this.capacity = maxEnergyStored.toLong()
        this.maxReceive = maxEnergyStored.toLong()
        return super.insert(maxAmount, transaction)
    }

    override fun extract(maxAmount: Long, transaction: TransactionContext): Long {
        if (VehicleData.getDefault(vehicle).isDefaultData) return 0

        this.capacity = maxEnergyStored.toLong()
        this.maxExtract = maxEnergyStored.toLong()
        return super.extract(maxAmount, transaction)
    }

    override fun canReceive(): Boolean {
        return !VehicleData.getDefault(vehicle).isDefaultData &&
                super.canReceive() &&
                vehicle.computed().maxEnergy > 0
    }

    override fun canExtract(): Boolean {
        return !VehicleData.getDefault(vehicle).isDefaultData && super.canExtract()
    }

    override fun getMaxEnergyStored(): Int {
        return VehicleData.compute(vehicle).maxEnergy
    }

    override fun getCapacity(): Long {
        return VehicleData.compute(vehicle).maxEnergy.toLong()
    }
}
