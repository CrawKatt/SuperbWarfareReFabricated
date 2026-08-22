package com.atsuishio.superbwarfare.network

import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player

/**
 * Fabric adaptation of NeoForge's [net.neoforged.neoforge.network.handling.IPayloadContext].
 *
 * Unifies the client ([net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.Context])
 * and server ([net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.Context]) receiver
 * contexts behind the single API that the message handlers rely on ([player]).
 */
class PayloadContext(val player: Player)

sealed class PacketPayload : CustomPacketPayload {
    override fun type() = payloadTypeMap[this::class.java]!!
    abstract fun PayloadContext.handler()
}

abstract class ServerPacketPayload : PacketPayload() {
    fun PayloadContext.sender() = player as ServerPlayer
}

abstract class ClientPacketPayload : PacketPayload()
