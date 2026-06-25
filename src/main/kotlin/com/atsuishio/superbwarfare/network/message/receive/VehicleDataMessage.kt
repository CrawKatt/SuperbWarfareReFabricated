package com.atsuishio.superbwarfare.network.message.receive

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.data.CustomData
import com.atsuishio.superbwarfare.data.vehicle.DefaultVehicleData
import com.atsuishio.superbwarfare.tools.BufferSerializer
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class VehiclesDataMessage(val data: MutableList<DefaultVehicleData>?) : CustomPacketPayload {
    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload?> {
        return TYPE
    }

    companion object {
        val TYPE: CustomPacketPayload.Type<VehiclesDataMessage?> =
            CustomPacketPayload.Type<VehiclesDataMessage?>(loc("set_vehicles_data"))


        val STREAM_CODEC: StreamCodec<FriendlyByteBuf?, VehiclesDataMessage?> =
            StreamCodec.ofMember<FriendlyByteBuf?, VehiclesDataMessage?>(
                { obj: VehiclesDataMessage?, buf: FriendlyByteBuf? ->
                    buf!!.writeVarInt(obj!!.data!!.size)
                    for (data in obj.data) {
                        buf.writeBytes(BufferSerializer.serialize(data).copy())
                    }
                },
                { buf: FriendlyByteBuf? ->
                    val size = buf!!.readVarInt()
                    val list: ArrayList<DefaultVehicleData> = ArrayList()
                    for (i in 0..<size) {
                        list.add(BufferSerializer.deserialize(buf, DefaultVehicleData()))
                    }
                    VehiclesDataMessage(list)
                }
            )

        fun create(): VehiclesDataMessage {
            return VehiclesDataMessage(CustomData.VEHICLE_DATA.values.stream().toList())
        }

        fun handler(message: VehiclesDataMessage) {
            CustomData.VEHICLE_DATA.clear()

            for (entry in message.data!!) {
                if (CustomData.VEHICLE_DATA.containsKey(entry.id)) continue
                CustomData.VEHICLE_DATA[entry.id] = entry
            }
        }
    }
}