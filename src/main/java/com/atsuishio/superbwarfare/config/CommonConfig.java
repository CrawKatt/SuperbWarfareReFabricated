package com.atsuishio.superbwarfare.config;

import com.atsuishio.superbwarfare.config.common.GameplayConfig;

public class CommonConfig {

    public static void init() {
        GameplayConfig.load();
    }
}
