package com.atsuishio.superbwarfare.config.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class EnvironmentChecksumConfig {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Path CONFIG_PATH = Path.of("config", "superbwarfare", "environment_checksum.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static String ENVIRONMENT_CHECKSUM = "";

    public static void load() {
        if (Files.notExists(CONFIG_PATH)) {
            save();
            return;
        }
        try {
            var reader = Files.newBufferedReader(CONFIG_PATH);
            var data = GSON.fromJson(reader, Data.class);
            if (data != null) {
                ENVIRONMENT_CHECKSUM = data.environmentChecksum;
            }
        } catch (IOException e) {
            LOGGER.error("Failed to load environment checksum config", e);
        }
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            var writer = Files.newBufferedWriter(CONFIG_PATH);
            GSON.toJson(new Data(), writer);
            writer.close();
        } catch (IOException e) {
            LOGGER.error("Failed to save environment checksum config", e);
        }
    }

    private static class Data {
        public String environmentChecksum = ENVIRONMENT_CHECKSUM;
    }
}
