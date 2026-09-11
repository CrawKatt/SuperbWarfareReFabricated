package com.atsuishio.superbwarfare.capability.player

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.capability.ModCapabilities
import com.atsuishio.superbwarfare.capability.sync.CapabilitySync
import com.atsuishio.superbwarfare.capability.sync.SyncTarget
import com.atsuishio.superbwarfare.capability.sync.SyncedCapability
import com.atsuishio.superbwarfare.data.gun.Ammo
import com.atsuishio.superbwarfare.serialization.ByteBufDecoder
import com.atsuishio.superbwarfare.serialization.ByteBufEncoder
import dev.onyxstudios.cca.api.v3.component.CopyableComponent
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import java.util.EnumMap
import java.util.function.Consumer

class PlayerVariable : AutoSyncedComponent, CopyableComponent<PlayerVariable>, SyncedCapability {
    private var old: PlayerVariable? = null

    @JvmField
    var ammo: MutableMap<Ammo, Int> = EnumMap(Ammo::class.java)
    var activeThermalImaging: Boolean = false

    /** 玩家变量只对本人有意义 */
    override val syncTarget: SyncTarget
        get() = SyncTarget.SELF

    /** 全量同步：字段顺序必须与 [readSync] 保持一致。 */
    override fun writeSync(encoder: ByteBufEncoder, full: Boolean) {
        encoder.encodeBoolean(activeThermalImaging)
        encoder.encodeInt(Ammo.entries.size)

        for (type in Ammo.entries) {
            encoder.encodeInt(type.get(this))
        }
    }

    override fun readSync(decoder: ByteBufDecoder, full: Boolean) {
        activeThermalImaging = decoder.decodeBoolean()

        val size = decoder.decodeInt()
        for (index in 0 until size) {
            val count = decoder.decodeInt()
            if (index < Ammo.entries.size) {
                // El servidor es la fuente de verdad; no aplicar límites locales.
                ammo[Ammo.entries[index]] = count
            }
        }
    }

    /** Registra el estado previo para comprobar si [modify] cambió algo. */
    fun watch(): PlayerVariable {
        old = copy()
        return this
    }

    fun changed(): Boolean = old != null && old != this

    fun writeToNBT(): CompoundTag = CompoundTag().also { tag ->
        for (type in Ammo.entries) type.set(tag, type.get(this))
        tag.putBoolean("ActiveThermalImaging", activeThermalImaging)
    }

    fun readFromNBT(tag: CompoundTag) {
        for (type in Ammo.entries) type.set(this, type.get(tag))
        activeThermalImaging = tag.getBoolean("ActiveThermalImaging")
    }

    fun copy() = PlayerVariable().also { copy ->
        for (type in Ammo.entries) type.set(copy, type.get(this))
        copy.activeThermalImaging = activeThermalImaging
    }

    override fun readFromNbt(tag: CompoundTag) = readFromNBT(tag)

    override fun writeToNbt(tag: CompoundTag) {
        val written = writeToNBT()
        for (key in written.allKeys) written.get(key)?.let { tag.put(key, it) }
    }

    override fun copyFrom(other: PlayerVariable) {
        for (type in Ammo.entries) type.set(this, type.get(other))
        activeThermalImaging = other.activeThermalImaging
    }

    override fun equals(other: Any?): Boolean {
        if (other !is PlayerVariable) return false
        return Ammo.entries.all { it.get(this) == it.get(other) } &&
            activeThermalImaging == other.activeThermalImaging
    }

    override fun hashCode(): Int = 31 * ammo.hashCode() + activeThermalImaging.hashCode()

    companion object {
        @JvmField
        val ID: ResourceLocation = loc("player_variables")

        @JvmStatic
        fun getOrDefault(entity: Entity): PlayerVariable =
            ModCapabilities.PLAYER_VARIABLE.maybeGet(entity).orElseGet(::PlayerVariable)

        /** Marca los datos para sincronizarlos al final del tick. */
        @JvmStatic
        fun markDirty(entity: Entity) {
            CapabilitySync.markDirty(entity, ID)
        }

        /** Edita la variable y sincroniza solo si cambió. */
        @JvmStatic
        fun modify(entity: Entity, consumer: Consumer<PlayerVariable>) {
            if (entity.level().isClientSide) return
            ModCapabilities.PLAYER_VARIABLE.maybeGet(entity).ifPresent { cap ->
                cap.watch()
                consumer.accept(cap)
                if (cap.changed()) markDirty(entity)
            }
        }
    }
}
