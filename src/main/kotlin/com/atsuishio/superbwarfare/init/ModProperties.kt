package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.Mod.loc
import com.atsuishio.superbwarfare.tools.tag
import net.minecraft.client.renderer.item.ItemProperties

object ModProperties {

    @JvmStatic
    fun init() {
        ItemProperties.register(ModItems.MONITOR, loc("monitor_linked")) { itemStack, _, _, _ ->
            if (itemStack.tag?.getBoolean("Linked") == true) 1f else 0f
        }
        ItemProperties.register(ModItems.ARMOR_PLATE, loc("armor_plate_infinite")) { itemStack, _, _, _ ->
            if (itemStack.tag?.getBoolean("Infinite") == true) 1f else 0f
        }
    }
}