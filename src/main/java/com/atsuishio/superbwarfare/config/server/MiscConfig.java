package com.atsuishio.superbwarfare.config.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MiscConfig {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Path CONFIG_PATH = Path.of("config", "superbwarfare", "misc.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static boolean ALLOW_TACTICAL_SPRINT = false;
    public static boolean SEND_KILL_FEEDBACK = true;
    public static boolean ALLOW_FORCE_DAMAGE = false;
    public static boolean DROP_AMMO_BOX = false;
    public static int DEFAULT_ARMOR_LEVEL = 1;
    public static int MILITARY_ARMOR_LEVEL = 2;
    public static int HEAVY_MILITARY_ARMOR_LEVEL = 3;
    public static int ARMOR_POINT_PER_LEVEL = 15;
    public static int CHARGING_STATION_MAX_ENERGY = 4000000;
    public static int CHARGING_STATION_GENERATE_SPEED = 128;
    public static int CHARGING_STATION_TRANSFER_SPEED = 100000;
    public static int CHARGING_STATION_CHARGE_RADIUS = 8;
    public static int CHARGING_STATION_DEFAULT_FUEL_TIME = 1600;
    public static int ARTILLERY_INDICATOR_LIST_SIZE = 32;
    public static boolean MINE_HITBOX_INVISIBLE = false;
    public static boolean SMOKE_HIDE_TARGET = false;

    public static void load() {
        if (Files.notExists(CONFIG_PATH)) {
            save();
            return;
        }
        try {
            var reader = Files.newBufferedReader(CONFIG_PATH);
            var data = GSON.fromJson(reader, Data.class);
            if (data != null) {
                ALLOW_TACTICAL_SPRINT = data.allowTacticalSprint;
                SEND_KILL_FEEDBACK = data.sendKillFeedback;
                ALLOW_FORCE_DAMAGE = data.allowForceDamage;
                DROP_AMMO_BOX = data.dropAmmoBox;
                DEFAULT_ARMOR_LEVEL = data.defaultArmorLevel;
                MILITARY_ARMOR_LEVEL = data.militaryArmorLevel;
                HEAVY_MILITARY_ARMOR_LEVEL = data.heavyMilitaryArmorLevel;
                ARMOR_POINT_PER_LEVEL = data.armorPointPerLevel;
                CHARGING_STATION_MAX_ENERGY = data.chargingStationMaxEnergy;
                CHARGING_STATION_GENERATE_SPEED = data.chargingStationGenerateSpeed;
                CHARGING_STATION_TRANSFER_SPEED = data.chargingStationTransferSpeed;
                CHARGING_STATION_CHARGE_RADIUS = data.chargingStationChargeRadius;
                CHARGING_STATION_DEFAULT_FUEL_TIME = data.chargingStationDefaultFuelTime;
                ARTILLERY_INDICATOR_LIST_SIZE = data.artilleryIndicatorListSize;
                MINE_HITBOX_INVISIBLE = data.mineHitboxInvisible;
                SMOKE_HIDE_TARGET = data.smokeHideTarget;
            }
        } catch (IOException e) {
            LOGGER.error("Failed to load misc config", e);
        }
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            var writer = Files.newBufferedWriter(CONFIG_PATH);
            GSON.toJson(new Data(), writer);
            writer.close();
        } catch (IOException e) {
            LOGGER.error("Failed to save misc config", e);
        }
    }

    private static class Data {
        public boolean allowTacticalSprint = ALLOW_TACTICAL_SPRINT;
        public boolean sendKillFeedback = SEND_KILL_FEEDBACK;
        public boolean allowForceDamage = ALLOW_FORCE_DAMAGE;
        public boolean dropAmmoBox = DROP_AMMO_BOX;
        public int defaultArmorLevel = DEFAULT_ARMOR_LEVEL;
        public int militaryArmorLevel = MILITARY_ARMOR_LEVEL;
        public int heavyMilitaryArmorLevel = HEAVY_MILITARY_ARMOR_LEVEL;
        public int armorPointPerLevel = ARMOR_POINT_PER_LEVEL;
        public int chargingStationMaxEnergy = CHARGING_STATION_MAX_ENERGY;
        public int chargingStationGenerateSpeed = CHARGING_STATION_GENERATE_SPEED;
        public int chargingStationTransferSpeed = CHARGING_STATION_TRANSFER_SPEED;
        public int chargingStationChargeRadius = CHARGING_STATION_CHARGE_RADIUS;
        public int chargingStationDefaultFuelTime = CHARGING_STATION_DEFAULT_FUEL_TIME;
        public int artilleryIndicatorListSize = ARTILLERY_INDICATOR_LIST_SIZE;
        public boolean mineHitboxInvisible = MINE_HITBOX_INVISIBLE;
        public boolean smokeHideTarget = SMOKE_HIDE_TARGET;
    }
}
