package com.atsuishio.superbwarfare.block.entity

import com.atsuishio.superbwarfare.capability.energy.EnergyStorageHelper
import com.atsuishio.superbwarfare.init.ModBlockEntities
import com.atsuishio.superbwarfare.init.ModCapabilities
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import team.reborn.energy.api.EnergyStorage
import team.reborn.energy.api.base.InfiniteEnergyStorage

open class CreativeChargingStationBlockEntity(pos: BlockPos, state: BlockState) :
    BlockEntity(ModBlockEntities.CREATIVE_CHARGING_STATION, pos, state) {

    private val energyStorage: EnergyStorage = InfiniteEnergyStorage.INSTANCE

    fun getEnergyStorage(side: Direction?): EnergyStorage = energyStorage

    private fun chargeEntity() {
        val level = this.level ?: return
        if (level.gameTime % 20 != 0L) return

        level.getEntitiesOfClass(
            Entity::class.java,
            AABB(blockPos).inflate(CHARGE_RADIUS.toDouble())
        ).forEach { entity ->
            val cap = ModCapabilities.getEntityEnergyStorage(entity)
            if (cap == null || !cap.supportsInsertion()) return@forEach

            EnergyStorageHelper.insert(cap, Long.MAX_VALUE)
        }
    }

    private fun chargeBlock() {
        val level = this.level ?: return
        for (direction in Direction.entries) {
            val blockEntity = level.getBlockEntity(this.blockPos.relative(direction)) ?: continue

            val energy = EnergyStorage.SIDED.find(level, blockEntity.blockPos, direction)
            if (energy == null || blockEntity is CreativeChargingStationBlockEntity) continue

            if (energy.supportsInsertion() && energy.amount < energy.capacity) {
                EnergyStorageHelper.insert(energy, Long.MAX_VALUE)
                blockEntity.setChanged()
            }
        }
    }

    override fun getUpdatePacket(): ClientboundBlockEntityDataPacket? =
        ClientboundBlockEntityDataPacket.create(this)

    companion object {
        const val CHARGE_RADIUS = 8

        @JvmStatic
        fun serverTick(blockEntity: CreativeChargingStationBlockEntity) {
            if (blockEntity.level == null) return
            blockEntity.chargeEntity()
            blockEntity.chargeBlock()
        }
    }
}
