package com.atsuishio.superbwarfare.config.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ExplosionConfig {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Path CONFIG_PATH = Path.of("config", "superbwarfare", "explosion.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static int EXPLOSION_PENETRATION_RATIO = 15;
    public static boolean EXPLOSION_DESTROY = true;
    public static boolean EXTRA_EXPLOSION_EFFECT = true;

    public static int RGO_GRENADE_EXPLOSION_DAMAGE = 90;
    public static int RGO_GRENADE_EXPLOSION_RADIUS = 5;

    public static int M67_GRENADE_EXPLOSION_DAMAGE = 120;
    public static int M67_GRENADE_EXPLOSION_RADIUS = 6;

    public static int C4_EXPLOSION_COUNTDOWN = 514;
    public static int C4_EXPLOSION_DAMAGE = 300;
    public static int C4_EXPLOSION_RADIUS = 10;

    public static int CLAYMORE_EXPLOSION_DAMAGE = 140;
    public static int CLAYMORE_EXPLOSION_RADIUS = 4;

    public static void load() {
        if (Files.notExists(CONFIG_PATH)) {
            save();
            return;
        }
        try {
            var reader = Files.newBufferedReader(CONFIG_PATH);
            var data = GSON.fromJson(reader, Data.class);
            if (data != null) {
                EXPLOSION_PENETRATION_RATIO = data.explosionPenetrationRatio;
                EXPLOSION_DESTROY = data.explosionDestroy;
                EXTRA_EXPLOSION_EFFECT = data.extraExplosionEffect;
                RGO_GRENADE_EXPLOSION_DAMAGE = data.rgoGrenadeExplosionDamage;
                RGO_GRENADE_EXPLOSION_RADIUS = data.rgoGrenadeExplosionRadius;
                M67_GRENADE_EXPLOSION_DAMAGE = data.m67GrenadeExplosionDamage;
                M67_GRENADE_EXPLOSION_RADIUS = data.m67GrenadeExplosionRadius;
                C4_EXPLOSION_COUNTDOWN = data.c4ExplosionCountdown;
                C4_EXPLOSION_DAMAGE = data.c4ExplosionDamage;
                C4_EXPLOSION_RADIUS = data.c4ExplosionRadius;
                CLAYMORE_EXPLOSION_DAMAGE = data.claymoreExplosionDamage;
                CLAYMORE_EXPLOSION_RADIUS = data.claymoreExplosionRadius;
            }
        } catch (IOException e) {
            LOGGER.error("Failed to load explosion config", e);
        }
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            var writer = Files.newBufferedWriter(CONFIG_PATH);
            GSON.toJson(new Data(), writer);
            writer.close();
        } catch (IOException e) {
            LOGGER.error("Failed to save explosion config", e);
        }
    }

    private static class Data {
        public int explosionPenetrationRatio = EXPLOSION_PENETRATION_RATIO;
        public boolean explosionDestroy = EXPLOSION_DESTROY;
        public boolean extraExplosionEffect = EXTRA_EXPLOSION_EFFECT;
        public int rgoGrenadeExplosionDamage = RGO_GRENADE_EXPLOSION_DAMAGE;
        public int rgoGrenadeExplosionRadius = RGO_GRENADE_EXPLOSION_RADIUS;
        public int m67GrenadeExplosionDamage = M67_GRENADE_EXPLOSION_DAMAGE;
        public int m67GrenadeExplosionRadius = M67_GRENADE_EXPLOSION_RADIUS;
        public int c4ExplosionCountdown = C4_EXPLOSION_COUNTDOWN;
        public int c4ExplosionDamage = C4_EXPLOSION_DAMAGE;
        public int c4ExplosionRadius = C4_EXPLOSION_RADIUS;
        public int claymoreExplosionDamage = CLAYMORE_EXPLOSION_DAMAGE;
        public int claymoreExplosionRadius = CLAYMORE_EXPLOSION_RADIUS;
    }
}
