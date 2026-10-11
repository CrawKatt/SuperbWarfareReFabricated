package com.atsuishio.superbwarfare.capability.player

import com.atsuishio.superbwarfare.tools.createStreamCodec
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.server.level.ServerPlayer
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent

class PlayerDeployComponent : AutoSyncedComponent {
    var value = PlayerDeployState()

    // Deployment is synchronized but never saved or copied on respawn, as in upstream.
    override fun readFromNbt(tag: CompoundTag, registryLookup: HolderLookup.Provider) {
        value = PlayerDeployState()
    }

    override fun writeToNbt(tag: CompoundTag, registryLookup: HolderLookup.Provider) = Unit

    override fun writeSyncPacket(buf: RegistryFriendlyByteBuf, recipient: ServerPlayer) {
        CODEC.encode(buf, value)
    }

    override fun applySyncPacket(buf: RegistryFriendlyByteBuf) {
        value = CODEC.decode(buf)
    }

    companion object {
        private val CODEC = createStreamCodec<PlayerDeployState>()
    }
}
