package com.atsuishio.superbwarfare.compat;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

public class CompatHolder {

    public static final String DMV = "dreamaticvoyage";
    public static final String VRC = "virtuarealcraft";
    /** Fabric Loader id (the Forge build uses {@code cloth_config}). */
    public static final String CLOTH_CONFIG = "cloth-config";
    public static final String COLD_SWEAT = "cold_sweat";
    public static final String REALCAMERA = "realcamera";
    public static final String NET_MUSIC = "netmusic";

    public static MobEffect getVrcCurseFlame() {
        return BuiltInRegistries.MOB_EFFECT.get(new ResourceLocation(VRC, "curse_flame"));
    }

    public static void hasMod(String modId, Runnable runnable) {
        if (FabricLoader.getInstance().isModLoaded(modId)) {
            runnable.run();
        }
    }
}
