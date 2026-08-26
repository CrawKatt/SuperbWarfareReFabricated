package com.atsuishio.superbwarfare.client.map

import com.atsuishio.superbwarfare.config.server.MapConfig
import com.atsuishio.superbwarfare.event.custom.ClientLevelLifecycleCallback
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents
import net.minecraft.world.level.chunk.LevelChunk

object TacticalMapChunkListener {

    fun isEnabled(): Boolean {
        return try {
            MapConfig.ENABLE_TACTICAL_MAP.get()
        } catch (_: Exception) {
            false
        }
    }

    @JvmStatic
    fun register() {
        ClientChunkEvents.CHUNK_LOAD.register { _, chunk ->
            if (!isEnabled()) return@register
            if (chunk is LevelChunk) {
                TacticalMapCache.queueChunkUpdate(chunk)
            }
        }

        ClientLevelLifecycleCallback.LOAD.register { _, level ->
            if (!isEnabled()) return@register
            val dim = level.dimension().location().toString()
            TacticalMapCache.initForDimension(dim, TacticalMapCache.getWorldIdentifier())
        }

        ClientLevelLifecycleCallback.UNLOAD.register { _, _ ->
            TacticalMapCache.clear()
        }
    }
}
