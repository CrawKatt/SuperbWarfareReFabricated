package com.atsuishio.superbwarfare.recipe;

import com.atsuishio.superbwarfare.init.ModPotions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class ModPotionRecipes {

    public static void register() {
        ItemStack water = potion(Potions.WATER);
        ItemStack shock = potion(ModPotions.SHOCK.get());
        ItemStack strongShock = potion(ModPotions.STRONG_SHOCK.get());
        ItemStack longShock = potion(ModPotions.LONG_SHOCK.get());
        PotionBrewing.addMix(Potions.WATER, Items.LIGHTNING_ROD, ModPotions.SHOCK.get());
        PotionBrewing.addMix(ModPotions.SHOCK.get(), Items.GLOWSTONE_DUST, ModPotions.STRONG_SHOCK.get());
        PotionBrewing.addMix(ModPotions.SHOCK.get(), Items.REDSTONE, ModPotions.LONG_SHOCK.get());
    }

    private static ItemStack potion(Potion potion) {
        return PotionUtils.setPotion(Items.POTION.getDefaultInstance(), potion);
    }
}
