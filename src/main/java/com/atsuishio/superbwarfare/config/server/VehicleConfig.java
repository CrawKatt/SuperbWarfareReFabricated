package com.atsuishio.superbwarfare.config.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class VehicleConfig {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Path CONFIG_PATH = Path.of("config", "superbwarfare", "vehicle.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static boolean COLLISION_DESTROY_SOFT_BLOCKS = false;
    public static boolean COLLISION_DESTROY_NORMAL_BLOCKS = false;
    public static boolean COLLISION_DESTROY_HARD_BLOCKS = false;
    public static boolean COLLISION_DESTROY_BLOCKS_BEASTLY = false;
    public static boolean VEHICLE_ITEM_PICKUP = true;
    public static boolean COLLECT_DROPS_BY_CRASHING = true;

    public static List<String> COLLISION_ENTITY_WHITELIST = List.of();

    public static final List<String> DEFAULT_COLLISION_ENTITY_WHITELIST = List.of();

    public static int REPAIR_COOLDOWN = 200;
    public static double REPAIR_AMOUNT = 0.05d;

    public static int SELF_EXPLOSION_DAMAGE = 114514;
    public static int SELF_EXPLOSION_COUNT = 5;
    public static int AIR_CRASH_EXPLOSION_DAMAGE = 114514;
    public static int AIR_CRASH_EXPLOSION_COUNT = 5;

    public static int VEHICLE_INFO_DISPLAY_DISTANCE = 512;

    public static void load() {
        if (Files.notExists(CONFIG_PATH)) {
            save();
            return;
        }
        try {
            var reader = Files.newBufferedReader(CONFIG_PATH);
            var data = GSON.fromJson(reader, Data.class);
            if (data != null) {
                COLLISION_DESTROY_SOFT_BLOCKS = data.collisionDestroySoftBlocks;
                COLLISION_DESTROY_NORMAL_BLOCKS = data.collisionDestroyNormalBlocks;
                COLLISION_DESTROY_HARD_BLOCKS = data.collisionDestroyHardBlocks;
                COLLISION_DESTROY_BLOCKS_BEASTLY = data.collisionDestroyBlocksBeastly;
                VEHICLE_ITEM_PICKUP = data.vehicleItemPickup;
                COLLECT_DROPS_BY_CRASHING = data.collectDropsByCrashing;
                COLLISION_ENTITY_WHITELIST = data.collisionEntityWhitelist;
                REPAIR_COOLDOWN = data.repairCooldown;
                REPAIR_AMOUNT = data.repairAmount;
                SELF_EXPLOSION_DAMAGE = data.selfExplosionDamage;
                SELF_EXPLOSION_COUNT = data.selfExplosionCount;
                AIR_CRASH_EXPLOSION_DAMAGE = data.airCrashExplosionDamage;
                AIR_CRASH_EXPLOSION_COUNT = data.airCrashExplosionCount;
                VEHICLE_INFO_DISPLAY_DISTANCE = data.vehicleInfoDisplayDistance;
            }
        } catch (IOException e) {
            LOGGER.error("Failed to load vehicle config", e);
        }
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            var writer = Files.newBufferedWriter(CONFIG_PATH);
            GSON.toJson(new Data(), writer);
            writer.close();
        } catch (IOException e) {
            LOGGER.error("Failed to save vehicle config", e);
        }
    }

    private static class Data {
        public boolean collisionDestroySoftBlocks = COLLISION_DESTROY_SOFT_BLOCKS;
        public boolean collisionDestroyNormalBlocks = COLLISION_DESTROY_NORMAL_BLOCKS;
        public boolean collisionDestroyHardBlocks = COLLISION_DESTROY_HARD_BLOCKS;
        public boolean collisionDestroyBlocksBeastly = COLLISION_DESTROY_BLOCKS_BEASTLY;
        public boolean vehicleItemPickup = VEHICLE_ITEM_PICKUP;
        public boolean collectDropsByCrashing = COLLECT_DROPS_BY_CRASHING;
        public List<String> collisionEntityWhitelist = COLLISION_ENTITY_WHITELIST;
        public int repairCooldown = REPAIR_COOLDOWN;
        public double repairAmount = REPAIR_AMOUNT;
        public int selfExplosionDamage = SELF_EXPLOSION_DAMAGE;
        public int selfExplosionCount = SELF_EXPLOSION_COUNT;
        public int airCrashExplosionDamage = AIR_CRASH_EXPLOSION_DAMAGE;
        public int airCrashExplosionCount = AIR_CRASH_EXPLOSION_COUNT;
        public int vehicleInfoDisplayDistance = VEHICLE_INFO_DISPLAY_DISTANCE;
    }
}
