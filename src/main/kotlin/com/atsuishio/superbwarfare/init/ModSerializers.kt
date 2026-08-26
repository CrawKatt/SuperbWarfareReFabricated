package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.data.gun.GunData
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.syncher.EntityDataSerializer
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.world.item.ItemStack
import net.minecraft.world.phys.Vec3

object ModSerializers {

    @JvmField
    val INT_LIST_SERIALIZER: EntityDataSerializer<List<Int>> =
        EntityDataSerializer.simple({ buf, list ->
            buf.writeVarInt(list.size)
            list.forEach(buf::writeVarInt)
        }, { buf ->
            List(buf.readVarInt()) { buf.readVarInt() }
        })

    @JvmField
    val FLOAT_LIST_SERIALIZER: EntityDataSerializer<List<Float>> =
        EntityDataSerializer.simple({ buf, list ->
            buf.writeVarInt(list.size)
            list.forEach(buf::writeFloat)
        }, { buf ->
            List(buf.readVarInt()) { buf.readFloat() }
        })

    @JvmField
    val VEC3_SERIALIZER: EntityDataSerializer<Vec3> =
        EntityDataSerializer.simple({ buf, v ->
            buf.writeDouble(v.x)
            buf.writeDouble(v.y)
            buf.writeDouble(v.z)
        }, { buf ->
            Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble())
        })

    @JvmField
    val VEHICLE_GUN_DATA_MAP_SERIALIZER: EntityDataSerializer<Map<String, GunData>> =
        EntityDataSerializer.simple({ buf, map ->
            buf.writeVarInt(map.size)
            map.forEach { (name, data) ->
                buf.writeUtf(name)
                // Snapshot the tag before handing it to the Netty IO thread.
                // The tag is a live reference to the ItemStack's internal
                // CompoundTag; if the server thread mutates it concurrently with
                // Netty serializing it, a ConcurrentModificationException is thrown
                // inside CompoundTag.copy() / NbtIo.write(). The defensive copy
                // here is cheap (one weapon's worth of NBT data) and eliminates
                // the race entirely.
                buf.writeNbt(data.stack.tag?.copy())
            }
        }, { buf ->
            buildMap {
                repeat(buf.readVarInt()) {
                    val name = buf.readUtf()
                    val stack = ItemStack(ModItems.VEHICLE_GUN)
                    stack.tag = buf.readNbt()
                    put(name, GunData.from(stack))
                }
            }
        })

    @JvmField
    val SHORT_LIST_LIST_SERIALIZER: EntityDataSerializer<List<List<Short>>> =
        EntityDataSerializer.simple({ buf, list ->
            buf.writeVarInt(list.size)
            list.forEach { shorts ->
                buf.writeVarInt(shorts.size)
                shorts.forEach { buf.writeShort(it.toInt()) }
            }
        }, { buf ->
            List(buf.readVarInt()) {
                List(buf.readVarInt()) { buf.readShort() }
            }
        })

    @JvmStatic
    fun init() {
        EntityDataSerializers.registerSerializer(INT_LIST_SERIALIZER)
        EntityDataSerializers.registerSerializer(FLOAT_LIST_SERIALIZER)
        EntityDataSerializers.registerSerializer(VEHICLE_GUN_DATA_MAP_SERIALIZER)
        EntityDataSerializers.registerSerializer(SHORT_LIST_LIST_SERIALIZER)
    }
}
