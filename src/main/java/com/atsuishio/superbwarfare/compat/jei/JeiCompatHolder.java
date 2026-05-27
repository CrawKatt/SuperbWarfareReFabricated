package com.atsuishio.superbwarfare.compat.jei;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.item.ItemStack;

public class JeiCompatHolder {

    public static final String JEI = "jei";

    public static boolean hasJEI() {
        return FabricLoader.getInstance().isModLoaded(JEI);
    }

    public static boolean showRecipes(ItemStack stack) {
        return SbwJEIPlugin.showRecipes(stack);
    }
}
