package com.atsuishio.superbwarfare.block.entity

import com.atsuishio.superbwarfare.block.FuMO25Block
import com.atsuishio.superbwarfare.capability.api.EnergyStorage
import com.atsuishio.superbwarfare.capability.api.IEnergyStorage
import com.atsuishio.superbwarfare.config.server.MiscConfig
import com.atsuishio.superbwarfare.config.server.VehicleConfig
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import com.atsuishio.superbwarfare.init.ModBlockEntities
import com.atsuishio.superbwarfare.init.ModSounds
import com.atsuishio.superbwarfare.inventory.menu.FuMO25Menu
import com.atsuishio.superbwarfare.network.dataslot.ContainerEnergyData
import com.atsuishio.superbwarfare.network.message.receive.EntitySyncMessage
import com.atsuishio.superbwarfare.tools.SeekTool
import com.atsuishio.superbwarfare.tools.VectorTool
import com.atsuishio.superbwarfare.tools.sendPacketTo
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundSource
import net.minecraft.util.Mth
import net.minecraft.world.MenuProvider
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerLevelAccess
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3
import software.bernie.geckolib.animatable.GeoBlockEntity
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.animation.AnimatableManager
import software.bernie.geckolib.constant.dataticket.SerializableDataTicket
import software.bernie.geckolib.util.GeckoLibUtil
import java.util.UUID
import javax.annotation.ParametersAreNonnullByDefault
import kotlin.math.abs

