package com.atsuishio.superbwarfare.network.message.receive

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.data.DataLoader
import com.atsuishio.superbwarfare.serialization.ByteBufDecoder
import com.atsuishio.superbwarfare.serialization.ByteBufEncoder
import com.atsuishio.superbwarfare.serialization.kserializer.CompressedString
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.serializer
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

@Serializable
data class DataSyncMessage(
    val path: String,
    val jsonData: CompressedString,
) : CustomPacketPayload {

    @Suppress("unchecked_cast")
    private fun handle() {
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

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE

    companion object {
        @JvmField
        val TYPE = CustomPacketPayload.Type<DataSyncMessage>(Mod.loc("data_sync"))

        @OptIn(ExperimentalSerializationApi::class)
        private val SERIALIZER = serializer<DataSyncMessage>()

        @JvmField
        val STREAM_CODEC: StreamCodec<FriendlyByteBuf, DataSyncMessage> = StreamCodec.ofMember(
            { message: DataSyncMessage, buf: FriendlyByteBuf ->
                ByteBufEncoder(buf).encodeSerializableValue(SERIALIZER, message)
            },
            { buf: FriendlyByteBuf ->
                ByteBufDecoder(buf).decodeSerializableValue(SERIALIZER)
            }
        )

        @JvmStatic
        fun handler(message: DataSyncMessage) {
            message.handle()
        }
    }
}
