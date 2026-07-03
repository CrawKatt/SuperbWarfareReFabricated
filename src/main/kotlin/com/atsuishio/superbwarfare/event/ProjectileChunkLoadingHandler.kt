package com.atsuishio.superbwarfare.event

import com.atsuishio.superbwarfare.config.server.ProjectileConfig
import com.atsuishio.superbwarfare.world.saveddata.ProjectileChunkManager
import net.minecraft.server.level.ServerLevel
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

/**
 * Drives [ProjectileChunkManager.tick] at the end of every level tick.
 * Registered on [net.minecraftforge.common.MinecraftForge.EVENT_BUS] in [com.atsuishio.superbwarfare.Mod].
 */
object ProjectileChunkLoadingHandler {

    @SubscribeEvent
    fun onLevelTick(event: TickEvent.LevelTickEvent) {
        if (!ProjectileConfig.PROJECTILE_CHUNK_LOADING.get()) return
        if (event.phase != TickEvent.Phase.END) return
        val level = event.level as? ServerLevel ?: return
        ProjectileChunkManager.tick(level)
    }
}
