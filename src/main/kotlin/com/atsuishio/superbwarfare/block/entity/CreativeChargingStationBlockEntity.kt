package com.atsuishio.superbwarfare.block.entity

import com.atsuishio.superbwarfare.capability.api.IEnergyStorage
import com.atsuishio.superbwarfare.capability.energy.InfinityEnergyStorage
import com.atsuishio.superbwarfare.init.ModBlockEntities
import com.atsuishio.superbwarfare.init.ModCapabilities
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB

open class CreativeChargingStationBlockEntity(pos: BlockPos, state: BlockState) :
    BlockEntity(ModBlockEntities.CREATIVE_CHARGING_STATION, pos, state) {

    private val energyStorage: IEnergyStorage = InfinityEnergyStorage()

    fun getEnergyStorage(side: Direction?): IEnergyStorage = energyStorage

    private fun chargeEntity() {
        val level = this.level ?: return
        if (level.gameTime % 20 != 0L) return

        level.getEntitiesOfClass(
            Entity::class.java,
            AABB(blockPos).inflate(CHARGE_RADIUS.toDouble())
        ).forEach { entity ->
            val cap = ModCapabilities.ENERGY_ENTITY.find(entity, null) ?: return@forEach
            if (cap.canReceive()) cap.receiveEnergy(Int.MAX_VALUE, false)
        }
    }

    private fun chargeBlock() {
        val level = this.level ?: return
        for (direction in Direction.entries) {
            val blockEntity = level.getBlockEntity(blockPos.relative(direction)) ?: continue
            if (blockEntity is CreativeChargingStationBlockEntity) continue
            val cap = ModCapabilities.ENERGY_BLOCK.find(level, blockEntity.blockPos, null) ?: continue
            if (cap.canReceive() && cap.energyStored < cap.maxEnergyStored) {
                cap.receiveEnergy(Int.MAX_VALUE, false)
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
