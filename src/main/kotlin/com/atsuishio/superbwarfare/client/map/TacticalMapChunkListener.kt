package com.atsuishio.superbwarfare.client.map

import com.atsuishio.superbwarfare.config.server.MapConfig
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
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

        ClientLifecycleEvents.CLIENT_STARTED.register { client ->
            val level = client.level ?: return@register
            val dim = level.dimension().location().toString()
            TacticalMapCache.initForDimension(dim, TacticalMapCache.getWorldIdentifier())
        }

        ClientLifecycleEvents.CLIENT_STOPPING.register { _ ->
            // Always clear — config may already be inaccessible during world
            // teardown, and stale data bleeds into the next world otherwise.
            TacticalMapCache.clear()
        }
    }
}
