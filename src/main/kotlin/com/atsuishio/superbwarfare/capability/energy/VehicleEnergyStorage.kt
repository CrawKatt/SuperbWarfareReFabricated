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

    override fun insert(maxAmount: Long, transaction: TransactionContext): Long {
        if (VehicleData.getDefault(vehicle).isDefaultData) return 0

        this.capacity = getCapacity()
        this.maxReceive = this.capacity
        return super.insert(maxAmount, transaction)
    }

    override fun extract(maxAmount: Long, transaction: TransactionContext): Long {
        if (VehicleData.getDefault(vehicle).isDefaultData) return 0

        this.capacity = getCapacity()
        this.maxExtract = this.capacity
        return super.extract(maxAmount, transaction)
    }

    override fun supportsInsertion(): Boolean {
        return !VehicleData.getDefault(vehicle).isDefaultData &&
                super.supportsInsertion() &&
                vehicle.computed().maxEnergy > 0
    }

    override fun supportsExtraction(): Boolean {
        return !VehicleData.getDefault(vehicle).isDefaultData && super.supportsExtraction()
    }

    override fun getCapacity(): Long {
        return VehicleData.compute(vehicle).maxEnergy.toLong()
    }
}