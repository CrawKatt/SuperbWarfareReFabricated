package com.atsuishio.superbwarfare.tools

import com.atsuishio.superbwarfare.Mod
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.ResourceManagerReloadListener
import org.apache.logging.log4j.Logger
import java.util.function.Consumer

class ResourceOnceLogger {
    private val logged = HashSet<Any>()

    init {
        LOGGERS.add(this)
    }

    fun log(obj: Any, logger: Consumer<Logger>) {
        if (logged.contains(obj)) {
            return
        }

        logged.add(obj)
        logger.accept(Mod.LOGGER)
    }

    private class ReloadListener : ResourceManagerReloadListener, IdentifiableResourceReloadListener {
        override fun getFabricId(): ResourceLocation {
            return Mod.loc("once_logger")
        }

        override fun onResourceManagerReload(resourceManager: ResourceManager) {
            LOGGERS.forEach { it.logged.clear() }
        }
    }

    companion object {
        private val INSTANCE = ReloadListener()
        private val LOGGERS = ArrayList<ResourceOnceLogger>()

        @JvmStatic
        fun register() {
            ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(INSTANCE)
        }
    }
}
