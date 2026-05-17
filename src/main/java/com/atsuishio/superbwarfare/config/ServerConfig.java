package com.atsuishio.superbwarfare.config;

import com.atsuishio.superbwarfare.config.server.*;

public class ServerConfig {

    public static void init() {
        SpawnConfig.load();
        ProjectileConfig.load();
        ExplosionConfig.load();
        VehicleConfig.load();
        MiscConfig.load();
    }
}
