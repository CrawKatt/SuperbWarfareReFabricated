package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.tools.ItemNBTTool;
import net.minecraft.client.renderer.item.ItemProperties;

public class ModProperties {
    public static void register() {
        ItemProperties.register(ModItems.MONITOR.get(), Mod.loc("monitor_linked"),
                (itemStack, clientWorld, livingEntity, seed) -> ItemNBTTool.getBoolean(itemStack, "Linked", false) ? 1 : 0);
        ItemProperties.register(ModItems.ARMOR_PLATE.get(), Mod.loc("armor_plate_infinite"),
                (itemStack, clientWorld, livingEntity, seed) -> ItemNBTTool.getBoolean(itemStack, "Infinite", false) ? 1 : 0);
    }
}
