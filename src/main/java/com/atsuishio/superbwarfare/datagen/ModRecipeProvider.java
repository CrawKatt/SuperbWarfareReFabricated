package com.atsuishio.superbwarfare.datagen;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.datagen.builder.VehicleAssemblingRecipeBuilder;
import com.atsuishio.superbwarfare.init.ModEntities;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModPerks;
import com.atsuishio.superbwarfare.init.ModTags;
import com.atsuishio.superbwarfare.perk.Perk;
import com.atsuishio.superbwarfare.recipe.*;
import com.atsuishio.superbwarfare.recipe.vehicle.VehicleAssemblingRecipe;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

import static com.atsuishio.superbwarfare.init.ModTags.commonItemTag;

public class ModRecipeProvider extends RecipeProvider {

    public static final TagKey<Item> PLATES_COPPER = commonItemTag("plates/copper");
    public static final TagKey<Item> INGOTS_LEAD = commonItemTag("ingots/lead");
    public static final TagKey<Item> INGOTS_SILVER = commonItemTag("ingots/silver");
    public static final TagKey<Item> INGOTS_TUNGSTEN = commonItemTag("ingots/tungsten");

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    public void buildRecipes(@NotNull RecipeOutput writer) {
        buildToolRecipes(writer);
        buildArmorRecipes(writer);
        buildAmmoRecipes(writer);
        buildMaterialRecipes(writer);
        buildBlockRecipes(writer);
        buildVehicleRecipes(writer);
        buildGunRecipes(writer);
        buildBlueprintRecipes(writer);
        buildPerkRecipes(writer);
        buildMiscRecipes(writer);
        buildSpecialRecipes(writer);
    }

