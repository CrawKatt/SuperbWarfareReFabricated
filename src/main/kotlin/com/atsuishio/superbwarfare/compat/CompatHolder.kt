package com.atsuishio.superbwarfare.compat

import net.fabricmc.loader.api.FabricLoader

object CompatHolder {
    const val DMV: String = "dreamaticvoyage"
    const val VRC: String = "virtuarealcraft"
    const val CLOTH_CONFIG: String = "cloth_config"
    const val COLD_SWEAT: String = "cold_sweat"
    const val THERMOO: String = "thermoo"
    const val SCORCHFUL: String = "scorchful"
    const val FROSTIFUL: String = "frostiful"
    const val REALCAMERA: String = "realcamera"
    const val NET_MUSIC: String = "netmusic"
    const val SABLE: String = "sable"

    @JvmStatic
    fun hasMod(modid: String, runnable: Runnable) {
        if (FabricLoader.getInstance().isModLoaded(modid)) {
            runnable.run()
        }
    }
}
