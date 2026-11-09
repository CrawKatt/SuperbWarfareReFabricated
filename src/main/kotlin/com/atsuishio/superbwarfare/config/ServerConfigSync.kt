package com.atsuishio.superbwarfare.config

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.config.server.GunConfig
import com.atsuishio.superbwarfare.config.server.MiscConfig
import com.atsuishio.superbwarfare.network.ClientPacketPayload
import com.atsuishio.superbwarfare.network.message.receive.RealisticShootModeSyncMessage
import com.atsuishio.superbwarfare.network.message.receive.UnrestrictedModificationSyncMessage
import com.atsuishio.superbwarfare.tools.sendPacketTo
import com.atsuishio.superbwarfare.tools.sendPacketToAll
import fuzs.forgeconfigapiport.api.config.v2.ModConfigEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.server.level.ServerPlayer
import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.fml.config.ModConfig
import kotlin.reflect.KProperty0

object ServerConfigSync {
    private val packetFactories = linkedMapOf<KProperty0<*>, () -> ClientPacketPayload>()

    init {
        register(GunConfig::UNRESTRICTED_MODIFICATION, ::UnrestrictedModificationSyncMessage)
        register(MiscConfig::REALISTIC_SHOOT_MODE, ::RealisticShootModeSyncMessage)
    }

    fun register() {
        ServerPlayConnectionEvents.JOIN.register { handler, _, _ -> onPlayerLoggedIn(handler.player) }
        ModConfigEvents.reloading(Mod.MODID).register(ConfigEvents::onReload)
    }

    private fun <T> register(config: KProperty0<ForgeConfigSpec.ConfigValue<T>>, packet: (T) -> ClientPacketPayload) {
        packetFactories[config] = { packet(config.get().get()) }
    }

    fun broadcast(config: ForgeConfigSpec.ConfigValue<*>) {
        val server = Mod.getServer() ?: return
        server.execute {
            if (!SERVER_CONFIG.isLoaded) return@execute
            val factory = packetFactories.entries.firstOrNull { it.key.get() === config }?.value ?: return@execute
            sendPacketToAll(factory())
        }
    }

    fun broadcastAll() {
        val server = Mod.getServer() ?: return
        server.execute {
            if (!SERVER_CONFIG.isLoaded) return@execute
            packetFactories.values.forEach { sendPacketToAll(it()) }
        }
    }

    fun onPlayerLoggedIn(player: ServerPlayer) {
        if (!SERVER_CONFIG.isLoaded) return
        packetFactories.values.forEach { sendPacketTo(player, it()) }
    }

    object ConfigEvents {
        fun onReload(event: ModConfig) {
            if (event.getSpec<ForgeConfigSpec>() === SERVER_CONFIG) broadcastAll()
        }
    }
}
