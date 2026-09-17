package com.atsuishio.superbwarfare.network.message.receive

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.data.DataLoader
import com.atsuishio.superbwarfare.ksp.annotation.RegisterPacket
import com.atsuishio.superbwarfare.network.ClientPacketPayload
import com.atsuishio.superbwarfare.network.PayloadContext
import com.atsuishio.superbwarfare.serialization.kserializer.CompressedString
import kotlinx.serialization.Serializable

@RegisterPacket
@Serializable
data class DataSyncMessage(
    val path: String,
    val jsonData: CompressedString,
) : ClientPacketPayload() {

    @Suppress("UNCHECKED_CAST")
    override fun PayloadContext.handler() {
        val data = DataLoader.LOADED_DATA[path] ?: run {
            Mod.LOGGER.error("unknown data path $path!")
            return
        }

        val map = DataLoader.JSON.decodeFromString(data.mapSerializer, jsonData)

        data.dataMap.clear()
        data.dataMap.putAll(map)
        data.onReload?.accept(map)
    }
}
