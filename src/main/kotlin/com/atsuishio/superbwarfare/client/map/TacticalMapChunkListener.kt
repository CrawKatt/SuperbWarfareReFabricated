package com.atsuishio.superbwarfare.client.map

import com.atsuishio.superbwarfare.config.server.MapConfig
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents

object TacticalMapChunkListener {

    fun isEnabled(): Boolean {
        return try {
            MapConfig.ENABLE_TACTICAL_MAP.get()
        } catch (_: Exception) {
            false
        }
    }

    @JvmStatic
    fun init() {
        ClientChunkEvents.CHUNK_LOAD.register { _, chunk ->
            if (!isEnabled()) return@register
            TacticalMapCache.queueChunkUpdate(chunk)
        }

        ClientPlayConnectionEvents.JOIN.register { _, _, client ->
            if (!isEnabled()) return@register
            client.level?.let { level ->
            val worldId = TacticalMapCache.getWorldIdentifier()
                val dim = level.dimension().location().toString()
            TacticalMapCache.initForDimension(dim, worldId)
            }
        }

        ClientPlayConnectionEvents.DISCONNECT.register { _, _ ->
            TacticalMapCache.clear()
        }
    }
}
