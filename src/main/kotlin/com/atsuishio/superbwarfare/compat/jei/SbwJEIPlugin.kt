package com.atsuishio.superbwarfare.compat.jei

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.client.screens.BlueprintResearchTableScreen
import com.atsuishio.superbwarfare.init.ModItems
import com.atsuishio.superbwarfare.init.ModRecipes
import com.atsuishio.superbwarfare.item.gun.GunItem
import com.atsuishio.superbwarfare.tools.clientLevel
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.constants.RecipeTypes
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter
import mezz.jei.api.recipe.RecipeIngredientRole
import mezz.jei.api.registration.IGuiHandlerRegistration
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration
import mezz.jei.api.registration.ISubtypeRegistration
import mezz.jei.api.runtime.IJeiRuntime
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.PotionUtils
import java.util.Optional

@JeiPlugin
class SbwJEIPlugin : IModPlugin {
    override fun getPluginUid(): ResourceLocation {
        return loc("jei_plugin")
    }

    override fun onRuntimeAvailable(jeiRuntime: IJeiRuntime) {
        Companion.jeiRuntime = jeiRuntime
    }

    override fun registerCategories(registration: IRecipeCategoryRegistration) {
        registration.addRecipeCategories(GunPerksCategory(registration.jeiHelpers.guiHelper))
        registration.addRecipeCategories(VehicleAssemblingCategory(registration.jeiHelpers.guiHelper))
        registration.addRecipeCategories(ResearchingCategory(registration.jeiHelpers.guiHelper))
    }

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
        registration.addRecipeCatalyst(ItemStack(ModItems.REFORGING_TABLE), GunPerksCategory.TYPE)
        registration.addRecipeCatalyst(
            ItemStack(ModItems.VEHICLE_ASSEMBLING_TABLE),
            VehicleAssemblingCategory.TYPE
        )
        registration.addRecipeCatalyst(
            ItemStack(ModItems.BLUEPRINT_RESEARCH_TABLE),
            ResearchingCategory.TYPE
        )
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        val level = clientLevel ?: return
        val recipeManager = level.recipeManager

        val guns = BuiltInRegistries.ITEM.stream()
            .filter { it is GunItem }
            .map { it.defaultInstance }
            .toList()
        registration.addRecipes(GunPerksCategory.TYPE, guns)
        registration.addRecipes(
            VehicleAssemblingCategory.TYPE,
            recipeManager.getAllRecipesFor(ModRecipes.VEHICLE_ASSEMBLING_TYPE)
        )
        registration.addRecipes(
            ResearchingCategory.TYPE,
            recipeManager.getAllRecipesFor(ModRecipes.RESEARCHING_TYPE)
        )

        registration.addItemStackInfo(
            ItemStack(ModItems.ANCIENT_CPU),
            Component.translatable("jei.superbwarfare.ancient_cpu")
        )
        registration.addItemStackInfo(
            ItemStack(ModItems.CHARGING_STATION),
            Component.translatable("jei.superbwarfare.charging_station")
        )

        registration.addRecipes(RecipeTypes.CRAFTING, PotionMortarShellRecipeMaker.createRecipes())
    }

    override fun registerItemSubtypes(registration: ISubtypeRegistration) {
        registration.registerSubtypeInterpreter(ModItems.CONTAINER) { ingredient, _ ->
            val tag = ingredient.tag ?: return@registerSubtypeInterpreter IIngredientSubtypeInterpreter.NONE
            tag.getCompound("BlockEntityTag").getString("EntityType")
        }

        registration.registerSubtypeInterpreter(ModItems.POTION_MORTAR_SHELL) { stack, _ ->
            if (!stack.hasTag()) {
                return@registerSubtypeInterpreter IIngredientSubtypeInterpreter.NONE
            }

            val potionTypeString = PotionUtils.getPotion(stack).getName("")
            val stringBuilder = StringBuilder(potionTypeString)
            for (effect in PotionUtils.getMobEffects(stack)) {
                stringBuilder.append(";").append(effect)
            }
            stringBuilder.toString()
        }

        registration.registerSubtypeInterpreter(ModItems.C4_BOMB) { ingredient, _ ->
            val tag = ingredient.tag ?: return@registerSubtypeInterpreter IIngredientSubtypeInterpreter.NONE
            tag.getBoolean("Control").toString()
        }
    }

    override fun registerGuiHandlers(registration: IGuiHandlerRegistration) {
        registration.addRecipeClickArea(
            BlueprintResearchTableScreen::class.java,
            64,
            23,
            48,
            12,
            ResearchingCategory.TYPE
        )
    }

    companion object {
        private var jeiRuntime: IJeiRuntime? = null

        fun getJeiRuntime(): Optional<IJeiRuntime> {
            return Optional.ofNullable(jeiRuntime)
        }

        /**
         * Code based on @Mafuyu404's [TACZ-addon](https://github.com/Mafuyu404/TACZ-addon)
         */
        @JvmStatic
        fun showRecipes(itemStack: ItemStack): Boolean {
            val result = booleanArrayOf(false)
            getJeiRuntime().ifPresent { runtime ->
                runtime.ingredientManager.getIngredientTypeChecked(itemStack).ifPresent { type ->
                    runtime.recipesGui.show(
                        runtime.jeiHelpers.focusFactory.createFocus(
                            RecipeIngredientRole.OUTPUT,
                            type,
                            itemStack
                        )
                    )
                    result[0] = true
                }
            }
            return result[0]
        }
    }
}
