package com.atsuishio.superbwarfare.datagen.builder

import net.minecraft.advancements.AdvancementHolder
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.ShapedRecipe

class ItemStackShapedRecipeBuilder(category: RecipeCategory, private val stack: ItemStack) :
    ShapedRecipeBuilder(category, stack.item, stack.count) {
    override fun save(output: RecipeOutput, id: ResourceLocation) {
        super.save(object : RecipeOutput by output {
            override fun accept(id: ResourceLocation, recipe: Recipe<*>, advancement: AdvancementHolder?) {
                val shaped = recipe as ShapedRecipe
                output.accept(
                    id,
                    ShapedRecipe(shaped.group, shaped.category(), shaped.pattern, stack, shaped.showNotification()),
                    advancement
                )
            }
        }, id)
    }
}
