package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.recipe.*;
import com.atsuishio.superbwarfare.recipe.vehicle.VehicleAssemblingRecipe;
import com.atsuishio.superbwarfare.recipe.vehicle.VehicleAssemblingRecipeSerializer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

import java.util.function.Supplier;

public class ModRecipes {

    public static final Supplier<RecipeSerializer<PotionMortarShellRecipe>> POTION_MORTAR_SHELL_SERIALIZER =
            Registration.recipeSerializer("potion_mortar_shell", () -> new SimpleCraftingRecipeSerializer<>(PotionMortarShellRecipe::new));
    public static final Supplier<RecipeSerializer<AmmoBoxAddAmmoRecipe>> AMMO_BOX_ADD_AMMO_SERIALIZER =
            Registration.recipeSerializer("ammo_box_add_ammo", () -> new SimpleCraftingRecipeSerializer<>(AmmoBoxAddAmmoRecipe::new));
    public static final Supplier<RecipeSerializer<AmmoBoxExtractAmmoRecipe>> AMMO_BOX_EXTRACT_AMMO_SERIALIZER =
            Registration.recipeSerializer("ammo_box_extract_ammo", () -> new SimpleCraftingRecipeSerializer<>(AmmoBoxExtractAmmoRecipe::new));
    public static final Supplier<RecipeSerializer<SmokeDyeRecipe>> SMOKE_DYE_SERIALIZER =
            Registration.recipeSerializer("smoke_dye", () -> new SimpleCraftingRecipeSerializer<>(SmokeDyeRecipe::new));
    public static final Supplier<RecipeSerializer<VehicleAssemblingRecipe>> VEHICLE_ASSEMBLING_SERIALIZER =
            Registration.recipeSerializer("vehicle_assembling", VehicleAssemblingRecipeSerializer::new);
    public static final Supplier<RecipeSerializer<VehicleResetRecipe>> VEHICLE_RESET_SERIALIZER =
            Registration.recipeSerializer("vehicle_reset", () -> new SimpleCraftingRecipeSerializer<>(VehicleResetRecipe::new));
    public static final Supplier<RecipeSerializer<CupidArrowRecipe>> CUPID_ARROW_SERIALIZER =
            Registration.recipeSerializer("cupid_arrow", () -> new SimpleCraftingRecipeSerializer<>(CupidArrowRecipe::new));
    public static final Supplier<RecipeSerializer<C4BombRcRecipe>> C4_BOMB_RC_SERIALIZER =
            Registration.recipeSerializer("c4_bomb_rc", () -> new SimpleCraftingRecipeSerializer<>(C4BombRcRecipe::new));

    public static final Supplier<RecipeType<VehicleAssemblingRecipe>> VEHICLE_ASSEMBLING_TYPE =
            Registration.recipeType("vehicle_assembling", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return Mod.MODID + ":vehicle_assembling";
                }
            });

    public static void register() {

    }
}
