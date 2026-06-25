package com.atsuishio.superbwarfare.client.language

import com.atsuishio.superbwarfare.Mod
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.client.resources.language.ClientLanguage
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.SimplePreparableReloadListener
import net.minecraft.util.profiling.ProfilerFiller

object ClientLanguageGetter {
    @JvmStatic
    lateinit var EN_US: ClientLanguage

    @JvmStatic
    fun register() {
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(ReloadListener)
    }

    object ReloadListener : SimplePreparableReloadListener<ClientLanguage>(), IdentifiableResourceReloadListener {
        override fun getFabricId(): ResourceLocation {
            return Mod.loc("client_language_getter")
        }

        override fun prepare(resourceManager: ResourceManager, profiler: ProfilerFiller): ClientLanguage {
            return ClientLanguage.loadFrom(resourceManager, listOf("en_us"), false)
        }

        override fun apply(language: ClientLanguage, resourceManager: ResourceManager, profiler: ProfilerFiller) {
            EN_US = language
        }
    }
}
