package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.event.ClientEventHandler
import com.atsuishio.superbwarfare.tools.localPlayer
import net.minecraft.client.renderer.item.ItemProperties

object ModProperties {
    @JvmStatic
    fun init() {
        ItemProperties.register(ModItems.MONITOR, loc("monitor_linked")) { _, _, holder, _ ->
            if (ClientEventHandler.hasRemoteBinding && (holder == null || holder === localPlayer)) 1f else 0f
        }
        ItemProperties.register(ModItems.ARMOR_PLATE, loc("armor_plate_infinite")) { itemStack, _, _, _ ->
            if (itemStack.tag?.getBoolean("Infinite") == true) 1f else 0f
        }
    }
}
