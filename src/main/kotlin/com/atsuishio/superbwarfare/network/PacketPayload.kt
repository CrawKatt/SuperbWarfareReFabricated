package com.atsuishio.superbwarfare.network

import net.minecraft.world.entity.player.Player
import net.minecraft.server.level.ServerPlayer

/**
 * Minimal Fabric counterpart of NeoForge's payload context. Scheduling is performed by the
 * Fabric receivers before invoking a payload handler.
 */
class PayloadContext(val player: Player)

sealed class PacketPayload {
    fun handleInternal(context: PayloadContext) {
        with(this) { context.handler() }
    }

    abstract fun PayloadContext.handler()
}

abstract class ServerPacketPayload : PacketPayload() {
    fun PayloadContext.sender() = player as ServerPlayer
}

abstract class ClientPacketPayload : PacketPayload()
