package com.atsuishio.superbwarfare.capability.energy

import com.atsuishio.superbwarfare.capability.api.IEnergyStorage
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext
import team.reborn.energy.api.EnergyStorage

/**
 * 无限供电能力，纯逆天
 */
class InfinityEnergyStorage : IEnergyStorage, EnergyStorage {
    override fun receiveEnergy(maxReceive: Int, simulate: Boolean): Int {
        return 0
    }

    override fun extractEnergy(maxExtract: Int, simulate: Boolean): Int {
        return maxExtract
    }

    override fun getEnergyStored(): Int {
        return Int.MAX_VALUE
    }

    override fun getMaxEnergyStored(): Int {
        return Int.MAX_VALUE
    }

    override fun canExtract(): Boolean {
        return true
    }

    override fun canReceive(): Boolean {
        return false
    }

    override fun insert(maxAmount: Long, transaction: TransactionContext): Long = 0

    override fun extract(maxAmount: Long, transaction: TransactionContext): Long = maxAmount

    override fun getAmount(): Long = Long.MAX_VALUE

    override fun getCapacity(): Long = Long.MAX_VALUE

    override fun supportsInsertion(): Boolean = false

    override fun supportsExtraction(): Boolean = true
}
