package com.atsuishio.superbwarfare.tools;

import com.atsuishio.superbwarfare.Mod;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

// 仅在客户端资源重载时记录一次的Logger
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
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(INSTANCE);
    }

    static class ReloadListener implements ResourceManagerReloadListener {

        @Override
        public void onResourceManagerReload(@NotNull ResourceManager resourceManager) {
            LOGGERS.forEach(l -> l.logged.clear());
        }
    }

}
