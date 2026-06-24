package com.atsuishio.superbwarfare.tools

import com.atsuishio.superbwarfare.network.message.receive.GunsDataMessage
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.EndDataPackReload
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.network.ServerGamePacketListenerImpl
import net.minecraft.server.packs.resources.CloseableResourceManager
import java.util.*

object GunsTool {
    fun register() {
        ServerPlayConnectionEvents.JOIN.register(ServerPlayConnectionEvents.Join { handler: ServerGamePacketListenerImpl?, sender: PacketSender?, server: MinecraftServer? ->
            onPlayerLogin(
                handler!!.getPlayer()
            )
        })
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register(EndDataPackReload { server: MinecraftServer?, serverResourceManager: CloseableResourceManager?, success: Boolean ->
            onDataPackSync(
                server!!
            )
        })
    }

    fun onPlayerLogin(player: ServerPlayer) {
        val server = player.getServer()
        if (server != null && server.isSingleplayerOwner(player.gameProfile)) {
            return
        }

        ServerPlayNetworking.send(player, GunsDataMessage.create())
    }

    fun onDataPackSync(server: MinecraftServer) {
        val message = GunsDataMessage.create()
        for (player in server.playerList.players) {
            if (server.isSingleplayerOwner(player.gameProfile)) {
                continue
            }

            ServerPlayNetworking.send(player, message)
        }
    }

    @JvmStatic
    fun getGunDoubleTag(tag: CompoundTag, name: String): Double {
        return getGunDoubleTag(tag, name, 0.0)
    }

    fun getGunDoubleTag(tag: CompoundTag, name: String, defaultValue: Double): Double {
        val data = tag.getCompound("GunData")
        if (!data.contains(name)) return defaultValue
        return data.getDouble(name)
    }

    fun getGunUUID(tag: CompoundTag): UUID? {
        if (!tag.contains("GunData")) return null

        val data = tag.getCompound("GunData")
        if (!data.hasUUID("UUID")) return null
        return data.getUUID("UUID")
    }
}