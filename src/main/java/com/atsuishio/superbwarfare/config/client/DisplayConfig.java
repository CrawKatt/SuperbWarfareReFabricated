package com.atsuishio.superbwarfare.config.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DisplayConfig {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Path CONFIG_PATH = Path.of("config", "superbwarfare", "display.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static boolean ENABLE_GUN_LOD = false;
    public static int WEAPON_HUD_X_OFFSET = 0;
    public static int WEAPON_HUD_Y_OFFSET = 0;
    public static boolean ENABLE_HEAT_BAR_HUD = true;
    public static int HEAT_BAR_HUD_X_OFFSET = 0;
    public static int HEAT_BAR_HUD_Y_OFFSET = 0;
    public static boolean KILL_INDICATION = true;
    public static boolean AMMO_HUD = true;
    public static boolean ADVANCED_AMMO_HUD = true;
    public static boolean VEHICLE_INFO = true;
    public static boolean FLOAT_CROSS_HAIR = true;
    public static boolean CAMERA_ROTATE = true;
    public static boolean ARMOR_PLATE_HUD = true;
    public static boolean STAMINA_HUD = true;
    public static boolean DOG_TAG_NAME_VISIBLE = true;
    public static boolean DOG_TAG_ICON_VISIBLE = false;
    public static int WEAPON_SCREEN_SHAKE = 100;
    public static int EXPLOSION_SCREEN_SHAKE = 100;
    public static int SHOCK_SCREEN_SHAKE = 100;
    public static boolean ENABLE_VERSION_CHECK_WARNING = true;

    public static void load() {
        if (Files.notExists(CONFIG_PATH)) {
            save();
            return;
        }
        try {
            var reader = Files.newBufferedReader(CONFIG_PATH);
            var data = GSON.fromJson(reader, Data.class);
            if (data != null) {
                ENABLE_GUN_LOD = data.enableGunLod;
                WEAPON_HUD_X_OFFSET = data.weaponHudXOffset;
                WEAPON_HUD_Y_OFFSET = data.weaponHudYOffset;
                ENABLE_HEAT_BAR_HUD = data.enableHeatBarHud;
                HEAT_BAR_HUD_X_OFFSET = data.heatBarHudXOffset;
                HEAT_BAR_HUD_Y_OFFSET = data.heatBarHudYOffset;
                KILL_INDICATION = data.killIndication;
                AMMO_HUD = data.ammoHud;
                ADVANCED_AMMO_HUD = data.advancedAmmoHud;
                VEHICLE_INFO = data.vehicleInfo;
                FLOAT_CROSS_HAIR = data.floatCrossHair;
                CAMERA_ROTATE = data.cameraRotate;
                ARMOR_PLATE_HUD = data.armorPlateHud;
                STAMINA_HUD = data.staminaHud;
                DOG_TAG_NAME_VISIBLE = data.dogTagNameVisible;
                DOG_TAG_ICON_VISIBLE = data.dogTagIconVisible;
                WEAPON_SCREEN_SHAKE = data.weaponScreenShake;
                EXPLOSION_SCREEN_SHAKE = data.explosionScreenShake;
                SHOCK_SCREEN_SHAKE = data.shockScreenShake;
                ENABLE_VERSION_CHECK_WARNING = data.enableVersionCheckWarning;
            }
        } catch (IOException e) {
            LOGGER.error("Failed to load display config", e);
        }
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            var writer = Files.newBufferedWriter(CONFIG_PATH);
            GSON.toJson(new Data(), writer);
            writer.close();
        } catch (IOException e) {
            LOGGER.error("Failed to save display config", e);
        }
    }

    private static class Data {
        public boolean enableGunLod = ENABLE_GUN_LOD;
        public int weaponHudXOffset = WEAPON_HUD_X_OFFSET;
        public int weaponHudYOffset = WEAPON_HUD_Y_OFFSET;
        public boolean enableHeatBarHud = ENABLE_HEAT_BAR_HUD;
        public int heatBarHudXOffset = HEAT_BAR_HUD_X_OFFSET;
        public int heatBarHudYOffset = HEAT_BAR_HUD_Y_OFFSET;
        public boolean killIndication = KILL_INDICATION;
        public boolean ammoHud = AMMO_HUD;
        public boolean advancedAmmoHud = ADVANCED_AMMO_HUD;
        public boolean vehicleInfo = VEHICLE_INFO;
        public boolean floatCrossHair = FLOAT_CROSS_HAIR;
        public boolean cameraRotate = CAMERA_ROTATE;
        public boolean armorPlateHud = ARMOR_PLATE_HUD;
        public boolean staminaHud = STAMINA_HUD;
        public boolean dogTagNameVisible = DOG_TAG_NAME_VISIBLE;
        public boolean dogTagIconVisible = DOG_TAG_ICON_VISIBLE;
        public int weaponScreenShake = WEAPON_SCREEN_SHAKE;
        public int explosionScreenShake = EXPLOSION_SCREEN_SHAKE;
        public int shockScreenShake = SHOCK_SCREEN_SHAKE;
        public boolean enableVersionCheckWarning = ENABLE_VERSION_CHECK_WARNING;
    }
}
