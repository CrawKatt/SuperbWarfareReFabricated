package com.atsuishio.superbwarfare.capability.player

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.capability.ModCapabilities
import com.atsuishio.superbwarfare.data.gun.Ammo
import com.atsuishio.superbwarfare.network.message.receive.PlayerVariablesSyncMessage
import com.atsuishio.superbwarfare.tools.sendPacketTo
import dev.onyxstudios.cca.api.v3.component.CopyableComponent
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import java.util.EnumMap
import java.util.function.Consumer

class PlayerVariable : AutoSyncedComponent, CopyableComponent<PlayerVariable> {
    private var old: PlayerVariable? = null

    @JvmField
    var ammo: MutableMap<Ammo, Int> = EnumMap(Ammo::class.java)
    var activeThermalImaging: Boolean = false

    fun sync(entity: Entity) {
        if (entity !is ServerPlayer) return
        val variable = ModCapabilities.PLAYER_VARIABLE.maybeGet(entity).orElse(null) ?: return
        if (old != null && old == variable) return
        sendPacketTo(entity, PlayerVariablesSyncMessage(entity.id, compareAndUpdate()))
    }

    fun watch(): PlayerVariable {
        old = copy()
        return this
    }

    fun forceUpdate(): MutableMap<Byte, Int> {
        val map = HashMap<Byte, Int>()
        for (type in Ammo.entries) map[type.ordinal.toByte()] = type.get(this)
        map[(-1).toByte()] = if (activeThermalImaging) 1 else 0
        return map
    }

    fun compareAndUpdate(): MutableMap<Byte, Int> {
        val map = HashMap<Byte, Int>()
        val previous = old ?: PlayerVariable()
        for (type in Ammo.entries) {
            val count = type.get(this)
            if (previous.ammo.getOrDefault(type, 0) != count) map[type.ordinal.toByte()] = count
        }
        if (previous.activeThermalImaging != activeThermalImaging) {
            map[(-1).toByte()] = if (activeThermalImaging) 1 else 0
        }
        return map
    }

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

        @JvmStatic
        fun modify(entity: Entity, consumer: Consumer<PlayerVariable>) {
            if (entity.level().isClientSide) return
            ModCapabilities.PLAYER_VARIABLE.maybeGet(entity).ifPresent { variable ->
                variable.watch()
                consumer.accept(variable)
                variable.sync(entity)
            }
        }

        @JvmStatic
        fun registerEvents() {
            ServerPlayConnectionEvents.JOIN.register { handler, _, _ ->
                val player = handler.player
                sendPacketTo(player, PlayerVariablesSyncMessage(player.id, getOrDefault(player).compareAndUpdate()))
            }
            ServerPlayerEvents.AFTER_RESPAWN.register { _, player, _ ->
                sendPacketTo(player, PlayerVariablesSyncMessage(player.id, getOrDefault(player).compareAndUpdate()))
            }
            ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register { player, _, _ ->
                sendPacketTo(player, PlayerVariablesSyncMessage(player.id, getOrDefault(player).forceUpdate()))
            }
        }
    }
}
