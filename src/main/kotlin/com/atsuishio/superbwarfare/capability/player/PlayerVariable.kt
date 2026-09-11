package com.atsuishio.superbwarfare.capability.player

import com.atsuishio.superbwarfare.data.gun.Ammo
import com.atsuishio.superbwarfare.init.ModComponents
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
import org.ladysnake.cca.api.v3.component.ComponentProvider
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent
import java.util.EnumMap
import java.util.function.Consumer

class PlayerVariable(private val owner: Player? = null) : Component, AutoSyncedComponent, CopyableComponent<PlayerVariable> {
    @JvmField
    var ammo: MutableMap<Ammo, Int> = EnumMap(Ammo::class.java)
    var activeThermalImaging: Boolean = false

    override fun shouldSyncWith(player: ServerPlayer): Boolean {
        return owner === player
    }

    /** Synchronizes this private component with the player who owns it. */
    fun sync(entity: Entity) {
        if (entity is ServerPlayer) {
            ModComponents.PLAYER_VARIABLE.syncWith(entity, entity as ComponentProvider)
        }
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
        val clone = PlayerVariable(owner)

        for (type in Ammo.entries) {
            type.set(clone, type.get(this))
        }

        clone.activeThermalImaging = activeThermalImaging

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

        activeThermalImaging = original.activeThermalImaging
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
            val cap = ModComponents.PLAYER_VARIABLE.get(player)
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
                getOrDefault(player).sync(player)
            }

            ServerPlayerEvents.AFTER_RESPAWN.register { _, newPlayer, _ ->
                getOrDefault(newPlayer).sync(newPlayer)
            }

            ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register { player, _, _ ->
                getOrDefault(player).sync(player)
            }
        }
    }
}
