package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.tools.NBTTool;
import net.minecraft.client.renderer.item.ItemProperties;

public class ModProperties {

    public static void init() {
        ItemProperties.register(ModItems.MONITOR, Mod.loc("monitor_linked"),
                (itemStack, clientWorld, livingEntity, seed) -> NBTTool.getTag(itemStack).getBoolean("Linked") ? 1F : 0);
        ItemProperties.register(ModItems.ARMOR_PLATE, Mod.loc("armor_plate_infinite"),
                (itemStack, clientWorld, livingEntity, seed) -> NBTTool.getTag(itemStack).getBoolean("Infinite") ? 1F : 0);
    }
}
