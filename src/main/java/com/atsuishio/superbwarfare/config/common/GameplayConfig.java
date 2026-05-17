package com.atsuishio.superbwarfare.config.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GameplayConfig {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Path CONFIG_PATH = Path.of("config", "superbwarfare", "gameplay.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static boolean RESPAWN_RELOAD = true;
    public static boolean GLOBAL_INDICATION = true;
    public static boolean RESPAWN_AUTO_ARMOR = true;

    public static void load() {
        if (Files.notExists(CONFIG_PATH)) {
            save();
            return;
        }
        try {
            var reader = Files.newBufferedReader(CONFIG_PATH);
            var data = GSON.fromJson(reader, Data.class);
            if (data != null) {
                RESPAWN_RELOAD = data.respawnReload;
                GLOBAL_INDICATION = data.globalIndication;
                RESPAWN_AUTO_ARMOR = data.respawnAutoArmor;
            }
        } catch (IOException e) {
            LOGGER.error("Failed to load gameplay config", e);
        }
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            var writer = Files.newBufferedWriter(CONFIG_PATH);
            GSON.toJson(new Data(), writer);
            writer.close();
        } catch (IOException e) {
            LOGGER.error("Failed to save gameplay config", e);
        }
    }

    private static class Data {
        public boolean respawnReload = RESPAWN_RELOAD;
        public boolean globalIndication = GLOBAL_INDICATION;
        public boolean respawnAutoArmor = RESPAWN_AUTO_ARMOR;
    }
}
