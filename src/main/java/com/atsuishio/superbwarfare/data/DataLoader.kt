package com.atsuishio.superbwarfare.data

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.data.DataLoader.JSON
import com.atsuishio.superbwarfare.network.message.receive.DataSyncMessage
import com.atsuishio.superbwarfare.tools.sendPacket
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.packs.PackType
import java.util.function.Consumer

/**
 * 数据包加载入口。
 *
 * 全部数据集都已经迁到 kotlinx.serialization（不再有 Gson 分支），
 * 所以这里只剩下 [JSON] 一份配置，以及"按目录把 `data/&lt;ns&gt;/&lt;directory&gt;/` 下的 `.json`
 * 解析成 `Map&lt;id, T&gt;`"的通用流程。
 */
object DataLoader {

    @OptIn(ExperimentalSerializationApi::class)
    val JSON = Json {
        isLenient = true
        ignoreUnknownKeys = true
        serializersModule = com.atsuishio.superbwarfare.serialization.serializersModule
        allowTrailingComma = true
        allowSpecialFloatingPointValues = true
    }

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
        onReload: Consumer<Map<String, Any>>? = null
    ): DataMap<T> {
        val data = LOADED_DATA[directory]

        return if (data != null) {
            data.proxyMap as DataMap<T>
        } else {
            val proxyMap = DataMap<T>(directory, LOADED_DATA)
            LOADED_DATA[directory] = GeneralData(clazz, proxyMap, HashMap(), synced, onReload)
            proxyMap
        }
    }

    @JvmStatic
    fun <T> createData(
        directory: String,
        clazz: Class<T>,
        onReload: Consumer<Map<String, Any>>
    ): DataMap<T> {
        return createData(directory, clazz, false, onReload)
    }

    @Suppress("unchecked_cast")
    @JvmStatic
    @JvmOverloads
    fun <T> createResource(
        directory: String,
        clazz: Class<T>,
        onReload: Consumer<Map<String, Any>>? = null
    ): DataMap<T> {
        val resource = LOADED_RESOURCE[directory]

        return if (resource != null) {
            resource.proxyMap as DataMap<T>
        } else {
            val proxyMap = DataMap<T>(directory, LOADED_RESOURCE)
            LOADED_RESOURCE[directory] = GeneralData(clazz, proxyMap, HashMap(), false, onReload)
            proxyMap
        }
    }

    /**
     * 将 StringOrObject 和 SingleOrList 转换为原始值
     */
    @JvmStatic
    fun processValue(value: Any?): Any? {
        return when (value) {
            is SingleOrList<*> -> value.list.map { value -> processValue(value) }
            is StringOrObject<*> -> processValue(value.value)
            else -> value
        }
    }

    data class GeneralData<T>(
        @JvmField val type: Class<*>,
        @JvmField val proxyMap: DataMap<T>,
        @JvmField val dataMap: HashMap<String, Any>,
        @JvmField val synced: Boolean,
        @JvmField val onReload: Consumer<Map<String, Any>>?
    ) {
        @JvmField
        val data: HashMap<String, Any> = dataMap

        fun getDataMap(): HashMap<String, Any> = dataMap

        /** `Map<String, T>` 的 serializer，用于数据同步（取代原来的 Gson `TypeToken` + `GSON.toJson`） */
        val mapSerializer: KSerializer<Map<String, Any>> by lazy {
            @Suppress("UNCHECKED_CAST")
            MapSerializer(String.serializer(), JSON.serializersModule.serializer(type) as KSerializer<Any>)
        }

        fun serializeToString(): String {
            return JSON.encodeToString(mapSerializer, dataMap)
        }
    }
}
