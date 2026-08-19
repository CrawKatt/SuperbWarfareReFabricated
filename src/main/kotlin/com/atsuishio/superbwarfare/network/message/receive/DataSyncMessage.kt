package com.atsuishio.superbwarfare.network.message.receive

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.data.DataLoader
import com.atsuishio.superbwarfare.network.ClientPacketPayload
import com.atsuishio.superbwarfare.network.PayloadContext
import com.atsuishio.superbwarfare.serialization.kserializer.CompressedString
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer

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

        val map = if (data.isKtData) {
            DataLoader.JSON.decodeFromString(serializer(data.mapType.type), jsonData)
        } else {
            DataLoader.GSON.fromJson(jsonData, data.mapType)
        } as Map<String, Any>

        data.dataMap.clear()
        data.dataMap.putAll(map)
        data.onReload?.accept(map)
    }
}