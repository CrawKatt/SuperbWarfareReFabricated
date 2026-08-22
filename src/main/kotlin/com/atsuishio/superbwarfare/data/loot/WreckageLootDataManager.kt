package com.atsuishio.superbwarfare.data.loot

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.tools.toKxJson
import com.google.gson.Gson
import com.google.gson.JsonElement
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener
import net.minecraft.util.profiling.ProfilerFiller
import net.minecraft.world.entity.EntityType

object WreckageLootDataManager : SimpleJsonResourceReloadListener(Gson(), "sbw/loot"), IdentifiableResourceReloadListener {
    private val data: MutableMap<ResourceLocation, WreckageLootData> = mutableMapOf()

    override fun apply(
        pObject: Map<ResourceLocation, JsonElement>,
        pResourceManager: ResourceManager,
        pProfiler: ProfilerFiller
    ) {
        data.clear()
        pObject.forEach { (id, json) ->
            try {
                val obj = json.asJsonObject
                val json = Json.decodeFromJsonElement<WreckageLootData>(obj.toKxJson())
                data[id] = json
            } catch (_: Exception) {
                Mod.LOGGER.error("Failed to load wreckage loot data for {}", id)
            }
        }
    }

    override fun getFabricId(): ResourceLocation {
        return Mod.loc("wreckage_loot")
    }

    fun getLootData(id: ResourceLocation): WreckageLootData? {
        return data[id]
    }

    fun getLootData(type: EntityType<*>): WreckageLootData? {
        return data[BuiltInRegistries.ENTITY_TYPE.getKey(type)]
    }

    @JvmStatic
    fun register() {
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(this)
    }
}
