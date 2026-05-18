package com.atsuishio.superbwarfare.tools;

import com.atsuishio.superbwarfare.Mod;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class ResourceOnceLogger {
    private static final ReloadListener INSTANCE = new ReloadListener();
    private static final List<ResourceOnceLogger> LOGGERS = new ArrayList<>();
    private final Set<Object> logged = new HashSet<>();

    public ResourceOnceLogger() {
        LOGGERS.add(this);
    }

    public void log(Object obj, Consumer<Logger> logger) {
        if (logged.contains(obj)) {
            return;
        }
        logged.add(obj);
        logger.accept(Mod.LOGGER);
    }

    public static void register() {
        net.fabricmc.fabric.api.resource.ResourceManagerHelper.get(net.minecraft.server.packs.PackType.CLIENT_RESOURCES).registerReloadListener(INSTANCE);
    }

    static class ReloadListener implements ResourceManagerReloadListener, IdentifiableResourceReloadListener {

        @Override
        public ResourceLocation getFabricId() {
            return Mod.loc("once_logger");
        }

        @Override
        public void onResourceManagerReload(@NotNull ResourceManager resourceManager) {
            LOGGERS.forEach(l -> l.logged.clear());
        }
    }

}
