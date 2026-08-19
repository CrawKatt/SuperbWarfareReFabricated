package com.atsuishio.superbwarfare.recipe

import com.atsuishio.superbwarfare.init.ModPotions
import com.atsuishio.superbwarfare.tools.isSameItemStack
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionUtils
import net.minecraft.world.item.alchemy.Potions

object ModPotionRecipes {
    private var recipes = emptyList<PotionRecipe>()

    @JvmStatic
    fun register() {
        val water = potion(Potions.WATER)
        val shock = potion(ModPotions.SHOCK)
        val strongShock = potion(ModPotions.STRONG_SHOCK)
        val longShock = potion(ModPotions.LONG_SHOCK)
        recipes = listOf(
            PotionRecipe(water, ItemStack(Items.LIGHTNING_ROD), shock),
            PotionRecipe(shock, ItemStack(Items.GLOWSTONE_DUST), strongShock),
            PotionRecipe(shock, ItemStack(Items.REDSTONE), longShock)
        )
    }

    private fun potion(potion: Potion): ItemStack {
        return PotionUtils.setPotion(Items.POTION.defaultInstance, potion)
    }

    @JvmStatic
    fun isIngredient(stack: ItemStack): Boolean {
        return recipes.any { isSameItemStack(it.ingredient, stack) }
    }

    @JvmStatic
    fun hasMix(input: ItemStack, ingredient: ItemStack): Boolean {
        return findRecipe(input, ingredient) != null
    }

    @JvmStatic
    fun mix(input: ItemStack, ingredient: ItemStack): ItemStack {
        return findRecipe(input, ingredient)?.output?.copy() ?: ItemStack.EMPTY
    }

    private fun findRecipe(input: ItemStack, ingredient: ItemStack): PotionRecipe? {
        if (input.isEmpty || input.count != 1 || ingredient.isEmpty) return null
        return recipes.firstOrNull {
            isSameItemStack(it.input, input) && isSameItemStack(it.ingredient, ingredient)
        }
    }

    private data class PotionRecipe(val input: ItemStack, val ingredient: ItemStack, val output: ItemStack)
}
