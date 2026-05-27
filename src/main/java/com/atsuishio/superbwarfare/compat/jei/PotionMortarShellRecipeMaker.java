package com.atsuishio.superbwarfare.compat.jei;

import com.atsuishio.superbwarfare.init.ModItems;
import mezz.jei.api.constants.ModIds;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;

import java.util.List;

public class PotionMortarShellRecipeMaker {

    public static List<CraftingRecipe> createRecipes() {
        String group = "jei.potion_mortar_shell";
        Ingredient ingredient = Ingredient.of(new ItemStack(ModItems.MORTAR_SHELL.get()));

        return BuiltInRegistries.POTION.stream()
                .<CraftingRecipe>map(potion -> {
                    ItemStack input = PotionUtils.setPotion(new ItemStack(Items.LINGERING_POTION), potion);
                    ItemStack output = PotionUtils.setPotion(new ItemStack(ModItems.POTION_MORTAR_SHELL.get(), 4), potion);

                    Ingredient potionIngredient = Ingredient.of(input);
                    NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY,
                            Ingredient.EMPTY, ingredient, Ingredient.EMPTY,
                            ingredient, potionIngredient, ingredient,
                            Ingredient.EMPTY, ingredient, Ingredient.EMPTY
                    );
                    ResourceLocation id = new ResourceLocation(ModIds.MINECRAFT_ID, group + "." + output.getDescriptionId());
                    return new ShapedRecipe(id, group, CraftingBookCategory.MISC, 3, 3, inputs, output);
                })
                .toList();
    }
}
