package com.atsuishio.superbwarfare.event

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.network.message.receive.ModVersionSyncMessage
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.fabricmc.loader.api.Version
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.world.level.saveddata.SavedData

object ModVersionEventHandler {
    @JvmField
    var previousVersion: Version? = null

    @JvmField
    var currentVersion: Version? = null

    private var serverPreviousVersion: String? = null
    private var serverCurrentVersion: String? = null

    @JvmStatic
    fun register() {
        ServerLifecycleEvents.SERVER_STARTED.register { server ->
            val current = FabricLoader.getInstance().getModContainer(Mod.MODID).orElseThrow().metadata.version.friendlyString
            val data = server.overworld().dataStorage.computeIfAbsent(VERSION_DATA_FACTORY, VERSION_DATA_FILE)

            serverPreviousVersion = data.version
            serverCurrentVersion = current
            if (data.version != current) {
                data.version = current
                data.setDirty()
            }
        }

        ServerPlayConnectionEvents.JOIN.register { handler, _, _ ->
            val previous = serverPreviousVersion
            val current = serverCurrentVersion
            if (previous != null && current != null && previous != current) {
                ServerPlayNetworking.send(handler.player, ModVersionSyncMessage(previous, current))
            }
        }
    }

    @JvmStatic
    fun updateClient(previous: String, current: String): Boolean {
        previousVersion = runCatching { Version.parse(previous) }.getOrNull()
        currentVersion = runCatching { Version.parse(current) }.getOrNull()
        return previousVersion != null && currentVersion != null && previousVersion != currentVersion
    }
}

private const val VERSION_DATA_FILE = "superbwarfare_mod_version"

private val VERSION_DATA_FACTORY = SavedData.Factory(
    { ModVersionSavedData() },
    ModVersionSavedData::load,
    null
)

private class ModVersionSavedData(var version: String? = null) : SavedData() {
    override fun save(tag: CompoundTag, registries: HolderLookup.Provider): CompoundTag {
        version?.let { tag.putString("Version", it) }
        return tag
    }

    companion object {
        fun load(tag: CompoundTag, registries: HolderLookup.Provider) = ModVersionSavedData(
            tag.getString("Version").takeIf { tag.contains("Version", Tag.TAG_STRING.toInt()) && it.isNotBlank() }
        )
    }
}
