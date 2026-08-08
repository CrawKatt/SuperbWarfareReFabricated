package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.recipe.*
import com.atsuishio.superbwarfare.recipe.vehicle.VehicleAssemblingRecipe
import com.atsuishio.superbwarfare.recipe.vehicle.VehicleAssemblingRecipeSerializer
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer

@Suppress("unused")
object ModRecipes {
    @JvmField
    val POTION_MORTAR_SHELL_SERIALIZER: RecipeSerializer<PotionMortarShellRecipe> =
        registerSerializer("potion_mortar_shell", SimpleCraftingRecipeSerializer(::PotionMortarShellRecipe))

    @JvmField
    val SMOKE_DYE_SERIALIZER: RecipeSerializer<SmokeDyeRecipe> =
        registerSerializer("smoke_dye", SimpleCraftingRecipeSerializer(::SmokeDyeRecipe))

    @JvmField
    val VEHICLE_ASSEMBLING_SERIALIZER: RecipeSerializer<VehicleAssemblingRecipe> =
        registerSerializer("vehicle_assembling", VehicleAssemblingRecipeSerializer)

    @JvmField
    val VEHICLE_RESET_SERIALIZER: RecipeSerializer<VehicleResetRecipe> =
        registerSerializer("vehicle_reset", SimpleCraftingRecipeSerializer(::VehicleResetRecipe))

    @JvmField
    val RESEARCHING_SERIALIZER: RecipeSerializer<ResearchingRecipe> =
        registerSerializer("researching", ResearchingRecipe.Serializer)

    @JvmField
    val VEHICLE_ASSEMBLING_TYPE: RecipeType<VehicleAssemblingRecipe> =
        registerType("vehicle_assembling", object : RecipeType<VehicleAssemblingRecipe> {
            override fun toString(): String {
                return Mod.MODID + ":vehicle_assembling"
            }
        })

    @JvmField
    val RESEARCHING_TYPE: RecipeType<ResearchingRecipe> =
        registerType("researching", object : RecipeType<ResearchingRecipe> {
            override fun toString(): String {
                return Mod.MODID + ":researching"
            }
        })

    private fun <T : RecipeSerializer<*>> registerSerializer(name: String, serializer: T): T {
        return Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, Mod.loc(name), serializer)
    }

    private fun <T : RecipeType<*>> registerType(name: String, type: T): T {
        return Registry.register(BuiltInRegistries.RECIPE_TYPE, Mod.loc(name), type)
    }

    @JvmStatic
    fun init() {
    }
}
