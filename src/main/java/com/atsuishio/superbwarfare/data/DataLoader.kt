package com.atsuishio.superbwarfare.data

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.data.ModColor.ModColorAdapter
import com.atsuishio.superbwarfare.data.StringOrVec3.StringOrVec3Adapter
import com.atsuishio.superbwarfare.data.vehicle.subdata.CollisionLevel
import com.atsuishio.superbwarfare.data.vehicle.subdata.CollisionLevel.LimitAdapter
import com.atsuishio.superbwarfare.network.message.receive.DataSyncMessage
import com.atsuishio.superbwarfare.tools.sendPacket
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.packs.PackType
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.phys.Vec2
import net.minecraft.world.phys.Vec3
import java.util.function.Consumer

object DataLoader {
    @JvmField
    val GSON: Gson = createCommonBuilder().create()

    @OptIn(ExperimentalSerializationApi::class)
    val JSON = Json {
        isLenient = true
        ignoreUnknownKeys = true
        serializersModule = com.atsuishio.superbwarfare.serialization.serializersModule
        allowTrailingComma = true
        allowSpecialFloatingPointValues = true
    }

    @JvmField
    val JSON_OBJECT_CACHE: LoadingCache<Any, JsonObject> = CacheBuilder.newBuilder()
        .weakKeys()
        .build(object : CacheLoader<Any, JsonObject>() {
            override fun load(obj: Any): JsonObject {
                return GSON.toJsonTree(obj).asJsonObject
            }
        })

    val LOADED_DATA = mutableMapOf<String, GeneralData<*>>()
    val LOADED_RESOURCE = mutableMapOf<String, GeneralData<*>>()

    val SERVER_LISTENER: ComplexJsonResourceReloadListener =
        ComplexJsonResourceReloadListener(Mod.loc("server_data_loader"), LOADED_DATA)

    val CLIENT_LISTENER: ComplexJsonResourceReloadListener =
        ComplexJsonResourceReloadListener(Mod.loc("client_resource_loader"), LOADED_RESOURCE)

    @JvmStatic
    fun register() {
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(SERVER_LISTENER)

        ServerPlayConnectionEvents.JOIN.register { handler, _, server ->
            syncDataToPlayer(server, handler.player)
        }

        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register { server, _, success ->
            if (!success) return@register

            for (player in server.playerList.players) {
                syncDataToPlayer(server, player)
            }
        }
    }

    @JvmStatic
    fun registerClient() {
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(CLIENT_LISTENER)
    }

    private fun syncDataToPlayer(server: MinecraftServer, player: ServerPlayer) {
        if (server.isSingleplayerOwner(player.gameProfile)) return

        LOADED_DATA.filter { it.value.synced }.forEach { (key, data) ->
            val packet = DataSyncMessage(key, data.serializeToString())
            player.sendPacket(packet)
        }
    }

    @Suppress("unchecked_cast")
    @JvmStatic
    @JvmOverloads
    fun <T> createData(
        directory: String,
        clazz: Class<T>,
        synced: Boolean = false,
        isKtData: Boolean = false,
        onReload: Consumer<Map<String, Any>>? = null
    ): DataMap<T> {
        val data = LOADED_DATA[directory]

        return if (data != null) {
            data.proxyMap as DataMap<T>
        } else {
            val proxyMap = DataMap<T>(directory, LOADED_DATA)
            LOADED_DATA[directory] = GeneralData(clazz, proxyMap, HashMap(), synced, isKtData, onReload)
            proxyMap
        }
    }

    @JvmStatic
    fun <T> createData(
        directory: String,
        clazz: Class<T>,
        onReload: Consumer<Map<String, Any>>
    ): DataMap<T> {
        return createData(directory, clazz, false, false, onReload)
    }

    @Suppress("unchecked_cast")
    @JvmStatic
    @JvmOverloads
    fun <T> createResource(
        directory: String,
        clazz: Class<T>,
        isKtData: Boolean = false,
        onReload: Consumer<Map<String, Any>>? = null
    ): DataMap<T> {
        val resource = LOADED_RESOURCE[directory]

        return if (resource != null) {
            resource.proxyMap as DataMap<T>
        } else {
            val proxyMap = DataMap<T>(directory, LOADED_RESOURCE)
            LOADED_RESOURCE[directory] = GeneralData(clazz, proxyMap, HashMap(), false, isKtData, onReload)
            proxyMap
        }
    }

    @JvmStatic
    fun <T> createResource(
        directory: String,
        clazz: Class<T>,
        onReload: Consumer<Map<String, Any>>
    ): DataMap<T> {
        return createResource(directory, clazz, false, onReload)
    }

    @JvmStatic
    fun createCommonBuilder(): GsonBuilder {
        return GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .setLenient()
            .serializeSpecialFloatingPointValues()
            .registerTypeAdapter(Vec2::class.java, Vec2Adapter())
            .registerTypeAdapter(Vec3::class.java, Vec3Adapter())
            .registerTypeAdapter(ResourceLocation::class.java, ResourceLocationAdapter())
            .registerTypeAdapter(SoundEvent::class.java, SoundEventAdapter())
            .registerTypeAdapter(ModColor::class.java, ModColorAdapter())
            .registerTypeAdapter(StringOrVec3::class.java, StringOrVec3Adapter())
            .registerTypeAdapter(CollisionLevel.Limit::class.java, LimitAdapter())
            .registerTypeAdapterFactory(ObjectToList.AdapterFactory())
            .registerTypeAdapterFactory(StringToObject.AdapterFactory())
    }

    @JvmStatic
    fun processValue(value: Any?): Any? {
        return when (value) {
            is ObjectToList<*> -> value.list.map { processValue(it) }
            is StringToObject<*> -> processValue(value.value)
            else -> value
        }
    }

    data class GeneralData<T>(
        @JvmField val type: Class<*>,
        @JvmField val proxyMap: DataMap<T>,
        @JvmField val dataMap: HashMap<String, Any>,
        @JvmField val synced: Boolean,
        @JvmField val isKtData: Boolean = false,
        @JvmField val onReload: Consumer<Map<String, Any>>?
    ) {
        @JvmField
        val data: HashMap<String, Any> = dataMap

        fun getDataMap(): HashMap<String, Any> = dataMap

        val mapType by lazy {
            TypeToken.getParameterized(HashMap::class.java, String::class.java, type)!!
        }

        fun serializeToString(): String {
            return if (isKtData) {
                JSON.encodeToString(serializer(mapType.type), dataMap)
            } else {
                GSON.toJson(dataMap)!!
            }
        }
    }
}
