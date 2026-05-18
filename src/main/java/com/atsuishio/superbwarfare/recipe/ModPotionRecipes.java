package com.atsuishio.superbwarfare.recipe;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.init.ModPotions;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;

public class ModPotionRecipes {

    public static void register() {
        Mod.LOGGER.info("Registering potion recipes - requires data pack or mixin for vanilla brewing");
    }

    public static ItemStack potion(net.minecraft.core.Holder<net.minecraft.world.item.alchemy.Potion> potion) {
        var stack = Items.POTION.getDefaultInstance();
        var contents = stack.get(DataComponents.POTION_CONTENTS);

        if (contents != null) {
            stack.set(DataComponents.POTION_CONTENTS, contents.withPotion(potion));
        }

        return stack;
    }
}
