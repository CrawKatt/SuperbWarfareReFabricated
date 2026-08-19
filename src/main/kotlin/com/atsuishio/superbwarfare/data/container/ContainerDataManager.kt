package com.atsuishio.superbwarfare.data.container

import com.atsuishio.superbwarfare.Mod
import com.google.gson.Gson
import com.google.gson.JsonElement
import it.unimi.dsi.fastutil.Pair
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener
import net.minecraft.util.profiling.ProfilerFiller

object ContainerDataManager :
    SimpleJsonResourceReloadListener(Gson(), "sbw/containers"),
    IdentifiableResourceReloadListener {

    private val containerData: MutableMap<ResourceLocation, MutableList<Pair<String, Int>>> = hashMapOf()

    override fun getFabricId(): ResourceLocation {
        return Mod.loc("container_data_manager")
    }

    @JvmStatic
    fun register() {
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(this)
    }

    override fun apply(
        objects: MutableMap<ResourceLocation, JsonElement>,
        manager: ResourceManager,
        profiler: ProfilerFiller
    ) {
        containerData.clear()

        objects.forEach { (id, json) ->
            try {
                val obj = json.asJsonObject
                val list: MutableList<Pair<String, Int>> = mutableListOf()
                val array = obj.getAsJsonArray("List")

                for (element in array) {
                    if (element.isJsonObject) {
                        val entry = element.asJsonObject
                        val type = entry.get("Type").asString
                        val weight = entry.get("Weight").asInt

                        list.add(Pair.of(type, weight))
                    } else {
                        list.add(Pair.of(element.asString, 1))
                    }
                }

                containerData[id] = list
            } catch (_: Exception) {
                Mod.LOGGER.error("Failed to load container data for {}", id)
            }
        }
    }

    fun getEntityTypes(id: ResourceLocation): MutableList<Pair<String, Int>> {
        return containerData[id] ?: mutableListOf()
    }
}
