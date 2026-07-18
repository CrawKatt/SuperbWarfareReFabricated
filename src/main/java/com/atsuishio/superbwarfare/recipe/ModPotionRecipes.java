package com.atsuishio.superbwarfare.recipe;

import com.atsuishio.superbwarfare.init.ModPotions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ModPotionRecipes {

    private static List<PotionRecipe> recipes = List.of();

    public static void register() {
        ItemStack water = potion(Potions.WATER);
        ItemStack shock = potion(ModPotions.SHOCK.get());
        ItemStack strongShock = potion(ModPotions.STRONG_SHOCK.get());
        ItemStack longShock = potion(ModPotions.LONG_SHOCK.get());
        recipes = List.of(
                new PotionRecipe(water, new ItemStack(Items.LIGHTNING_ROD), shock),
                new PotionRecipe(shock, new ItemStack(Items.GLOWSTONE_DUST), strongShock),
                new PotionRecipe(shock, new ItemStack(Items.REDSTONE), longShock)
        );
    }

    private static ItemStack potion(Potion potion) {
        return PotionUtils.setPotion(Items.POTION.getDefaultInstance(), potion);
    }

    public static boolean isIngredient(ItemStack stack) {
        return recipes.stream().anyMatch(recipe ->
                ItemStack.isSameItemSameTags(recipe.ingredient(), stack));
    }

    public static boolean hasMix(ItemStack input, ItemStack ingredient) {
        return findRecipe(input, ingredient) != null;
    }

    public static ItemStack mix(ItemStack input, ItemStack ingredient) {
        PotionRecipe recipe = findRecipe(input, ingredient);
        return recipe == null ? ItemStack.EMPTY : recipe.output().copy();
    }

    @Nullable
    private static PotionRecipe findRecipe(ItemStack input, ItemStack ingredient) {
        if (input.isEmpty() || input.getCount() != 1 || ingredient.isEmpty()) {
            return null;
        }

        for (PotionRecipe recipe : recipes) {
            if (ItemStack.isSameItemSameTags(recipe.input(), input)
                    && ItemStack.isSameItemSameTags(recipe.ingredient(), ingredient)) {
                return recipe;
            }
        }
        return null;
    }

    private record PotionRecipe(ItemStack input, ItemStack ingredient, ItemStack output) {
    }
}
