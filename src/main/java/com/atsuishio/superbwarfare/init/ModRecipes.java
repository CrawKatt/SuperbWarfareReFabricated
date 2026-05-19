package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.recipe.*;
import com.atsuishio.superbwarfare.recipe.vehicle.VehicleAssemblingRecipe;
import com.atsuishio.superbwarfare.recipe.vehicle.VehicleAssemblingRecipeSerializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

@SuppressWarnings("unused")
public class ModRecipes {

    public static final RecipeSerializer<PotionMortarShellRecipe> POTION_MORTAR_SHELL_SERIALIZER =
            registerSerializer("potion_mortar_shell", new SimpleCraftingRecipeSerializer<>(PotionMortarShellRecipe::new));

    public static final RecipeSerializer<AmmoBoxAddAmmoRecipe> AMMO_BOX_ADD_AMMO_SERIALIZER =
            registerSerializer("ammo_box_add_ammo", new SimpleCraftingRecipeSerializer<>(AmmoBoxAddAmmoRecipe::new));

    public static final RecipeSerializer<AmmoBoxExtractAmmoRecipe> AMMO_BOX_EXTRACT_AMMO_SERIALIZER =
            registerSerializer("ammo_box_extract_ammo", new SimpleCraftingRecipeSerializer<>(AmmoBoxExtractAmmoRecipe::new));

    public static final RecipeSerializer<SmokeDyeRecipe> SMOKE_DYE_SERIALIZER =
            registerSerializer("smoke_dye", new SimpleCraftingRecipeSerializer<>(SmokeDyeRecipe::new));

    public static final RecipeSerializer<VehicleAssemblingRecipe> VEHICLE_ASSEMBLING_SERIALIZER =
            registerSerializer("vehicle_assembling", new VehicleAssemblingRecipeSerializer());

    public static final RecipeSerializer<VehicleResetRecipe> VEHICLE_RESET_SERIALIZER =
            registerSerializer("vehicle_reset", new SimpleCraftingRecipeSerializer<>(VehicleResetRecipe::new));

    public static final RecipeType<VehicleAssemblingRecipe> VEHICLE_ASSEMBLING_TYPE =
            registerType("vehicle_assembling", new RecipeType<>() {
                @Override
                public String toString() {
                    return Mod.MODID + ":vehicle_assembling";
                }
            });

    private static <T extends RecipeSerializer<?>> T registerSerializer(String name, T serializer) {
        return Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, Mod.loc(name), serializer);
    }

    private static <T extends RecipeType<?>> T registerType(String name, T type) {
        return Registry.register(BuiltInRegistries.RECIPE_TYPE, Mod.loc(name), type);
    }

    public static void init() {

    }
}
