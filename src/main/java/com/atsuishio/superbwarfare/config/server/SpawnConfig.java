package com.atsuishio.superbwarfare.config.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SpawnConfig {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Path CONFIG_PATH = Path.of("config", "superbwarfare", "spawn.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static boolean SPAWN_SENPAI = false;
    public static boolean SPAWN_MOB_WITH_GUNS = false;

    public static void load() {
        if (Files.notExists(CONFIG_PATH)) {
            save();
            return;
        }
        try {
            var reader = Files.newBufferedReader(CONFIG_PATH);
            var data = GSON.fromJson(reader, Data.class);
            if (data != null) {
                SPAWN_SENPAI = data.spawnSenpai;
                SPAWN_MOB_WITH_GUNS = data.spawnMobWithGuns;
            }
        } catch (IOException e) {
            LOGGER.error("Failed to load spawn config", e);
        }
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            var writer = Files.newBufferedWriter(CONFIG_PATH);
            GSON.toJson(new Data(), writer);
            writer.close();
        } catch (IOException e) {
            LOGGER.error("Failed to save spawn config", e);
        }
    }

    private static class Data {
        public boolean spawnSenpai = SPAWN_SENPAI;
        public boolean spawnMobWithGuns = SPAWN_MOB_WITH_GUNS;
    }
}
