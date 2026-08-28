package com.atsuishio.superbwarfare.block.entity

import com.atsuishio.superbwarfare.capability.energy.EnergyStorageHelper
import com.atsuishio.superbwarfare.init.ModBlockEntities
import com.atsuishio.superbwarfare.init.ModCapabilities
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import team.reborn.energy.api.EnergyStorage
import team.reborn.energy.api.base.InfiniteEnergyStorage

/**
 * Energy Data Slot Code based on @GoryMoon's Chargers
 */
open class CreativeChargingStationBlockEntity(pos: BlockPos, state: BlockState) :
    BlockEntity(ModBlockEntities.CREATIVE_CHARGING_STATION, pos, state) {
    var showRange: Boolean = false

    override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag {
        val tag = CompoundTag()
        tag.putBoolean("ShowRange", this.showRange)
        return tag
    }

    override fun getUpdatePacket(): Packet<ClientGamePacketListener> {
        return ClientboundBlockEntityDataPacket.create(this)
    }

    private fun chargeEntity() {
        val level = this.level ?: return
        if (level.gameTime % 20 != 0L) return

        val entities = level.getEntitiesOfClass(
            Entity::class.java, AABB(this.blockPos).inflate(
                CHARGE_RADIUS.toDouble()
            )
        )
        entities.forEach { entity ->
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

    private val energyStorage: EnergyStorage = InfiniteEnergyStorage.INSTANCE

    fun getEnergyStorage(side: Direction?): EnergyStorage {
        return energyStorage
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)
        this.showRange = tag.getBoolean("ShowRange")
    }

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)
        tag.putBoolean("ShowRange", this.showRange)
    }

    companion object {
        const val CHARGE_RADIUS: Int = 8

        @JvmStatic
        fun serverTick(
            pLevel: Level,
            pPos: BlockPos,
            pState: BlockState,
            blockEntity: CreativeChargingStationBlockEntity
        ) {
            blockEntity.chargeEntity()
            blockEntity.chargeBlock()
        }
    }
}
