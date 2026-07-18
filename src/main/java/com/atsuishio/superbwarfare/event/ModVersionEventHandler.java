package com.atsuishio.superbwarfare.event;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.network.NetworkRegistry;
import com.atsuishio.superbwarfare.network.message.receive.ModVersionMismatchMessage;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.Nullable;

public final class ModVersionEventHandler {

    private static final String FILE_ID = "superbwarfare_version";

    @Nullable
    private static String previousVersion;
    @Nullable
    private static String currentVersion;

    private ModVersionEventHandler() {
    }

    public static void registerEvents() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            String loadedVersion = getLoadedVersion();
            VersionData data = server.overworld().getDataStorage().computeIfAbsent(
                    VersionData::load,
                    () -> VersionData.create(loadedVersion),
                    FILE_ID
            );

            currentVersion = loadedVersion;
            previousVersion = !data.version().isBlank() && !data.version().equals(loadedVersion)
                    ? data.version()
                    : null;
            data.setVersion(loadedVersion);
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            if (previousVersion != null && currentVersion != null) {
                NetworkRegistry.sendToPlayer(handler.player,
                        new ModVersionMismatchMessage(previousVersion, currentVersion));
            }
        });

        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            previousVersion = null;
            currentVersion = null;
        });
    }

    private static String getLoadedVersion() {
        return FabricLoader.getInstance().getModContainer(Mod.MODID)
                .map(container -> container.getMetadata().getVersion().getFriendlyString())
                .orElse("unknown");
    }

    private static final class VersionData extends SavedData {
        private String version;

        private VersionData(String version) {
            this.version = version;
        }

        private static VersionData load(CompoundTag tag) {
            return new VersionData(tag.getString("Version"));
        }

        private static VersionData create(String version) {
            VersionData data = new VersionData(version);
            data.setDirty();
            return data;
        }

        private String version() {
            return this.version;
        }

        private void setVersion(String version) {
            if (!this.version.equals(version)) {
                this.version = version;
                this.setDirty();
            }
        }

        @Override
        public CompoundTag save(CompoundTag tag) {
            tag.putString("Version", this.version);
            return tag;
        }
    }
}
