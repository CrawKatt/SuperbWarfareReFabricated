package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.data.gun.GunData
import io.netty.buffer.ByteBuf
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.syncher.EntityDataSerializer
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.world.phys.Vec3

object ModSerializers {

    @JvmField
    val INT_LIST_SERIALIZER: EntityDataSerializer<List<Int>> =
        EntityDataSerializer.forValueType(
            ByteBufCodecs.VAR_INT.apply(ByteBufCodecs.list())
        )

    @JvmField
    val FLOAT_LIST_SERIALIZER: EntityDataSerializer<List<Float>> =
        EntityDataSerializer.forValueType(
            ByteBufCodecs.FLOAT.apply(ByteBufCodecs.list())
        )

    @JvmField
    val VEC3_SERIALIZER: EntityDataSerializer<Vec3> =
        EntityDataSerializer.forValueType(
            object : StreamCodec<ByteBuf, Vec3> {
                override fun decode(buf: ByteBuf) = Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble())

                override fun encode(buf: ByteBuf, value: Vec3) {
                    buf.writeDouble(value.x)
                    buf.writeDouble(value.y)
                    buf.writeDouble(value.z)
                }
            }
        )

    @JvmField
    val VEHICLE_GUN_DATA_MAP_SERIALIZER: EntityDataSerializer<Map<String, GunData>> =
        object : EntityDataSerializer<Map<String, GunData>> {
            override fun codec(): StreamCodec<in RegistryFriendlyByteBuf, Map<String, GunData>> {
                return ByteBufCodecs.map(
                    { HashMap(it) },
                    ByteBufCodecs.STRING_UTF8,
                    GunData.VEHICLE_GUN_STREAM_CODEC
                )
            }

            override fun copy(map: Map<String, GunData>): Map<String, GunData> {
                val newMap = HashMap<String, GunData>()
                map.forEach { (key: String, value: GunData) -> newMap[key] = value.copy() }
                return newMap
            }
        }

    @JvmField
    val SHORT_LIST_LIST_SERIALIZER: EntityDataSerializer<List<List<Short>>> =
        EntityDataSerializer.forValueType(
            ByteBufCodecs.SHORT.apply(ByteBufCodecs.list()).apply(ByteBufCodecs.list())
        )

    @JvmStatic
    fun init() {
        EntityDataSerializers.registerSerializer(INT_LIST_SERIALIZER)
        EntityDataSerializers.registerSerializer(FLOAT_LIST_SERIALIZER)
        EntityDataSerializers.registerSerializer(VEC3_SERIALIZER)
        EntityDataSerializers.registerSerializer(VEHICLE_GUN_DATA_MAP_SERIALIZER)
        EntityDataSerializers.registerSerializer(SHORT_LIST_LIST_SERIALIZER)
    }
}
