package com.atsuishio.superbwarfare.compat.modmenu

import com.atsuishio.superbwarfare.compat.CompatHolder
import com.atsuishio.superbwarfare.compat.clothconfig.ClothConfigHelper
import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import net.fabricmc.loader.api.FabricLoader

class ModMenuCompat : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> =
        if (FabricLoader.getInstance().isModLoaded(CompatHolder.CLOTH_CONFIG)) {
            ConfigScreenFactory { parent -> ClothConfigHelper.getConfigScreen(parent) }
        } else {
            super.getModConfigScreenFactory()
        }
}
