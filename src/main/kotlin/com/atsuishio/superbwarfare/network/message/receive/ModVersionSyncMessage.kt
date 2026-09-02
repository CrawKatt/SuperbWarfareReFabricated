package com.atsuishio.superbwarfare.network.message.receive

import com.atsuishio.superbwarfare.event.ClientEventHandler
import com.atsuishio.superbwarfare.event.ModVersionEventHandler
import com.atsuishio.superbwarfare.ksp.annotation.RegisterPacket
import com.atsuishio.superbwarfare.network.ClientPacketPayload
import com.atsuishio.superbwarfare.network.PayloadContext
import kotlinx.serialization.Serializable

@Serializable
@RegisterPacket
data class ModVersionSyncMessage(val previous: String, val current: String) : ClientPacketPayload() {
    override fun PayloadContext.handler() {
        if (ModVersionEventHandler.updateClient(previous, current)) {
            ClientEventHandler.onPlayerLoggedIn()
        }
    }
}
