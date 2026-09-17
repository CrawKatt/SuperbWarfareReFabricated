package com.atsuishio.superbwarfare.data

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * 创建一个List包装类，反序列化时将单个对象解析为单元素List，或直接以List方式进行读取，不影响序列化
 * {} -> [{}]
 */
@Serializable(SingleOrListSerializer::class)
@Suppress("DelegationToVarProperty")
data class SingleOrList<T>(@JvmField var list: MutableList<T>) : List<T> by list {
    @SafeVarargs
    constructor(vararg objects: T) : this(mutableListOf(*objects))

    internal class ListOrObjectAdapter<T>(type: Type, private val gson: Gson) : TypeAdapter<SingleOrList<T>>() {
        private val type = (type as ParameterizedType).actualTypeArguments[0]

        @Throws(IOException::class)
        override fun write(jsonWriter: JsonWriter, singleOrList: SingleOrList<T>?) {
            val list = singleOrList?.list
            if (singleOrList == null || list == null) {
                jsonWriter.beginArray().endArray()
                return
            }

            if (list.size == 1) {
                gson.toJson(list[0], type, jsonWriter)
            } else {
                gson.toJson(
                    list,
                    TypeToken.getParameterized(MutableList::class.java, type).type,
                    jsonWriter
                )
            }
        }

        @Throws(IOException::class)
        override fun read(jsonReader: JsonReader): SingleOrList<T> {
            if (jsonReader.peek() != JsonToken.BEGIN_ARRAY) {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull()
                    return SingleOrList()
                }
                return SingleOrList(gson.fromJson<T>(jsonReader, type))
            }

            val listType = TypeToken.getParameterized(MutableList::class.java, type).type
            return SingleOrList(gson.fromJson<MutableList<T>>(jsonReader, listType))
        }
    }

    internal class AdapterFactory : TypeAdapterFactory {
        override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
            if (SingleOrList::class.java.isAssignableFrom(type.rawType)) {
                @Suppress("UNCHECKED_CAST")
                return ListOrObjectAdapter<T>(type.type, gson) as TypeAdapter<T>
            }
            return null
        }
    }
}

class SingleOrListSerializer<T>(val elementSerializer: KSerializer<T>) : KSerializer<SingleOrList<T>> {
    override val descriptor = elementSerializer.descriptor

    override fun serialize(
        encoder: Encoder,
        value: SingleOrList<T>
    ) {
        encoder.encodeSerializableValue(ListSerializer(elementSerializer), value.list)
    }

    override fun deserialize(decoder: Decoder): SingleOrList<T> {
        require(decoder is JsonDecoder) { "only JsonDecoder is supported!" }

        val element = decoder.decodeJsonElement()
        return if (element is JsonArray) {
            SingleOrList(element.map { decoder.json.decodeFromJsonElement(elementSerializer, it) }.toMutableList())
        } else {
            SingleOrList(listOf(decoder.json.decodeFromJsonElement(elementSerializer, element)).toMutableList())
        }
    }

}
