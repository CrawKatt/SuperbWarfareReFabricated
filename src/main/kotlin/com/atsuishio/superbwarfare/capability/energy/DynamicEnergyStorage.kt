package com.atsuishio.superbwarfare.capability.energy

import com.atsuishio.superbwarfare.capability.api.EnergyStorage
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext
import java.util.function.Supplier

open class DynamicEnergyStorage @JvmOverloads constructor(
    protected val maxStorageGetter: Supplier<Int?>,
    protected val maxReceiveGetter: Supplier<Int?> = maxStorageGetter,
    protected val maxExtractGetter: Supplier<Int?> = maxStorageGetter
) : EnergyStorage(Int.MAX_VALUE.toLong()) {

    override fun insert(maxAmount: Long, transaction: TransactionContext): Long {
        updateProps()
        return super.insert(maxAmount, transaction)
    }

    override fun extract(maxAmount: Long, transaction: TransactionContext): Long {
        updateProps()
        return super.extract(maxAmount, transaction)
    }

    override fun supportsInsertion(): Boolean {
        updateProps()
        return super.supportsInsertion()
    }

    override fun supportsExtraction(): Boolean {
        updateProps()
        return super.supportsExtraction()
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
