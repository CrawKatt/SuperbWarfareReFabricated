package com.atsuishio.superbwarfare.network.message.send

import com.atsuishio.superbwarfare.config.server.MiscConfig
import com.atsuishio.superbwarfare.init.ModComponents
import com.atsuishio.superbwarfare.network.PayloadContext
import com.atsuishio.superbwarfare.network.ServerPacketPayload
import kotlinx.serialization.Serializable

@Serializable
data class TacticalSprintMessage(val sprint: Boolean) : ServerPacketPayload() {
    override fun PayloadContext.handler() {
        val player = sender()

        val cap = ModComponents.PLAYER_VARIABLE.get(player)
        cap.tacticalSprint = MiscConfig.ALLOW_TACTICAL_SPRINT.get() && sprint
        ModComponents.PLAYER_VARIABLE.sync(player)
    }
}
