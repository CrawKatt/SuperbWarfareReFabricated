package com.atsuishio.superbwarfare.config;

import com.atsuishio.superbwarfare.config.client.*;

public class ClientConfig {

    public static void init() {
        ReloadConfig.load();
        KillMessageConfig.load();
        DisplayConfig.load();
        ControlConfig.load();
        EnvironmentChecksumConfig.load();
    }
}
