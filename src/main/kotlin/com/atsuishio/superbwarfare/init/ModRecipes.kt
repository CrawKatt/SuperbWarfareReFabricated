package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.recipe.*
import com.atsuishio.superbwarfare.recipe.vehicle.VehicleAssemblingRecipe
import com.atsuishio.superbwarfare.recipe.vehicle.VehicleAssemblingRecipeSerializer
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer
import java.util.function.Supplier

@Suppress("unused")
object ModRecipes {
    @JvmField
    val CUPID_ARROW_SERIALIZER: RecipeSerializer<CupidArrowRecipe> =
        Registration.recipeSerializer("cupid_arrow") {
            SimpleCraftingRecipeSerializer { id, category -> CupidArrowRecipe(id, category) }
        }

    @JvmField
    val C4_BOMB_RC_SERIALIZER: RecipeSerializer<C4BombRcRecipe> =
        Registration.recipeSerializer("c4_bomb_rc") {
            SimpleCraftingRecipeSerializer { id, category -> C4BombRcRecipe(id, category) }
        }

    @JvmField
    val POTION_MORTAR_SHELL_SERIALIZER: RecipeSerializer<PotionMortarShellRecipe> =
        Registration.recipeSerializer("potion_mortar_shell") {
            SimpleCraftingRecipeSerializer { id, category -> PotionMortarShellRecipe(id, category) }
        }

    @JvmField
    val SMOKE_DYE_SERIALIZER: RecipeSerializer<SmokeDyeRecipe> =
        Registration.recipeSerializer("smoke_dye") {
            SimpleCraftingRecipeSerializer { id, category -> SmokeDyeRecipe(id, category) }
        }

    @JvmField
    val VEHICLE_ASSEMBLING_SERIALIZER: RecipeSerializer<VehicleAssemblingRecipe> =
        Registration.recipeSerializer("vehicle_assembling") { VehicleAssemblingRecipeSerializer() }

    @JvmField
    val VEHICLE_RESET_SERIALIZER: RecipeSerializer<VehicleResetRecipe> =
        Registration.recipeSerializer("vehicle_reset") {
            SimpleCraftingRecipeSerializer { id, category -> VehicleResetRecipe(id, category) }
        }

    @JvmField
    val RESEARCHING_SERIALIZER: RecipeSerializer<ResearchingRecipe> =
        Registration.recipeSerializer("researching") { ResearchingRecipe.Serializer() }

    @JvmField
    val VEHICLE_ASSEMBLING_TYPE: RecipeType<VehicleAssemblingRecipe> =
        Registration.recipeType("vehicle_assembling") {
            object : RecipeType<VehicleAssemblingRecipe> {
                override fun toString(): String {
                    return Mod.MODID + ":vehicle_assembling"
                }
            }
        }

    @JvmField
    val RESEARCHING_TYPE: RecipeType<ResearchingRecipe> =
        Registration.recipeType("researching") {
            object : RecipeType<ResearchingRecipe> {
                override fun toString(): String {
                    return Mod.MODID + ":researching"
                }
            }
        }

    @JvmStatic
    fun init() {
    }
}
