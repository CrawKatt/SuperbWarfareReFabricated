package com.atsuishio.superbwarfare.config.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ProjectileConfig {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Path CONFIG_PATH = Path.of("config", "superbwarfare", "projectile.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static boolean ALLOW_PROJECTILE_DESTROY_BLOCKS = false;

    public static void load() {
        if (Files.notExists(CONFIG_PATH)) {
            save();
            return;
        }
        try {
            var reader = Files.newBufferedReader(CONFIG_PATH);
            var data = GSON.fromJson(reader, Data.class);
            if (data != null) {
                ALLOW_PROJECTILE_DESTROY_BLOCKS = data.allowProjectileDestroyBlocks;
            }
        } catch (IOException e) {
            LOGGER.error("Failed to load projectile config", e);
        }
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            var writer = Files.newBufferedWriter(CONFIG_PATH);
            GSON.toJson(new Data(), writer);
            writer.close();
        } catch (IOException e) {
            LOGGER.error("Failed to save projectile config", e);
        }
    }

    private static class Data {
        public boolean allowProjectileDestroyBlocks = ALLOW_PROJECTILE_DESTROY_BLOCKS;
    }
}
