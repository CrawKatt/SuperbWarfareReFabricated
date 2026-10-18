
package com.atsuishio.superbwarfare.capability.player

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.init.ModComponents
import kotlinx.serialization.Serializable
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import org.ladysnake.cca.api.v3.component.Component
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent

@Serializable
data class PlayerSpinState(var spinning: Boolean = false) : Component, AutoSyncedComponent {
    override fun readFromNbt(tag: CompoundTag, registryLookup: HolderLookup.Provider) {
        spinning = false
    }

    override fun writeToNbt(tag: CompoundTag, registryLookup: HolderLookup.Provider) {
    }

    override fun writeSyncPacket(buf: RegistryFriendlyByteBuf, recipient: ServerPlayer) {
        buf.writeBoolean(spinning)
    }

    override fun applySyncPacket(buf: RegistryFriendlyByteBuf) {
        spinning = buf.readBoolean()
    }

    companion object {
        val ID: ResourceLocation = loc("player_spin_state")

        @JvmStatic
        fun isSpinning(entity: Entity): Boolean = ModComponents.PLAYER_SPIN_STATE.get(entity).spinning

        @JvmStatic
        fun set(entity: Entity, spinning: Boolean) {
            ModComponents.PLAYER_SPIN_STATE.get(entity).spinning = spinning
            if (!entity.level().isClientSide) {
                ModComponents.PLAYER_SPIN_STATE.sync(entity)
            }
        }
    }
}
