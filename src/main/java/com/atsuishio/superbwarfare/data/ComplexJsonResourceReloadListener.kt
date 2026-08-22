package com.atsuishio.superbwarfare.data

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.api.event.LoadingDataEvent
import com.atsuishio.superbwarfare.api.event.LoadingJsonEvent
import com.atsuishio.superbwarfare.data.gun.DefaultGunData
import com.atsuishio.superbwarfare.data.vehicle.DefaultVehicleData
import com.atsuishio.superbwarfare.tools.postEvent
import kotlinx.serialization.serializer
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.minecraft.resources.FileToIdConverter
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.SimplePreparableReloadListener
import net.minecraft.util.profiling.ProfilerFiller

class ComplexJsonResourceReloadListener(
    private val id: ResourceLocation,
    private val data: MutableMap<String, DataLoader.GeneralData<*>>
) : SimplePreparableReloadListener<Any>(), IdentifiableResourceReloadListener {

    override fun getFabricId(): ResourceLocation {
        return id
    }

    override fun prepare(resourceManager: ResourceManager, profiler: ProfilerFiller): Any {
        this.data.forEach { (name, value) ->
            val map = value.dataMap
            map.clear()

            val converter = FileToIdConverter.json(name)

            for (entry in converter.listMatchingResources(resourceManager).entries) {
                val location = entry.key
                val pathLocation = converter.fileToId(location)

                try {
                    entry.value.openAsReader().use { reader ->
                        val id = pathLocation.toString()

                        var jsonStr = reader.lineSequence().joinToString("\n")

                        val jsonEvent = LoadingJsonEvent(id, jsonStr)
                        postEvent(jsonEvent)

                        if (!jsonEvent.isCanceled) {
                            jsonStr = jsonEvent.jsonStr
                        }

                        var loaded = if (value.isKtData) {
                            DataLoader.JSON.decodeFromString(serializer(value.type), jsonStr)
                        } else {
                            DataLoader.GSON.fromJson(jsonStr, value.type)
                        }

                        if (loaded is IDBasedData<*>) {
                            loaded.id = id
                        }

                        if (loaded is DefaultGunData) {
                            val event = LoadingDataEvent.Gun(id, loaded)
                            postEvent(event)

                            if (!event.isCanceled) {
                                loaded = event.data
                            }
                        }

                        if (loaded is DefaultVehicleData) {
                            val event = LoadingDataEvent.Vehicle(id, loaded)
                            postEvent(event)

                            if (!event.isCanceled) {
                                loaded = event.data
                            }
                        }

                        map[id] = loaded
                    }
                } catch (exception: Exception) {
                    Mod.LOGGER.error("Couldn't parse data file {} from {}", pathLocation, location, exception)
                }
            }

            value.onReload?.accept(map)
        }

        return NULL
    }

    override fun apply(obj: Any, resourceManager: ResourceManager, profiler: ProfilerFiller) {
    }

    companion object {
        private val NULL = Any()
    }
}
