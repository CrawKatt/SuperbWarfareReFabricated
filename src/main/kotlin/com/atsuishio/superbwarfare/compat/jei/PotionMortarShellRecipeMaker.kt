package com.atsuishio.superbwarfare.compat.jei

import com.atsuishio.superbwarfare.init.ModItems
import mezz.jei.api.constants.ModIds
import net.minecraft.core.NonNullList
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.PotionUtils
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.CraftingRecipe
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.ShapedRecipe

object PotionMortarShellRecipeMaker {
    fun createRecipes(): List<CraftingRecipe> {
        val group = "jei.potion_mortar_shell"
        val ingredient = Ingredient.of(ItemStack(ModItems.MORTAR_SHELL))

        return BuiltInRegistries.POTION.map { potion ->
            val input = PotionUtils.setPotion(ItemStack(Items.LINGERING_POTION), potion)
            val output = PotionUtils.setPotion(ItemStack(ModItems.POTION_MORTAR_SHELL, 4), potion)

            val potionIngredient = Ingredient.of(input)
            val inputs = NonNullList.of(
                Ingredient.EMPTY,
                Ingredient.EMPTY, ingredient, Ingredient.EMPTY,
                ingredient, potionIngredient, ingredient,
                Ingredient.EMPTY, ingredient, Ingredient.EMPTY
            )

            val id = ResourceLocation(ModIds.MINECRAFT_ID, group + "." + output.descriptionId)
            ShapedRecipe(id, group, CraftingBookCategory.MISC, 3, 3, inputs, output)
        }
    }
}
