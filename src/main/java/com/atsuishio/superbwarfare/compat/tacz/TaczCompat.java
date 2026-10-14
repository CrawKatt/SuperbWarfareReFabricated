package com.atsuishio.superbwarfare.compat.tacz;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.api.metadata.version.VersionPredicate;

public final class TaczCompat {
    public static final String SUPPORTED_VERSION = "0.7.0-forge1.1.8-hotfix";
    public static final VersionPredicate SUPPORTED_VERSIONS = createVersionPredicate();

    private TaczCompat() {
    }

    public static boolean isLoaded() {
        return FabricLoader.getInstance().isModLoaded("tacz");
    }

    private static VersionPredicate createVersionPredicate() {
        try {
            return VersionPredicate.parse(">=" + SUPPORTED_VERSION);
        } catch (VersionParsingException why) {
            throw new IllegalStateException("Invalid TACZ version predicate", why);
        }
    }

    public static boolean isCompatible() {
        return FabricLoader.getInstance().getModContainer("tacz")
                .map(container -> SUPPORTED_VERSIONS.test(container.getMetadata().getVersion()))
                .orElse(false);
    }
}
