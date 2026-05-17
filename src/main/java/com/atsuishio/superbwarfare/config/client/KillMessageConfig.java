package com.atsuishio.superbwarfare.config.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class KillMessageConfig {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Path CONFIG_PATH = Path.of("config", "superbwarfare", "kill_message.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static boolean SHOW_KILL_MESSAGE = true;
    public static int KILL_MESSAGE_COUNT = 5;
    public static int KILL_MESSAGE_MARGIN_X = 0;
    public static int KILL_MESSAGE_MARGIN_Y = 5;
    public static KillMessagePosition KILL_MESSAGE_POSITION = KillMessagePosition.RIGHT_TOP;

    public static void load() {
        if (Files.notExists(CONFIG_PATH)) {
            save();
            return;
        }
        try {
            var reader = Files.newBufferedReader(CONFIG_PATH);
            var data = GSON.fromJson(reader, Data.class);
            if (data != null) {
                SHOW_KILL_MESSAGE = data.showKillMessage;
                KILL_MESSAGE_COUNT = data.killMessageCount;
                KILL_MESSAGE_MARGIN_X = data.killMessageMarginX;
                KILL_MESSAGE_MARGIN_Y = data.killMessageMarginY;
                KILL_MESSAGE_POSITION = data.killMessagePosition;
            }
        } catch (IOException e) {
            LOGGER.error("Failed to load kill message config", e);
        }
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            var writer = Files.newBufferedWriter(CONFIG_PATH);
            GSON.toJson(new Data(), writer);
            writer.close();
        } catch (IOException e) {
            LOGGER.error("Failed to save kill message config", e);
        }
    }

    public enum KillMessagePosition {
        LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM,
    }

    private static class Data {
        public boolean showKillMessage = SHOW_KILL_MESSAGE;
        public int killMessageCount = KILL_MESSAGE_COUNT;
        public int killMessageMarginX = KILL_MESSAGE_MARGIN_X;
        public int killMessageMarginY = KILL_MESSAGE_MARGIN_Y;
        public KillMessagePosition killMessagePosition = KILL_MESSAGE_POSITION;
    }
}
