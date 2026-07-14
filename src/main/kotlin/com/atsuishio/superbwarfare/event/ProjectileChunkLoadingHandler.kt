package com.atsuishio.superbwarfare.event

import com.atsuishio.superbwarfare.config.server.ProjectileConfig
import com.atsuishio.superbwarfare.world.saveddata.ProjectileChunkManager
import net.minecraft.server.level.ServerLevel
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.tick.LevelTickEvent

/**
 * Drives [ProjectileChunkManager.tick] at the end of every level tick.
 */
object ProjectileChunkLoadingHandler {

    @SubscribeEvent
    fun onLevelTick(event: LevelTickEvent.Post) {
        if (!ProjectileConfig.PROJECTILE_CHUNK_LOADING.get()) return
        val level = event.level as? ServerLevel ?: return
        ProjectileChunkManager.tick(level)
    }
}
