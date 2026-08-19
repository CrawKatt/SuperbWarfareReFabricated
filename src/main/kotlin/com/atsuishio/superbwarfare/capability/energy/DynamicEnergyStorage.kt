package com.atsuishio.superbwarfare.capability.energy

import com.atsuishio.superbwarfare.capability.api.EnergyStorage
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext
import java.util.function.Supplier

open class DynamicEnergyStorage @JvmOverloads constructor(
    protected val maxStorageGetter: Supplier<Int?>,
    protected val maxReceiveGetter: Supplier<Int?> = maxStorageGetter,
    protected val maxExtractGetter: Supplier<Int?> = maxStorageGetter
) : EnergyStorage(Int.MAX_VALUE.toLong()) {

    override fun extractEnergy(maxExtract: Int, simulate: Boolean): Int {
        updateProps()
        return super.extractEnergy(maxExtract, simulate)
    }

    override fun receiveEnergy(maxReceive: Int, simulate: Boolean): Int {
        updateProps()
        return super.receiveEnergy(maxReceive, simulate)
    }

    override fun insert(maxAmount: Long, transaction: TransactionContext): Long {
        updateProps()
        return super.insert(maxAmount, transaction)
    }

    override fun extract(maxAmount: Long, transaction: TransactionContext): Long {
        updateProps()
        return super.extract(maxAmount, transaction)
    }

    override fun canReceive(): Boolean {
        updateProps()
        return super.canReceive()
    }

    override fun canExtract(): Boolean {
        updateProps()
        return super.canExtract()
    }

    override fun getMaxEnergyStored(): Int {
        updateProps()
        return super.getMaxEnergyStored()
    }

    override fun getCapacity(): Long {
        updateProps()
        return super.getCapacity()
    }

    protected fun updateProps() {
        this.capacity = maxStorageGetter.get()!!.toLong()
        this.maxExtract = maxExtractGetter.get()!!.toLong()
        this.maxReceive = maxReceiveGetter.get()!!.toLong()
    }
}