open class FuMO25BlockEntity(
    pos: BlockPos,
    state: BlockState
) : BlockEntity(ModBlockEntities.FUMO_25, pos, state), MenuProvider, GeoBlockEntity {

    private val cache: AnimatableInstanceCache = GeckoLibUtil.createInstanceCache(this)

    private val energyStorage: IEnergyStorage = EnergyStorage(MAX_ENERGY.toLong())

    var type: FuncType = FuncType.NORMAL
    var powered: Boolean = false
    var tickO: Int = 0
    var tick: Int = 0
    var ownerUUID: UUID? = null

    protected val dataAccess: ContainerEnergyData = object : ContainerEnergyData {
        override fun get(index: Int): Long {
            return when (index) {
                0 -> this@FuMO25BlockEntity.energyStorage.energyStored
                1 -> this@FuMO25BlockEntity.type.ordinal
                2 -> if (this@FuMO25BlockEntity.powered) 1 else 0
                3 -> this@FuMO25BlockEntity.tick
                4 -> this@FuMO25BlockEntity.tickO
                else -> 0
            }.toLong()
        }

        override fun set(index: Int, value: Long) {
            when (index) {
                0 -> this@FuMO25BlockEntity.energyStorage.receiveEnergy(value.toInt(), false)
                1 -> this@FuMO25BlockEntity.type = FuncType.entries[value.toInt()]
                2 -> this@FuMO25BlockEntity.powered = value == 1L
                3 -> this@FuMO25BlockEntity.tick = value.toInt()
                4 -> this@FuMO25BlockEntity.tickO = value.toInt()
            }
        }

        override fun getCount(): Int {
            return MAX_DATA_COUNT
        }
    }

    fun getEnergyStorage(direction: Direction?): IEnergyStorage {
        return this.energyStorage
    }

    private fun setGlowEffect() {
        if (this.type != FuncType.GLOW) return

        val level = this.level ?: return
        val pos = this.blockPos

        val entities: List<Entity> = SeekTool.getEntitiesWithinRange(pos, level, GLOW_RANGE)

        entities.forEach { entity ->
            if (entity is LivingEntity) {
                entity.addEffect(
                    MobEffectInstance(
                        MobEffects.GLOWING,
                        110,
                        0,
                        true,
                        false
                    )
                )
            }
        }
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)

        val energyTag = tag.get("Energy")
        if (energyTag != null) {
            (energyStorage as EnergyStorage).deserializeNBT(registries, energyTag)
        }
        this.type = FuncType.entries[tag.getInt("Type").coerceIn(0, 3)]
        this.powered = tag.getBoolean("Powered")
        this.tick = tag.getInt("Tick")
        this.tickO = tag.getInt("TickO")

        if (tag.contains("OwnerUUID")) {
            this.ownerUUID = tag.getUUID("OwnerUUID")
        }
    }

    @ParametersAreNonnullByDefault
    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)

        tag.put("Energy", (energyStorage as EnergyStorage).serializeNBT(registries))
        tag.putInt("Type", this.type.ordinal)
        tag.putBoolean("Powered", this.powered)
        tag.putInt("Tick", this.tick)
        tag.putInt("TickO", this.tickO)

        this.ownerUUID?.let { tag.putUUID("OwnerUUID", it) }
    }

    override fun getDisplayName(): Component {
        return Component.empty()
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? {
        val level = this.level ?: return null
        return FuMO25Menu(
            containerId,
            playerInventory,
            ContainerLevelAccess.create(level, this.blockPos),
            this.dataAccess
        )
    }

    fun sync() {
        val level = this.level ?: return
        if (level.isClientSide) return
        this.setChanged()
        level.sendBlockUpdated(this.worldPosition, this.blockState, this.blockState, 3)
    }

    override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag {
        val tag = CompoundTag()
        this.saveAdditional(tag, registries)
        return tag
    }

    override fun getUpdatePacket(): ClientboundBlockEntityDataPacket {
        return ClientboundBlockEntityDataPacket.create(this)
    }

    override fun registerControllers(data: AnimatableManager.ControllerRegistrar) {
    }

    override fun getAnimatableInstanceCache(): AnimatableInstanceCache {
        return this.cache
    }

    enum class FuncType {
        NORMAL,
        WIDER,
        GLOW,
        GUIDE
    }

    companion object {
        @JvmField
        var FUMO25_TICK: SerializableDataTicket<Int>? = null

        const val MAX_ENERGY: Int = 1000000

        const val DEFAULT_RANGE: Int = 96
        const val MAX_RANGE: Int = 128
        const val GLOW_RANGE: Double = 64.0

        const val DEFAULT_ENERGY_COST: Int = 256
        const val MAX_ENERGY_COST: Int = 1024

        const val DEFAULT_MIN_ENERGY: Int = 64000

        const val MAX_DATA_COUNT: Int = 5

        @JvmStatic
        fun serverTick(
            level: Level,
            pos: BlockPos,
            state: BlockState,
            blockEntity: FuMO25BlockEntity
        ) {
            if (!MiscConfig.SYNC_ENTITY_OVER_RANGE.get()) return
            val energyStorage = blockEntity.getEnergyStorage(null)
            val energy = energyStorage.energyStored

            blockEntity.tickO = blockEntity.tick

            if (state.getValue(FuMO25Block.POWERED)) {
                blockEntity.tick++
                blockEntity.setAnimData(FUMO25_TICK, blockEntity.tick)
                blockEntity.sync()
            }

            val funcType = blockEntity.type
            val energyCost = if (funcType == FuncType.WIDER) {
                MAX_ENERGY_COST
            } else {
                DEFAULT_ENERGY_COST
            }

            val f = Mth.sin(blockEntity.tick * (Math.PI.toFloat() / 180f)).toDouble()
            val f1 = -Mth.cos(blockEntity.tick * (Math.PI.toFloat() / 180f)).toDouble()

            val direct = Vec3(f, 0.0, f1)

            if (energy < energyCost) {
                if (state.getValue(FuMO25Block.POWERED)) {
                    level.setBlockAndUpdate(
                        pos,
                        state.setValue(FuMO25Block.POWERED, false)
                    )

                    level.playSound(
                        null,
                        pos,
                        ModSounds.RADAR_SEARCH_END,
                        SoundSource.BLOCKS,
                        1f,
                        1f
                    )

                    blockEntity.powered = false
                    setChanged(level, pos, state)
                }
            } else {
                if (!state.getValue(FuMO25Block.POWERED)) {
                    if (energy >= DEFAULT_MIN_ENERGY) {
                        level.setBlockAndUpdate(
                            pos,
                            state.setValue(FuMO25Block.POWERED, true)
                        )

                        level.playSound(
                            null,
                            pos,
                            ModSounds.RADAR_SEARCH_START,
                            SoundSource.BLOCKS,
                            1f,
                            1f
                        )

                        blockEntity.powered = true
                        setChanged(level, pos, state)
                    }
                } else {
                    energyStorage.extractEnergy(energyCost, false)

                    if (blockEntity.tick == 360) {
                        level.playSound(
                            null,
                            pos,
                            ModSounds.RADAR_SEARCH_IDLE,
                            SoundSource.BLOCKS,
                            1f,
                            1f
                        )
                    }

                    if (blockEntity.tick % 100 == 0) {
                        blockEntity.setGlowEffect()
                    }

                    val uuid = blockEntity.ownerUUID
                    if (uuid != null) {
                        val owner = level.getPlayerByUUID(uuid)
                        if (owner != null && level is ServerLevel) {
                            scanEntities(level, pos, blockEntity, owner, direct)
                        }
                    }
                }
            }

            val deltaT = abs(blockEntity.tick - blockEntity.tickO)
            while (blockEntity.tick > 360) {
                blockEntity.tick -= 360
                blockEntity.tickO = blockEntity.tick - deltaT
            }
            while (blockEntity.tick <= 0) {
                blockEntity.tick += 360
                blockEntity.tickO = deltaT + blockEntity.tick
            }
        }

        fun scanEntities(
            level: ServerLevel,
            pos: BlockPos,
            blockEntity: FuMO25BlockEntity,
            player: Player,
            vec3: Vec3
        ) {
            if (level.server.tickCount % MiscConfig.SYNC_ENTITY_INTERVAL.get() != 0) return

            val range = if (blockEntity.type == FuncType.WIDER) 2048 else 1024
            val hostileList = level.allEntities.asSequence().mapNotNull {
                val seekRange =
                    range * range * if (it is VehicleEntity && !it.isWreck) it.computed().trackDistanceMultiply * it.computed().trackDistanceMultiply else 1.0
                val flag = (it is VehicleEntity || VehicleConfig.inScanList(it.type))
                        && SeekTool.NOT_IN_SMOKE.test(it)
                        && it.distanceToSqr(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble()) <= seekRange
                        && !SeekTool.IS_FRIENDLY.test(player, it)
                        && SeekTool.calculateAngle(
                    Vec3(
                        pos.x.toDouble() + 0.5,
                        pos.y.toDouble() + 2.5,
                        pos.z.toDouble() + 0.5
                    ), vec3, it
                ) < 60 && VectorTool.checkNoClip(
                    Vec3(
                        pos.x.toDouble() + 0.5,
                        pos.y.toDouble() + 2.5,
                        pos.z.toDouble() + 0.5
                    ), it.eyePosition, level
                )
                if (!flag) return@mapNotNull null
                EntitySyncMessage.SyncedEntity(
                    it.id,
                    BuiltInRegistries.ENTITY_TYPE.getKey(it.type),
                    it.position(),
                    it.deltaMovement,
                    CompoundTag().also { tag -> it.saveWithoutId(tag) }
                )
            }.toList()

            level.players()
                .asSequence()
                .filter { SeekTool.IS_FRIENDLY.test(player, it) }
                .forEach { sendPacketTo(it, EntitySyncMessage(level.dimension().location(), hostileList, false)) }
        }
    }
}
