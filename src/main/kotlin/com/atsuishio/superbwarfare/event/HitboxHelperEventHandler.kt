package com.atsuishio.superbwarfare.event

import com.atsuishio.superbwarfare.tools.HitboxHelper
import net.minecraft.world.entity.player.Player

object HitboxHelperEventHandler {
    @JvmStatic
    fun onPlayerTick(player: Player) {
        if (!player.level().isClientSide) {
            HitboxHelper.onPlayerTick(player)
        }
    }

    @JvmStatic
    fun onPlayerLoggedOut(player: Player) {
        HitboxHelper.onPlayerLoggedOut(player)
    }
}
