package com.atsuishio.superbwarfare.compat.tacz;

import net.fabricmc.loader.api.FabricLoader;

public final class TaczCompat {
    public static final String SUPPORTED_VERSION = "0.7.0-forge1.1.8-hotfix";

    private TaczCompat() {
    }

    public static boolean isLoaded() {
        return FabricLoader.getInstance().isModLoaded("tacz");
    }

    public static boolean isCompatible() {
        return FabricLoader.getInstance().getModContainer("tacz")
                .map(container -> SUPPORTED_VERSION.equals(container.getMetadata().getVersion().getFriendlyString()))
                .orElse(false);
    }
}
