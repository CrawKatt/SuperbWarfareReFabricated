package com.atsuishio.superbwarfare.tools

import com.atsuishio.superbwarfare.data.DataLoader
import com.atsuishio.superbwarfare.serialization.decodeFromCompoundTag
import com.atsuishio.superbwarfare.serialization.encodeToCompoundTag
import com.atsuishio.superbwarfare.serialization.serializersModule
import com.mojang.datafixers.util.Pair
import com.mojang.serialization.*
import kotlinx.serialization.KSerializer
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import com.mojang.serialization.JsonOps

/**
 * Derives a Mojang [MapCodec] from a kotlinx.serialization [KSerializer].
 *
 * Encoding: serializer -> NBT [CompoundTag] via the project's own NBT encoder, then converted
 * into the target [com.mojang.serialization.DynamicOps] element type.
 * Decoding is the reverse. This works with any DynamicOps (NbtOps, JsonOps, ...) while keeping a
 * single, native NBT representation for the actual data.
 */
fun <T : Any> serializerToMapCodec(serializer: KSerializer<T>): MapCodec<T> {
    val encoder = object : Encoder<T> {
        override fun <U> encode(input: T, ops: DynamicOps<U>, prefix: U): DataResult<U> {
            val nbt = encodeToCompoundTag(serializer, input, serializersModule)

            val map = HashMap<U, U>()
            for (key in nbt.allKeys) {
                val value = nbt.get(key) ?: continue
                map[ops.createString(key)] = NbtOps.INSTANCE.convertTo(ops, value)
            }
            return ops.mergeToMap(prefix, map)
        }
    }

    val decoder = object : Decoder<T> {
        override fun <U> decode(ops: DynamicOps<U>, input: U): DataResult<Pair<T, U>> {
            val nbt = ops.convertTo(NbtOps.INSTANCE, input)
            if (nbt !is CompoundTag) {
                return DataResult.error { "serializerToMapCodec: expected CompoundTag, got ${nbt.type.name}" }
            }

            return try {
                DataResult.success(
                    Pair(decodeFromCompoundTag(serializer, nbt, serializersModule), ops.empty())
                )
            } catch (e: Exception) {
                DataResult.error { "serializerToMapCodec: ${e.message}" }
            }
        }
    }

    return MapCodec.assumeMapUnsafe(Codec.of(encoder, decoder))
}

/** Exposes a kotlinx JSON serializer through Minecraft's 1.21 recipe codec API. */
fun <T : Any> serializerToJsonMapCodec(serializer: KSerializer<T>): MapCodec<T> {
    val encoder = object : Encoder<T> {
        override fun <U> encode(input: T, ops: DynamicOps<U>, prefix: U): DataResult<U> {
            return try {
                val json = DataLoader.JSON.encodeToJsonElement(serializer, input).toGson()
                if (json !is GsonObject) {
                    return DataResult.error { "serializerToJsonMapCodec: expected a JSON object" }
                }

                val map = HashMap<U, U>()
                for ((key, value) in json.entrySet()) {
                    map[ops.createString(key)] = JsonOps.INSTANCE.convertTo(ops, value)
                }
                ops.mergeToMap(prefix, map)
            } catch (e: Exception) {
                DataResult.error { "serializerToJsonMapCodec: ${e.message}" }
            }
        }
    }

    val decoder = object : Decoder<T> {
        override fun <U> decode(ops: DynamicOps<U>, input: U): DataResult<Pair<T, U>> {
            return try {
                val json = ops.convertTo(JsonOps.INSTANCE, input)
                if (json !is GsonObject) {
                    return DataResult.error { "serializerToJsonMapCodec: expected a JSON object" }
                }

                DataResult.success(
                    Pair(
                        DataLoader.JSON.decodeFromJsonElement(serializer, json.toKxJson()),
                        ops.empty()
                    )
                )
            } catch (e: Exception) {
                DataResult.error { "serializerToJsonMapCodec: ${e.message}" }
            }
        }
    }

    return MapCodec.assumeMapUnsafe(Codec.of(encoder, decoder))
}