    private static void buildToolRecipes(@NotNull RecipeOutput writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ARTILLERY_INDICATOR)
                .pattern(" b ")
                .pattern("aca")
                .define('a', Items.SPYGLASS)
                .define('b', ModItems.MONITOR)
                .define('c', ModItems.FIRING_PARAMETERS)
                .unlockedBy(getHasName(Items.SPYGLASS), has(Items.SPYGLASS))
                .save(writer, Mod.loc(getItemName(ModItems.ARTILLERY_INDICATOR)));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ARTILLERY_INDICATOR)
                .requires(ModItems.ARTILLERY_INDICATOR)
                .unlockedBy(getHasName(ModItems.ARTILLERY_INDICATOR), has(ModItems.ARTILLERY_INDICATOR))
                .save(writer, Mod.loc(getItemName(ModItems.ARTILLERY_INDICATOR) + "_clear"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.STEEL_PIPE)
                .pattern(" a")
                .pattern("a ")
                .define('a', ModItems.STEEL_MATERIALS.barrel())
                .unlockedBy(getHasName(ModItems.STEEL_MATERIALS.barrel()), has(ModItems.STEEL_MATERIALS.barrel()))
                .save(writer, Mod.loc(getItemName(ModItems.STEEL_PIPE)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.MEDICAL_KIT, 4)
                .pattern("aba")
                .pattern("bcb")
                .pattern("aba")
                .define('a', Items.STRING)
                .define('b', ItemTags.WOOL_CARPETS)
                .define('c', Items.GOLDEN_APPLE)
                .unlockedBy(getHasName(Items.GOLDEN_APPLE), has(Items.GOLDEN_APPLE))
                .save(writer, Mod.loc(getItemName(ModItems.MEDICAL_KIT)));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ARMOR_PLATE, 4)
                .pattern("aba")
                .pattern("ccc")
                .pattern("ada")
                .define('a', Items.STRING)
                .define('b', ItemTags.TERRACOTTA)
                .define('c', ModTags.Items.INGOTS_STEEL)
                .define('d', ItemTags.WOOL)
                .unlockedBy(getHasName(Items.STRING), has(Items.STRING))
                .save(writer, Mod.loc(getItemName(ModItems.ARMOR_PLATE)));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.VEHICLE_DAMAGE_ANALYZER)
                .pattern("aba")
                .pattern("aca")
                .pattern("ada")
                .define('a', ModTags.Items.INGOTS_GOLD)
                .define('b', Items.OBSERVER)
                .define('c', Items.NOTE_BLOCK)
                .define('d', ModTags.Items.INGOTS_IRON)
                .unlockedBy(getHasName(Items.OBSERVER), has(Items.OBSERVER))
                .save(writer, Mod.loc(getItemName(ModItems.VEHICLE_DAMAGE_ANALYZER)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.HAMMER)
                .pattern("aba")
                .pattern(" c ")
                .pattern(" c ")
                .define('a', ModTags.Items.INGOTS_IRON)
                .define('b', ModTags.Items.STORAGE_BLOCKS_IRON)
                .define('c', Items.STICK)
                .unlockedBy(getHasName(Items.IRON_BLOCK), has(ModTags.Items.STORAGE_BLOCKS_IRON))
                .save(writer, Mod.loc(getItemName(ModItems.HAMMER)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.GOLDEN_HAMMER)
                .pattern("aba")
                .pattern(" c ")
                .pattern(" c ")
                .define('a', ModTags.Items.INGOTS_GOLD)
                .define('b', ModTags.Items.STORAGE_BLOCKS_GOLD)
                .define('c', Items.STICK)
                .unlockedBy(getHasName(Items.GOLD_BLOCK), has(ModTags.Items.STORAGE_BLOCKS_GOLD))
                .save(writer, Mod.loc(getItemName(ModItems.GOLDEN_HAMMER)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.STEEL_HAMMER)
                .pattern("aba")
                .pattern(" c ")
                .pattern(" c ")
                .define('a', ModTags.Items.INGOTS_STEEL)
                .define('b', ModTags.Items.STORAGE_BLOCK_STEEL)
                .define('c', Items.STICK)
                .unlockedBy(getHasName(ModItems.STEEL_BLOCK), has(ModTags.Items.STORAGE_BLOCK_STEEL))
                .save(writer, Mod.loc(getItemName(ModItems.STEEL_HAMMER)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.DIAMOND_HAMMER)
                .pattern("aba")
                .pattern(" c ")
                .pattern(" c ")
                .define('a', ModTags.Items.GEMS_DIAMOND)
                .define('b', ModTags.Items.STORAGE_BLOCKS_DIAMOND)
                .define('c', Items.STICK)
                .unlockedBy(getHasName(Items.DIAMOND_BLOCK), has(ModTags.Items.STORAGE_BLOCKS_DIAMOND))
                .save(writer, Mod.loc(getItemName(ModItems.DIAMOND_HAMMER)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.CEMENTED_CARBIDE_HAMMER)
                .pattern("aba")
                .pattern(" c ")
                .pattern(" c ")
                .define('a', ModTags.Items.INGOTS_CEMENTED_CARBIDE)
                .define('b', ModTags.Items.STORAGE_BLOCK_CEMENTED_CARBIDE)
                .define('c', Items.STICK)
                .unlockedBy(getHasName(ModItems.CEMENTED_CARBIDE_BLOCK), has(ModTags.Items.STORAGE_BLOCK_CEMENTED_CARBIDE))
                .save(writer, Mod.loc(getItemName(ModItems.CEMENTED_CARBIDE_HAMMER)));
        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                        Ingredient.of(ModItems.CEMENTED_CARBIDE_HAMMER),
                        Ingredient.of(ModTags.Items.STORAGE_BLOCKS_NETHERITE),
                        RecipeCategory.MISC,
                        ModItems.NETHERITE_HAMMER
                )
                .unlocks(getHasName(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), has(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE))
                .unlocks(getHasName(ModItems.CEMENTED_CARBIDE_HAMMER), has(ModItems.CEMENTED_CARBIDE_HAMMER))
                .save(writer, Mod.loc(getItemName(ModItems.NETHERITE_HAMMER)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.CROWBAR)
                .pattern("  a")
                .pattern(" b ")
                .pattern("b  ")
                .define('a', ModTags.Items.INGOTS_STEEL)
                .define('b', ModTags.Items.INGOTS_IRON)
                .unlockedBy(getHasName(ModItems.STEEL_INGOT), has(ModTags.Items.INGOTS_STEEL))
                .save(writer, Mod.loc(getItemName(ModItems.CROWBAR)));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.DEFUSER)
                .pattern("  a")
                .pattern("cb ")
                .pattern(" c ")
                .define('a', ModTags.Items.INGOTS_STEEL)
                .define('b', ModTags.Items.NUGGETS_IRON)
                .define('c', Items.STICK)
                .unlockedBy(getHasName(ModItems.STEEL_INGOT), has(ModTags.Items.INGOTS_STEEL))
                .save(writer, Mod.loc(getItemName(ModItems.DEFUSER)));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.DETONATOR)
                .pattern(" a")
                .pattern("bc")
                .define('a', Items.REDSTONE_TORCH)
                .define('b', Items.STONE_BUTTON)
                .define('c', ModTags.Items.INGOTS_IRON)
                .unlockedBy(getHasName(Items.REDSTONE_TORCH), has(Items.REDSTONE_TORCH))
                .save(writer, Mod.loc(getItemName(ModItems.DETONATOR)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.ELECTRIC_BATON)
                .pattern("  a")
                .pattern(" b ")
                .pattern("c  ")
                .define('a', Items.LIGHTNING_ROD)
                .define('b', ModItems.BATTERY)
                .define('c', ModTags.Items.INGOTS_STEEL)
                .unlockedBy(getHasName(Items.LIGHTNING_ROD), has(Items.LIGHTNING_ROD))
                .unlockedBy(getHasName(ModItems.BATTERY), has(ModItems.BATTERY))
                .save(writer, Mod.loc(getItemName(ModItems.ELECTRIC_BATON)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.KNIFE)
                .pattern(" a")
                .pattern("b ")
                .define('a', ModTags.Items.INGOTS_STEEL)
                .define('b', Items.STICK)
                .unlockedBy(getHasName(ModItems.STEEL_INGOT), has(ModTags.Items.INGOTS_STEEL))
                .save(writer, Mod.loc(getItemName(ModItems.KNIFE)));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MONITOR)
                .pattern("a a")
                .pattern("bcb")
                .pattern("ded")
                .define('a', Items.LIGHTNING_ROD)
                .define('b', Items.LEVER)
                .define('c', ModTags.Items.INGOTS_IRON)
                .define('d', Items.AMETHYST_SHARD)
                .define('e', ModTags.Items.GLASS_PANES)
                .unlockedBy(getHasName(Items.LIGHTNING_ROD), has(Items.LIGHTNING_ROD))
                .save(writer, Mod.loc(getItemName(ModItems.MONITOR)));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.MONITOR)
                .requires(ModItems.MONITOR)
                .unlockedBy(getHasName(ModItems.MONITOR), has(ModItems.MONITOR))
                .save(writer, Mod.loc(getItemName(ModItems.MONITOR) + "_clear"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MORTAR_DEPLOYER)
                .pattern("a ")
                .pattern("bc")
                .define('a', ModItems.MORTAR_BARREL)
                .define('b', ModItems.MORTAR_BIPOD)
                .define('c', ModItems.MORTAR_BASE_PLATE)
                .unlockedBy(getHasName(ModItems.MORTAR_BARREL), has(ModItems.MORTAR_BARREL))
                .unlockedBy(getHasName(ModItems.MORTAR_BIPOD), has(ModItems.MORTAR_BIPOD))
                .unlockedBy(getHasName(ModItems.MORTAR_BASE_PLATE), has(ModItems.MORTAR_BASE_PLATE))
                .save(writer, Mod.loc(getItemName(ModItems.MORTAR_DEPLOYER)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.T_BATON)
                .pattern("  a")
                .pattern(" a ")
                .pattern("ab ")
                .define('a', ModTags.Items.INGOTS_IRON)
                .define('b', ModTags.Items.INGOTS_STEEL)
                .unlockedBy(getHasName(ModItems.STEEL_INGOT), has(ModTags.Items.INGOTS_STEEL))
                .save(writer, Mod.loc(getItemName(ModItems.T_BATON)));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.DPS_GENERATOR_DEPLOYER)
                .pattern("a")
                .pattern("b")
                .pattern("c")
                .define('a', ModItems.TARGET_DEPLOYER)
                .define('b', ModItems.LARGE_MOTOR)
                .define('c', ModItems.CHARGING_STATION)
                .unlockedBy(getHasName(ModItems.CHARGING_STATION), has(ModItems.CHARGING_STATION))
                .save(writer, Mod.loc(getItemName(ModItems.DPS_GENERATOR_DEPLOYER)));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.TARGET_DEPLOYER)
                .pattern("a")
                .pattern("b")
                .pattern("c")
                .define('a', Items.TARGET)
                .define('b', ModTags.Items.INGOTS_STEEL)
                .define('c', Items.ARMOR_STAND)
                .unlockedBy(getHasName(Items.TARGET), has(Items.TARGET))
                .save(writer, Mod.loc(getItemName(ModItems.TARGET_DEPLOYER)));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.TOW_DEPLOYER)
                .pattern("c ")
                .pattern("ab")
                .pattern("d ")
                .define('a', Items.DISPENSER)
                .define('b', ModItems.MORTAR_BARREL)
                .define('c', ModItems.ARTILLERY_INDICATOR)
                .define('d', ModTags.Items.STORAGE_BLOCK_STEEL)
                .unlockedBy(getHasName(Items.DISPENSER), has(Items.DISPENSER))
                .save(writer, Mod.loc(getItemName(ModItems.TOW_DEPLOYER)));
    }

    private static void buildArmorRecipes(@NotNull RecipeOutput writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.GE_HELMET_M_35)
                .pattern("aaa")
                .pattern("aba")
                .define('a', ModTags.Items.INGOTS_STEEL)
                .define('b', ModTags.Items.DYES_BLACK)
                .unlockedBy(getHasName(ModItems.STEEL_INGOT), has(ModTags.Items.INGOTS_STEEL))
                .save(writer, Mod.loc(getItemName(ModItems.GE_HELMET_M_35)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.RU_HELMET_6B47)
                .pattern("aca")
                .pattern("aba")
                .define('a', ModTags.Items.INGOTS_CEMENTED_CARBIDE)
                .define('b', ModTags.Items.DYES_GREEN)
                .define('c', ModItems.CEMENTED_CARBIDE_INGOT)
                .unlockedBy(getHasName(ModItems.CEMENTED_CARBIDE_INGOT), has(ModItems.CEMENTED_CARBIDE_INGOT))
                .save(writer, Mod.loc(getItemName(ModItems.RU_HELMET_6B47)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.RU_CHEST_6B43)
                .pattern("aba")
                .pattern("aca")
                .pattern("aaa")
                .define('a', ModTags.Items.INGOTS_CEMENTED_CARBIDE)
                .define('b', ModTags.Items.DYES_GREEN)
                .define('c', ModItems.CEMENTED_CARBIDE_INGOT)
                .unlockedBy(getHasName(ModItems.CEMENTED_CARBIDE_INGOT), has(ModItems.CEMENTED_CARBIDE_INGOT))
                .save(writer, Mod.loc(getItemName(ModItems.RU_CHEST_6B43)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.US_HELMET_PASGT)
                .pattern("aca")
                .pattern("aba")
                .define('a', ModTags.Items.INGOTS_CEMENTED_CARBIDE)
                .define('b', ModTags.Items.SANDS)
                .define('c', ModItems.CEMENTED_CARBIDE_INGOT)
                .unlockedBy(getHasName(ModItems.CEMENTED_CARBIDE_INGOT), has(ModItems.CEMENTED_CARBIDE_INGOT))
                .save(writer, Mod.loc(getItemName(ModItems.US_HELMET_PASGT)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.US_CHEST_IOTV)
                .pattern("aba")
                .pattern("aca")
                .pattern("aaa")
                .define('a', ModTags.Items.INGOTS_CEMENTED_CARBIDE)
                .define('b', ModTags.Items.SANDS)
                .define('c', ModItems.CEMENTED_CARBIDE_INGOT)
                .unlockedBy(getHasName(ModItems.CEMENTED_CARBIDE_INGOT), has(ModItems.CEMENTED_CARBIDE_INGOT))
                .save(writer, Mod.loc(getItemName(ModItems.US_CHEST_IOTV)));
    }

    private static void buildAmmoRecipes(@NotNull RecipeOutput writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.AMMO_BOX)
                .pattern("aba")
                .pattern("aaa")
                .define('a', ModTags.Items.INGOTS_IRON)
                .define('b', ModTags.Items.DYES_GREEN)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(writer, Mod.loc(getItemName(ModItems.AMMO_BOX)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.LARGE_ANTI_GROUND_MISSILE)
                .pattern(" b ")
                .pattern("ada")
                .pattern("cec")
                .define('a', PLATES_COPPER)
                .define('b', ModItems.SEEKER)
                .define('c', ModItems.HIGH_ENERGY_EXPLOSIVES)
                .define('d', Items.TNT)
                .define('e', ModItems.MISSILE_ENGINE)
                .unlockedBy(getHasName(ModItems.MISSILE_ENGINE), has(ModItems.MISSILE_ENGINE))
                .save(writer, Mod.loc(getItemName(ModItems.LARGE_ANTI_GROUND_MISSILE)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SMALL_ROCKET, 4)
                .pattern(" a ")
                .pattern("bcb")
                .pattern(" d ")
                .define('a', ModItems.FUSEE)
                .define('b', Items.COPPER_INGOT)
                .define('c', ModItems.HIGH_ENERGY_EXPLOSIVES)
                .define('d', ModItems.GRAIN)
                .unlockedBy(getHasName(ModItems.FUSEE), has(ModItems.FUSEE))
                .save(writer, Mod.loc(getItemName(ModItems.SMALL_ROCKET)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.RPG_ROCKET_TBG, 2)
                .pattern(" a ")
                .pattern("bcb")
                .pattern(" d ")
                .define('a', ModItems.FUSEE)
                .define('b', Items.IRON_INGOT)
                .define('c', ModItems.HIGH_ENERGY_EXPLOSIVES)
                .define('d', ModItems.GRAIN)
                .unlockedBy(getHasName(ModItems.FUSEE), has(ModItems.FUSEE))
                .save(writer, Mod.loc(getItemName(ModItems.RPG_ROCKET_TBG)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.RPG_ROCKET_STANDARD, 2)
                .pattern(" a ")
                .pattern("bcb")
                .pattern("ede")
                .define('a', ModItems.FUSEE)
                .define('b', Items.IRON_INGOT)
                .define('c', PLATES_COPPER)
                .define('d', ModItems.GRAIN)
                .define('e', Items.GUNPOWDER)
                .unlockedBy(getHasName(ModItems.FUSEE), has(ModItems.FUSEE))
                .save(writer, Mod.loc(getItemName(ModItems.RPG_ROCKET_STANDARD)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.C4_BOMB, 2)
                .pattern("aaa")
                .pattern("aba")
                .pattern("aaa")
                .define('a', ModItems.HIGH_ENERGY_EXPLOSIVES)
                .define('b', Items.CLOCK)
                .unlockedBy(getHasName(ModItems.HIGH_ENERGY_EXPLOSIVES), has(ModItems.HIGH_ENERGY_EXPLOSIVES))
                .save(writer, Mod.loc(getItemName(ModItems.C4_BOMB)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.C4_BOMB, 2)
                .pattern("aaa")
                .pattern("aba")
                .pattern("aaa")
                .define('a', ModItems.HIGH_ENERGY_EXPLOSIVES)
                .define('b', Items.COMPARATOR)
                .unlockedBy(getHasName(ModItems.HIGH_ENERGY_EXPLOSIVES), has(ModItems.HIGH_ENERGY_EXPLOSIVES))
                .save(writer, Mod.loc(getItemName(ModItems.C4_BOMB) + "_rc"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.AP_5_INCHES)
                .pattern("c")
                .pattern("a")
                .pattern("b")
                .define('a', ModItems.AP_HEAD)
                .define('b', ModItems.GRAIN)
                .define('c', ModItems.FUSEE)
                .unlockedBy(getHasName(ModItems.AP_HEAD), has(ModItems.AP_HEAD))
                .save(writer, Mod.loc(getItemName(ModItems.AP_5_INCHES)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.BLU_43_MINE, 8)
                .pattern("a")
                .pattern("b")
                .pattern("c")
                .define('a', Items.STONE_PRESSURE_PLATE)
                .define('b', ModItems.HIGH_ENERGY_EXPLOSIVES)
                .define('c', Items.GREEN_CONCRETE)
                .unlockedBy(getHasName(ModItems.HIGH_ENERGY_EXPLOSIVES), has(ModItems.HIGH_ENERGY_EXPLOSIVES))
                .save(writer, Mod.loc(getItemName(ModItems.BLU_43_MINE)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.CLAYMORE_MINE, 2)
                .pattern(" a ")
                .pattern("bcb")
                .pattern("d d")
                .define('a', Items.TRIPWIRE_HOOK)
                .define('b', ModTags.Items.INGOTS_IRON)
                .define('c', ModItems.HIGH_ENERGY_EXPLOSIVES)
                .define('d', Items.STICK)
                .unlockedBy(getHasName(ModItems.HIGH_ENERGY_EXPLOSIVES), has(ModItems.HIGH_ENERGY_EXPLOSIVES))
                .save(writer, Mod.loc(getItemName(ModItems.CLAYMORE_MINE)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.CM_5_INCHES)
                .pattern("c")
                .pattern("a")
                .pattern("b")
                .define('a', ModItems.CM_HEAD)
                .define('b', ModItems.GRAIN)
                .define('c', ModItems.FUSEE)
                .unlockedBy(getHasName(ModItems.CM_HEAD), has(ModItems.CM_HEAD))
                .save(writer, Mod.loc(getItemName(ModItems.CM_5_INCHES)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.GRENADE_40MM, 6)
                .pattern(" a ")
                .pattern("bcb")
                .pattern(" d ")
                .define('a', ModItems.FUSEE)
                .define('b', ModTags.Items.INGOTS_IRON)
                .define('c', ModItems.HIGH_ENERGY_EXPLOSIVES)
                .define('d', ModItems.PRIMER)
                .unlockedBy(getHasName(ModItems.HIGH_ENERGY_EXPLOSIVES), has(ModItems.HIGH_ENERGY_EXPLOSIVES))
                .unlockedBy(getHasName(ModItems.FUSEE), has(ModItems.FUSEE))
                .save(writer, Mod.loc(getItemName(ModItems.GRENADE_40MM)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.GS_5_INCHES)
                .pattern("c")
                .pattern("a")
                .pattern("b")
                .define('a', ModItems.GS_HEAD)
                .define('b', ModItems.GRAIN)
                .define('c', ModItems.FUSEE)
                .unlockedBy(getHasName(ModItems.GS_HEAD), has(ModItems.GS_HEAD))
                .save(writer, Mod.loc(getItemName(ModItems.GS_5_INCHES)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.HAND_GRENADE, 4)
                .pattern(" a ")
                .pattern("bcb")
                .pattern("bcb")
                .define('a', Items.TRIPWIRE_HOOK)
                .define('b', ModTags.Items.INGOTS_IRON)
                .define('c', ModItems.HIGH_ENERGY_EXPLOSIVES)
                .unlockedBy(getHasName(ModItems.HIGH_ENERGY_EXPLOSIVES), has(ModItems.HIGH_ENERGY_EXPLOSIVES))
                .save(writer, Mod.loc(getItemName(ModItems.HAND_GRENADE)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.HANDGUN_AMMO, 64)
                .pattern(" a ")
                .pattern("bcb")
                .pattern(" d ")
                .define('a', ModTags.Items.INGOTS_COPPER)
                .define('b', PLATES_COPPER)
                .define('c', Items.GUNPOWDER)
                .define('d', ModItems.PRIMER)
                .unlockedBy(getHasName(ModItems.PRIMER), has(ModItems.PRIMER))
                .save(writer, Mod.loc(getItemName(ModItems.HANDGUN_AMMO)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.HE_5_INCHES)
                .pattern("c")
                .pattern("a")
                .pattern("b")
                .define('a', ModItems.HE_HEAD)
                .define('b', ModItems.GRAIN)
                .define('c', ModItems.FUSEE)
                .unlockedBy(getHasName(ModItems.HE_HEAD), has(ModItems.HE_HEAD))
                .save(writer, Mod.loc(getItemName(ModItems.HE_5_INCHES)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.HEAVY_AMMO, 12)
                .pattern(" a ")
                .pattern("bcb")
                .pattern(" d ")
                .define('a', ModTags.Items.INGOTS_STEEL)
                .define('b', ModTags.Items.INGOTS_COPPER)
                .define('c', Items.GUNPOWDER)
                .define('d', ModItems.PRIMER)
                .unlockedBy(getHasName(ModItems.PRIMER), has(ModItems.PRIMER))
                .save(writer, Mod.loc(getItemName(ModItems.HEAVY_AMMO)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.JAVELIN_MISSILE)
                .pattern(" a ")
                .pattern("bcb")
                .pattern(" d ")
                .define('a', ModItems.SEEKER)
                .define('b', ModTags.Items.INGOTS_IRON)
                .define('c', ModItems.AP_HEAD)
                .define('d', ModItems.MISSILE_ENGINE)
                .unlockedBy(getHasName(ModItems.AP_HEAD), has(ModItems.AP_HEAD))
                .unlockedBy(getHasName(ModItems.MISSILE_ENGINE), has(ModItems.MISSILE_ENGINE))
                .save(writer, Mod.loc(getItemName(ModItems.JAVELIN_MISSILE)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.MEDIUM_ANTI_AIR_MISSILE)
                .pattern("eae")
                .pattern("bcb")
                .pattern(" d ")
                .define('a', ModItems.SEEKER)
                .define('b', ModTags.Items.INGOTS_IRON)
                .define('c', ModItems.HIGH_ENERGY_EXPLOSIVES)
                .define('d', ModItems.MISSILE_ENGINE)
                .define('e', Items.IRON_BARS)
                .unlockedBy(getHasName(ModItems.HIGH_ENERGY_EXPLOSIVES), has(ModItems.HIGH_ENERGY_EXPLOSIVES))
                .unlockedBy(getHasName(ModItems.MISSILE_ENGINE), has(ModItems.MISSILE_ENGINE))
                .save(writer, Mod.loc(getItemName(ModItems.MEDIUM_ANTI_AIR_MISSILE)));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.MEDIUM_ANTI_GROUND_MISSILE)
                .requires(ModItems.JAVELIN_MISSILE)
                .unlockedBy(getHasName(ModItems.JAVELIN_MISSILE), has(ModItems.JAVELIN_MISSILE))
                .save(writer, Mod.loc(getItemName(ModItems.MEDIUM_ANTI_GROUND_MISSILE)));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.JAVELIN_MISSILE)
                .requires(ModItems.MEDIUM_ANTI_GROUND_MISSILE)
                .unlockedBy(getHasName(ModItems.MEDIUM_ANTI_GROUND_MISSILE), has(ModItems.MEDIUM_ANTI_GROUND_MISSILE))
                .save(writer, Mod.loc(getItemName(ModItems.JAVELIN_MISSILE) + "_convert"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.LUNGE_MINE, 2)
                .pattern(" ba")
                .pattern(" cb")
                .pattern("c  ")
                .define('a', Items.TNT)
                .define('b', ModItems.HIGH_ENERGY_EXPLOSIVES)
                .define('c', Items.STICK)
                .unlockedBy(getHasName(ModItems.HIGH_ENERGY_EXPLOSIVES), has(ModItems.HIGH_ENERGY_EXPLOSIVES))
                .save(writer, Mod.loc(getItemName(ModItems.LUNGE_MINE)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.M18_SMOKE_GRENADE, 2)
                .pattern(" a ")
                .pattern("bcb")
                .pattern("bdb")
                .define('a', Items.TRIPWIRE_HOOK)
                .define('b', ModTags.Items.NUGGETS_IRON)
                .define('c', Items.WHEAT)
                .define('d', Items.GUNPOWDER)
                .unlockedBy(getHasName(Items.TRIPWIRE_HOOK), has(Items.TRIPWIRE_HOOK))
                .save(writer, Mod.loc(getItemName(ModItems.M18_SMOKE_GRENADE)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.MEDIUM_AERIAL_BOMB)
                .pattern(" c ")
                .pattern("dad")
                .pattern(" b ")
                .define('a', Items.TNT)
                .define('b', ModTags.Items.INGOTS_IRON)
                .define('c', ModItems.FUSEE)
                .define('d', ModItems.HIGH_ENERGY_EXPLOSIVES)
                .unlockedBy(getHasName(ModItems.FUSEE), has(ModItems.FUSEE))
                .unlockedBy(getHasName(ModItems.HIGH_ENERGY_EXPLOSIVES), has(ModItems.HIGH_ENERGY_EXPLOSIVES))
                .save(writer, Mod.loc(getItemName(ModItems.MEDIUM_AERIAL_BOMB)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.MEDIUM_ROCKET_AP)
                .pattern("a")
                .pattern("b")
                .pattern("b")
                .define('a', ModItems.AP_HEAD)
                .define('b', ModItems.SMALL_ROCKET)
                .unlockedBy(getHasName(ModItems.AP_HEAD), has(ModItems.AP_HEAD))
                .save(writer, Mod.loc(getItemName(ModItems.MEDIUM_ROCKET_AP)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.MEDIUM_ROCKET_CM)
                .pattern("a")
                .pattern("b")
                .pattern("b")
                .define('a', ModItems.CM_HEAD)
                .define('b', ModItems.SMALL_ROCKET)
                .unlockedBy(getHasName(ModItems.CM_HEAD), has(ModItems.CM_HEAD))
                .save(writer, Mod.loc(getItemName(ModItems.MEDIUM_ROCKET_CM)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.MEDIUM_ROCKET_HE)
                .pattern("a")
                .pattern("b")
                .pattern("b")
                .define('a', ModItems.HE_HEAD)
                .define('b', ModItems.SMALL_ROCKET)
                .unlockedBy(getHasName(ModItems.HE_HEAD), has(ModItems.HE_HEAD))
                .save(writer, Mod.loc(getItemName(ModItems.MEDIUM_ROCKET_HE)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.MORTAR_SHELL, 4)
                .pattern(" a ")
                .pattern("bcb")
                .pattern(" d ")
                .define('a', ModItems.FUSEE)
                .define('b', ModTags.Items.INGOTS_STEEL)
                .define('c', ModItems.HIGH_ENERGY_EXPLOSIVES)
                .define('d', ModItems.GRAIN)
                .unlockedBy(getHasName(ModItems.HIGH_ENERGY_EXPLOSIVES), has(ModItems.HIGH_ENERGY_EXPLOSIVES))
                .unlockedBy(getHasName(ModItems.GRAIN), has(ModItems.GRAIN))
                .save(writer, Mod.loc(getItemName(ModItems.MORTAR_SHELL)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.PTKM_1R)
                .pattern(" b ")
                .pattern("dad")
                .pattern("ece")
                .define('a', Items.GUNPOWDER)
                .define('b', ModItems.AP_5_INCHES)
                .define('c', Items.CALIBRATED_SCULK_SENSOR)
                .define('d', ModTags.Items.INGOTS_IRON)
                .define('e', Items.IRON_BARS)
                .unlockedBy(getHasName(ModItems.AP_5_INCHES), has(ModItems.AP_5_INCHES))
                .unlockedBy(getHasName(Items.CALIBRATED_SCULK_SENSOR), has(Items.CALIBRATED_SCULK_SENSOR))
                .save(writer, Mod.loc(getItemName(ModItems.PTKM_1R)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.RGO_GRENADE, 4)
                .pattern("abc")
                .pattern("aba")
                .pattern(" da")
                .define('a', ModTags.Items.INGOTS_IRON)
                .define('b', ModItems.HIGH_ENERGY_EXPLOSIVES)
                .define('c', Items.TRIPWIRE_HOOK)
                .define('d', Items.STONE_BUTTON)
                .unlockedBy(getHasName(ModItems.HIGH_ENERGY_EXPLOSIVES), has(ModItems.HIGH_ENERGY_EXPLOSIVES))
                .save(writer, Mod.loc(getItemName(ModItems.RGO_GRENADE)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.RIFLE_AMMO, 48)
                .pattern(" a ")
                .pattern("bcb")
                .pattern(" d ")
                .define('a', ModTags.Items.INGOTS_STEEL)
                .define('b', PLATES_COPPER)
                .define('c', Items.GUNPOWDER)
                .define('d', ModItems.PRIMER)
                .unlockedBy(getHasName(ModItems.PRIMER), has(ModItems.PRIMER))
                .save(writer, Mod.loc(getItemName(ModItems.RIFLE_AMMO)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SHOTGUN_AMMO, 24)
                .pattern(" a ")
                .pattern("bcb")
                .pattern(" d ")
                .define('a', INGOTS_LEAD)
                .define('b', PLATES_COPPER)
                .define('c', Items.GUNPOWDER)
                .define('d', ModItems.PRIMER)
                .unlockedBy(getHasName(ModItems.PRIMER), has(ModItems.PRIMER))
                .save(writer, Mod.loc(getItemName(ModItems.SHOTGUN_AMMO)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SNIPER_AMMO, 16)
                .pattern(" a ")
                .pattern("bcb")
                .pattern(" d ")
                .define('a', INGOTS_TUNGSTEN)
                .define('b', PLATES_COPPER)
                .define('c', Items.GUNPOWDER)
                .define('d', ModItems.PRIMER)
                .unlockedBy(getHasName(ModItems.PRIMER), has(ModItems.PRIMER))
                .save(writer, Mod.loc(getItemName(ModItems.SNIPER_AMMO)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SMALL_SHELL, 4)
                .pattern("ea ")
                .pattern("bcb")
                .pattern(" d ")
                .define('a', ModTags.Items.INGOTS_STEEL)
                .define('b', ModTags.Items.INGOTS_COPPER)
                .define('c', Items.GUNPOWDER)
                .define('d', ModItems.PRIMER)
                .define('e', ModItems.HIGH_ENERGY_EXPLOSIVES)
                .unlockedBy(getHasName(ModItems.PRIMER), has(ModItems.PRIMER))
                .unlockedBy(getHasName(ModItems.HIGH_ENERGY_EXPLOSIVES), has(ModItems.HIGH_ENERGY_EXPLOSIVES))
                .save(writer, Mod.loc(getItemName(ModItems.SMALL_SHELL)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SWARM_DRONE, 4)
                .pattern(" a ")
                .pattern("bcb")
                .pattern("ded")
                .define('a', ModItems.SEEKER)
                .define('b', ModItems.PROPELLER)
                .define('c', ModItems.MOTOR)
                .define('d', ModItems.HIGH_ENERGY_EXPLOSIVES)
                .define('e', ModItems.CELL)
                .unlockedBy(getHasName(ModItems.PROPELLER), has(ModItems.PROPELLER))
                .unlockedBy(getHasName(ModItems.HIGH_ENERGY_EXPLOSIVES), has(ModItems.HIGH_ENERGY_EXPLOSIVES))
                .save(writer, Mod.loc(getItemName(ModItems.SWARM_DRONE)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.TASER_ELECTRODE, 4)
                .pattern("a a")
                .pattern("b b")
                .pattern("b b")
                .define('a', Items.LIGHTNING_ROD)
                .define('b', Items.STRING)
                .unlockedBy(getHasName(Items.LIGHTNING_ROD), has(Items.LIGHTNING_ROD))
                .save(writer, Mod.loc(getItemName(ModItems.TASER_ELECTRODE)));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.TM_62, 2)
                .pattern("cac")
                .pattern("bbb")
                .pattern("bbb")
                .define('a', Items.STONE_PRESSURE_PLATE)
                .define('b', ModItems.HIGH_ENERGY_EXPLOSIVES)
                .define('c', Items.GREEN_CONCRETE)
                .unlockedBy(getHasName(ModItems.HIGH_ENERGY_EXPLOSIVES), has(ModItems.HIGH_ENERGY_EXPLOSIVES))
                .save(writer, Mod.loc(getItemName(ModItems.TM_62)));
    }

    private static void buildMaterialRecipes(@NotNull RecipeOutput writer) {
        generateMaterialRecipes(writer, ModItems.IRON_MATERIALS, Items.IRON_INGOT);
        generateMaterialRecipes(writer, ModItems.STEEL_MATERIALS, ModTags.Items.INGOTS_STEEL, ModItems.STEEL_INGOT);
        generateMaterialRecipes(writer, ModItems.CEMENTED_CARBIDE_MATERIALS, ModTags.Items.INGOTS_CEMENTED_CARBIDE, ModItems.CEMENTED_CARBIDE_INGOT);
        generateSmithingMaterialRecipe(writer, ModItems.CEMENTED_CARBIDE_MATERIALS, ModItems.NETHERITE_MATERIALS, Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, Items.NETHERITE_INGOT);

        generateMaterialPackRecipe(writer, ModItems.IRON_MATERIALS, ModItems.COMMON_MATERIAL_PACK);
        generateMaterialPackRecipe(writer, ModItems.STEEL_MATERIALS, ModItems.RARE_MATERIAL_PACK);
        generateMaterialPackRecipe(writer, ModItems.CEMENTED_CARBIDE_MATERIALS, ModItems.EPIC_MATERIAL_PACK);
        generateMaterialPackRecipe(writer, ModItems.NETHERITE_MATERIALS, ModItems.LEGENDARY_MATERIAL_PACK);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ANCIENT_CPU)
                .pattern("bcb")
                .pattern("cac")
                .pattern("bcb")
                .define('a', ModItems.EMPTY_PERK)
                .define('b', ModTags.Items.GEMS_DIAMOND)
                .define('c', ModTags.Items.ORES_NETHERITE_SCRAP)
                .unlockedBy(getHasName(ModItems.EMPTY_PERK), has(ModItems.EMPTY_PERK))
                .save(writer, Mod.loc(getItemName(ModItems.ANCIENT_CPU)));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.AP_HEAD, 2)
                .pattern(" b ")
                .pattern("bdb")
                .pattern("cac")
                .define('a', ModItems.HIGH_ENERGY_EXPLOSIVES)
                .define('b', ModTags.Items.INGOTS_IRON)
                .define('c', ModTags.Items.INGOTS_STEEL)
                .define('d', ModItems.TUNGSTEN_ROD)
                .unlockedBy(getHasName(ModItems.HIGH_ENERGY_EXPLOSIVES), has(ModItems.HIGH_ENERGY_EXPLOSIVES))
                .save(writer, Mod.loc(getItemName(ModItems.AP_HEAD)));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BATTERY)
                .pattern(" b ")
                .pattern("cac")
                .pattern(" d ")
                .define('a', ModTags.Items.DUSTS_REDSTONE)
                .define('b', PLATES_COPPER)
                .define('c', ModTags.Items.GLASS_PANES)
                .define('d', ModTags.Items.INGOTS_IRON)
                .unlockedBy(getHasName(Items.REDSTONE), has(Items.REDSTONE))
                .save(writer, Mod.loc(getItemName(ModItems.BATTERY)));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BATTERY)
                .pattern("aa")
                .pattern("aa")
                .define('a', ModItems.CELL)
                .unlockedBy(getHasName(ModItems.CELL), has(ModItems.CELL))
                .save(writer, Mod.loc(getItemName(ModItems.BATTERY) + "_from_cell"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.CANNON_CORE)
                .pattern("aaa")
                .pattern("bcd")
                .pattern("aaa")
                .define('a', ModTags.Items.INGOTS_STEEL)
                .define('b', Items.DISPENSER)
                .define('c', ModItems.CEMENTED_CARBIDE_MATERIALS.action())
                .define('d', Items.PISTON)
                .unlockedBy(getHasName(ModItems.CEMENTED_CARBIDE_MATERIALS.action()), has(ModItems.CEMENTED_CARBIDE_MATERIALS.action()))
                .save(writer, Mod.loc(getItemName(ModItems.CANNON_CORE)));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.CELL)
                .pattern("a")
                .pattern("b")
                .pattern("c")
                .define('a', ModTags.Items.NUGGETS_GOLD)
                .define('b', ModTags.Items.DUSTS_REDSTONE)
                .define('c', ModTags.Items.NUGGETS_IRON)
                .unlockedBy(getHasName(Items.GOLD_NUGGET), has(Items.GOLD_NUGGET))
                .save(writer, Mod.loc(getItemName(ModItems.CELL)));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.LASER_UNIT)
                .pattern("eae")
                .pattern("dbd")
                .pattern("dcd")
                .define('a', Items.AMETHYST_SHARD)
                .define('b', Items.DIAMOND)
                .define('c', Items.REDSTONE)
                .define('d', ModTags.Items.INGOTS_STEEL)
                .define('e', Items.COPPER_INGOT)
                .unlockedBy(getHasName(Items.REDSTONE), has(Items.REDSTONE))
                .save(writer, Mod.loc(getItemName(ModItems.LASER_UNIT)));
        SimpleCookingRecipeBuilder.generic(Ingredient.of(ModItems.RAW_CEMENTED_CARBIDE_POWDER),
                        RecipeCategory.MISC,
                        ModItems.CEMENTED_CARBIDE_INGOT,
                        8,
                        200,
                        RecipeSerializer.BLASTING_RECIPE,
                        BlastingRecipe::new)
                .unlockedBy(getHasName(ModItems.RAW_CEMENTED_CARBIDE_POWDER), has(ModItems.RAW_CEMENTED_CARBIDE_POWDER))
                .save(writer, Mod.loc(getItemName(ModItems.CEMENTED_CARBIDE_INGOT) + "_blasting"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.CEMENTED_CARBIDE_INGOT, 9)
                .requires(ModItems.CEMENTED_CARBIDE_BLOCK)
                .unlockedBy(getHasName(ModItems.CEMENTED_CARBIDE_BLOCK), has(ModItems.CEMENTED_CARBIDE_BLOCK))
                .save(writer, Mod.loc(getItemName(ModItems.CEMENTED_CARBIDE_INGOT) + "_from_block"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.CM_HEAD, 2)
                .pattern("ddd")
                .pattern("bdb")
                .pattern("cac")
                .define('a', Items.GUNPOWDER)
                .define('b', ModTags.Items.INGOTS_IRON)
                .define('c', ModTags.Items.INGOTS_STEEL)
                .define('d', ModItems.GRENADE_40MM)
                .unlockedBy(getHasName(ModItems.GRENADE_40MM), has(ModItems.GRENADE_40MM))
                .save(writer, Mod.loc(getItemName(ModItems.CM_HEAD)));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.COAL_IRON_POWDER)
                .requires(commonItemTag("dusts/iron"))
                .requires(commonItemTag("dusts/coal_coke"))
                .unlockedBy(getHasName(ModItems.IRON_POWDER), has(ModItems.IRON_POWDER))
                .unlockedBy(getHasName(ModItems.COAL_POWDER), has(ModItems.COAL_POWDER))
                .save(writer, Mod.loc(getItemName(ModItems.COAL_IRON_POWDER)));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.COAL_POWDER)
                .requires(ItemTags.COALS)
                .requires(ModTags.Items.HAMMER)
                .unlockedBy(getHasName(ModItems.HAMMER), has(ModTags.Items.HAMMER))
                .save(writer, Mod.loc(getItemName(ModItems.COAL_POWDER)));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.IRON_POWDER)
                .requires(ModTags.Items.INGOTS_IRON)
                .requires(ModTags.Items.HAMMER)
                .unlockedBy(getHasName(ModItems.HAMMER), has(ModTags.Items.HAMMER))
                .save(writer, Mod.loc(getItemName(ModItems.IRON_POWDER)));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.COPPER_PLATE)
                .requires(ModTags.Items.INGOTS_COPPER)
                .requires(ModTags.Items.HAMMER)
                .unlockedBy(getHasName(ModItems.HAMMER), has(ModTags.Items.HAMMER))
                .save(writer, Mod.loc(getItemName(ModItems.COPPER_PLATE)));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.FUSEE, 4)
                .pattern("a")
                .pattern("b")
                .pattern("c")
                .define('a', Items.STONE_BUTTON)
                .define('b', ModTags.Items.DUSTS_REDSTONE)
                .define('c', ModTags.Items.NUGGETS_IRON)
                .unlockedBy(getHasName(Items.STONE_BUTTON), has(Items.STONE_BUTTON))
                .save(writer, Mod.loc(getItemName(ModItems.FUSEE)));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.GRAIN, 8)
                .pattern("aba")
                .pattern("aba")
                .pattern(" c ")
                .define('a', PLATES_COPPER)
                .define('b', Items.GUNPOWDER)
                .define('c', ModItems.PRIMER)
                .unlockedBy(getHasName(ModItems.PRIMER), has(ModItems.PRIMER))
                .save(writer, Mod.loc(getItemName(ModItems.GRAIN)));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.GS_HEAD, 2)
                .pattern("ddd")
                .pattern("bdb")
                .pattern("cac")
                .define('a', Items.GUNPOWDER)
                .define('b', ModTags.Items.INGOTS_IRON)
                .define('c', ModTags.Items.INGOTS_STEEL)
                .define('d', INGOTS_LEAD)
                .unlockedBy(getHasName(Items.GUNPOWDER), has(Items.GUNPOWDER))
                .save(writer, Mod.loc(getItemName(ModItems.GS_HEAD)));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.HE_HEAD, 2)
                .pattern(" b ")
                .pattern("bab")
                .pattern("cac")
                .define('a', ModItems.HIGH_ENERGY_EXPLOSIVES)
                .define('b', ModTags.Items.INGOTS_IRON)
                .define('c', ModTags.Items.INGOTS_STEEL)
                .unlockedBy(getHasName(ModItems.HIGH_ENERGY_EXPLOSIVES), has(ModItems.HIGH_ENERGY_EXPLOSIVES))
                .save(writer, Mod.loc(getItemName(ModItems.HE_HEAD)));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.HEAVY_ARMAMENT_MODULE)
                .pattern("ddd")
                .pattern("abc")
                .pattern("ddd")
                .define('a', ModItems.CANNON_CORE)
                .define('b', ModItems.LEGENDARY_MATERIAL_PACK)
                .define('c', ModItems.MEDIUM_ARMAMENT_MODULE)
                .define('d', ModTags.Items.INGOTS_NETHERITE)
                .unlockedBy(getHasName(ModItems.MEDIUM_ARMAMENT_MODULE), has(ModItems.MEDIUM_ARMAMENT_MODULE))
                .save(writer, Mod.loc(getItemName(ModItems.HEAVY_ARMAMENT_MODULE)));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.HIGH_ENERGY_EXPLOSIVES, 4)
                .pattern("aba")
                .pattern("cac")
                .pattern("aba")
                .define('a', Items.GUNPOWDER)
                .define('b', Items.SUGAR)
                .define('c', ModTags.Items.SANDS)
                .unlockedBy(getHasName(Items.GUNPOWDER), has(Items.GUNPOWDER))
                .save(writer, Mod.loc(getItemName(ModItems.HIGH_ENERGY_EXPLOSIVES)));
        SimpleCookingRecipeBuilder.generic(Ingredient.of(ModItems.IRON_POWDER),
                        RecipeCategory.MISC,
                        Items.IRON_INGOT,
                        0.7f,
                        100,
                        RecipeSerializer.BLASTING_RECIPE,
                        BlastingRecipe::new)
                .unlockedBy(getHasName(ModItems.IRON_POWDER), has(ModItems.IRON_POWDER))
                .save(writer, Mod.loc(getItemName(Items.IRON_INGOT) + "_blasting_from_powder"));
        SimpleCookingRecipeBuilder.generic(Ingredient.of(ModItems.IRON_POWDER),
                        RecipeCategory.MISC,
                        Items.IRON_INGOT,
                        0.7f,
                        200,
                        RecipeSerializer.SMELTING_RECIPE,
                        SmeltingRecipe::new)
                .unlockedBy(getHasName(ModItems.IRON_POWDER), has(ModItems.IRON_POWDER))
                .save(writer, Mod.loc(getItemName(Items.IRON_INGOT) + "_smelting_from_powder"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.LARGE_BATTERY_PACK)
                .pattern("aa")
                .pattern("aa")
                .define('a', ModItems.MEDIUM_BATTERY_PACK)
                .unlockedBy(getHasName(ModItems.MEDIUM_BATTERY_PACK), has(ModItems.MEDIUM_BATTERY_PACK))
                .save(writer, Mod.loc(getItemName(ModItems.LARGE_BATTERY_PACK)));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.LARGE_MOTOR)
                .pattern(" a ")
                .pattern("bcd")
                .pattern("bcd")
                .define('a', ModTags.Items.INGOTS_IRON)
                .define('b', ModTags.Items.STORAGE_BLOCKS_LAPIS)
                .define('c', ModTags.Items.STORAGE_BLOCKS_COPPER)
                .define('d', ModTags.Items.STORAGE_BLOCKS_REDSTONE)
                .unlockedBy(getHasName(Items.COPPER_BLOCK), has(ModTags.Items.STORAGE_BLOCKS_COPPER))
                .save(writer, Mod.loc(getItemName(ModItems.LARGE_MOTOR)));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.LARGE_PROPELLER)
                .pattern(" a ")
                .pattern("aba")
                .pattern(" a ")
                .define('a', ModTags.Items.INGOTS_IRON)
                .define('b', ModTags.Items.INGOTS_CEMENTED_CARBIDE)
                .unlockedBy(getHasName(ModItems.CEMENTED_CARBIDE_INGOT), has(ModItems.CEMENTED_CARBIDE_INGOT))
                .save(writer, Mod.loc(getItemName(ModItems.LARGE_PROPELLER)));
        SimpleCookingRecipeBuilder.generic(Ingredient.of(ModItems.GALENA),
                        RecipeCategory.MISC,
                        ModItems.LEAD_INGOT,
                        0.7f,
                        100,
                        RecipeSerializer.BLASTING_RECIPE,
                        BlastingRecipe::new)
                .unlockedBy(getHasName(ModItems.GALENA), has(ModItems.GALENA))
                .save(writer, Mod.loc(getItemName(ModItems.LEAD_INGOT) + "_blasting"));
        SimpleCookingRecipeBuilder.generic(Ingredient.of(ModItems.GALENA_ORE, ModItems.DEEPSLATE_GALENA_ORE),
                        RecipeCategory.MISC,
                        ModItems.LEAD_INGOT,
                        0.7f,
                        100,
                        RecipeSerializer.BLASTING_RECIPE,
                        BlastingRecipe::new)
                .unlockedBy(getHasName(ModItems.GALENA_ORE), has(commonItemTag("ores/lead")))
                .save(writer, Mod.loc(getItemName(Items.IRON_INGOT) + "_blasting_from_ore"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.LEAD_INGOT, 9)
                .requires(ModItems.LEAD_BLOCK)
                .unlockedBy(getHasName(ModItems.LEAD_BLOCK), has(ModItems.LEAD_BLOCK))
                .save(writer, Mod.loc(getItemName(ModItems.LEAD_INGOT) + "_from_block"));
        SimpleCookingRecipeBuilder.generic(Ingredient.of(ModItems.GALENA),
                        RecipeCategory.MISC,
                        ModItems.LEAD_INGOT,
                        0.7f,
                        200,
                        RecipeSerializer.SMELTING_RECIPE,
                        SmeltingRecipe::new)
                .unlockedBy(getHasName(ModItems.GALENA), has(ModItems.GALENA))
                .save(writer, Mod.loc(getItemName(ModItems.LEAD_INGOT) + "_smelting"));
        SimpleCookingRecipeBuilder.generic(Ingredient.of(ModItems.GALENA_ORE, ModItems.DEEPSLATE_GALENA_ORE),
                        RecipeCategory.MISC,
                        ModItems.LEAD_INGOT,
                        0.7f,
                        200,
                        RecipeSerializer.SMELTING_RECIPE,
                        SmeltingRecipe::new)
                .unlockedBy(getHasName(ModItems.GALENA_ORE), has(commonItemTag("ores/lead")))
                .save(writer, Mod.loc(getItemName(ModItems.LEAD_INGOT) + "_smelting_from_ore"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.LIGHT_ARMAMENT_MODULE)
                .pattern("ddd")
                .pattern("abc")
                .pattern("ddd")
                .define('a', ModItems.STEEL_MATERIALS.barrel())
                .define('b', ModItems.RARE_MATERIAL_PACK)
                .define('c', Items.DISPENSER)
                .define('d', ModTags.Items.INGOTS_STEEL)
                .unlockedBy(getHasName(ModItems.RARE_MATERIAL_PACK), has(ModItems.RARE_MATERIAL_PACK))
                .save(writer, Mod.loc(getItemName(ModItems.LIGHT_ARMAMENT_MODULE)));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MEDIUM_ARMAMENT_MODULE)
                .pattern("ddd")
                .pattern("abc")
                .pattern("ddd")
                .define('a', ModItems.CEMENTED_CARBIDE_MATERIALS.barrel())
                .define('b', ModItems.EPIC_MATERIAL_PACK)
                .define('c', ModItems.LIGHT_ARMAMENT_MODULE)
                .define('d', ModTags.Items.INGOTS_CEMENTED_CARBIDE)
                .unlockedBy(getHasName(ModItems.EPIC_MATERIAL_PACK), has(ModItems.EPIC_MATERIAL_PACK))
                .save(writer, Mod.loc(getItemName(ModItems.MEDIUM_ARMAMENT_MODULE)));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MEDIUM_BATTERY_PACK)
                .pattern("aaa")
                .pattern("aaa")
                .pattern("aaa")
                .define('a', ModItems.SMALL_BATTERY_PACK)
                .unlockedBy(getHasName(ModItems.SMALL_BATTERY_PACK), has(ModItems.SMALL_BATTERY_PACK))
                .save(writer, Mod.loc(getItemName(ModItems.MEDIUM_BATTERY_PACK)));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MISSILE_ENGINE, 4)
                .pattern("aba")
                .pattern("cbc")
                .pattern(" d ")
                .define('a', ModTags.Items.INGOTS_COPPER)
                .define('b', ModItems.GRAIN)
                .define('c', ModTags.Items.INGOTS_IRON)
                .define('d', Items.FIREWORK_ROCKET)
                .unlockedBy(getHasName(ModItems.GRAIN), has(ModItems.GRAIN))
                .save(writer, Mod.loc(getItemName(ModItems.MISSILE_ENGINE)));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MORTAR_BARREL)
                .pattern("a a")
                .pattern("a a")
                .pattern("aba")
                .define('a', ModTags.Items.INGOTS_IRON)
                .define('b', ModTags.Items.DYES_GREEN)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(ModTags.Items.INGOTS_IRON))
                .save(writer, Mod.loc(getItemName(ModItems.MORTAR_BARREL)));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MORTAR_BASE_PLATE)
                .pattern(" b ")
                .pattern("aaa")
                .define('a', ModTags.Items.INGOTS_IRON)
                .define('b', ModTags.Items.DYES_GREEN)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(ModTags.Items.INGOTS_IRON))
                .save(writer, Mod.loc(getItemName(ModItems.MORTAR_BASE_PLATE)));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MORTAR_BIPOD)
                .pattern(" a ")
                .pattern("bbb")
                .pattern("cdc")
                .define('a', ModTags.Items.INGOTS_IRON)
                .define('b', ModTags.Items.NUGGETS_IRON)
                .define('c', Items.IRON_BARS)
                .define('d', ModTags.Items.DYES_GREEN)
                .unlockedBy(getHasName(Items.IRON_BARS), has(Items.IRON_BARS))
                .save(writer, Mod.loc(getItemName(ModItems.MORTAR_BIPOD)));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MOTOR, 2)
                .pattern(" a ")
                .pattern("bcd")
                .pattern("bcd")
                .define('a', ModTags.Items.NUGGETS_IRON)
                .define('b', ModTags.Items.GEMS_LAPIS)
                .define('c', ModTags.Items.INGOTS_COPPER)
                .define('d', ModTags.Items.DUSTS_REDSTONE)
                .unlockedBy(getHasName(Items.COPPER_INGOT), has(ModTags.Items.INGOTS_COPPER))
                .save(writer, Mod.loc(getItemName(ModItems.MOTOR)));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PRIMER, 4)
                .pattern("a")
                .pattern("b")
                .define('a', Items.FLINT)
                .define('b', PLATES_COPPER)
                .unlockedBy(getHasName(Items.FLINT), has(Items.FLINT))
                .save(writer, Mod.loc(getItemName(ModItems.PRIMER)));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PROPELLER, 2)
                .pattern(" a ")
                .pattern("aba")
                .pattern(" a ")
                .define('a', ItemTags.PLANKS)
                .define('b', ModTags.Items.NUGGETS_IRON)
                .unlockedBy(getHasName(Items.OAK_PLANKS), has(ItemTags.PLANKS))
                .unlockedBy(getHasName(Items.IRON_NUGGET), has(ModTags.Items.NUGGETS_IRON))
                .save(writer, Mod.loc(getItemName(ModItems.PROPELLER)));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.RAW_CEMENTED_CARBIDE_POWDER, 8)
                .requires(Ingredient.of(commonItemTag("dusts/tungsten")), 7)
                .requires(commonItemTag("dusts/iron"))
                .requires(commonItemTag("dusts/coal_coke"))
                .unlockedBy(getHasName(ModItems.TUNGSTEN_POWDER), has(commonItemTag("dusts/tungsten")))
                .save(writer, Mod.loc(getItemName(ModItems.RAW_CEMENTED_CARBIDE_POWDER)));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SEEKER, 4)
                .pattern(" a ")
                .pattern("bcb")
                .pattern("ded")
                .define('a', Items.AMETHYST_SHARD)
                .define('b', ModTags.Items.INGOTS_IRON)
                .define('c', Items.COMPASS)
                .define('d', ModTags.Items.GEMS_QUARTZ)
                .define('e', Items.COMPARATOR)
                .unlockedBy(getHasName(Items.COMPASS), has(Items.COMPASS))
                .save(writer, Mod.loc(getItemName(ModItems.SEEKER)));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SHORTCUT_PACK)
                .requires(ModItems.EPIC_MATERIAL_PACK)
                .requires(Items.NETHER_STAR)
                .unlockedBy(getHasName(ModItems.EPIC_MATERIAL_PACK), has(ModItems.EPIC_MATERIAL_PACK))
                .unlockedBy(getHasName(Items.NETHER_STAR), has(Items.NETHER_STAR))
                .save(writer, Mod.loc(getItemName(ModItems.SHORTCUT_PACK)));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.REPAIR_TOOL)
                .pattern(" aa")
                .pattern("bcd")
                .pattern("efg")
                .define('a', Items.IRON_INGOT)
                .define('b', ModItems.STEEL_MATERIALS.barrel())
                .define('c', Items.FLINT_AND_STEEL)
                .define('d', ModItems.MOTOR)
                .define('e', Items.LAVA_BUCKET)
                .define('f', ModItems.BATTERY)
                .define('g', ModItems.STEEL_MATERIALS.trigger())
                .unlockedBy(getHasName(Items.COMPASS), has(Items.COMPASS))
                .save(writer, Mod.loc(getItemName(ModItems.REPAIR_TOOL)));
        SimpleCookingRecipeBuilder.generic(Ingredient.of(ModItems.RAW_SILVER),
                        RecipeCategory.MISC,
                        ModItems.SILVER_INGOT,
                        0.7f,
                        100,
                        RecipeSerializer.BLASTING_RECIPE,
                        BlastingRecipe::new)
                .unlockedBy(getHasName(ModItems.RAW_SILVER), has(ModItems.RAW_SILVER))
                .save(writer, Mod.loc(getItemName(ModItems.SILVER_INGOT) + "_blasting"));
        SimpleCookingRecipeBuilder.generic(Ingredient.of(ModItems.SILVER_ORE, ModItems.DEEPSLATE_SILVER_ORE),
                        RecipeCategory.MISC,
                        ModItems.SILVER_INGOT,
                        0.7f,
                        100,
                        RecipeSerializer.BLASTING_RECIPE,
                        BlastingRecipe::new)
                .unlockedBy(getHasName(ModItems.SILVER_ORE), has(commonItemTag("ores/silver")))
                .save(writer, Mod.loc(getItemName(ModItems.SILVER_INGOT) + "_blasting_from_ore"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SILVER_INGOT, 9)
                .requires(ModItems.SILVER_BLOCK)
                .unlockedBy(getHasName(ModItems.SILVER_BLOCK), has(ModItems.SILVER_BLOCK))
                .save(writer, Mod.loc(getItemName(ModItems.SILVER_INGOT) + "_from_block"));
        SimpleCookingRecipeBuilder.generic(Ingredient.of(ModItems.RAW_SILVER),
                        RecipeCategory.MISC,
                        ModItems.SILVER_INGOT,
                        0.7f,
                        200,
                        RecipeSerializer.SMELTING_RECIPE,
                        SmeltingRecipe::new)
                .unlockedBy(getHasName(ModItems.RAW_SILVER), has(ModItems.RAW_SILVER))
                .save(writer, Mod.loc(getItemName(ModItems.SILVER_INGOT) + "_smelting"));
        SimpleCookingRecipeBuilder.generic(Ingredient.of(ModItems.SILVER_ORE, ModItems.DEEPSLATE_SILVER_ORE),
                        RecipeCategory.MISC,
                        ModItems.SILVER_INGOT,
                        0.7f,
                        200,
                        RecipeSerializer.SMELTING_RECIPE,
                        SmeltingRecipe::new)
                .unlockedBy(getHasName(ModItems.GALENA_ORE), has(commonItemTag("ores/silver")))
                .save(writer, Mod.loc(getItemName(ModItems.SILVER_INGOT) + "_smelting_from_ore"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SMALL_BATTERY_PACK)
                .pattern("aa")
                .pattern("aa")
                .define('a', ModItems.BATTERY)
                .unlockedBy(getHasName(ModItems.BATTERY), has(ModItems.BATTERY))
                .save(writer, Mod.loc(getItemName(ModItems.SMALL_BATTERY_PACK) + "_from_battery"));
        SimpleCookingRecipeBuilder.generic(Ingredient.of(ModItems.COAL_IRON_POWDER),
                        RecipeCategory.MISC,
                        ModItems.STEEL_INGOT,
                        0.7f,
                        100,
                        RecipeSerializer.BLASTING_RECIPE,
                        BlastingRecipe::new)
                .unlockedBy(getHasName(ModItems.COAL_IRON_POWDER), has(ModItems.COAL_IRON_POWDER))
                .save(writer, Mod.loc(getItemName(ModItems.STEEL_INGOT) + "_blasting"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.STEEL_INGOT, 9)
                .requires(ModItems.STEEL_BLOCK)
                .unlockedBy(getHasName(ModItems.STEEL_BLOCK), has(ModItems.STEEL_BLOCK))
                .save(writer, Mod.loc(getItemName(ModItems.STEEL_INGOT) + "_from_block"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.TRACK)
                .pattern("aaa")
                .pattern("b b")
                .pattern("aaa")
                .define('a', ModTags.Items.INGOTS_STEEL)
                .define('b', ModItems.WHEEL)
                .unlockedBy(getHasName(ModItems.WHEEL), has(ModItems.WHEEL))
                .save(writer, Mod.loc(getItemName(ModItems.TRACK)));
        SimpleCookingRecipeBuilder.generic(Ingredient.of(ModItems.SCHEELITE),
                        RecipeCategory.MISC,
                        ModItems.TUNGSTEN_INGOT,
                        4,
                        100,
                        RecipeSerializer.BLASTING_RECIPE,
                        BlastingRecipe::new)
                .unlockedBy(getHasName(ModItems.SCHEELITE), has(ModItems.SCHEELITE))
                .save(writer, Mod.loc(getItemName(ModItems.TUNGSTEN_INGOT) + "_blasting"));
        SimpleCookingRecipeBuilder.generic(Ingredient.of(ModItems.SCHEELITE_ORE, ModItems.DEEPSLATE_SCHEELITE_ORE),
                        RecipeCategory.MISC,
                        ModItems.TUNGSTEN_INGOT,
                        4,
                        100,
                        RecipeSerializer.BLASTING_RECIPE,
                        BlastingRecipe::new)
                .unlockedBy(getHasName(ModItems.SCHEELITE_ORE), has(commonItemTag("ores/tungsten")))
                .save(writer, Mod.loc(getItemName(ModItems.TUNGSTEN_INGOT) + "_blasting_from_ore"));
        SimpleCookingRecipeBuilder.generic(Ingredient.of(ModItems.TUNGSTEN_POWDER),
                        RecipeCategory.MISC,
                        ModItems.TUNGSTEN_INGOT,
                        4,
                        100,
                        RecipeSerializer.BLASTING_RECIPE,
                        BlastingRecipe::new)
                .unlockedBy(getHasName(ModItems.TUNGSTEN_POWDER), has(ModItems.TUNGSTEN_POWDER))
                .save(writer, Mod.loc(getItemName(ModItems.TUNGSTEN_INGOT) + "_blasting_from_powder"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.TUNGSTEN_INGOT, 9)
                .requires(ModItems.TUNGSTEN_BLOCK)
                .unlockedBy(getHasName(ModItems.TUNGSTEN_BLOCK), has(ModItems.TUNGSTEN_BLOCK))
                .save(writer, Mod.loc(getItemName(ModItems.TUNGSTEN_INGOT) + "_from_block"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.TUNGSTEN_POWDER)
                .requires(INGOTS_TUNGSTEN)
                .requires(ModTags.Items.HAMMER)
                .unlockedBy(getHasName(ModItems.HAMMER), has(ModTags.Items.HAMMER))
                .save(writer, Mod.loc(getItemName(ModItems.TUNGSTEN_POWDER)));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.TUNGSTEN_ROD)
                .pattern("a")
                .pattern("a")
                .define('a', INGOTS_TUNGSTEN)
                .unlockedBy(getHasName(ModItems.TUNGSTEN_INGOT), has(INGOTS_TUNGSTEN))
                .save(writer, Mod.loc(getItemName(ModItems.TUNGSTEN_ROD)));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.WHEEL, 2)
                .pattern(" a ")
                .pattern("aba")
                .pattern(" a ")
                .define('a', Items.BLACK_WOOL)
                .define('b', ModTags.Items.INGOTS_IRON)
                .unlockedBy(getHasName(Items.BLACK_WOOL), has(Items.BLACK_WOOL))
                .save(writer, Mod.loc(getItemName(ModItems.WHEEL)));
    }

    private static void buildBlockRecipes(@NotNull RecipeOutput writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModItems.AIRCRAFT_CATAPULT, 8)
                .pattern("aaa")
                .pattern("cbc")
                .pattern("ddd")
                .define('a', Items.POWERED_RAIL)
                .define('b', ModTags.Items.STORAGE_BLOCKS_REDSTONE)
                .define('c', ModTags.Items.INGOTS_COPPER)
                .define('d', ModTags.Items.INGOTS_IRON)
                .unlockedBy(getHasName(Items.POWERED_RAIL), has(Items.POWERED_RAIL))
                .save(writer, Mod.loc(getItemName(ModItems.AIRCRAFT_CATAPULT)));
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModItems.SUPERB_ITEM_INTERFACE)
                .pattern("cac")
                .pattern("aba")
                .pattern("cac")
                .define('a', Items.HOPPER)
                .define('b', Items.DROPPER)
                .define('c', ModTags.Items.INGOTS_STEEL)
                .unlockedBy(getHasName(Items.HOPPER), has(Items.DROPPER))
                .save(writer, Mod.loc(getItemName(ModItems.SUPERB_ITEM_INTERFACE)));
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModItems.VEHICLE_ASSEMBLING_TABLE)
                .pattern("aaa")
                .pattern("bcd")
                .pattern("eee")
                .define('a', Items.IRON_INGOT)
                .define('b', ModTags.Items.STORAGE_BLOCKS_IRON)
                .define('c', Items.SMITHING_TABLE)
                .define('d', ModTags.Items.GLASS_PANES)
                .define('e', ModTags.Items.INGOTS_STEEL)
                .unlockedBy(getHasName(Items.SMITHING_TABLE), has(Items.SMITHING_TABLE))
                .save(writer, Mod.loc(getItemName(ModItems.VEHICLE_ASSEMBLING_TABLE)));
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.BARBED_WIRE, 2)
                .pattern("aba")
                .define('a', ItemTags.PLANKS)
                .define('b', Items.IRON_BARS)
                .unlockedBy(getHasName(Items.IRON_BARS), has(Items.IRON_BARS))
                .save(writer, Mod.loc(getItemName(ModItems.BARBED_WIRE)));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.CEMENTED_CARBIDE_BLOCK)
                .pattern("aaa")
                .pattern("aaa")
                .pattern("aaa")
                .define('a', ModItems.CEMENTED_CARBIDE_INGOT)
                .unlockedBy(getHasName(ModItems.CEMENTED_CARBIDE_INGOT), has(ModItems.CEMENTED_CARBIDE_INGOT))
                .save(writer, Mod.loc(getItemName(ModItems.CEMENTED_CARBIDE_BLOCK)));
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.CHARGING_STATION)
                .pattern("ada")
                .pattern("dcd")
                .pattern("aba")
                .define('a', PLATES_COPPER)
                .define('b', ModTags.Items.INGOTS_IRON)
                .define('c', Items.BLAST_FURNACE)
                .define('d', ModItems.CELL)
                .unlockedBy(getHasName(ModItems.CELL), has(ModItems.CELL))
                .save(writer, Mod.loc(getItemName(ModItems.CHARGING_STATION)));
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.DRAGON_TEETH, 4)
                .pattern(" a ")
                .pattern("bbb")
                .pattern("bbb")
                .define('a', ModTags.Items.NUGGETS_IRON)
                .define('b', Items.SMOOTH_STONE)
                .unlockedBy(getHasName(Items.SMOOTH_STONE), has(Items.SMOOTH_STONE))
                .save(writer, Mod.loc(getItemName(ModItems.DRAGON_TEETH)));
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.JUMP_PAD)
                .pattern(" a ")
                .pattern("bcb")
                .pattern("bcb")
                .define('a', Items.STONE_PRESSURE_PLATE)
                .define('b', Items.LIME_CONCRETE)
                .define('c', Items.PISTON)
                .unlockedBy(getHasName(Items.PISTON), has(Items.PISTON))
                .save(writer, Mod.loc(getItemName(ModItems.JUMP_PAD)));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.LEAD_BLOCK)
                .pattern("aaa")
                .pattern("aba")
                .pattern("aaa")
                .define('a', INGOTS_LEAD)
                .define('b', ModItems.LEAD_INGOT)
                .unlockedBy(getHasName(ModItems.LEAD_INGOT), has(ModItems.LEAD_INGOT))
                .save(writer, Mod.loc(getItemName(ModItems.LEAD_BLOCK)));
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.REFORGING_TABLE)
                .pattern("abc")
                .pattern("ded")
                .pattern("ddd")
                .define('a', ModTags.Items.INGOTS_GOLD)
                .define('b', ModTags.Items.GEMS_DIAMOND)
                .define('c', ModTags.Items.DUSTS_REDSTONE)
                .define('d', Items.POLISHED_BASALT)
                .define('e', ModItems.ANCIENT_CPU)
                .unlockedBy(getHasName(ModItems.ANCIENT_CPU), has(ModItems.ANCIENT_CPU))
                .save(writer, Mod.loc(getItemName(ModItems.REFORGING_TABLE)));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.SANDBAG)
                .pattern("aba")
                .define('a', Items.PAPER)
                .define('b', ModTags.Items.SANDS)
                .unlockedBy(getHasName(Items.PAPER), has(Items.PAPER))
                .save(writer, Mod.loc(getItemName(ModItems.SANDBAG)));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.SILVER_BLOCK)
                .pattern("aaa")
                .pattern("aba")
                .pattern("aaa")
                .define('a', INGOTS_SILVER)
                .define('b', ModItems.SILVER_INGOT)
                .unlockedBy(getHasName(ModItems.SILVER_INGOT), has(ModItems.SILVER_INGOT))
                .save(writer, Mod.loc(getItemName(ModItems.SILVER_BLOCK)));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.STEEL_BLOCK)
                .pattern("aaa")
                .pattern("aaa")
                .pattern("aaa")
                .define('a', ModTags.Items.INGOTS_STEEL)
                .unlockedBy(getHasName(ModItems.STEEL_INGOT), has(ModTags.Items.INGOTS_STEEL))
                .save(writer, Mod.loc(getItemName(ModItems.STEEL_BLOCK)));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.TUNGSTEN_BLOCK)
                .pattern("aaa")
                .pattern("aba")
                .pattern("aaa")
                .define('a', INGOTS_TUNGSTEN)
                .define('b', ModItems.TUNGSTEN_INGOT)
                .unlockedBy(getHasName(ModItems.TUNGSTEN_INGOT), has(ModItems.TUNGSTEN_INGOT))
                .save(writer, Mod.loc(getItemName(ModItems.TUNGSTEN_BLOCK)));
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.FUMO_25)
                .pattern("ada")
                .pattern(" c ")
                .pattern("beb")
                .define('a', Items.IRON_BARS)
                .define('b', ModTags.Items.INGOTS_IRON)
                .define('c', ModItems.MOTOR)
                .define('d', Items.OBSERVER)
                .define('e', ModItems.CELL)
                .unlockedBy(getHasName(ModItems.MOTOR), has(ModItems.MOTOR))
                .save(writer, Mod.loc(getItemName(ModItems.FUMO_25)));
    }

    private static void buildVehicleRecipes(@NotNull RecipeOutput writer) {
        VehicleAssemblingRecipeBuilder.entity(ModEntities.TOM_6, VehicleAssemblingRecipe.Category.AIRCRAFT)
                .require(ItemTags.PLANKS, 5)
                .require(ModItems.BATTERY)
                .require(Items.MINECART)
                .unlockedBy(getHasName(Items.MINECART), has(Items.MINECART))
                .save(writer, Mod.loc(getEntityTypeName(ModEntities.TOM_6)));
        VehicleAssemblingRecipeBuilder.entity(ModEntities.ANNIHILATOR, VehicleAssemblingRecipe.Category.DEFENSE)
                .require(ModTags.Items.STORAGE_BLOCK_STEEL, 24)
                .require(Items.NETHERITE_BLOCK, 3)
                .require(ModItems.LASER_UNIT, 32)
                .require(ModItems.LARGE_BATTERY_PACK)
                .require(ModItems.ANNIHILATOR_BLUEPRINT)
                .unlockedBy(getHasName(ModItems.ANNIHILATOR_BLUEPRINT), has(ModItems.ANNIHILATOR_BLUEPRINT))
                .save(writer, Mod.loc(getEntityTypeName(ModEntities.ANNIHILATOR)));
        VehicleAssemblingRecipeBuilder.entity(ModEntities.BL_132, VehicleAssemblingRecipe.Category.DEFENSE)
                .require(ModTags.Items.STORAGE_BLOCK_STEEL, 10)
                .require(ModItems.BL_132_BLUEPRINT)
                .require(ModItems.CANNON_CORE, 4)
                .unlockedBy(getHasName(ModItems.BL_132_BLUEPRINT), has(ModItems.BL_132_BLUEPRINT))
                .save(writer, Mod.loc(getEntityTypeName(ModEntities.BL_132)));
        VehicleAssemblingRecipeBuilder.entity(ModEntities.MLE_1934, VehicleAssemblingRecipe.Category.DEFENSE)
                .require(ModTags.Items.STORAGE_BLOCK_STEEL, 8)
                .require(ModItems.MLE_1934_BLUEPRINT)
                .require(ModItems.CANNON_CORE, 2)
                .unlockedBy(getHasName(ModItems.MLE_1934_BLUEPRINT), has(ModItems.MLE_1934_BLUEPRINT))
                .save(writer, Mod.loc(getEntityTypeName(ModEntities.MLE_1934)));
        VehicleAssemblingRecipeBuilder.entity(ModEntities.MK_42, VehicleAssemblingRecipe.Category.DEFENSE)
                .require(ModTags.Items.STORAGE_BLOCK_STEEL, 6)
                .require(ModItems.MK_42_BLUEPRINT)
                .require(ModItems.CANNON_CORE)
                .unlockedBy(getHasName(ModItems.MK_42_BLUEPRINT), has(ModItems.MK_42_BLUEPRINT))
                .save(writer, Mod.loc(getEntityTypeName(ModEntities.MK_42)));
        VehicleAssemblingRecipeBuilder.entity(ModEntities.TYPE_63, VehicleAssemblingRecipe.Category.DEFENSE)
                .require(ModTags.Items.STORAGE_BLOCK_STEEL, 1)
                .require(ModItems.MORTAR_BARREL, 12)
                .require(ModItems.WHEEL, 2)
                .unlockedBy(getHasName(ModItems.MORTAR_BARREL), has(ModItems.MORTAR_BARREL))
                .save(writer, Mod.loc(getEntityTypeName(ModEntities.TYPE_63)));
        VehicleAssemblingRecipeBuilder.entity(ModEntities.HPJ_11, VehicleAssemblingRecipe.Category.DEFENSE)
                .require(ModTags.Items.STORAGE_BLOCK_STEEL, 5)
                .require(ModItems.HPJ_11_BLUEPRINT)
                .require(ModItems.CANNON_CORE)
                .require(ModItems.MEDIUM_BATTERY_PACK)
                .require(ModItems.LARGE_MOTOR)
                .require(Items.OBSERVER)
                .unlockedBy(getHasName(ModItems.HPJ_11_BLUEPRINT), has(ModItems.HPJ_11_BLUEPRINT))
                .save(writer, Mod.loc(getEntityTypeName(ModEntities.HPJ_11)));
        VehicleAssemblingRecipeBuilder.entity(ModEntities.LASER_TOWER, VehicleAssemblingRecipe.Category.DEFENSE)
                .require(ModTags.Items.STORAGE_BLOCK_STEEL, 1)
                .require(ModItems.LASER_UNIT)
                .require(ModItems.SMALL_BATTERY_PACK)
                .require(ModItems.MOTOR)
                .unlockedBy(getHasName(ModItems.LASER_UNIT), has(ModItems.LASER_UNIT))
                .save(writer, Mod.loc(getEntityTypeName(ModEntities.LASER_TOWER)));
        VehicleAssemblingRecipeBuilder.entity(ModEntities.WAVEFORCE_TOWER, VehicleAssemblingRecipe.Category.DEFENSE)
                .require(ModTags.Items.STORAGE_BLOCK_STEEL, 10)
                .require(ModItems.CEMENTED_CARBIDE_BLOCK, 2)
                .require(Items.REDSTONE_BLOCK, 8)
                .require(ModItems.LASER_UNIT, 9)
                .require(ModItems.MEDIUM_BATTERY_PACK, 2)
                .require(ModItems.LARGE_MOTOR)
                .unlockedBy(getHasName(ModItems.LASER_UNIT), has(ModItems.LASER_UNIT))
                .save(writer, Mod.loc(getEntityTypeName(ModEntities.WAVEFORCE_TOWER)));
        VehicleAssemblingRecipeBuilder.entity(ModEntities.WHEEL_CHAIR, VehicleAssemblingRecipe.Category.CIVILIAN)
                .require(ModItems.WHEEL, 2)
                .require(ModItems.CELL)
                .require(ModItems.MOTOR)
                .require(Items.MINECART)
                .unlockedBy(getHasName(Items.MINECART), has(Items.MINECART))
                .save(writer, Mod.loc(getEntityTypeName(ModEntities.WHEEL_CHAIR)));
        VehicleAssemblingRecipeBuilder.entity(ModEntities.LAV_150, VehicleAssemblingRecipe.Category.LAND)
                .require(ModTags.Items.STORAGE_BLOCK_STEEL, 6)
                .require(ModItems.LIGHT_ARMAMENT_MODULE)
                .require(ModItems.MEDIUM_BATTERY_PACK)
                .require(ModItems.WHEEL, 4)
                .require(ModItems.LARGE_MOTOR)
                .unlockedBy(getHasName(ModItems.LARGE_MOTOR), has(ModItems.LARGE_MOTOR))
                .save(writer, Mod.loc(getEntityTypeName(ModEntities.LAV_150)));
        VehicleAssemblingRecipeBuilder.entity(ModEntities.BMP_2, VehicleAssemblingRecipe.Category.LAND)
                .require(ModTags.Items.STORAGE_BLOCK_STEEL, 8)
                .require(ModItems.MEDIUM_ARMAMENT_MODULE)
                .require(ModItems.MEDIUM_BATTERY_PACK)
                .require(ModItems.TRACK, 2)
                .require(ModItems.LARGE_MOTOR)
                .unlockedBy(getHasName(ModItems.LARGE_MOTOR), has(ModItems.LARGE_MOTOR))
                .save(writer, Mod.loc(getEntityTypeName(ModEntities.BMP_2)));
        VehicleAssemblingRecipeBuilder.entity(ModEntities.PRISM_TANK, VehicleAssemblingRecipe.Category.LAND)
                .require(ModTags.Items.STORAGE_BLOCK_STEEL, 9)
                .require(ModItems.LASER_UNIT, 16)
                .require(ModItems.LARGE_BATTERY_PACK)
                .require(ModItems.TRACK, 2)
                .require(ModItems.LARGE_MOTOR)
                .unlockedBy(getHasName(ModItems.LARGE_MOTOR), has(ModItems.LARGE_MOTOR))
                .save(writer, Mod.loc(getEntityTypeName(ModEntities.PRISM_TANK)));
        VehicleAssemblingRecipeBuilder.entity(ModEntities.YX_100, VehicleAssemblingRecipe.Category.LAND)
                .require(ModTags.Items.STORAGE_BLOCK_STEEL, 12)
                .require(ModItems.CEMENTED_CARBIDE_BLOCK, 2)
                .require(ModItems.HEAVY_ARMAMENT_MODULE)
                .require(ModItems.LARGE_BATTERY_PACK)
                .require(ModItems.TRACK, 2)
                .require(ModItems.LARGE_MOTOR)
                .unlockedBy(getHasName(ModItems.LARGE_MOTOR), has(ModItems.LARGE_MOTOR))
                .save(writer, Mod.loc(getEntityTypeName(ModEntities.YX_100)));
        VehicleAssemblingRecipeBuilder.entity(ModEntities.PLZ_05, VehicleAssemblingRecipe.Category.LAND)
                .require(ModTags.Items.STORAGE_BLOCK_STEEL, 10)
                .require(ModItems.CANNON_CORE, 1)
                .require(ModItems.HEAVY_ARMAMENT_MODULE)
                .require(ModItems.MEDIUM_BATTERY_PACK)
                .require(ModItems.TRACK, 2)
                .require(ModItems.LARGE_MOTOR)
                .unlockedBy(getHasName(ModItems.LARGE_MOTOR), has(ModItems.LARGE_MOTOR))
                .save(writer, Mod.loc(getEntityTypeName(ModEntities.PLZ_05)));
        VehicleAssemblingRecipeBuilder.entity(ModEntities.SPEEDBOAT, VehicleAssemblingRecipe.Category.WATER)
                .require(ModTags.Items.STORAGE_BLOCK_STEEL, 2)
                .require(ItemTags.BOATS)
                .require(ModItems.M_2_HB)
                .require(ModItems.SMALL_BATTERY_PACK)
                .require(ModItems.LARGE_PROPELLER)
                .require(ModItems.LARGE_MOTOR)
                .unlockedBy(getHasName(ModItems.M_2_HB), has(ModItems.M_2_HB))
                .save(writer, Mod.loc(getEntityTypeName(ModEntities.SPEEDBOAT)));
        VehicleAssemblingRecipeBuilder.entity(ModEntities.AH_6, VehicleAssemblingRecipe.Category.AIRCRAFT)
                .require(ModTags.Items.STORAGE_BLOCK_STEEL, 3)
                .require(ModItems.LIGHT_ARMAMENT_MODULE)
                .require(ModItems.MEDIUM_BATTERY_PACK)
                .require(ModItems.LARGE_PROPELLER)
                .require(ModItems.PROPELLER)
                .require(ModItems.LARGE_MOTOR)
                .unlockedBy(getHasName(ModItems.LARGE_PROPELLER), has(ModItems.LARGE_PROPELLER))
                .save(writer, Mod.loc(getEntityTypeName(ModEntities.AH_6)));
        VehicleAssemblingRecipeBuilder.entity(ModEntities.A_10A, VehicleAssemblingRecipe.Category.AIRCRAFT)
                .require(ModTags.Items.STORAGE_BLOCK_STEEL, 10)
                .require(ModItems.HEAVY_ARMAMENT_MODULE)
                .require(ModItems.LARGE_BATTERY_PACK)
                .require(ModItems.LARGE_PROPELLER, 2)
                .require(ModItems.LARGE_MOTOR, 2)
                .require(ModItems.WHEEL, 3)
                .unlockedBy(getHasName(ModItems.LARGE_PROPELLER), has(ModItems.LARGE_PROPELLER))
                .save(writer, Mod.loc(getEntityTypeName(ModEntities.A_10A)));
        VehicleAssemblingRecipeBuilder.entity(ModEntities.TRUCK, VehicleAssemblingRecipe.Category.CIVILIAN)
                .require(ModTags.Items.STORAGE_BLOCK_STEEL, 8)
                .require(Items.CHEST, 4)
                .require(ModItems.MEDIUM_BATTERY_PACK)
                .require(ModItems.WHEEL, 6)
                .require(ModItems.LARGE_MOTOR)
                .unlockedBy(getHasName(ModItems.LARGE_MOTOR), has(ModItems.LARGE_MOTOR))
                .save(writer, Mod.loc(getEntityTypeName(ModEntities.TRUCK)));
        VehicleAssemblingRecipeBuilder.entity(ModEntities.MI_28, VehicleAssemblingRecipe.Category.AIRCRAFT)
                .require(ModTags.Items.STORAGE_BLOCK_STEEL, 8)
                .require(ModItems.HEAVY_ARMAMENT_MODULE)
                .require(ModItems.MEDIUM_BATTERY_PACK, 2)
                .require(ModItems.WHEEL, 3)
                .require(ModItems.LARGE_PROPELLER)
                .require(ModItems.PROPELLER)
                .require(ModItems.LARGE_MOTOR)
                .unlockedBy(getHasName(ModItems.HEAVY_ARMAMENT_MODULE), has(ModItems.HEAVY_ARMAMENT_MODULE))
                .save(writer, Mod.loc(getEntityTypeName(ModEntities.MI_28)));

        VehicleAssemblingRecipeBuilder.item(ModItems.SMALL_BATTERY_PACK, 1, VehicleAssemblingRecipe.Category.MISC)
                .require(PLATES_COPPER, 4)
                .require(ModTags.Items.GLASS_PANES, 8)
                .require(Items.REDSTONE, 4)
                .require(Items.IRON_INGOT, 4)
                .unlockedBy(getHasName(ModItems.COPPER_PLATE), has(ModItems.COPPER_PLATE))
                .save(writer, Mod.loc(getItemName(ModItems.SMALL_BATTERY_PACK) + "_assembling"));
        VehicleAssemblingRecipeBuilder.item(ModItems.MEDIUM_BATTERY_PACK, 1, VehicleAssemblingRecipe.Category.MISC)
                .require(PLATES_COPPER, 36)
                .require(ModTags.Items.GLASS_PANES, 72)
                .require(Items.REDSTONE, 36)
                .require(Items.IRON_INGOT, 36)
                .unlockedBy(getHasName(ModItems.COPPER_PLATE), has(ModItems.COPPER_PLATE))
                .save(writer, Mod.loc(getItemName(ModItems.MEDIUM_BATTERY_PACK) + "_assembling"));
        VehicleAssemblingRecipeBuilder.item(ModItems.LARGE_BATTERY_PACK, 1, VehicleAssemblingRecipe.Category.MISC)
                .require(PLATES_COPPER, 144)
                .require(ModTags.Items.GLASS_PANES, 288)
                .require(Items.REDSTONE, 144)
                .require(Items.IRON_INGOT, 144)
                .unlockedBy(getHasName(ModItems.COPPER_PLATE), has(ModItems.COPPER_PLATE))
                .save(writer, Mod.loc(getItemName(ModItems.LARGE_BATTERY_PACK) + "_assembling"));
        VehicleAssemblingRecipeBuilder.item(ModItems.VEHICLE_RESET_KIT, 1, VehicleAssemblingRecipe.Category.MISC)
                .require(ModTags.Items.INGOTS_STEEL)
                .require(Items.PAPER, 4)
                .unlockedBy(getHasName(Items.PAPER), has(Items.PAPER))
                .save(writer, Mod.loc(getItemName(ModItems.VEHICLE_RESET_KIT) + "_assembling"));
    }

    private static void buildGunRecipes(@NotNull RecipeOutput writer) {
        gunSmithing(writer, ModItems.TRACHELIUM_BLUEPRINT, GunRarity.EPIC, ModTags.Items.INGOTS_CEMENTED_CARBIDE, ModItems.TRACHELIUM);
        gunSmithing(writer, ModItems.GLOCK_17_BLUEPRINT, GunRarity.COMMON, Items.IRON_INGOT, ModItems.GLOCK_17);
        gunSmithing(writer, ModItems.MP_443_BLUEPRINT, GunRarity.COMMON, Items.IRON_INGOT, ModItems.MP_443);
        gunSmithing(writer, ModItems.GLOCK_18_BLUEPRINT, GunRarity.RARE, Items.GOLD_INGOT, ModItems.GLOCK_18);
        gunSmithing(writer, ModItems.HUNTING_RIFLE_BLUEPRINT, GunRarity.RARE, ItemTags.LOGS, ModItems.HUNTING_RIFLE);
        gunSmithing(writer, ModItems.M_79_BLUEPRINT, GunRarity.RARE, Items.DISPENSER, ModItems.M_79);
        gunSmithing(writer, ModItems.RPG_BLUEPRINT, GunRarity.RARE, Items.DISPENSER, ModItems.RPG);
        gunSmithing(writer, ModItems.BOCEK_BLUEPRINT, GunRarity.EPIC, Items.BOW, ModItems.BOCEK);
        gunSmithing(writer, ModItems.M_4_BLUEPRINT, GunRarity.RARE, ModTags.Items.INGOTS_STEEL, ModItems.M_4);
        gunSmithing(writer, ModItems.AA_12_BLUEPRINT, GunRarity.LEGENDARY, Items.NETHERITE_INGOT, ModItems.AA_12);
        gunSmithing(writer, ModItems.HK_416_BLUEPRINT, GunRarity.RARE, ModTags.Items.INGOTS_STEEL, ModItems.HK_416);
        gunSmithing(writer, ModItems.RPK_BLUEPRINT, GunRarity.EPIC, ItemTags.LOGS, ModItems.RPK);
        gunSmithing(writer, ModItems.SKS_BLUEPRINT, GunRarity.RARE, ItemTags.LOGS, ModItems.SKS);
        gunSmithing(writer, ModItems.NTW_20_BLUEPRINT, GunRarity.LEGENDARY, Items.SPYGLASS, ModItems.NTW_20);
        gunSmithing(writer, ModItems.MP_5_BLUEPRINT, GunRarity.RARE, Items.IRON_INGOT, ModItems.MP_5);
        gunSmithing(writer, ModItems.VECTOR_BLUEPRINT, GunRarity.EPIC, ModTags.Items.INGOTS_CEMENTED_CARBIDE, ModItems.VECTOR);
        gunSmithing(writer, ModItems.MINIGUN_BLUEPRINT, GunRarity.LEGENDARY, ModItems.MOTOR, ModItems.MINIGUN);
        gunSmithing(writer, ModItems.MK_14_BLUEPRINT, GunRarity.EPIC, ModTags.Items.INGOTS_CEMENTED_CARBIDE, ModItems.MK_14);
        gunSmithing(writer, ModItems.SENTINEL_BLUEPRINT, GunRarity.EPIC, ModItems.CELL, ModItems.SENTINEL);
        gunSmithing(writer, ModItems.M_60_BLUEPRINT, GunRarity.EPIC, ModTags.Items.INGOTS_CEMENTED_CARBIDE, ModItems.M_60);
        gunSmithing(writer, ModItems.SVD_BLUEPRINT, GunRarity.EPIC, ModTags.Items.INGOTS_CEMENTED_CARBIDE, ModItems.SVD);
        gunSmithing(writer, ModItems.MARLIN_BLUEPRINT, GunRarity.COMMON, ItemTags.LOGS, ModItems.MARLIN);
        gunSmithing(writer, ModItems.M_870_BLUEPRINT, GunRarity.RARE, ModTags.Items.INGOTS_STEEL, ModItems.M_870);
        gunSmithing(writer, ModItems.M_98B_BLUEPRINT, GunRarity.EPIC, Items.SPYGLASS, ModItems.M_98B);
        gunSmithing(writer, ModItems.AK_47_BLUEPRINT, GunRarity.RARE, ItemTags.LOGS, ModItems.AK_47);
        gunSmithing(writer, ModItems.AK_12_BLUEPRINT, GunRarity.RARE, ModTags.Items.INGOTS_STEEL, ModItems.AK_12);
        gunSmithing(writer, ModItems.DEVOTION_BLUEPRINT, GunRarity.EPIC, ModTags.Items.INGOTS_CEMENTED_CARBIDE, ModItems.DEVOTION);
        gunSmithing(writer, ModItems.TASER_BLUEPRINT, GunRarity.COMMON, Items.YELLOW_CONCRETE, ModItems.TASER);
        gunSmithing(writer, ModItems.M_1911_BLUEPRINT, GunRarity.COMMON, ModTags.Items.INGOTS_STEEL, ModItems.M_1911);
        gunSmithing(writer, ModItems.QBZ_95_BLUEPRINT, GunRarity.RARE, ModTags.Items.INGOTS_STEEL, ModItems.QBZ_95);
        gunSmithing(writer, ModItems.QBZ_191_BLUEPRINT, GunRarity.EPIC, ModTags.Items.INGOTS_CEMENTED_CARBIDE, ModItems.QBZ_191);
        gunSmithing(writer, ModItems.AWM_BLUEPRINT, GunRarity.EPIC, Items.SPYGLASS, ModItems.AWM);
        gunSmithing(writer, ModItems.K_98_BLUEPRINT, GunRarity.RARE, ItemTags.LOGS, ModItems.K_98);
        gunSmithing(writer, ModItems.MOSIN_NAGANT_BLUEPRINT, GunRarity.RARE, ItemTags.LOGS, ModItems.MOSIN_NAGANT);
        gunSmithing(writer, ModItems.JAVELIN_BLUEPRINT, GunRarity.LEGENDARY, ModItems.ANCIENT_CPU, ModItems.JAVELIN);
        gunSmithing(writer, ModItems.IGLA_BLUEPRINT, GunRarity.EPIC, ModItems.ANCIENT_CPU, ModItems.IGLA_9K38);
        gunSmithing(writer, ModItems.M_2_HB_BLUEPRINT, GunRarity.RARE, ModTags.Items.STORAGE_BLOCK_STEEL, ModItems.M_2_HB);
        gunSmithing(writer, ModItems.SECONDARY_CATACLYSM_BLUEPRINT, GunRarity.LEGENDARY, ModItems.KNIFE, ModItems.SECONDARY_CATACLYSM);
        gunSmithing(writer, ModItems.INSIDIOUS_BLUEPRINT, GunRarity.EPIC, ModTags.Items.INGOTS_CEMENTED_CARBIDE, ModItems.INSIDIOUS);
        gunSmithing(writer, ModItems.AURELIA_SCEPTRE_BLUEPRINT, GunRarity.LEGENDARY, Items.END_CRYSTAL, ModItems.AURELIA_SCEPTRE);
        gunSmithing(writer, ModItems.QL_1031_BLUEPRINT, GunRarity.LEGENDARY, ModItems.BATTERY, ModItems.QL_1031);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.HOMEMADE_SHOTGUN)
                .pattern("aab")
                .pattern("ccc")
                .pattern(" dc")
                .define('a', ModItems.IRON_MATERIALS.barrel())
                .define('b', Items.FLINT_AND_STEEL)
                .define('c', ItemTags.PLANKS)
                .define('d', ModTags.Items.DUSTS_REDSTONE)
                .unlockedBy(getHasName(ModItems.IRON_MATERIALS.barrel()), has(ModItems.IRON_MATERIALS.barrel()))
                .save(writer, Mod.loc(getItemName(ModItems.HOMEMADE_SHOTGUN)));
    }

    private static void buildBlueprintRecipes(@NotNull RecipeOutput writer) {
        copyBlueprint(writer, ModItems.TRACHELIUM_BLUEPRINT);
        copyBlueprint(writer, ModItems.GLOCK_17_BLUEPRINT);
        copyBlueprint(writer, ModItems.MP_443_BLUEPRINT);
        copyBlueprint(writer, ModItems.GLOCK_18_BLUEPRINT);
        copyBlueprint(writer, ModItems.HUNTING_RIFLE_BLUEPRINT);
        copyBlueprint(writer, ModItems.M_79_BLUEPRINT);
        copyBlueprint(writer, ModItems.RPG_BLUEPRINT);
        copyBlueprint(writer, ModItems.BOCEK_BLUEPRINT);
        copyBlueprint(writer, ModItems.M_4_BLUEPRINT);
        copyBlueprint(writer, ModItems.AA_12_BLUEPRINT);
        copyBlueprint(writer, ModItems.HK_416_BLUEPRINT);
        copyBlueprint(writer, ModItems.RPK_BLUEPRINT);
        copyBlueprint(writer, ModItems.SKS_BLUEPRINT);
        copyBlueprint(writer, ModItems.NTW_20_BLUEPRINT);
        copyBlueprint(writer, ModItems.MP_5_BLUEPRINT);
        copyBlueprint(writer, ModItems.VECTOR_BLUEPRINT);
        copyBlueprint(writer, ModItems.MINIGUN_BLUEPRINT);
        copyBlueprint(writer, ModItems.MK_14_BLUEPRINT);
        copyBlueprint(writer, ModItems.SENTINEL_BLUEPRINT);
        copyBlueprint(writer, ModItems.M_60_BLUEPRINT);
        copyBlueprint(writer, ModItems.SVD_BLUEPRINT);
        copyBlueprint(writer, ModItems.MARLIN_BLUEPRINT);
        copyBlueprint(writer, ModItems.M_870_BLUEPRINT);
        copyBlueprint(writer, ModItems.AWM_BLUEPRINT);
        copyBlueprint(writer, ModItems.M_98B_BLUEPRINT);
        copyBlueprint(writer, ModItems.AK_47_BLUEPRINT);
        copyBlueprint(writer, ModItems.AK_12_BLUEPRINT);
        copyBlueprint(writer, ModItems.DEVOTION_BLUEPRINT);
        copyBlueprint(writer, ModItems.TASER_BLUEPRINT);
        copyBlueprint(writer, ModItems.M_1911_BLUEPRINT);
        copyBlueprint(writer, ModItems.QBZ_95_BLUEPRINT);
        copyBlueprint(writer, ModItems.QBZ_191_BLUEPRINT);
        copyBlueprint(writer, ModItems.K_98_BLUEPRINT);
        copyBlueprint(writer, ModItems.MOSIN_NAGANT_BLUEPRINT);
        copyBlueprint(writer, ModItems.JAVELIN_BLUEPRINT);
        copyBlueprint(writer, ModItems.IGLA_BLUEPRINT);
        copyBlueprint(writer, ModItems.M_2_HB_BLUEPRINT);
        copyBlueprint(writer, ModItems.SECONDARY_CATACLYSM_BLUEPRINT);
        copyBlueprint(writer, ModItems.INSIDIOUS_BLUEPRINT);
        copyBlueprint(writer, ModItems.AURELIA_SCEPTRE_BLUEPRINT);
        copyBlueprint(writer, ModItems.MK_42_BLUEPRINT);
        copyBlueprint(writer, ModItems.MLE_1934_BLUEPRINT);
        copyBlueprint(writer, ModItems.BL_132_BLUEPRINT);
        copyBlueprint(writer, ModItems.HPJ_11_BLUEPRINT);
        copyBlueprint(writer, ModItems.ANNIHILATOR_BLUEPRINT);
        copyBlueprint(writer, ModItems.QL_1031_BLUEPRINT);
    }

    private static void buildPerkRecipes(@NotNull RecipeOutput writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.EMPTY_PERK)
                .pattern("cbc")
                .pattern("bab")
                .pattern("cbc")
                .define('a', Items.PAPER)
                .define('b', Items.LAPIS_LAZULI)
                .define('c', ModTags.Items.INGOTS_IRON)
                .unlockedBy(getHasName(Items.PAPER), has(Items.PAPER))
                .save(writer, Mod.loc(getItemName(ModItems.EMPTY_PERK)));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PERK_ITEMS.get(ModPerks.AP_BULLET))
                .pattern("cbc")
                .pattern("bab")
                .pattern("cbc")
                .define('a', ModItems.EMPTY_PERK)
                .define('b', commonItemTag("storage_blocks/tungsten"))
                .define('c', INGOTS_TUNGSTEN)
                .unlockedBy(getHasName(ModItems.EMPTY_PERK), has(ModItems.EMPTY_PERK))
                .save(writer, perkLoc(ModPerks.AP_BULLET));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PERK_ITEMS.get(ModPerks.CUPID_ARROW))
                .pattern("cbc")
                .pattern("dad")
                .pattern("cbc")
                .define('a', ModItems.EMPTY_PERK)
                .define('b', Items.BOW)
                .define('c', ItemTags.ARROWS)
                .define('d', getPotionIngredient(Potions.HEALING))
                .unlockedBy(getHasName(ModItems.EMPTY_PERK), has(ModItems.EMPTY_PERK))
                .save(writer, perkLoc(ModPerks.CUPID_ARROW));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PERK_ITEMS.get(ModPerks.FIREFLY))
                .pattern("cbc")
                .pattern("bab")
                .pattern("cbc")
                .define('a', ModItems.EMPTY_PERK)
                .define('b', Ingredient.of(Items.OCHRE_FROGLIGHT, Items.VERDANT_FROGLIGHT, Items.PEARLESCENT_FROGLIGHT))
                .define('c', ModItems.HIGH_ENERGY_EXPLOSIVES)
                .unlockedBy(getHasName(ModItems.EMPTY_PERK), has(ModItems.EMPTY_PERK))
                .save(writer, perkLoc(ModPerks.FIREFLY));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PERK_ITEMS.get(ModPerks.HE_BULLET))
                .pattern("cbc")
                .pattern("bab")
                .pattern("cbc")
                .define('a', ModItems.EMPTY_PERK)
                .define('b', Items.TNT)
                .define('c', ModItems.HIGH_ENERGY_EXPLOSIVES)
                .unlockedBy(getHasName(ModItems.EMPTY_PERK), has(ModItems.EMPTY_PERK))
                .save(writer, perkLoc(ModPerks.HE_BULLET));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PERK_ITEMS.get(ModPerks.INCENDIARY_BULLET))
                .pattern("bbb")
                .pattern("cac")
                .pattern("bbb")
                .define('a', ModItems.EMPTY_PERK)
                .define('b', Items.BLAZE_POWDER)
                .define('c', Items.DRAGON_BREATH)
                .unlockedBy(getHasName(ModItems.EMPTY_PERK), has(ModItems.EMPTY_PERK))
                .save(writer, perkLoc(ModPerks.INCENDIARY_BULLET));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PERK_ITEMS.get(ModPerks.INTELLIGENT_CHIP))
                .pattern("bbb")
                .pattern("bab")
                .pattern("bbb")
                .define('a', ModItems.EMPTY_PERK)
                .define('b', ModItems.ANCIENT_CPU)
                .unlockedBy(getHasName(ModItems.EMPTY_PERK), has(ModItems.EMPTY_PERK))
                .save(writer, perkLoc(ModPerks.INTELLIGENT_CHIP));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PERK_ITEMS.get(ModPerks.JHP_BULLET))
                .pattern("cbc")
                .pattern("bab")
                .pattern("cbc")
                .define('a', ModItems.EMPTY_PERK)
                .define('b', ModTags.Items.STORAGE_BLOCKS_COPPER)
                .define('c', ModTags.Items.INGOTS_COPPER)
                .unlockedBy(getHasName(ModItems.EMPTY_PERK), has(ModItems.EMPTY_PERK))
                .save(writer, perkLoc(ModPerks.JHP_BULLET));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PERK_ITEMS.get(ModPerks.LONGER_WIRE))
                .pattern("bbb")
                .pattern("bab")
                .pattern("bbb")
                .define('a', ModItems.EMPTY_PERK)
                .define('b', Items.STRING)
                .unlockedBy(getHasName(ModItems.EMPTY_PERK), has(ModItems.EMPTY_PERK))
                .save(writer, perkLoc(ModPerks.LONGER_WIRE));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PERK_ITEMS.get(ModPerks.MICRO_MISSILE))
                .pattern("cbc")
                .pattern("bab")
                .pattern("cbc")
                .define('a', ModItems.EMPTY_PERK)
                .define('b', ModItems.GRAIN)
                .define('c', Items.FIREWORK_ROCKET)
                .unlockedBy(getHasName(ModItems.EMPTY_PERK), has(ModItems.EMPTY_PERK))
                .save(writer, perkLoc(ModPerks.MICRO_MISSILE));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PERK_ITEMS.get(ModPerks.PHASE_PENETRATING_BULLET))
                .pattern("cbc")
                .pattern("bab")
                .pattern("cbc")
                .define('a', ModItems.EMPTY_PERK)
                .define('b', ModTags.Items.INGOTS_NETHERITE)
                .define('c', ModItems.AP_HEAD)
                .unlockedBy(getHasName(ModItems.EMPTY_PERK), has(ModItems.EMPTY_PERK))
                .save(writer, perkLoc(ModPerks.PHASE_PENETRATING_BULLET));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PERK_ITEMS.get(ModPerks.POISONOUS_BULLET))
                .pattern("cbc")
                .pattern("bab")
                .pattern("cbc")
                .define('a', ModItems.EMPTY_PERK)
                .define('b', commonItemTag("storage_blocks/lead"))
                .define('c', Items.SPIDER_EYE)
                .unlockedBy(getHasName(ModItems.EMPTY_PERK), has(ModItems.EMPTY_PERK))
                .save(writer, perkLoc(ModPerks.POISONOUS_BULLET));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PERK_ITEMS.get(ModPerks.POWERFUL_ATTRACTION))
                .pattern("dbe")
                .pattern("cac")
                .pattern(" c ")
                .define('a', ModItems.EMPTY_PERK)
                .define('b', ModTags.Items.ENDER_PEARLS)
                .define('c', ModTags.Items.INGOTS_IRON)
                .define('d', ModTags.Items.DUSTS_REDSTONE)
                .define('e', ModTags.Items.GEMS_LAPIS)
                .unlockedBy(getHasName(ModItems.EMPTY_PERK), has(ModItems.EMPTY_PERK))
                .save(writer, perkLoc(ModPerks.POWERFUL_ATTRACTION));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PERK_ITEMS.get(ModPerks.REGENERATION))
                .pattern("ccc")
                .pattern("bab")
                .pattern("ddd")
                .define('a', ModItems.EMPTY_PERK)
                .define('b', ModItems.CELL)
                .define('c', Items.DAYLIGHT_DETECTOR)
                .define('d', ModTags.Items.INGOTS_GOLD)
                .unlockedBy(getHasName(ModItems.EMPTY_PERK), has(ModItems.EMPTY_PERK))
                .save(writer, perkLoc(ModPerks.REGENERATION));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PERK_ITEMS.get(ModPerks.RIOT_BULLET))
                .pattern("cbc")
                .pattern("bab")
                .pattern("cbc")
                .define('a', ModItems.EMPTY_PERK)
                .define('b', Items.SLIME_BLOCK)
                .define('c', Items.COBWEB)
                .unlockedBy(getHasName(ModItems.EMPTY_PERK), has(ModItems.EMPTY_PERK))
                .save(writer, perkLoc(ModPerks.RIOT_BULLET));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PERK_ITEMS.get(ModPerks.SILVER_BULLET))
                .pattern("cbc")
                .pattern("bab")
                .pattern("cbc")
                .define('a', ModItems.EMPTY_PERK)
                .define('b', commonItemTag("storage_blocks/silver"))
                .define('c', INGOTS_SILVER)
                .unlockedBy(getHasName(ModItems.EMPTY_PERK), has(ModItems.EMPTY_PERK))
                .save(writer, perkLoc(ModPerks.SILVER_BULLET));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PERK_ITEMS.get(ModPerks.TURBO_CHARGER))
                .pattern("cbc")
                .pattern("bab")
                .pattern("cbc")
                .define('a', ModItems.EMPTY_PERK)
                .define('b', Items.PISTON)
                .define('c', ModTags.Items.INGOTS_STEEL)
                .unlockedBy(getHasName(ModItems.EMPTY_PERK), has(ModItems.EMPTY_PERK))
                .save(writer, perkLoc(ModPerks.TURBO_CHARGER));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PERK_ITEMS.get(ModPerks.VOLT_OVERLOAD))
                .pattern("cec")
                .pattern("bab")
                .pattern("bdb")
                .define('a', ModItems.EMPTY_PERK)
                .define('b', ModItems.CELL)
                .define('c', Items.LIGHTNING_ROD)
                .define('d', commonItemTag("dusts/coal_coke"))
                .define('e', ModTags.Items.INGOTS_IRON)
                .unlockedBy(getHasName(ModItems.EMPTY_PERK), has(ModItems.EMPTY_PERK))
                .save(writer, perkLoc(ModPerks.VOLT_OVERLOAD));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PERK_ITEMS.get(ModPerks.BACKPACK_LINKED_MAGAZINE))
                .pattern("cbc")
                .pattern("bab")
                .pattern("cbc")
                .define('a', ModItems.PERK_ITEMS.get(ModPerks.SUBSISTENCE))
                .define('b', ModTags.Items.CHESTS_ENDER)
                .define('c', ModTags.Items.CHESTS_WOODEN)
                .unlockedBy(getHasName(ModItems.PERK_ITEMS.get(ModPerks.SUBSISTENCE)), has(ModItems.PERK_ITEMS.get(ModPerks.SUBSISTENCE)))
                .save(writer, perkLoc(ModPerks.BACKPACK_LINKED_MAGAZINE));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PERK_ITEMS.get(ModPerks.POWERFUL_COOLER))
                .pattern("cdc")
                .pattern("bab")
                .pattern("cdc")
                .define('a', ModItems.EMPTY_PERK)
                .define('b', Items.POWDER_SNOW_BUCKET)
                .define('c', Items.BLUE_ICE)
                .define('d', commonItemTag("storage_blocks/silver"))
                .unlockedBy(getHasName(ModItems.EMPTY_PERK), has(ModItems.EMPTY_PERK))
                .unlockedBy(getHasName(Items.POWDER_SNOW_BUCKET), has(Items.POWDER_SNOW_BUCKET))
                .save(writer, perkLoc(ModPerks.POWERFUL_COOLER));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PERK_ITEMS.get(ModPerks.BLADE_BULLET))
                .pattern("dbd")
                .pattern("cac")
                .pattern("ebe")
                .define('a', ModItems.EMPTY_PERK)
                .define('b', ModTags.Items.STORAGE_BLOCK_STEEL)
                .define('c', ModItems.BARBED_WIRE)
                .define('d', ModItems.KNIFE)
                .define('e', ModItems.CLAYMORE_MINE)
                .unlockedBy(getHasName(ModItems.EMPTY_PERK), has(ModItems.EMPTY_PERK))
                .unlockedBy(getHasName(ModItems.CLAYMORE_MINE), has(ModItems.CLAYMORE_MINE))
                .save(writer, perkLoc(ModPerks.BLADE_BULLET));
    }

    private static void buildMiscRecipes(@NotNull RecipeOutput writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.DOG_TAG)
                .pattern("a")
                .pattern("b")
                .define('a', Items.CHAIN)
                .define('b', Items.NAME_TAG)
                .unlockedBy(getHasName(Items.NAME_TAG), has(Items.NAME_TAG))
                .save(writer, Mod.loc(getItemName(ModItems.DOG_TAG)));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.DRONE, 4)
                .pattern("a a")
                .pattern("bcb")
                .pattern("ded")
                .define('a', ModItems.PROPELLER)
                .define('b', ModItems.MOTOR)
                .define('c', Items.COMPASS)
                .define('d', ModTags.Items.NUGGETS_IRON)
                .define('e', ModItems.CELL)
                .unlockedBy(getHasName(ModItems.PROPELLER), has(ModItems.PROPELLER))
                .unlockedBy(getHasName(ModItems.MOTOR), has(ModItems.MOTOR))
                .save(writer, Mod.loc(getItemName(ModItems.DRONE)));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.FIRING_PARAMETERS)
                .pattern("a")
                .pattern("b")
                .pattern("c")
                .define('a', Items.TARGET)
                .define('b', Items.PAPER)
                .define('c', ItemTags.PLANKS)
                .unlockedBy(getHasName(Items.TARGET), has(Items.TARGET))
                .save(writer, Mod.loc(getItemName(ModItems.FIRING_PARAMETERS)));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.IFF)
                .pattern("ab")
                .pattern("c ")
                .define('a', ModTags.Items.DUSTS_REDSTONE)
                .define('b', ModTags.Items.GEMS_LAPIS)
                .define('c', PLATES_COPPER)
                .unlockedBy(getHasName(Items.LAPIS_LAZULI), has(Items.LAPIS_LAZULI))
                .save(writer, Mod.loc(getItemName(ModItems.IFF)));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PARACHUTE)
                .pattern("aaa")
                .pattern("b b")
                .pattern("bcb")
                .define('a', Items.PHANTOM_MEMBRANE)
                .define('b', Items.STRING)
                .define('c', Items.LEATHER)
                .unlockedBy(getHasName(Items.PHANTOM_MEMBRANE), has(Items.PHANTOM_MEMBRANE))
                .save(writer, Mod.loc(getItemName(ModItems.PARACHUTE)));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.TRANSCRIPT)
                .pattern("a")
                .pattern("b")
                .pattern("c")
                .define('a', ModTags.Items.NUGGETS_IRON)
                .define('b', Items.PAPER)
                .define('c', ItemTags.PLANKS)
                .unlockedBy(getHasName(Items.PAPER), has(Items.PAPER))
                .save(writer, Mod.loc(getItemName(ModItems.TRANSCRIPT)));
    }

    private static void buildSpecialRecipes(@NotNull RecipeOutput writer) {
        SpecialRecipeBuilder.special(PotionMortarShellRecipe::new).save(writer, "potion_mortar_shell");
        SpecialRecipeBuilder.special(AmmoBoxAddAmmoRecipe::new).save(writer, "ammo_box_add_ammo");
        SpecialRecipeBuilder.special(AmmoBoxExtractAmmoRecipe::new).save(writer, "ammo_box_extract_ammo");
        SpecialRecipeBuilder.special(SmokeDyeRecipe::new).save(writer, "smoke_dye");
        SpecialRecipeBuilder.special(VehicleResetRecipe::new).save(writer, "vehicle_reset");
    }

    public static void copyBlueprint(RecipeOutput writer, ItemLike result) {
        copySmithingTemplate(writer, result, Items.LAPIS_LAZULI);
    }

    public static void gunSmithing(RecipeOutput writer, ItemLike blueprint, GunRarity rarity, TagKey<Item> tagKey, Item pResultItem) {
        gunSmithing(writer, blueprint, rarity, Ingredient.of(tagKey), pResultItem);
    }

    public static void gunSmithing(RecipeOutput writer, ItemLike blueprint, GunRarity rarity, ItemLike ingredient, Item pResultItem) {
        gunSmithing(writer, blueprint, rarity, Ingredient.of(ingredient), pResultItem);
    }

    public static void gunSmithing(RecipeOutput writer, ItemLike blueprint, GunRarity rarity, Ingredient ingredient, Item pResultItem) {
        ItemLike pack = switch (rarity) {
            case COMMON -> ModItems.COMMON_MATERIAL_PACK;
            case RARE -> ModItems.RARE_MATERIAL_PACK;
            case EPIC -> ModItems.EPIC_MATERIAL_PACK;
            case LEGENDARY -> ModItems.LEGENDARY_MATERIAL_PACK;
        };

        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(blueprint),
                        Ingredient.of(pack),
                        ingredient,
                        RecipeCategory.COMBAT,
                        pResultItem
                )
                .unlocks(getHasName(blueprint), has(blueprint))
                .save(writer, Mod.loc(getItemName(pResultItem) + "_smithing"));
    }

    public enum GunRarity {
        COMMON,
        RARE,
        EPIC,
        LEGENDARY,
    }

    public static ResourceLocation perkLoc(Perk perk) {
        return Mod.loc("perk/" + getItemName(ModItems.PERK_ITEMS.get(perk)));
    }

    protected static String getEntityTypeName(EntityType<?> entityType) {
        return BuiltInRegistries.ENTITY_TYPE.getKey(entityType).getPath();
    }

    // 生成材料包所有材料的配方
    public static void generateMaterialRecipes(@NotNull RecipeOutput writer, ModItems.Materials material, ItemLike ingredient) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, material.barrel())
                .pattern("AAA")
                .define('A', ingredient)
                .unlockedBy(getHasName(ingredient), has(ingredient))
                .save(writer, Mod.loc(getItemName(material.barrel())));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, material.action())
                .pattern("AAA")
                .pattern("  A")
                .define('A', ingredient)
                .unlockedBy(getHasName(ingredient), has(ingredient))
                .save(writer, Mod.loc(getItemName(material.action())));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, material.spring())
                .pattern("A")
                .pattern("A")
                .pattern("A")
                .define('A', ingredient)
                .unlockedBy(getHasName(ingredient), has(ingredient))
                .save(writer, Mod.loc(getItemName(material.spring())));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, material.trigger())
                .pattern("BA")
                .pattern(" A")
                .define('A', ingredient)
                .define('B', Items.TRIPWIRE_HOOK)
                .unlockedBy(getHasName(ingredient), has(ingredient))
                .save(writer, Mod.loc(getItemName(material.trigger())));
    }

    public static void generateMaterialRecipes(@NotNull RecipeOutput writer, ModItems.Materials material, TagKey<Item> tagKey, Item name) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, material.barrel())
                .pattern("AAA")
                .define('A', tagKey)
                .unlockedBy(getHasName(name), has(tagKey))
                .save(writer, Mod.loc(getItemName(material.barrel())));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, material.action())
                .pattern("AAA")
                .pattern("  A")
                .define('A', tagKey)
                .unlockedBy(getHasName(name), has(tagKey))
                .save(writer, Mod.loc(getItemName(material.action())));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, material.spring())
                .pattern("A")
                .pattern("A")
                .pattern("A")
                .define('A', tagKey)
                .unlockedBy(getHasName(name), has(tagKey))
                .save(writer, Mod.loc(getItemName(material.spring())));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, material.trigger())
                .pattern("BA")
                .pattern(" A")
                .define('A', tagKey)
                .define('B', Items.TRIPWIRE_HOOK)
                .unlockedBy(getHasName(name), has(tagKey))
                .save(writer, Mod.loc(getItemName(material.trigger())));
    }

    public static void generateSmithingMaterialRecipe(@NotNull RecipeOutput writer, ModItems.Materials material, ModItems.Materials result, Item template, Item ingredient) {
        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(template),
                        Ingredient.of(material.barrel()),
                        Ingredient.of(ingredient),
                        RecipeCategory.MISC,
                        result.barrel()
                )
                .unlocks(getHasName(template), has(template))
                .unlocks(getHasName(material.barrel()), has(material.barrel()))
                .save(writer, Mod.loc(getItemName(result.barrel())));

        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(template),
                        Ingredient.of(material.action()),
                        Ingredient.of(ingredient),
                        RecipeCategory.MISC,
                        result.action()
                )
                .unlocks(getHasName(template), has(template))
                .unlocks(getHasName(material.action()), has(material.action()))
                .save(writer, Mod.loc(getItemName(result.action())));

        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(template),
                        Ingredient.of(material.spring()),
                        Ingredient.of(ingredient),
                        RecipeCategory.MISC,
                        result.spring()
                )
                .unlocks(getHasName(template), has(template))
                .unlocks(getHasName(material.spring()), has(material.spring()))
                .save(writer, Mod.loc(getItemName(result.spring())));

        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(template),
                        Ingredient.of(material.trigger()),
                        Ingredient.of(ingredient),
                        RecipeCategory.MISC,
                        result.trigger()
                )
                .unlocks(getHasName(template), has(template))
                .unlocks(getHasName(material.trigger()), has(material.trigger()))
                .save(writer, Mod.loc(getItemName(result.trigger())));
    }

    public static void generateMaterialPackRecipe(@NotNull RecipeOutput writer, ModItems.Materials material, Item pack) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, pack)
                .requires(material.barrel())
                .requires(material.action())
                .requires(material.spring())
                .requires(material.trigger())
                .unlockedBy(getHasName(material.barrel()), has(material.barrel()))
                .unlockedBy(getHasName(material.action()), has(material.action()))
                .unlockedBy(getHasName(material.spring()), has(material.spring()))
                .unlockedBy(getHasName(material.trigger()), has(material.trigger()))
                .save(writer, Mod.loc(getItemName(pack)));
    }

    public static Ingredient getPotionIngredient(Holder<Potion> potion) {
        ItemStack stack = new ItemStack(Items.POTION);
        stack.set(DataComponents.POTION_CONTENTS, new PotionContents(potion));
        return Ingredient.of(stack);
    }
}
