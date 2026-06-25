package com.atsuishio.superbwarfare.network.message.receive

import com.atsuishio.superbwarfare.data.CustomData
import com.atsuishio.superbwarfare.data.gun.DefaultGunData
import com.atsuishio.superbwarfare.data.gun.GunData
import com.atsuishio.superbwarfare.network.ClientPacketPayload
import com.atsuishio.superbwarfare.network.PayloadContext
import kotlinx.serialization.Serializable

@Serializable
data class GunsDataMessage(val data: List<DefaultGunData>) : ClientPacketPayload() {
    override fun PayloadContext.handler() {
        CustomData.GUN_DATA.clear()

        for (entry in data) {
            if (CustomData.GUN_DATA.containsKey(entry.id)) continue
            CustomData.GUN_DATA[entry.id] = entry
        }

        GunData.DATA_CACHE.invalidateAll()
    }

    companion object {
        fun create(): GunsDataMessage {
            return GunsDataMessage(CustomData.GUN_DATA.values.toList())
        }
    }
}