package com.atsuishio.superbwarfare.procedures

import com.atsuishio.superbwarfare.Mod
import net.fabricmc.loader.api.FabricLoader

object WelcomeProcedure {
    @JvmStatic
    fun onCommonSetup() {
        val version = FabricLoader.getInstance().getModContainer(Mod.MODID)
            .map { it.metadata.version.friendlyString }
            .orElse("unknown")
        Mod.LOGGER.info(
            """Now Loading...
* This Mod used to be made by MCreator *
  _____  ______  __          __ 
 / ____| |  __ \ \ \        / / 
| (___   | |__) | \ \  /\  / /  
 \___ \  |  __ (   \ \/  \/ /   
 ____) | | |__) |   \  /\  /    
|_____/  |_____/     \/  \/
* Superb Warfare - Version: $version *
            """.trimIndent()
        )
    }
}
