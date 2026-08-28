package com.atsuishio.superbwarfare.block.entity

import com.atsuishio.superbwarfare.capability.energy.EnergyStorageHelper
import com.atsuishio.superbwarfare.entity.living.SenpaiEntity
import com.atsuishio.superbwarfare.init.ModBlockEntities
import com.atsuishio.superbwarfare.init.ModCapabilities
import com.atsuishio.superbwarfare.init.ModTags
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.animal.Animal
import net.minecraft.world.entity.animal.Cow
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import team.reborn.energy.api.EnergyStorage

open class BiogasGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    BlockEntity(ModBlockEntities.BIOGAS_GENERATOR, pos, state) {
    var power: Float = 0f

    fun checkAndGetPowerLevel(): Float {
        if (this.level == null) return 0F
        val above = blockPos.above()
        val state = this.level!!.getBlockState(above)
        if (!state.`is`(Blocks.COMPOSTER)) return 0F
        val list = this.level!!.getEntities(null, AABB(above)) {
            it.isAlive && (it is Animal || it.type.`is`(ModTags.EntityTypes.BIOGAS_GENERATOR_WHITELIST))
        }
        if (list.isEmpty()) return 0F
        var count = 0f
        list.forEach {
            count += when (it) {
                is SenpaiEntity -> {
                    2f
                }

                is Cow -> {
                    1.5f
                }

                else -> {
                    1f
                }
            } * it.boundingBox.size.toFloat()
        }
        count = count.coerceAtMost(48f)
        return count - count * (count - 1) / 2f / 48f
    }

    override fun saveAdditional(pTag: CompoundTag) {
        super.saveAdditional(pTag)
        pTag.putFloat("Power", this.power)
    }

    override fun load(pTag: CompoundTag) {
        super.load(pTag)
        this.power = pTag.getFloat("Power")
    }

    companion object {
        const val ENERGY_RATE: Int = 64

        fun serverTick(level: Level, pos: BlockPos, state: BlockState, entity: BiogasGeneratorBlockEntity) {
            if (level.gameTime % 20 == 0L) {
                entity.power = entity.checkAndGetPowerLevel()
                entity.setChanged()
            }
            val list = mutableListOf<EnergyStorage>()
            for (face in Direction.entries) {
                if (face == Direction.UP) continue

                EnergyStorage.SIDED.find(level, pos.relative(face), face)?.let {
                    if (it.supportsInsertion() && it.amount < it.capacity) {
                        list += it
                    }
                }
            }
            list.forEach { EnergyStorageHelper.insert(it, (entity.power * ENERGY_RATE / list.size).toLong()) }
        }
    }
}
