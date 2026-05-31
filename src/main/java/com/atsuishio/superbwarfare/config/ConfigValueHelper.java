package com.atsuishio.superbwarfare.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class ConfigValueHelper {

    private ConfigValueHelper() {
    }

    public static <T> T getOrDefault(ForgeConfigSpec.ConfigValue<T> value) {
        try {
            return value.get();
        } catch (IllegalStateException exception) {
            return value.getDefault();
        }
    }
}
