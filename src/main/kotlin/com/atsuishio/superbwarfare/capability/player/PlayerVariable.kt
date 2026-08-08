package com.atsuishio.superbwarfare.capability.player

import com.atsuishio.superbwarfare.data.gun.Ammo
import com.atsuishio.superbwarfare.init.ModComponents
import com.atsuishio.superbwarfare.network.message.receive.PlayerVariablesSyncMessage
import com.atsuishio.superbwarfare.tools.sendPacket
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import org.ladysnake.cca.api.v3.component.Component
import org.ladysnake.cca.api.v3.component.CopyableComponent
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent
import java.util.EnumMap
import java.util.function.Consumer

class PlayerVariable : Component, AutoSyncedComponent, CopyableComponent<PlayerVariable> {
    private var old: PlayerVariable? = null

    @JvmField
    var ammo: MutableMap<Ammo, Int> = EnumMap(Ammo::class.java)
    var activeThermalImaging: Boolean = false

    fun sync(entity: Entity) {
        val newVariable = getOrDefault(entity)

        if (old != null && old == newVariable) return

        if (entity is ServerPlayer) {
            entity.sendPacket(
                PlayerVariablesSyncMessage(
                    entity.id,
                    compareAndUpdate()
                )
            )
        }
    }

    fun watch(): PlayerVariable {
        this.old = this.copy()
        return this
    }

    fun forceUpdate(): MutableMap<Byte, Int> {
        val map = hashMapOf<Byte, Int>()

        for (type in Ammo.entries) {
            map[type.ordinal.toByte()] = type.get(this)
        }

        map[(-1).toByte()] = if (this.activeThermalImaging) 1 else 0
        return map
    }

    fun compareAndUpdate(): MutableMap<Byte, Int> {
        val map = hashMapOf<Byte, Int>()
        val old = this.old ?: PlayerVariable()

        for (type in Ammo.entries) {
            val oldCount = old.ammo.getOrDefault(type, 0)
            val newCount = type.get(this)

            if (oldCount != newCount) {
                map[type.ordinal.toByte()] = newCount
            }
        }

        if (old.activeThermalImaging != this.activeThermalImaging) {
            map[(-1).toByte()] = if (this.activeThermalImaging) 1 else 0
        }

        return map
    }

    fun writeToNBT(): CompoundTag {
        val nbt = CompoundTag()

        for (type in Ammo.entries) {
            type.set(nbt, type.get(this))
        }

        nbt.putBoolean("ActiveThermalImaging", activeThermalImaging)

        return nbt
    }

    fun readFromNBT(tag: CompoundTag) {
        for (type in Ammo.entries) {
            type.set(this, type.get(tag))
        }

        activeThermalImaging = tag.getBoolean("ActiveThermalImaging")
    }

    fun copy(): PlayerVariable {
        val clone = PlayerVariable()

        for (type in Ammo.entries) {
            type.set(clone, type.get(this))
        }

        clone.activeThermalImaging = this.activeThermalImaging

        return clone
    }

    override fun readFromNbt(tag: CompoundTag, registryLookup: HolderLookup.Provider) {
        readFromNBT(tag)
    }

    override fun writeToNbt(tag: CompoundTag, registryLookup: HolderLookup.Provider) {
        val written = writeToNBT()

        for (key in written.allKeys) {
            tag.put(key, written.get(key))
        }
    }

    override fun copyFrom(original: PlayerVariable, registryLookup: HolderLookup.Provider) {
        for (type in Ammo.entries) {
            type.set(this, type.get(original))
        }

        this.activeThermalImaging = original.activeThermalImaging
        this.old = original.old?.copy()
    }

    override fun equals(other: Any?): Boolean {
        if (other !is PlayerVariable) return false

        for (type in Ammo.entries) {
            if (type.get(this) != type.get(other)) return false
        }

        return activeThermalImaging == other.activeThermalImaging
    }

    override fun hashCode(): Int {
        var result = ammo.hashCode()
        result = 31 * result + activeThermalImaging.hashCode()
        return result
    }

    companion object {
        @JvmStatic
        fun modify(player: Player, consumer: Consumer<PlayerVariable>) {
            val cap = ModComponents.PLAYER_VARIABLE.get(player).watch()
            consumer.accept(cap)
            cap.sync(player)
        }

        @JvmStatic
        fun getOrDefault(entity: Entity): PlayerVariable {
            return ModComponents.PLAYER_VARIABLE.get(entity)
        }

        @JvmStatic
        fun registerEvents() {
            ServerPlayConnectionEvents.JOIN.register { handler, _, _ ->
                val player = handler.player

                player.sendPacket(
                    PlayerVariablesSyncMessage(
                        player.id,
                        getOrDefault(player).compareAndUpdate()
                    )
                )
            }

            ServerPlayerEvents.AFTER_RESPAWN.register { _, newPlayer, _ ->
                newPlayer.sendPacket(
                    PlayerVariablesSyncMessage(
                        newPlayer.id,
                        getOrDefault(newPlayer).compareAndUpdate()
                    )
                )
            }

            ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register { player, _, _ ->
                player.sendPacket(
                    PlayerVariablesSyncMessage(
                        player.id,
                        getOrDefault(player).forceUpdate()
                    )
                )
            }
        }
    }
}
