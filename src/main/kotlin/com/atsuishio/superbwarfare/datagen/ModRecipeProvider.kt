package com.atsuishio.superbwarfare.datagen

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.datagen.builder.ResearchingRecipeBuilder
import com.atsuishio.superbwarfare.datagen.builder.VehicleAssemblingRecipeBuilder
import com.atsuishio.superbwarfare.init.*
import com.atsuishio.superbwarfare.init.ModItems.Materials
import com.atsuishio.superbwarfare.init.ModTags.commonItemTag
import com.atsuishio.superbwarfare.perk.Perk
import com.atsuishio.superbwarfare.recipe.vehicle.VehicleAssemblingRecipe
import com.google.gson.JsonObject
import net.fabricmc.fabric.api.recipe.v1.ingredient.DefaultCustomIngredients
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.RequirementsStrategy
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.*
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionUtils
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.ItemLike
import java.util.function.Consumer

class ModRecipeProvider(pOutput: PackOutput) : RecipeProvider(pOutput) {
    override fun buildRecipes(writer: Consumer<FinishedRecipe>) {
        buildToolRecipes(writer)
        buildArmorRecipes(writer)
        buildAmmoRecipes(writer)
        buildMaterialRecipes(writer)
        buildBlockRecipes(writer)
        buildVehicleRecipes(writer)
        buildGunRecipes(writer)
        buildBlueprintRecipes(writer)
        buildPerkRecipes(writer)
        buildMiscRecipes(writer)
        buildSpecialRecipes(writer)
        buildResearchRecipes(writer)
    }

    enum class GunRarity {
        COMMON,
        RARE,
        EPIC,
        LEGENDARY,
        SUPERB,
        VIRTUAL
    }

    companion object {
        val PLATES_COPPER: TagKey<Item> = commonItemTag("plates/copper")
        val PLATES_STEEL: TagKey<Item> = commonItemTag("plates/steel")
        val PLATES_PLASTIC: TagKey<Item> = commonItemTag("plates/plastic")
        val INGOTS_STEEL: TagKey<Item> = commonItemTag("ingots/steel")
        val INGOTS_LEAD: TagKey<Item> = commonItemTag("ingots/lead")
        val INGOTS_SILVER: TagKey<Item> = commonItemTag("ingots/silver")
        val INGOTS_TUNGSTEN: TagKey<Item> = commonItemTag("ingots/tungsten")
        val STORAGE_BLOCK_STEEL = commonItemTag("storage_blocks/steel")

        private fun buildToolRecipes(writer: Consumer<FinishedRecipe>) {
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ARTILLERY_INDICATOR)
                .pattern(" b ")
                .pattern("aca")
                .define('a', Items.SPYGLASS)
                .define('b', ModItems.MONITOR)
                .define('c', ModItems.FIRING_PARAMETERS)
                .unlockedBy(getHasName(Items.SPYGLASS), has(Items.SPYGLASS))
                .save(writer, loc(getItemName(ModItems.ARTILLERY_INDICATOR)))
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ARTILLERY_INDICATOR)
                .requires(ModItems.ARTILLERY_INDICATOR)
                .unlockedBy(getHasName(ModItems.ARTILLERY_INDICATOR), has(ModItems.ARTILLERY_INDICATOR))
                .save(writer, loc(getItemName(ModItems.ARTILLERY_INDICATOR) + "_clear"))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.STEEL_PIPE)
                .pattern(" a")
                .pattern("a ")
                .define('a', ModItems.STEEL_MATERIALS.barrel)
                .unlockedBy(
                    getHasName(ModItems.STEEL_MATERIALS.barrel),
                    has(ModItems.STEEL_MATERIALS.barrel)
                )
                .save(writer, loc(getItemName(ModItems.STEEL_PIPE)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.MEDICAL_KIT, 4)
                .pattern("aba")
                .pattern("bcb")
                .pattern("aba")
                .define('a', Items.STRING)
                .define('b', ItemTags.WOOL_CARPETS)
                .define('c', Items.GOLDEN_APPLE)
                .unlockedBy(getHasName(Items.GOLDEN_APPLE), has(Items.GOLDEN_APPLE))
                .save(writer, loc(getItemName(ModItems.MEDICAL_KIT)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ARMOR_PLATE, 2)
                .pattern("aba")
                .define('a', Items.STRING)
                .define('b', PLATES_STEEL)
                .unlockedBy(getHasName(Items.STRING), has(Items.STRING))
                .save(writer, loc(getItemName(ModItems.ARMOR_PLATE)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.VEHICLE_DAMAGE_ANALYZER)
                .pattern("aba")
                .pattern("aca")
                .pattern("ada")
                .define('a', ModTags.Items.INGOTS_GOLD)
                .define('b', Items.OBSERVER)
                .define('c', Items.NOTE_BLOCK)
                .define('d', ModTags.Items.INGOTS_IRON)
                .unlockedBy(getHasName(Items.OBSERVER), has(Items.OBSERVER))
                .save(writer, loc(getItemName(ModItems.VEHICLE_DAMAGE_ANALYZER)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.HAMMER)
                .pattern("aba")
                .pattern(" c ")
                .pattern(" c ")
                .define('a', ModTags.Items.INGOTS_IRON)
                .define('b', ModTags.Items.STORAGE_BLOCKS_IRON)
                .define('c', Items.STICK)
                .unlockedBy(getHasName(Items.IRON_BLOCK), has(ModTags.Items.STORAGE_BLOCKS_IRON))
                .save(writer, loc(getItemName(ModItems.HAMMER)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.GOLDEN_HAMMER)
                .pattern("aba")
                .pattern(" c ")
                .pattern(" c ")
                .define('a', ModTags.Items.INGOTS_GOLD)
                .define('b', ModTags.Items.STORAGE_BLOCKS_GOLD)
                .define('c', Items.STICK)
                .unlockedBy(getHasName(Items.GOLD_BLOCK), has(ModTags.Items.STORAGE_BLOCKS_GOLD))
                .save(writer, loc(getItemName(ModItems.GOLDEN_HAMMER)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.STEEL_HAMMER)
                .pattern("aba")
                .pattern(" c ")
                .pattern(" c ")
                .define('a', INGOTS_STEEL)
                .define('b', STORAGE_BLOCK_STEEL)
                .define('c', Items.STICK)
                .unlockedBy(getHasName(ModItems.STEEL_BLOCK), has(STORAGE_BLOCK_STEEL))
                .save(writer, loc(getItemName(ModItems.STEEL_HAMMER)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.DIAMOND_HAMMER)
                .pattern("aba")
                .pattern(" c ")
                .pattern(" c ")
                .define('a', ModTags.Items.GEMS_DIAMOND)
                .define('b', ModTags.Items.STORAGE_BLOCKS_DIAMOND)
                .define('c', Items.STICK)
                .unlockedBy(getHasName(Items.DIAMOND_BLOCK), has(ModTags.Items.STORAGE_BLOCKS_DIAMOND))
                .save(writer, loc(getItemName(ModItems.DIAMOND_HAMMER)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.CEMENTED_CARBIDE_HAMMER)
                .pattern("aba")
                .pattern(" c ")
                .pattern(" c ")
                .define('a', ModTags.Items.INGOTS_CEMENTED_CARBIDE)
                .define('b', ModTags.Items.STORAGE_BLOCK_CEMENTED_CARBIDE)
                .define('c', Items.STICK)
                .unlockedBy(
                    getHasName(ModItems.CEMENTED_CARBIDE_BLOCK),
                    has(ModTags.Items.STORAGE_BLOCK_CEMENTED_CARBIDE)
                )
                .save(writer, loc(getItemName(ModItems.CEMENTED_CARBIDE_HAMMER)))
            SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                Ingredient.of(ModItems.CEMENTED_CARBIDE_HAMMER),
                Ingredient.of(ModTags.Items.STORAGE_BLOCKS_NETHERITE),
                RecipeCategory.MISC,
                ModItems.NETHERITE_HAMMER
            )
                .unlocks(
                    getHasName(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                    has(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
                )
                .unlocks(
                    getHasName(ModItems.CEMENTED_CARBIDE_HAMMER),
                    has(ModItems.CEMENTED_CARBIDE_HAMMER)
                )
                .save(writer, loc(getItemName(ModItems.NETHERITE_HAMMER)))
            // cemented carbide tools
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.CEMENTED_CARBIDE_SWORD)
                .pattern("a")
                .pattern("a")
                .pattern("b")
                .define('a', ModTags.Items.INGOTS_CEMENTED_CARBIDE)
                .define('b', Items.STICK)
                .unlockedBy(
                    getHasName(ModItems.CEMENTED_CARBIDE_INGOT),
                    has(ModItems.CEMENTED_CARBIDE_INGOT)
                )
                .save(writer, loc(getItemName(ModItems.CEMENTED_CARBIDE_SWORD)))
            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.CEMENTED_CARBIDE_PICKAXE)
                .pattern("aaa")
                .pattern(" b ")
                .pattern(" b ")
                .define('a', ModTags.Items.INGOTS_CEMENTED_CARBIDE)
                .define('b', Items.STICK)
                .unlockedBy(
                    getHasName(ModItems.CEMENTED_CARBIDE_INGOT),
                    has(ModItems.CEMENTED_CARBIDE_INGOT)
                )
                .save(writer, loc(getItemName(ModItems.CEMENTED_CARBIDE_PICKAXE)))
            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.CEMENTED_CARBIDE_AXE)
                .pattern("aa")
                .pattern("ab")
                .pattern(" b")
                .define('a', ModTags.Items.INGOTS_CEMENTED_CARBIDE)
                .define('b', Items.STICK)
                .unlockedBy(
                    getHasName(ModItems.CEMENTED_CARBIDE_INGOT),
                    has(ModItems.CEMENTED_CARBIDE_INGOT)
                )
                .save(writer, loc(getItemName(ModItems.CEMENTED_CARBIDE_AXE)))
            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.CEMENTED_CARBIDE_SHOVEL)
                .pattern("a")
                .pattern("b")
                .pattern("b")
                .define('a', ModTags.Items.INGOTS_CEMENTED_CARBIDE)
                .define('b', Items.STICK)
                .unlockedBy(
                    getHasName(ModItems.CEMENTED_CARBIDE_INGOT),
                    has(ModItems.CEMENTED_CARBIDE_INGOT)
                )
                .save(writer, loc(getItemName(ModItems.CEMENTED_CARBIDE_SHOVEL)))
            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.CEMENTED_CARBIDE_HOE)
                .pattern("aa")
                .pattern(" b")
                .pattern(" b")
                .define('a', ModTags.Items.INGOTS_CEMENTED_CARBIDE)
                .define('b', Items.STICK)
                .unlockedBy(
                    getHasName(ModItems.CEMENTED_CARBIDE_INGOT),
                    has(ModItems.CEMENTED_CARBIDE_INGOT)
                )
                .save(writer, loc(getItemName(ModItems.CEMENTED_CARBIDE_HOE)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.CROWBAR)
                .pattern("  a")
                .pattern(" b ")
                .pattern("b  ")
                .define('a', INGOTS_STEEL)
                .define('b', ModTags.Items.INGOTS_IRON)
                .unlockedBy(getHasName(ModItems.STEEL_INGOT), has(INGOTS_STEEL))
                .save(writer, loc(getItemName(ModItems.CROWBAR)))
            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.MILITARY_SHOVEL)
                .pattern(" aa")
                .pattern(" ba")
                .pattern("a  ")
                .define('a', INGOTS_STEEL)
                .define('b', ModTags.Items.INGOTS_CEMENTED_CARBIDE)
                .unlockedBy(getHasName(ModItems.STEEL_INGOT), has(INGOTS_STEEL))
                .save(writer, loc(getItemName(ModItems.MILITARY_SHOVEL)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.DEFUSER)
                .pattern("  a")
                .pattern("cb ")
                .pattern(" c ")
                .define('a', INGOTS_STEEL)
                .define('b', ModTags.Items.NUGGETS_IRON)
                .define('c', Items.STICK)
                .unlockedBy(getHasName(ModItems.STEEL_INGOT), has(INGOTS_STEEL))
                .save(writer, loc(getItemName(ModItems.DEFUSER)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.DETONATOR)
                .pattern(" a")
                .pattern("bc")
                .define('a', Items.REDSTONE_TORCH)
                .define('b', Items.STONE_BUTTON)
                .define('c', PLATES_PLASTIC)
                .unlockedBy(getHasName(Items.REDSTONE_TORCH), has(Items.REDSTONE_TORCH))
                .save(writer, loc(getItemName(ModItems.DETONATOR)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.ELECTRIC_BATON)
                .pattern("  a")
                .pattern(" b ")
                .pattern("c  ")
                .define('a', Items.LIGHTNING_ROD)
                .define('b', ModItems.BATTERY)
                .define('c', INGOTS_STEEL)
                .unlockedBy(getHasName(Items.LIGHTNING_ROD), has(Items.LIGHTNING_ROD))
                .unlockedBy(getHasName(ModItems.BATTERY), has(ModItems.BATTERY))
                .save(writer, loc(getItemName(ModItems.ELECTRIC_BATON)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.KNIFE)
                .pattern(" a")
                .pattern("b ")
                .define('a', INGOTS_STEEL)
                .define('b', Items.STICK)
                .unlockedBy(getHasName(ModItems.STEEL_INGOT), has(INGOTS_STEEL))
                .save(writer, loc(getItemName(ModItems.KNIFE)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MONITOR)
                .pattern("a a")
                .pattern("beb")
                .pattern("dcd")
                .define('a', Items.LIGHTNING_ROD)
                .define('b', Items.LEVER)
                .define('c', PLATES_PLASTIC)
                .define('d', Items.AMETHYST_SHARD)
                .define('e', ModTags.Items.GLASS_PANES)
                .unlockedBy(getHasName(Items.LIGHTNING_ROD), has(Items.LIGHTNING_ROD))
                .save(writer, loc(getItemName(ModItems.MONITOR)))
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.MONITOR)
                .requires(ModItems.MONITOR)
                .unlockedBy(getHasName(ModItems.MONITOR), has(ModItems.MONITOR))
                .save(writer, loc(getItemName(ModItems.MONITOR) + "_clear"))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MORTAR_DEPLOYER)
                .pattern("a ")
                .pattern("bc")
                .define('a', ModItems.MORTAR_BARREL)
                .define('b', ModItems.MORTAR_BIPOD)
                .define('c', ModItems.MORTAR_BASE_PLATE)
                .unlockedBy(getHasName(ModItems.MORTAR_BARREL), has(ModItems.MORTAR_BARREL))
                .unlockedBy(getHasName(ModItems.MORTAR_BIPOD), has(ModItems.MORTAR_BIPOD))
                .unlockedBy(getHasName(ModItems.MORTAR_BASE_PLATE), has(ModItems.MORTAR_BASE_PLATE))
                .save(writer, loc(getItemName(ModItems.MORTAR_DEPLOYER)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.T_BATON)
                .pattern("  a")
                .pattern(" a ")
                .pattern("ab ")
                .define('a', ModTags.Items.INGOTS_IRON)
                .define('b', INGOTS_STEEL)
                .unlockedBy(getHasName(ModItems.STEEL_INGOT), has(INGOTS_STEEL))
                .save(writer, loc(getItemName(ModItems.T_BATON)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.DPS_GENERATOR_DEPLOYER)
                .pattern("a")
                .pattern("b")
                .pattern("c")
                .define('a', ModItems.TARGET_DEPLOYER)
                .define('b', ModItems.LARGE_MOTOR)
                .define('c', ModItems.CHARGING_STATION)
                .unlockedBy(getHasName(ModItems.CHARGING_STATION), has(ModItems.CHARGING_STATION))
                .save(writer, loc(getItemName(ModItems.DPS_GENERATOR_DEPLOYER)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.TARGET_DEPLOYER)
                .pattern("a")
                .pattern("b")
                .pattern("c")
                .define('a', Items.TARGET)
                .define('b', PLATES_STEEL)
                .define('c', Items.ARMOR_STAND)
                .unlockedBy(getHasName(Items.TARGET), has(Items.TARGET))
                .save(writer, loc(getItemName(ModItems.TARGET_DEPLOYER)))
        }

        private fun buildArmorRecipes(writer: Consumer<FinishedRecipe>) {
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.GE_HELMET_M_35)
                .pattern("aaa")
                .pattern("aba")
                .define('a', INGOTS_STEEL)
                .define('b', ModTags.Items.DYES_BLACK)
                .unlockedBy(getHasName(ModItems.STEEL_INGOT), has(INGOTS_STEEL))
                .save(writer, loc(getItemName(ModItems.GE_HELMET_M_35)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.RU_HELMET_6B47)
                .pattern("aca")
                .pattern("aba")
                .define('a', ModTags.Items.INGOTS_CEMENTED_CARBIDE)
                .define('b', ModTags.Items.DYES_GREEN)
                .define('c', ModItems.CEMENTED_CARBIDE_INGOT)
                .unlockedBy(
                    getHasName(ModItems.CEMENTED_CARBIDE_INGOT),
                    has(ModItems.CEMENTED_CARBIDE_INGOT)
                )
                .save(writer, loc(getItemName(ModItems.RU_HELMET_6B47)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.RU_CHEST_6B43)
                .pattern("aba")
                .pattern("aca")
                .pattern("aaa")
                .define('a', ModTags.Items.INGOTS_CEMENTED_CARBIDE)
                .define('b', ModTags.Items.DYES_GREEN)
                .define('c', ModItems.CEMENTED_CARBIDE_INGOT)
                .unlockedBy(
                    getHasName(ModItems.CEMENTED_CARBIDE_INGOT),
                    has(ModItems.CEMENTED_CARBIDE_INGOT)
                )
                .save(writer, loc(getItemName(ModItems.RU_CHEST_6B43)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.US_HELMET_PASGT)
                .pattern("aca")
                .pattern("aba")
                .define('a', ModTags.Items.INGOTS_CEMENTED_CARBIDE)
                .define('b', ModTags.Items.SANDS)
                .define('c', ModItems.CEMENTED_CARBIDE_INGOT)
                .unlockedBy(
                    getHasName(ModItems.CEMENTED_CARBIDE_INGOT),
                    has(ModItems.CEMENTED_CARBIDE_INGOT)
                )
                .save(writer, loc(getItemName(ModItems.US_HELMET_PASGT)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.US_CHEST_IOTV)
                .pattern("aba")
                .pattern("aca")
                .pattern("aaa")
                .define('a', ModTags.Items.INGOTS_CEMENTED_CARBIDE)
                .define('b', ModTags.Items.SANDS)
                .define('c', ModItems.CEMENTED_CARBIDE_INGOT)
                .unlockedBy(
                    getHasName(ModItems.CEMENTED_CARBIDE_INGOT),
                    has(ModItems.CEMENTED_CARBIDE_INGOT)
                )
                .save(writer, loc(getItemName(ModItems.US_CHEST_IOTV)))

            VehicleAssemblingRecipeBuilder.item(
                ModItems.HANDSOME_GOGGLES,
                1,
                VehicleAssemblingRecipe.Category.AIRCRAFT
            )
                .require(Items.DISPENSER, 8)
                .require(Items.IRON_TRAPDOOR, 8)
                .require(ModItems.LIGHT_ARMAMENT_MODULE, 2)
                .require(ModItems.HEAVY_ARMAMENT_MODULE)
                .require(ModItems.MEDIUM_BATTERY_PACK)
                .require(Items.LEVER, 2)
                .require(INGOTS_STEEL, 4)
                .require(commonItemTag("stained_glass_panes"), 3)
                .unlockedBy(getHasName(ModItems.HEAVY_ARMAMENT_MODULE), has(ModItems.HEAVY_ARMAMENT_MODULE))
                .save(writer, loc(getItemName(ModItems.HANDSOME_GOGGLES) + "_assembling"))
        }

        private fun buildAmmoRecipes(writer: Consumer<FinishedRecipe>) {
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.AMMO_BOX)
                .pattern("aba")
                .pattern("aaa")
                .define('a', ModTags.Items.INGOTS_IRON)
                .define('b', ModTags.Items.DYES_GREEN)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(writer, loc(getItemName(ModItems.AMMO_BOX)))
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
                .save(writer, loc(getItemName(ModItems.LARGE_ANTI_GROUND_MISSILE)))

            ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.EXTRA_LARGE_ANTI_GROUND_MISSILE)
                .requires(ModItems.LARGE_ANTI_GROUND_MISSILE, 2)
                .unlockedBy(
                    getHasName(ModItems.LARGE_ANTI_GROUND_MISSILE),
                    has(ModItems.LARGE_ANTI_GROUND_MISSILE)
                )
                .save(writer, loc(getItemName(ModItems.EXTRA_LARGE_ANTI_GROUND_MISSILE)))

            ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.LARGE_ANTI_GROUND_MISSILE, 2)
                .requires(ModItems.EXTRA_LARGE_ANTI_GROUND_MISSILE)
                .unlockedBy(
                    getHasName(ModItems.LARGE_ANTI_GROUND_MISSILE),
                    has(ModItems.LARGE_ANTI_GROUND_MISSILE)
                )
                .save(
                    writer,
                    loc(getItemName(ModItems.LARGE_ANTI_GROUND_MISSILE) + "_from_extra_large_anti_ground_missile")
                )

            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SMALL_ROCKET, 4)
                .pattern(" a ")
                .pattern("bcb")
                .pattern(" d ")
                .define('a', ModItems.FUSEE)
                .define('b', Items.COPPER_INGOT)
                .define('c', ModItems.HIGH_ENERGY_EXPLOSIVES)
                .define('d', ModItems.GRAIN)
                .unlockedBy(getHasName(ModItems.FUSEE), has(ModItems.FUSEE))
                .save(writer, loc(getItemName(ModItems.SMALL_ROCKET)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.RPG_ROCKET_TBG, 2)
                .pattern(" a ")
                .pattern("bcb")
                .pattern(" d ")
                .define('a', ModItems.FUSEE)
                .define('b', Items.IRON_INGOT)
                .define('c', ModItems.HIGH_ENERGY_EXPLOSIVES)
                .define('d', ModItems.GRAIN)
                .unlockedBy(getHasName(ModItems.FUSEE), has(ModItems.FUSEE))
                .save(writer, loc(getItemName(ModItems.RPG_ROCKET_TBG)))
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
                .save(writer, loc(getItemName(ModItems.RPG_ROCKET_STANDARD)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.C4_BOMB, 2)
                .pattern("aaa")
                .pattern("aba")
                .pattern("aaa")
                .define('a', ModItems.HIGH_ENERGY_EXPLOSIVES)
                .define('b', Items.CLOCK)
                .unlockedBy(
                    getHasName(ModItems.HIGH_ENERGY_EXPLOSIVES),
                    has(ModItems.HIGH_ENERGY_EXPLOSIVES)
                )
                .save(writer, loc(getItemName(ModItems.C4_BOMB)))
            // Remote-controlled C4: custom recipe type (see C4BombRcRecipe)
            val rcId = loc("c4_bomb_rc")
            writer.accept(object : FinishedRecipe {
                private val advancement: Advancement.Builder =
                    Advancement.Builder.recipeAdvancement()
                        .addCriterion("has_high_energy_explosives", has(ModItems.HIGH_ENERGY_EXPLOSIVES))
                        .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(rcId))
                        .requirements(RequirementsStrategy.OR)

                override fun serializeRecipeData(json: JsonObject) {
                    json.addProperty("type", "superbwarfare:c4_bomb_rc")
                    json.addProperty("category", "equipment")
                }

                override fun getId(): ResourceLocation = rcId

                override fun getType(): RecipeSerializer<*> = ModRecipes.C4_BOMB_RC_SERIALIZER

                override fun serializeAdvancement(): JsonObject = advancement.serializeToJson()

                override fun getAdvancementId(): ResourceLocation = loc("recipes/combat/c4_bomb_rc")
            })
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.LARGE_SHELL_AP)
                .pattern("a")
                .pattern("b")
                .define('a', ModItems.AP_HEAD)
                .define('b', ModItems.GRAIN)
                .unlockedBy(getHasName(ModItems.AP_HEAD), has(ModItems.AP_HEAD))
                .save(writer, loc(getItemName(ModItems.LARGE_SHELL_AP)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.BLU_43_MINE, 8)
                .pattern("a")
                .pattern("b")
                .pattern("c")
                .define('a', Items.STONE_PRESSURE_PLATE)
                .define('b', ModItems.HIGH_ENERGY_EXPLOSIVES)
                .define('c', PLATES_PLASTIC)
                .unlockedBy(
                    getHasName(ModItems.HIGH_ENERGY_EXPLOSIVES),
                    has(ModItems.HIGH_ENERGY_EXPLOSIVES)
                )
                .save(writer, loc(getItemName(ModItems.BLU_43_MINE)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.CLAYMORE_MINE, 2)
                .pattern(" a ")
                .pattern("bcb")
                .pattern("d d")
                .define('a', Items.TRIPWIRE_HOOK)
                .define('b', ModTags.Items.INGOTS_IRON)
                .define('c', ModItems.HIGH_ENERGY_EXPLOSIVES)
                .define('d', Items.STICK)
                .unlockedBy(
                    getHasName(ModItems.HIGH_ENERGY_EXPLOSIVES),
                    has(ModItems.HIGH_ENERGY_EXPLOSIVES)
                )
                .save(writer, loc(getItemName(ModItems.CLAYMORE_MINE)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.EDD, 4)
                .pattern(" a ")
                .pattern("bcb")
                .pattern(" d ")
                .define('a', ModItems.LASER_UNIT)
                .define('b', ModTags.Items.INGOTS_IRON)
                .define('c', ModItems.HIGH_ENERGY_EXPLOSIVES)
                .define('d', ItemTags.PLANKS)
                .unlockedBy(
                    getHasName(ModItems.LASER_UNIT),
                    has(ModItems.LASER_UNIT)
                )
                .save(writer, loc(getItemName(ModItems.EDD)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.LARGE_SHELL_CM)
                .pattern("a")
                .pattern("b")
                .define('a', ModItems.CM_HEAD)
                .define('b', ModItems.GRAIN)
                .unlockedBy(getHasName(ModItems.CM_HEAD), has(ModItems.CM_HEAD))
                .save(writer, loc(getItemName(ModItems.LARGE_SHELL_CM)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.GRENADE_40MM, 6)
                .pattern(" a ")
                .pattern("bcb")
                .pattern(" d ")
                .define('a', ModItems.FUSEE)
                .define('b', ModTags.Items.INGOTS_IRON)
                .define('c', ModItems.HIGH_ENERGY_EXPLOSIVES)
                .define('d', ModItems.PRIMER)
                .unlockedBy(
                    getHasName(ModItems.HIGH_ENERGY_EXPLOSIVES),
                    has(ModItems.HIGH_ENERGY_EXPLOSIVES)
                )
                .unlockedBy(getHasName(ModItems.FUSEE), has(ModItems.FUSEE))
                .save(writer, loc(getItemName(ModItems.GRENADE_40MM)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.LARGE_SHELL_GS)
                .pattern("a")
                .pattern("b")
                .define('a', ModItems.GS_HEAD)
                .define('b', ModItems.GRAIN)

                .unlockedBy(getHasName(ModItems.GS_HEAD), has(ModItems.GS_HEAD))
                .save(writer, loc(getItemName(ModItems.LARGE_SHELL_GS)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.LARGE_SHELL_WP)
                .pattern("a")
                .pattern("b")
                .define('a', ModItems.WP_HEAD)
                .define('b', ModItems.GRAIN)
                .unlockedBy(getHasName(ModItems.WP_HEAD), has(ModItems.WP_HEAD))
                .save(writer, loc(getItemName(ModItems.LARGE_SHELL_WP)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.HAND_GRENADE, 4)
                .pattern(" a ")
                .pattern("bcb")
                .pattern("bcb")
                .define('a', Items.TRIPWIRE_HOOK)
                .define('b', ModTags.Items.INGOTS_IRON)
                .define('c', ModItems.HIGH_ENERGY_EXPLOSIVES)
                .unlockedBy(
                    getHasName(ModItems.HIGH_ENERGY_EXPLOSIVES),
                    has(ModItems.HIGH_ENERGY_EXPLOSIVES)
                )
                .save(writer, loc(getItemName(ModItems.HAND_GRENADE)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.HANDGUN_AMMO, 64)
                .pattern(" a ")
                .pattern("bcb")
                .pattern(" d ")
                .define('a', ModTags.Items.INGOTS_COPPER)
                .define('b', PLATES_COPPER)
                .define('c', Items.GUNPOWDER)
                .define('d', ModItems.PRIMER)
                .unlockedBy(getHasName(ModItems.PRIMER), has(ModItems.PRIMER))
                .save(writer, loc(getItemName(ModItems.HANDGUN_AMMO)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.LARGE_SHELL_HE)
                .pattern("a")
                .pattern("b")
                .define('a', ModItems.HE_HEAD)
                .define('b', ModItems.GRAIN)
                .unlockedBy(getHasName(ModItems.HE_HEAD), has(ModItems.HE_HEAD))
                .save(writer, loc(getItemName(ModItems.LARGE_SHELL_HE)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.HEAVY_AMMO, 12)
                .pattern(" a ")
                .pattern("bcb")
                .pattern(" d ")
                .define('a', INGOTS_STEEL)
                .define('b', ModTags.Items.INGOTS_COPPER)
                .define('c', Items.GUNPOWDER)
                .define('d', ModItems.PRIMER)
                .unlockedBy(getHasName(ModItems.PRIMER), has(ModItems.PRIMER))
                .save(writer, loc(getItemName(ModItems.HEAVY_AMMO)))
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
                .save(writer, loc(getItemName(ModItems.JAVELIN_MISSILE)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.MEDIUM_ANTI_AIR_MISSILE)
                .pattern("eae")
                .pattern("bcb")
                .pattern(" d ")
                .define('a', ModItems.SEEKER)
                .define('b', ModTags.Items.INGOTS_IRON)
                .define('c', ModItems.HIGH_ENERGY_EXPLOSIVES)
                .define('d', ModItems.MISSILE_ENGINE)
                .define('e', Items.IRON_BARS)
                .unlockedBy(
                    getHasName(ModItems.HIGH_ENERGY_EXPLOSIVES),
                    has(ModItems.HIGH_ENERGY_EXPLOSIVES)
                )
                .unlockedBy(getHasName(ModItems.MISSILE_ENGINE), has(ModItems.MISSILE_ENGINE))
                .save(writer, loc(getItemName(ModItems.MEDIUM_ANTI_AIR_MISSILE)))

            ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.LARGE_ANTI_AIR_MISSILE)
                .requires(ModItems.MEDIUM_ANTI_AIR_MISSILE, 2)
                .unlockedBy(
                    getHasName(ModItems.MEDIUM_ANTI_AIR_MISSILE),
                    has(ModItems.MEDIUM_ANTI_AIR_MISSILE)
                )
                .save(writer, loc(getItemName(ModItems.LARGE_ANTI_AIR_MISSILE)))

            ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.MEDIUM_ANTI_AIR_MISSILE, 2)
                .requires(ModItems.LARGE_ANTI_AIR_MISSILE)
                .unlockedBy(
                    getHasName(ModItems.MEDIUM_ANTI_AIR_MISSILE),
                    has(ModItems.MEDIUM_ANTI_AIR_MISSILE)
                )
                .save(writer, loc(getItemName(ModItems.MEDIUM_ANTI_AIR_MISSILE) + "_from_large_anti_air_missile"))

            ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.MEDIUM_ANTI_GROUND_MISSILE)
                .requires(ModItems.JAVELIN_MISSILE)
                .unlockedBy(getHasName(ModItems.JAVELIN_MISSILE), has(ModItems.JAVELIN_MISSILE))
                .save(writer, loc(getItemName(ModItems.MEDIUM_ANTI_GROUND_MISSILE)))

            ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.JAVELIN_MISSILE)
                .requires(ModItems.MEDIUM_ANTI_GROUND_MISSILE)
                .unlockedBy(
                    getHasName(ModItems.MEDIUM_ANTI_GROUND_MISSILE),
                    has(ModItems.MEDIUM_ANTI_GROUND_MISSILE)
                )
                .save(writer, loc(getItemName(ModItems.JAVELIN_MISSILE) + "_convert"))

            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.LUNGE_MINE, 2)
                .pattern(" ba")
                .pattern(" cb")
                .pattern("c  ")
                .define('a', Items.TNT)
                .define('b', ModItems.HIGH_ENERGY_EXPLOSIVES)
                .define('c', Items.STICK)
                .unlockedBy(
                    getHasName(ModItems.HIGH_ENERGY_EXPLOSIVES),
                    has(ModItems.HIGH_ENERGY_EXPLOSIVES)
                )
                .save(writer, loc(getItemName(ModItems.LUNGE_MINE)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.M18_SMOKE_GRENADE, 2)
                .pattern(" a ")
                .pattern("bcb")
                .pattern("bdb")
                .define('a', Items.TRIPWIRE_HOOK)
                .define('b', ModTags.Items.NUGGETS_IRON)
                .define('c', Items.WHEAT)
                .define('d', Items.GUNPOWDER)
                .unlockedBy(getHasName(Items.TRIPWIRE_HOOK), has(Items.TRIPWIRE_HOOK))
                .save(writer, loc(getItemName(ModItems.M18_SMOKE_GRENADE)))

            // vehicle_smoke_ammo <-> m18_smoke_grenade
            ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.VEHICLE_SMOKE_AMMO, 1)
                .requires(ModItems.M18_SMOKE_GRENADE, 1)
                .unlockedBy(
                    getHasName(ModItems.M18_SMOKE_GRENADE),
                    has(ModItems.M18_SMOKE_GRENADE)
                )
                .save(writer, loc(getItemName(ModItems.VEHICLE_SMOKE_AMMO) + "_from_m18_smoke_grenade"))
            ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.M18_SMOKE_GRENADE, 1)
                .requires(ModItems.VEHICLE_SMOKE_AMMO, 1)
                .unlockedBy(
                    getHasName(ModItems.VEHICLE_SMOKE_AMMO),
                    has(ModItems.VEHICLE_SMOKE_AMMO)
                )
                .save(writer, loc(getItemName(ModItems.M18_SMOKE_GRENADE) + "_from_vehicle_smoke_ammo"))
            ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.FLYING_FLARE_AMMO, 16)
                .requires(Items.BLAZE_POWDER)
                .requires(Items.GUNPOWDER)
                .requires(commonItemTag("dusts/iron"))
                .unlockedBy(getHasName(Items.BLAZE_POWDER), has(Items.BLAZE_POWDER))
                .save(writer, loc(getItemName(ModItems.FLYING_FLARE_AMMO)))

            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.MEDIUM_AERIAL_BOMB)
                .pattern(" c ")
                .pattern("dad")
                .pattern(" b ")
                .define('a', Items.TNT)
                .define('b', ModTags.Items.INGOTS_IRON)
                .define('c', ModItems.FUSEE)
                .define('d', ModItems.HIGH_ENERGY_EXPLOSIVES)
                .unlockedBy(getHasName(ModItems.FUSEE), has(ModItems.FUSEE))
                .unlockedBy(
                    getHasName(ModItems.HIGH_ENERGY_EXPLOSIVES),
                    has(ModItems.HIGH_ENERGY_EXPLOSIVES)
                )
                .save(writer, loc(getItemName(ModItems.MEDIUM_AERIAL_BOMB)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.MEDIUM_ROCKET_AP)
                .pattern("a")
                .pattern("b")
                .pattern("b")
                .define('a', ModItems.AP_HEAD)
                .define('b', ModItems.SMALL_ROCKET)
                .unlockedBy(getHasName(ModItems.AP_HEAD), has(ModItems.AP_HEAD))
                .save(writer, loc(getItemName(ModItems.MEDIUM_ROCKET_AP)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.MEDIUM_ROCKET_CM)
                .pattern("a")
                .pattern("b")
                .pattern("b")
                .define('a', ModItems.CM_HEAD)
                .define('b', ModItems.SMALL_ROCKET)
                .unlockedBy(getHasName(ModItems.CM_HEAD), has(ModItems.CM_HEAD))
                .save(writer, loc(getItemName(ModItems.MEDIUM_ROCKET_CM)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.MEDIUM_ROCKET_HE)
                .pattern("a")
                .pattern("b")
                .pattern("b")
                .define('a', ModItems.HE_HEAD)
                .define('b', ModItems.SMALL_ROCKET)
                .unlockedBy(getHasName(ModItems.HE_HEAD), has(ModItems.HE_HEAD))
                .save(writer, loc(getItemName(ModItems.MEDIUM_ROCKET_HE)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.MORTAR_SHELL, 8)
                .pattern(" a ")
                .pattern("bcb")
                .pattern(" d ")
                .define('a', ModItems.FUSEE)
                .define('b', INGOTS_STEEL)
                .define('c', ModItems.HIGH_ENERGY_EXPLOSIVES)
                .define('d', ModItems.GRAIN)
                .unlockedBy(
                    getHasName(ModItems.HIGH_ENERGY_EXPLOSIVES),
                    has(ModItems.HIGH_ENERGY_EXPLOSIVES)
                )
                .unlockedBy(getHasName(ModItems.GRAIN), has(ModItems.GRAIN))
                .save(writer, loc(getItemName(ModItems.MORTAR_SHELL)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.MORTAR_SHELL_WP, 8)
                .pattern("eaf")
                .pattern("bcb")
                .pattern("fde")
                .define('a', ModItems.FUSEE)
                .define('b', INGOTS_STEEL)
                .define('c', Items.GUNPOWDER)
                .define('d', ModItems.GRAIN)
                .define('e', Items.BLAZE_POWDER)
                .define('f', Items.BONE_MEAL)
                .unlockedBy(
                    getHasName(Items.BLAZE_POWDER),
                    has(Items.BLAZE_POWDER)
                )
                .unlockedBy(getHasName(ModItems.GRAIN), has(ModItems.GRAIN))
                .save(writer, loc(getItemName(ModItems.MORTAR_SHELL_WP)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.MORTAR_SHELL_SMOKE, 8)
                .pattern(" a ")
                .pattern("bcb")
                .pattern(" d ")
                .define('a', ModItems.FUSEE)
                .define('b', INGOTS_STEEL)
                .define('c', Items.WHEAT)
                .define('d', ModItems.GRAIN)
                .unlockedBy(getHasName(Items.WHEAT), has(Items.WHEAT))
                .unlockedBy(getHasName(ModItems.GRAIN), has(ModItems.GRAIN))
                .save(writer, loc(getItemName(ModItems.MORTAR_SHELL_SMOKE)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.PTKM_1R)
                .pattern(" b ")
                .pattern("dad")
                .pattern("ece")
                .define('a', Items.GUNPOWDER)
                .define('b', ModItems.LARGE_SHELL_AP)
                .define('c', Items.CALIBRATED_SCULK_SENSOR)
                .define('d', ModTags.Items.INGOTS_IRON)
                .define('e', Items.IRON_BARS)
                .unlockedBy(getHasName(ModItems.LARGE_SHELL_AP), has(ModItems.LARGE_SHELL_AP))
                .unlockedBy(getHasName(Items.CALIBRATED_SCULK_SENSOR), has(Items.CALIBRATED_SCULK_SENSOR))
                .save(writer, loc(getItemName(ModItems.PTKM_1R)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.RGO_GRENADE, 4)
                .pattern("abc")
                .pattern("aba")
                .pattern(" da")
                .define('a', ModTags.Items.INGOTS_IRON)
                .define('b', ModItems.HIGH_ENERGY_EXPLOSIVES)
                .define('c', Items.TRIPWIRE_HOOK)
                .define('d', Items.STONE_BUTTON)
                .unlockedBy(
                    getHasName(ModItems.HIGH_ENERGY_EXPLOSIVES),
                    has(ModItems.HIGH_ENERGY_EXPLOSIVES)
                )
                .save(writer, loc(getItemName(ModItems.RGO_GRENADE)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.RIFLE_AMMO, 48)
                .pattern(" a ")
                .pattern("bcb")
                .pattern(" d ")
                .define('a', INGOTS_STEEL)
                .define('b', PLATES_COPPER)
                .define('c', Items.GUNPOWDER)
                .define('d', ModItems.PRIMER)
                .unlockedBy(getHasName(ModItems.PRIMER), has(ModItems.PRIMER))
                .save(writer, loc(getItemName(ModItems.RIFLE_AMMO)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SHOTGUN_AMMO, 24)
                .pattern(" a ")
                .pattern("bcb")
                .pattern(" d ")
                .define('a', INGOTS_LEAD)
                .define('b', PLATES_COPPER)
                .define('c', Items.GUNPOWDER)
                .define('d', ModItems.PRIMER)
                .unlockedBy(getHasName(ModItems.PRIMER), has(ModItems.PRIMER))
                .save(writer, loc(getItemName(ModItems.SHOTGUN_AMMO)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SNIPER_AMMO, 16)
                .pattern(" a ")
                .pattern("bcb")
                .pattern(" d ")
                .define('a', INGOTS_TUNGSTEN)
                .define('b', PLATES_COPPER)
                .define('c', Items.GUNPOWDER)
                .define('d', ModItems.PRIMER)
                .unlockedBy(getHasName(ModItems.PRIMER), has(ModItems.PRIMER))
                .save(writer, loc(getItemName(ModItems.SNIPER_AMMO)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SMALL_SHELL_AP, 8)
                .pattern(" a ")
                .pattern("bcb")
                .pattern(" d ")
                .define('a', ModItems.TUNGSTEN_ROD)
                .define('b', ModTags.Items.INGOTS_COPPER)
                .define('c', Items.GUNPOWDER)
                .define('d', ModItems.PRIMER)
                .unlockedBy(getHasName(ModItems.PRIMER), has(ModItems.PRIMER))
                .unlockedBy(getHasName(ModItems.TUNGSTEN_ROD), has(ModItems.TUNGSTEN_ROD))
                .save(writer, loc(getItemName(ModItems.SMALL_SHELL_AP)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SMALL_SHELL_HE, 8)
                .pattern(" a ")
                .pattern("bcb")
                .pattern(" d ")
                .define('a', INGOTS_STEEL)
                .define('b', ModTags.Items.INGOTS_COPPER)
                .define('c', ModItems.HIGH_ENERGY_EXPLOSIVES)
                .define('d', ModItems.PRIMER)
                .unlockedBy(getHasName(ModItems.PRIMER), has(ModItems.PRIMER))
                .unlockedBy(
                    getHasName(ModItems.HIGH_ENERGY_EXPLOSIVES),
                    has(ModItems.HIGH_ENERGY_EXPLOSIVES)
                )
                .save(writer, loc(getItemName(ModItems.SMALL_SHELL_HE)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SMALL_SHELL_GS, 12)
                .pattern(" a ")
                .pattern("bcb")
                .pattern(" d ")
                .define('a', INGOTS_LEAD)
                .define('b', ModTags.Items.INGOTS_COPPER)
                .define('c', Items.GUNPOWDER)
                .define('d', ModItems.PRIMER)
                .unlockedBy(getHasName(ModItems.PRIMER), has(ModItems.PRIMER))
                .unlockedBy(getHasName(ModItems.LEAD_INGOT), has(ModItems.LEAD_INGOT))
                .save(writer, loc(getItemName(ModItems.SMALL_SHELL_GS)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SMALL_SHELL_AA, 16)
                .pattern(" a ")
                .pattern("bcb")
                .pattern(" d ")
                .define('a', Items.IRON_INGOT)
                .define('b', ModTags.Items.INGOTS_COPPER)
                .define('c', Items.GUNPOWDER)
                .define('d', ModItems.PRIMER)
                .unlockedBy(getHasName(ModItems.PRIMER), has(ModItems.PRIMER))
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(writer, loc(getItemName(ModItems.SMALL_SHELL_AA)))

            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.MEDIUM_SHELL_AP, 4)
                .pattern(" a ")
                .pattern("bcb")
                .pattern(" d ")
                .define('a', ModItems.TUNGSTEN_ROD)
                .define('b', INGOTS_STEEL)
                .define('c', Items.GUNPOWDER)
                .define('d', ModItems.PRIMER)
                .unlockedBy(getHasName(ModItems.PRIMER), has(ModItems.PRIMER))
                .unlockedBy(getHasName(ModItems.TUNGSTEN_ROD), has(ModItems.TUNGSTEN_ROD))
                .save(writer, loc(getItemName(ModItems.MEDIUM_SHELL_AP)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.MEDIUM_SHELL_HE, 4)
                .pattern(" a ")
                .pattern("aca")
                .pattern(" d ")
                .define('a', INGOTS_STEEL)
                .define('c', ModItems.HIGH_ENERGY_EXPLOSIVES)
                .define('d', ModItems.PRIMER)
                .unlockedBy(getHasName(ModItems.PRIMER), has(ModItems.PRIMER))
                .unlockedBy(
                    getHasName(ModItems.HIGH_ENERGY_EXPLOSIVES),
                    has(ModItems.HIGH_ENERGY_EXPLOSIVES)
                )
                .save(writer, loc(getItemName(ModItems.MEDIUM_SHELL_HE)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.MEDIUM_SHELL_GS, 6)
                .pattern(" a ")
                .pattern("bcb")
                .pattern(" d ")
                .define('a', INGOTS_LEAD)
                .define('b', INGOTS_STEEL)
                .define('c', Items.GUNPOWDER)
                .define('d', ModItems.PRIMER)
                .unlockedBy(getHasName(ModItems.PRIMER), has(ModItems.PRIMER))
                .unlockedBy(getHasName(ModItems.LEAD_INGOT), has(ModItems.LEAD_INGOT))
                .save(writer, loc(getItemName(ModItems.MEDIUM_SHELL_GS)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.MEDIUM_SHELL_AA, 8)
                .pattern(" a ")
                .pattern("bcb")
                .pattern(" d ")
                .define('a', ModItems.FUSEE)
                .define('b', ModTags.Items.INGOTS_COPPER)
                .define('c', Items.GUNPOWDER)
                .define('d', ModItems.PRIMER)
                .unlockedBy(getHasName(ModItems.PRIMER), has(ModItems.PRIMER))
                .unlockedBy(getHasName(ModItems.FUSEE), has(ModItems.FUSEE))
                .save(writer, loc(getItemName(ModItems.MEDIUM_SHELL_AA)))

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
                .unlockedBy(
                    getHasName(ModItems.HIGH_ENERGY_EXPLOSIVES),
                    has(ModItems.HIGH_ENERGY_EXPLOSIVES)
                )
                .save(writer, loc(getItemName(ModItems.SWARM_DRONE)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.TASER_ELECTRODE, 4)
                .pattern("a a")
                .pattern("b b")
                .pattern("b b")
                .define('a', Items.LIGHTNING_ROD)
                .define('b', Items.STRING)
                .unlockedBy(getHasName(Items.LIGHTNING_ROD), has(Items.LIGHTNING_ROD))
                .save(writer, loc(getItemName(ModItems.TASER_ELECTRODE)))
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.TM_62, 2)
                .pattern("cac")
                .pattern("bbb")
                .pattern("bbb")
                .define('a', Items.STONE_PRESSURE_PLATE)
                .define('b', ModItems.HIGH_ENERGY_EXPLOSIVES)
                .define('c', Items.GREEN_CONCRETE)
                .unlockedBy(
                    getHasName(ModItems.HIGH_ENERGY_EXPLOSIVES),
                    has(ModItems.HIGH_ENERGY_EXPLOSIVES)
                )
                .save(writer, loc(getItemName(ModItems.TM_62)))
        }

        private fun buildMaterialRecipes(writer: Consumer<FinishedRecipe>) {
            generateMaterialRecipes(writer, ModItems.IRON_MATERIALS, Items.IRON_INGOT)
            generateMaterialRecipes(
                writer,
                ModItems.STEEL_MATERIALS,
                INGOTS_STEEL,
                ModItems.STEEL_INGOT
            )
            generateMaterialRecipes(
                writer,
                ModItems.CEMENTED_CARBIDE_MATERIALS,
                ModTags.Items.INGOTS_CEMENTED_CARBIDE,
                ModItems.CEMENTED_CARBIDE_INGOT
            )
            generateSmithingMaterialRecipe(
                writer,
                ModItems.CEMENTED_CARBIDE_MATERIALS,
                ModItems.NETHERITE_MATERIALS,
                Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
                Items.NETHERITE_INGOT
            )
            mapOf(
                ModItems.CRYSTAL_MATERIALS.action to ModItems.CEMENTED_CARBIDE_MATERIALS.action,
                ModItems.CRYSTAL_MATERIALS.barrel to ModItems.CEMENTED_CARBIDE_MATERIALS.barrel,
                ModItems.CRYSTAL_MATERIALS.trigger to ModItems.CEMENTED_CARBIDE_MATERIALS.trigger,
                ModItems.CRYSTAL_MATERIALS.spring to ModItems.CEMENTED_CARBIDE_MATERIALS.spring
            ).forEach { (cry, cem) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, cry)
                    .pattern(" C ")
                    .pattern("ABA")
                    .pattern(" C ")
                    .define('A', ModTags.Items.GEMS_AMETHYST)
                    .define('B', cem)
                    .define('C', ModTags.Items.GEMS_DIAMOND)
                    .unlockedBy(getHasName(cem), has(cem))
                    .unlockedBy(getHasName(Items.AMETHYST_SHARD), has(ModTags.Items.GEMS_AMETHYST))
                    .save(writer, loc(getItemName(cry)))
            }

            generateMaterialPackRecipe(writer, ModItems.IRON_MATERIALS, ModItems.COMMON_MATERIAL_PACK)
            generateMaterialPackRecipe(writer, ModItems.STEEL_MATERIALS, ModItems.RARE_MATERIAL_PACK)
            generateMaterialPackRecipe(writer, ModItems.CEMENTED_CARBIDE_MATERIALS, ModItems.EPIC_MATERIAL_PACK)
            generateMaterialPackRecipe(writer, ModItems.NETHERITE_MATERIALS, ModItems.LEGENDARY_MATERIAL_PACK)
            generateMaterialPackRecipe(writer, ModItems.CRYSTAL_MATERIALS, ModItems.VIRTUAL_MATERIAL_PACK)
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SUPERB_MATERIAL_PACK)
                .pattern(" A ")
                .pattern("BEC")
                .pattern(" D ")
                .define('A', ModItems.COMMON_MATERIAL_PACK)
                .define('B', ModItems.RARE_MATERIAL_PACK)
                .define('C', ModItems.EPIC_MATERIAL_PACK)
                .define('D', ModItems.LEGENDARY_MATERIAL_PACK)
                .define('E', Items.NETHER_STAR)
                .unlockedBy(getHasName(Items.NETHER_STAR), has(Items.NETHER_STAR))
                .save(writer, loc(getItemName(ModItems.SUPERB_MATERIAL_PACK)))

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ANCIENT_CPU)
                .pattern("bcb")
                .pattern("cac")
                .pattern("bcb")
                .define('a', ModItems.EMPTY_PERK)
                .define('b', ModTags.Items.GEMS_DIAMOND)
                .define('c', ModTags.Items.ORES_NETHERITE_SCRAP)
                .unlockedBy(getHasName(ModItems.EMPTY_PERK), has(ModItems.EMPTY_PERK))
                .save(writer, loc(getItemName(ModItems.ANCIENT_CPU)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.AP_HEAD, 2)
                .pattern(" e ")
                .pattern("bdb")
                .pattern(" a ")
                .define('a', Items.GUNPOWDER)
                .define('b', PLATES_STEEL)
                .define('d', ModItems.TUNGSTEN_ROD)
                .define('e', ModItems.FUSEE)
                .unlockedBy(getHasName(ModItems.ENGINEERING_PLASTIC), has(PLATES_STEEL))
                .save(writer, loc(getItemName(ModItems.AP_HEAD)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BATTERY)
                .pattern(" b ")
                .pattern("cac")
                .pattern(" d ")
                .define('a', ModTags.Items.DUSTS_REDSTONE)
                .define('b', PLATES_COPPER)
                .define('c', ModTags.Items.GLASS_PANES)
                .define('d', ModTags.Items.INGOTS_IRON)
                .unlockedBy(getHasName(Items.REDSTONE), has(Items.REDSTONE))
                .save(writer, loc(getItemName(ModItems.BATTERY)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BATTERY)
                .pattern("aa")
                .pattern("aa")
                .define('a', ModItems.CELL)
                .unlockedBy(getHasName(ModItems.CELL), has(ModItems.CELL))
                .save(writer, loc(getItemName(ModItems.BATTERY) + "_from_cell"))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MEDIUM_AERIAL_BOMB)
                .pattern("aa")
                .pattern("aa")
                .define('a', ModItems.SMALL_AERIAL_BOMB)
                .unlockedBy(getHasName(ModItems.SMALL_AERIAL_BOMB), has(ModItems.SMALL_AERIAL_BOMB))
                .save(writer, loc(getItemName(ModItems.MEDIUM_AERIAL_BOMB) + "_from_small_aerial_bomb"))
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SMALL_AERIAL_BOMB, 4)
                .requires(ModItems.MEDIUM_AERIAL_BOMB)
                .unlockedBy(
                    getHasName(ModItems.MEDIUM_AERIAL_BOMB),
                    has(ModItems.MEDIUM_AERIAL_BOMB)
                )
                .save(writer, loc(getItemName(ModItems.SMALL_AERIAL_BOMB) + "_from_medium_aerial_bomb"))

            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.LARGE_AERIAL_BOMB)
                .requires(ModItems.MEDIUM_AERIAL_BOMB, 2)
                .unlockedBy(
                    getHasName(ModItems.MEDIUM_AERIAL_BOMB),
                    has(ModItems.MEDIUM_AERIAL_BOMB)
                )
                .save(writer, loc(getItemName(ModItems.LARGE_AERIAL_BOMB)))

            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.MEDIUM_AERIAL_BOMB, 2)
                .requires(ModItems.LARGE_AERIAL_BOMB)
                .unlockedBy(
                    getHasName(ModItems.MEDIUM_AERIAL_BOMB),
                    has(ModItems.MEDIUM_AERIAL_BOMB)
                )
                .save(writer, loc(getItemName(ModItems.MEDIUM_AERIAL_BOMB) + "_from_large_aerial_bomb"))

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.CANNON_CORE)
                .pattern("aaa")
                .pattern("bcd")
                .pattern("aaa")
                .define('a', INGOTS_STEEL)
                .define('b', Items.DISPENSER)
                .define('c', ModItems.CEMENTED_CARBIDE_MATERIALS.action)
                .define('d', Items.PISTON)
                .unlockedBy(
                    getHasName(ModItems.CEMENTED_CARBIDE_MATERIALS.action),
                    has(ModItems.CEMENTED_CARBIDE_MATERIALS.action)
                )
                .save(writer, loc(getItemName(ModItems.CANNON_CORE)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.CELL)
                .pattern("a")
                .pattern("b")
                .pattern("c")
                .define('a', ModTags.Items.NUGGETS_GOLD)
                .define('b', ModTags.Items.DUSTS_REDSTONE)
                .define('c', ModTags.Items.NUGGETS_IRON)
                .unlockedBy(getHasName(Items.GOLD_NUGGET), has(Items.GOLD_NUGGET))
                .save(writer, loc(getItemName(ModItems.CELL)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.LASER_UNIT)
                .pattern("eae")
                .pattern("dbd")
                .pattern("dcd")
                .define('a', Items.AMETHYST_SHARD)
                .define('b', Items.DIAMOND)
                .define('c', Items.REDSTONE)
                .define('d', INGOTS_STEEL)
                .define('e', Items.COPPER_INGOT)
                .unlockedBy(getHasName(Items.REDSTONE), has(Items.REDSTONE))
                .save(writer, loc(getItemName(ModItems.LASER_UNIT)))
            SimpleCookingRecipeBuilder.generic(
                Ingredient.of(ModItems.RAW_CEMENTED_CARBIDE_POWDER),
                RecipeCategory.MISC,
                ModItems.CEMENTED_CARBIDE_INGOT,
                8f,
                200,
                RecipeSerializer.BLASTING_RECIPE
            )
                .unlockedBy(
                    getHasName(ModItems.RAW_CEMENTED_CARBIDE_POWDER),
                    has(ModItems.RAW_CEMENTED_CARBIDE_POWDER)
                )
                .save(writer, loc(getItemName(ModItems.CEMENTED_CARBIDE_INGOT) + "_blasting"))
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.CEMENTED_CARBIDE_INGOT, 9)
                .requires(ModItems.CEMENTED_CARBIDE_BLOCK)
                .unlockedBy(
                    getHasName(ModItems.CEMENTED_CARBIDE_BLOCK),
                    has(ModItems.CEMENTED_CARBIDE_BLOCK)
                )
                .save(writer, loc(getItemName(ModItems.CEMENTED_CARBIDE_INGOT) + "_from_block"))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.CM_HEAD, 2)
                .pattern("ddd")
                .pattern("bdb")
                .pattern(" a ")
                .define('a', Items.GUNPOWDER)
                .define('b', PLATES_STEEL)
                .define('d', ModItems.GRENADE_40MM)
                .unlockedBy(getHasName(ModItems.GRENADE_40MM), has(ModItems.GRENADE_40MM))
                .save(writer, loc(getItemName(ModItems.CM_HEAD)))
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.COAL_IRON_POWDER)
                .requires(commonItemTag("dusts/iron"))
                .requires(
                    DefaultCustomIngredients.any(
                        Ingredient.of(commonItemTag("dusts/coal_coke")),
                        Ingredient.of(commonItemTag("dusts/coal"))
                    )
                )
                .unlockedBy(getHasName(ModItems.IRON_POWDER), has(ModItems.IRON_POWDER))
                .unlockedBy(getHasName(ModItems.COAL_POWDER), has(ModItems.COAL_POWDER))
                .save(writer, loc(getItemName(ModItems.COAL_IRON_POWDER)))
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.COAL_POWDER)
                .requires(ItemTags.COALS)
                .requires(ModTags.Items.HAMMER)
                .unlockedBy(getHasName(ModItems.HAMMER), has(ModTags.Items.HAMMER))
                .save(writer, loc(getItemName(ModItems.COAL_POWDER)))
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.IRON_POWDER)
                .requires(ModTags.Items.INGOTS_IRON)
                .requires(ModTags.Items.HAMMER)
                .unlockedBy(getHasName(ModItems.HAMMER), has(ModTags.Items.HAMMER))
                .save(writer, loc(getItemName(ModItems.IRON_POWDER)))
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.COPPER_PLATE)
                .requires(ModTags.Items.INGOTS_COPPER)
                .requires(ModTags.Items.HAMMER)
                .unlockedBy(getHasName(ModItems.HAMMER), has(ModTags.Items.HAMMER))
                .save(writer, loc(getItemName(ModItems.COPPER_PLATE)))
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.STEEL_PLATE)
                .requires(INGOTS_STEEL)
                .requires(ModTags.Items.HAMMER)
                .unlockedBy(getHasName(ModItems.HAMMER), has(ModTags.Items.HAMMER))
                .save(writer, loc(getItemName(ModItems.STEEL_PLATE)))
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SLIME_COVERED_LEATHER)
                .requires(Items.LEATHER)
                .requires(Items.SLIME_BALL)
                .requires(Items.BLAZE_POWDER)
                .unlockedBy(getHasName(Items.SLIME_BALL), has(Items.SLIME_BALL))
                .save(writer, loc(getItemName(ModItems.SLIME_COVERED_LEATHER)))
            SimpleCookingRecipeBuilder.generic(
                Ingredient.of(ModItems.SLIME_COVERED_LEATHER),
                RecipeCategory.MISC,
                ModItems.ENGINEERING_PLASTIC,
                0.3f,
                200,
                RecipeSerializer.SMELTING_RECIPE
            )
                .unlockedBy(
                    getHasName(ModItems.SLIME_COVERED_LEATHER),
                    has(ModItems.SLIME_COVERED_LEATHER)
                )
                .save(writer, loc(getItemName(ModItems.ENGINEERING_PLASTIC)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.FUSEE, 4)
                .pattern("a")
                .pattern("b")
                .pattern("c")
                .define('a', Items.STONE_BUTTON)
                .define('b', ModTags.Items.DUSTS_REDSTONE)
                .define('c', ModTags.Items.NUGGETS_IRON)
                .unlockedBy(getHasName(Items.STONE_BUTTON), has(Items.STONE_BUTTON))
                .save(writer, loc(getItemName(ModItems.FUSEE)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.GRAIN, 8)
                .pattern("aba")
                .pattern("aba")
                .pattern(" c ")
                .define('a', PLATES_COPPER)
                .define('b', Items.GUNPOWDER)
                .define('c', ModItems.PRIMER)
                .unlockedBy(getHasName(ModItems.PRIMER), has(ModItems.PRIMER))
                .save(writer, loc(getItemName(ModItems.GRAIN)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.GS_HEAD, 2)
                .pattern("ddd")
                .pattern("bdb")
                .pattern(" a ")
                .define('a', Items.GUNPOWDER)
                .define('b', PLATES_STEEL)
                .define('d', INGOTS_LEAD)
                .unlockedBy(getHasName(Items.GUNPOWDER), has(Items.GUNPOWDER))
                .save(writer, loc(getItemName(ModItems.GS_HEAD)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.WP_HEAD, 2)
                .pattern("ede")
                .pattern("bdb")
                .pattern(" a ")
                .define('a', Items.GUNPOWDER)
                .define('b', PLATES_STEEL)
                .define('d', Items.BONE_MEAL)
                .define('e', Items.BLAZE_POWDER)
                .unlockedBy(getHasName(Items.GUNPOWDER), has(Items.GUNPOWDER))
                .save(writer, loc(getItemName(ModItems.WP_HEAD)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.HE_HEAD, 2)
                .pattern(" e ")
                .pattern("bab")
                .pattern(" c ")
                .define('a', ModItems.HIGH_ENERGY_EXPLOSIVES)
                .define('b', PLATES_STEEL)
                .define('c', Items.GUNPOWDER)
                .define('e', ModItems.FUSEE)
                .unlockedBy(
                    getHasName(ModItems.HIGH_ENERGY_EXPLOSIVES),
                    has(ModItems.HIGH_ENERGY_EXPLOSIVES)
                )
                .save(writer, loc(getItemName(ModItems.HE_HEAD)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.HEAVY_ARMAMENT_MODULE)
                .pattern("ddd")
                .pattern("abc")
                .pattern("ddd")
                .define('a', ModItems.CANNON_CORE)
                .define('b', ModItems.LEGENDARY_MATERIAL_PACK)
                .define('c', ModItems.MEDIUM_ARMAMENT_MODULE)
                .define('d', ModTags.Items.INGOTS_NETHERITE)
                .unlockedBy(
                    getHasName(ModItems.MEDIUM_ARMAMENT_MODULE),
                    has(ModItems.MEDIUM_ARMAMENT_MODULE)
                )
                .save(writer, loc(getItemName(ModItems.HEAVY_ARMAMENT_MODULE)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.HIGH_ENERGY_EXPLOSIVES, 4)
                .pattern("aba")
                .pattern("cac")
                .pattern("aba")
                .define('a', Items.GUNPOWDER)
                .define('b', Items.SUGAR)
                .define('c', ModTags.Items.SANDS)
                .unlockedBy(getHasName(Items.GUNPOWDER), has(Items.GUNPOWDER))
                .save(writer, loc(getItemName(ModItems.HIGH_ENERGY_EXPLOSIVES)))
            SimpleCookingRecipeBuilder.generic(
                Ingredient.of(ModItems.IRON_POWDER),
                RecipeCategory.MISC,
                Items.IRON_INGOT,
                0.7f,
                100,
                RecipeSerializer.BLASTING_RECIPE
            )
                .unlockedBy(getHasName(ModItems.IRON_POWDER), has(ModItems.IRON_POWDER))
                .save(writer, loc(getItemName(Items.IRON_INGOT) + "_blasting_from_powder"))
            SimpleCookingRecipeBuilder.generic(
                Ingredient.of(ModItems.IRON_POWDER),
                RecipeCategory.MISC,
                Items.IRON_INGOT,
                0.7f,
                200,
                RecipeSerializer.SMELTING_RECIPE
            )
                .unlockedBy(getHasName(ModItems.IRON_POWDER), has(ModItems.IRON_POWDER))
                .save(writer, loc(getItemName(Items.IRON_INGOT) + "_smelting_from_powder"))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.LARGE_BATTERY_PACK)
                .pattern("aa")
                .pattern("aa")
                .define('a', ModItems.MEDIUM_BATTERY_PACK)
                .unlockedBy(getHasName(ModItems.MEDIUM_BATTERY_PACK), has(ModItems.MEDIUM_BATTERY_PACK))
                .save(writer, loc(getItemName(ModItems.LARGE_BATTERY_PACK)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.LARGE_MOTOR)
                .pattern(" a ")
                .pattern("bcd")
                .pattern("bcd")
                .define('a', ModTags.Items.INGOTS_IRON)
                .define('b', ModTags.Items.STORAGE_BLOCKS_LAPIS)
                .define('c', ModTags.Items.STORAGE_BLOCKS_COPPER)
                .define('d', ModTags.Items.STORAGE_BLOCKS_REDSTONE)
                .unlockedBy(getHasName(Items.COPPER_BLOCK), has(ModTags.Items.STORAGE_BLOCKS_COPPER))
                .save(writer, loc(getItemName(ModItems.LARGE_MOTOR)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.LARGE_PROPELLER)
                .pattern(" a ")
                .pattern("aba")
                .pattern(" a ")
                .define('a', ModTags.Items.INGOTS_IRON)
                .define('b', ModTags.Items.INGOTS_CEMENTED_CARBIDE)
                .unlockedBy(
                    getHasName(ModItems.CEMENTED_CARBIDE_INGOT),
                    has(ModItems.CEMENTED_CARBIDE_INGOT)
                )
                .save(writer, loc(getItemName(ModItems.LARGE_PROPELLER)))
            SimpleCookingRecipeBuilder.generic(
                Ingredient.of(ModItems.GALENA),
                RecipeCategory.MISC,
                ModItems.LEAD_INGOT,
                0.7f,
                100,
                RecipeSerializer.BLASTING_RECIPE
            )
                .unlockedBy(getHasName(ModItems.GALENA), has(ModItems.GALENA))
                .save(writer, loc(getItemName(ModItems.LEAD_INGOT) + "_blasting"))
            SimpleCookingRecipeBuilder.generic(
                Ingredient.of(ModItems.GALENA_ORE, ModItems.DEEPSLATE_GALENA_ORE),
                RecipeCategory.MISC,
                ModItems.LEAD_INGOT,
                0.7f,
                100,
                RecipeSerializer.BLASTING_RECIPE
            )
                .unlockedBy(getHasName(ModItems.GALENA_ORE), has(commonItemTag("ores/lead")))
                .save(writer, loc(getItemName(Items.IRON_INGOT) + "_blasting_from_ore"))
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.LEAD_INGOT, 9)
                .requires(ModItems.LEAD_BLOCK)
                .unlockedBy(getHasName(ModItems.LEAD_BLOCK), has(ModItems.LEAD_BLOCK))
                .save(writer, loc(getItemName(ModItems.LEAD_INGOT) + "_from_block"))
            SimpleCookingRecipeBuilder.generic(
                Ingredient.of(ModItems.GALENA),
                RecipeCategory.MISC,
                ModItems.LEAD_INGOT,
                0.7f,
                200,
                RecipeSerializer.SMELTING_RECIPE
            )
                .unlockedBy(getHasName(ModItems.GALENA), has(ModItems.GALENA))
                .save(writer, loc(getItemName(ModItems.LEAD_INGOT) + "_smelting"))
            SimpleCookingRecipeBuilder.generic(
                Ingredient.of(ModItems.GALENA_ORE, ModItems.DEEPSLATE_GALENA_ORE),
                RecipeCategory.MISC,
                ModItems.LEAD_INGOT,
                0.7f,
                200,
                RecipeSerializer.SMELTING_RECIPE
            )
                .unlockedBy(getHasName(ModItems.GALENA_ORE), has(commonItemTag("ores/lead")))
                .save(writer, loc(getItemName(ModItems.LEAD_INGOT) + "_smelting_from_ore"))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.LIGHT_ARMAMENT_MODULE)
                .pattern("ddd")
                .pattern("abc")
                .pattern("ddd")
                .define('a', ModItems.STEEL_MATERIALS.barrel)
                .define('b', ModItems.RARE_MATERIAL_PACK)
                .define('c', Items.DISPENSER)
                .define('d', INGOTS_STEEL)
                .unlockedBy(getHasName(ModItems.RARE_MATERIAL_PACK), has(ModItems.RARE_MATERIAL_PACK))
                .save(writer, loc(getItemName(ModItems.LIGHT_ARMAMENT_MODULE)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MEDIUM_ARMAMENT_MODULE)
                .pattern("ddd")
                .pattern("abc")
                .pattern("ddd")
                .define('a', ModItems.CEMENTED_CARBIDE_MATERIALS.barrel)
                .define('b', ModItems.EPIC_MATERIAL_PACK)
                .define('c', ModItems.LIGHT_ARMAMENT_MODULE)
                .define('d', ModTags.Items.INGOTS_CEMENTED_CARBIDE)
                .unlockedBy(getHasName(ModItems.EPIC_MATERIAL_PACK), has(ModItems.EPIC_MATERIAL_PACK))
                .save(writer, loc(getItemName(ModItems.MEDIUM_ARMAMENT_MODULE)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MEDIUM_BATTERY_PACK)
                .pattern("aaa")
                .pattern("aaa")
                .pattern("aaa")
                .define('a', ModItems.SMALL_BATTERY_PACK)
                .unlockedBy(getHasName(ModItems.SMALL_BATTERY_PACK), has(ModItems.SMALL_BATTERY_PACK))
                .save(writer, loc(getItemName(ModItems.MEDIUM_BATTERY_PACK)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MISSILE_ENGINE, 4)
                .pattern("aba")
                .pattern("cbc")
                .pattern(" d ")
                .define('a', ModTags.Items.INGOTS_COPPER)
                .define('b', ModItems.GRAIN)
                .define('c', ModTags.Items.INGOTS_IRON)
                .define('d', Items.FIREWORK_ROCKET)
                .unlockedBy(getHasName(ModItems.GRAIN), has(ModItems.GRAIN))
                .save(writer, loc(getItemName(ModItems.MISSILE_ENGINE)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MORTAR_BARREL)
                .pattern("a")
                .pattern("a")
                .pattern("a")
                .define('a', PLATES_STEEL)
                .unlockedBy(getHasName(ModItems.ENGINEERING_PLASTIC), has(PLATES_STEEL))
                .save(writer, loc(getItemName(ModItems.MORTAR_BARREL)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MORTAR_BASE_PLATE)
                .pattern("b")
                .pattern("a")
                .define('a', PLATES_STEEL)
                .define('b', Items.IRON_NUGGET)
                .unlockedBy(getHasName(Items.IRON_NUGGET), has(ModTags.Items.NUGGETS_IRON))
                .save(writer, loc(getItemName(ModItems.MORTAR_BASE_PLATE)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MORTAR_BIPOD)
                .pattern(" a ")
                .pattern("bbb")
                .define('a', INGOTS_STEEL)
                .define('b', Items.IRON_BARS)
                .unlockedBy(getHasName(Items.IRON_BARS), has(Items.IRON_BARS))
                .save(writer, loc(getItemName(ModItems.MORTAR_BIPOD)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MOTOR, 2)
                .pattern(" a ")
                .pattern("bcd")
                .pattern("bcd")
                .define('a', ModTags.Items.NUGGETS_IRON)
                .define('b', ModTags.Items.GEMS_LAPIS)
                .define('c', ModTags.Items.INGOTS_COPPER)
                .define('d', ModTags.Items.DUSTS_REDSTONE)
                .unlockedBy(getHasName(Items.COPPER_INGOT), has(ModTags.Items.INGOTS_COPPER))
                .save(writer, loc(getItemName(ModItems.MOTOR)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PRIMER, 4)
                .pattern("a")
                .pattern("b")
                .define('a', Items.FLINT)
                .define('b', PLATES_COPPER)
                .unlockedBy(getHasName(Items.FLINT), has(Items.FLINT))
                .save(writer, loc(getItemName(ModItems.PRIMER)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PROPELLER, 2)
                .pattern(" a ")
                .pattern("aba")
                .pattern(" a ")
                .define('a', ItemTags.PLANKS)
                .define('b', ModTags.Items.NUGGETS_IRON)
                .unlockedBy(getHasName(Items.OAK_PLANKS), has(ItemTags.PLANKS))
                .unlockedBy(getHasName(Items.IRON_NUGGET), has(ModTags.Items.NUGGETS_IRON))
                .save(writer, loc(getItemName(ModItems.PROPELLER)))
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.RAW_CEMENTED_CARBIDE_POWDER, 8)
                .requires(
                    DefaultCustomIngredients.any(
                        Ingredient.of(commonItemTag("dusts/tungsten")),
                        Ingredient.of(commonItemTag("dusts/scheelite"))
                    ), 7
                )
                .requires(commonItemTag("dusts/iron"))
                .requires(
                    DefaultCustomIngredients.any(
                        Ingredient.of(commonItemTag("dusts/coal_coke")),
                        Ingredient.of(commonItemTag("dusts/coal"))
                    )
                )
                .unlockedBy(getHasName(ModItems.TUNGSTEN_POWDER), has(commonItemTag("dusts/tungsten")))
                .save(writer, loc(getItemName(ModItems.RAW_CEMENTED_CARBIDE_POWDER)))
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
                .save(writer, loc(getItemName(ModItems.SEEKER)))
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SHORTCUT_PACK)
                .requires(ModItems.EPIC_MATERIAL_PACK)
                .requires(Items.NETHER_STAR)
                .unlockedBy(getHasName(ModItems.EPIC_MATERIAL_PACK), has(ModItems.EPIC_MATERIAL_PACK))
                .unlockedBy(getHasName(Items.NETHER_STAR), has(Items.NETHER_STAR))
                .save(writer, loc(getItemName(ModItems.SHORTCUT_PACK)))
            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.REPAIR_TOOL)
                .pattern(" aa")
                .pattern("bcd")
                .pattern("efg")
                .define('a', Items.IRON_INGOT)
                .define('b', ModItems.STEEL_MATERIALS.barrel)
                .define('c', Items.FLINT_AND_STEEL)
                .define('d', ModItems.MOTOR)
                .define('e', Items.LAVA_BUCKET)
                .define('f', ModItems.BATTERY)
                .define('g', ModItems.STEEL_MATERIALS.trigger)
                .unlockedBy(getHasName(Items.COMPASS), has(Items.COMPASS))
                .save(writer, loc(getItemName(ModItems.REPAIR_TOOL)))
            SimpleCookingRecipeBuilder.generic(
                Ingredient.of(ModItems.RAW_SILVER),
                RecipeCategory.MISC,
                ModItems.SILVER_INGOT,
                0.7f,
                100,
                RecipeSerializer.BLASTING_RECIPE
            )
                .unlockedBy(getHasName(ModItems.RAW_SILVER), has(ModItems.RAW_SILVER))
                .save(writer, loc(getItemName(ModItems.SILVER_INGOT) + "_blasting"))
            SimpleCookingRecipeBuilder.generic(
                Ingredient.of(ModItems.SILVER_ORE, ModItems.DEEPSLATE_SILVER_ORE),
                RecipeCategory.MISC,
                ModItems.SILVER_INGOT,
                0.7f,
                100,
                RecipeSerializer.BLASTING_RECIPE
            )
                .unlockedBy(getHasName(ModItems.SILVER_ORE), has(commonItemTag("ores/silver")))
                .save(writer, loc(getItemName(ModItems.SILVER_INGOT) + "_blasting_from_ore"))
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SILVER_INGOT, 9)
                .requires(ModItems.SILVER_BLOCK)
                .unlockedBy(getHasName(ModItems.SILVER_BLOCK), has(ModItems.SILVER_BLOCK))
                .save(writer, loc(getItemName(ModItems.SILVER_INGOT) + "_from_block"))
            SimpleCookingRecipeBuilder.generic(
                Ingredient.of(ModItems.RAW_SILVER),
                RecipeCategory.MISC,
                ModItems.SILVER_INGOT,
                0.7f,
                200,
                RecipeSerializer.SMELTING_RECIPE
            )
                .unlockedBy(getHasName(ModItems.RAW_SILVER), has(ModItems.RAW_SILVER))
                .save(writer, loc(getItemName(ModItems.SILVER_INGOT) + "_smelting"))
            SimpleCookingRecipeBuilder.generic(
                Ingredient.of(ModItems.SILVER_ORE, ModItems.DEEPSLATE_SILVER_ORE),
                RecipeCategory.MISC,
                ModItems.SILVER_INGOT,
                0.7f,
                200,
                RecipeSerializer.SMELTING_RECIPE
            )
                .unlockedBy(getHasName(ModItems.GALENA_ORE), has(commonItemTag("ores/silver")))
                .save(writer, loc(getItemName(ModItems.SILVER_INGOT) + "_smelting_from_ore"))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SMALL_BATTERY_PACK)
                .pattern("aa")
                .pattern("aa")
                .define('a', ModItems.BATTERY)
                .unlockedBy(getHasName(ModItems.BATTERY), has(ModItems.BATTERY))
                .save(writer, loc(getItemName(ModItems.SMALL_BATTERY_PACK) + "_from_battery"))
            SimpleCookingRecipeBuilder.generic(
                Ingredient.of(ModItems.COAL_IRON_POWDER),
                RecipeCategory.MISC,
                ModItems.STEEL_INGOT,
                0.7f,
                100,
                RecipeSerializer.BLASTING_RECIPE
            )
                .unlockedBy(getHasName(ModItems.COAL_IRON_POWDER), has(ModItems.COAL_IRON_POWDER))
                .save(writer, loc(getItemName(ModItems.STEEL_INGOT) + "_blasting"))
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.STEEL_INGOT, 9)
                .requires(ModItems.STEEL_BLOCK)
                .unlockedBy(getHasName(ModItems.STEEL_BLOCK), has(ModItems.STEEL_BLOCK))
                .save(writer, loc(getItemName(ModItems.STEEL_INGOT) + "_from_block"))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.TRACK)
                .pattern("aaa")
                .pattern("b b")
                .pattern("aaa")
                .define('a', INGOTS_STEEL)
                .define('b', ModItems.WHEEL)
                .unlockedBy(getHasName(ModItems.WHEEL), has(ModItems.WHEEL))
                .save(writer, loc(getItemName(ModItems.TRACK)))
            SimpleCookingRecipeBuilder.generic(
                Ingredient.of(ModItems.SCHEELITE),
                RecipeCategory.MISC,
                ModItems.TUNGSTEN_INGOT,
                4f,
                100,
                RecipeSerializer.BLASTING_RECIPE
            )
                .unlockedBy(getHasName(ModItems.SCHEELITE), has(ModItems.SCHEELITE))
                .save(writer, loc(getItemName(ModItems.TUNGSTEN_INGOT) + "_blasting"))
            SimpleCookingRecipeBuilder.generic(
                Ingredient.of(ModItems.SCHEELITE_ORE, ModItems.DEEPSLATE_SCHEELITE_ORE),
                RecipeCategory.MISC,
                ModItems.TUNGSTEN_INGOT,
                4f,
                100,
                RecipeSerializer.BLASTING_RECIPE
            )
                .unlockedBy(getHasName(ModItems.SCHEELITE_ORE), has(commonItemTag("ores/tungsten")))
                .save(writer, loc(getItemName(ModItems.TUNGSTEN_INGOT) + "_blasting_from_ore"))
            SimpleCookingRecipeBuilder.generic(
                Ingredient.of(ModItems.TUNGSTEN_POWDER),
                RecipeCategory.MISC,
                ModItems.TUNGSTEN_INGOT,
                4f,
                100,
                RecipeSerializer.BLASTING_RECIPE
            )
                .unlockedBy(getHasName(ModItems.TUNGSTEN_POWDER), has(ModItems.TUNGSTEN_POWDER))
                .save(writer, loc(getItemName(ModItems.TUNGSTEN_INGOT) + "_blasting_from_powder"))
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.TUNGSTEN_INGOT, 9)
                .requires(ModItems.TUNGSTEN_BLOCK)
                .unlockedBy(getHasName(ModItems.TUNGSTEN_BLOCK), has(ModItems.TUNGSTEN_BLOCK))
                .save(writer, loc(getItemName(ModItems.TUNGSTEN_INGOT) + "_from_block"))
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.TUNGSTEN_POWDER)
                .requires(INGOTS_TUNGSTEN)
                .requires(ModTags.Items.HAMMER)
                .unlockedBy(getHasName(ModItems.HAMMER), has(ModTags.Items.HAMMER))
                .save(writer, loc(getItemName(ModItems.TUNGSTEN_POWDER)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.TUNGSTEN_ROD, 4)
                .pattern("a")
                .pattern("a")
                .define('a', INGOTS_TUNGSTEN)
                .unlockedBy(getHasName(ModItems.TUNGSTEN_INGOT), has(INGOTS_TUNGSTEN))
                .save(writer, loc(getItemName(ModItems.TUNGSTEN_ROD)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.WHEEL, 2)
                .pattern(" a ")
                .pattern("aba")
                .pattern(" a ")
                .define('a', Items.BLACK_WOOL)
                .define('b', ModTags.Items.INGOTS_IRON)
                .unlockedBy(getHasName(Items.BLACK_WOOL), has(Items.BLACK_WOOL))
                .save(writer, loc(getItemName(ModItems.WHEEL)))
        }

        private fun buildBlockRecipes(writer: Consumer<FinishedRecipe>) {
            ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModItems.AIRCRAFT_CATAPULT, 8)
                .pattern("aaa")
                .pattern("cbc")
                .pattern("ddd")
                .define('a', Items.POWERED_RAIL)
                .define('b', ModTags.Items.STORAGE_BLOCKS_REDSTONE)
                .define('c', ModTags.Items.INGOTS_COPPER)
                .define('d', ModTags.Items.INGOTS_IRON)
                .unlockedBy(getHasName(Items.POWERED_RAIL), has(Items.POWERED_RAIL))
                .save(writer, loc(getItemName(ModItems.AIRCRAFT_CATAPULT)))
            ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModItems.CATAPULT_CONTROLLER, 1)
                .pattern("ddd")
                .pattern("cac")
                .pattern("ddd")
                .define('a', Items.COMPARATOR)
                .define('c', ModTags.Items.INGOTS_COPPER)
                .define('d', ModTags.Items.INGOTS_IRON)
                .unlockedBy(getHasName(Items.POWERED_RAIL), has(Items.POWERED_RAIL))
                .save(writer, loc(getItemName(ModItems.CATAPULT_CONTROLLER)))
            ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModItems.SUPERB_ITEM_INTERFACE)
                .pattern("cac")
                .pattern("aba")
                .pattern("cac")
                .define('a', Items.HOPPER)
                .define('b', Items.DROPPER)
                .define('c', INGOTS_STEEL)
                .unlockedBy(getHasName(Items.HOPPER), has(Items.DROPPER))
                .save(writer, loc(getItemName(ModItems.SUPERB_ITEM_INTERFACE)))
            ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModItems.VEHICLE_ASSEMBLING_TABLE)
                .pattern("aaa")
                .pattern("bcd")
                .pattern("eee")
                .define('a', Items.IRON_INGOT)
                .define('b', ModTags.Items.STORAGE_BLOCKS_IRON)
                .define('c', Items.SMITHING_TABLE)
                .define('d', ModTags.Items.GLASS_PANES)
                .define('e', INGOTS_STEEL)
                .unlockedBy(getHasName(Items.SMITHING_TABLE), has(Items.SMITHING_TABLE))
                .save(writer, loc(getItemName(ModItems.VEHICLE_ASSEMBLING_TABLE)))
            ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.BARBED_WIRE, 2)
                .pattern("aba")
                .define('a', ItemTags.PLANKS)
                .define('b', Items.IRON_BARS)
                .unlockedBy(getHasName(Items.IRON_BARS), has(Items.IRON_BARS))
                .save(writer, loc(getItemName(ModItems.BARBED_WIRE)))
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.CEMENTED_CARBIDE_BLOCK)
                .pattern("aaa")
                .pattern("aaa")
                .pattern("aaa")
                .define('a', ModItems.CEMENTED_CARBIDE_INGOT)
                .unlockedBy(
                    getHasName(ModItems.CEMENTED_CARBIDE_INGOT),
                    has(ModItems.CEMENTED_CARBIDE_INGOT)
                )
                .save(writer, loc(getItemName(ModItems.CEMENTED_CARBIDE_BLOCK)))
            ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.CHARGING_STATION)
                .pattern("ada")
                .pattern("dcd")
                .pattern("aba")
                .define('a', PLATES_COPPER)
                .define('b', ModTags.Items.INGOTS_IRON)
                .define('c', Items.BLAST_FURNACE)
                .define('d', ModItems.CELL)
                .unlockedBy(getHasName(ModItems.CELL), has(ModItems.CELL))
                .save(writer, loc(getItemName(ModItems.CHARGING_STATION)))
            ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.DRAGON_TEETH, 4)
                .pattern(" a ")
                .pattern("bbb")
                .pattern("bbb")
                .define('a', ModTags.Items.NUGGETS_IRON)
                .define('b', Items.SMOOTH_STONE)
                .unlockedBy(getHasName(Items.SMOOTH_STONE), has(Items.SMOOTH_STONE))
                .save(writer, loc(getItemName(ModItems.DRAGON_TEETH)))
            ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.JUMP_PAD)
                .pattern(" a ")
                .pattern("bcb")
                .pattern("bcb")
                .define('a', Items.STONE_PRESSURE_PLATE)
                .define('b', Items.LIME_CONCRETE)
                .define('c', Items.PISTON)
                .unlockedBy(getHasName(Items.PISTON), has(Items.PISTON))
                .save(writer, loc(getItemName(ModItems.JUMP_PAD)))
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.LEAD_BLOCK)
                .pattern("aaa")
                .pattern("aba")
                .pattern("aaa")
                .define('a', INGOTS_LEAD)
                .define('b', ModItems.LEAD_INGOT)
                .unlockedBy(getHasName(ModItems.LEAD_INGOT), has(ModItems.LEAD_INGOT))
                .save(writer, loc(getItemName(ModItems.LEAD_BLOCK)))
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
                .save(writer, loc(getItemName(ModItems.REFORGING_TABLE)))
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.SANDBAG)
                .pattern("aba")
                .define('a', Items.PAPER)
                .define('b', ModTags.Items.SANDS)
                .unlockedBy(getHasName(Items.PAPER), has(Items.PAPER))
                .save(writer, loc(getItemName(ModItems.SANDBAG)))
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.SILVER_BLOCK)
                .pattern("aaa")
                .pattern("aba")
                .pattern("aaa")
                .define('a', INGOTS_SILVER)
                .define('b', ModItems.SILVER_INGOT)
                .unlockedBy(getHasName(ModItems.SILVER_INGOT), has(ModItems.SILVER_INGOT))
                .save(writer, loc(getItemName(ModItems.SILVER_BLOCK)))
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.STEEL_BLOCK)
                .pattern("aaa")
                .pattern("aaa")
                .pattern("aaa")
                .define('a', INGOTS_STEEL)
                .unlockedBy(getHasName(ModItems.STEEL_INGOT), has(INGOTS_STEEL))
                .save(writer, loc(getItemName(ModItems.STEEL_BLOCK)))
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.TUNGSTEN_BLOCK)
                .pattern("aaa")
                .pattern("aba")
                .pattern("aaa")
                .define('a', INGOTS_TUNGSTEN)
                .define('b', ModItems.TUNGSTEN_INGOT)
                .unlockedBy(getHasName(ModItems.TUNGSTEN_INGOT), has(ModItems.TUNGSTEN_INGOT))
                .save(writer, loc(getItemName(ModItems.TUNGSTEN_BLOCK)))
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
                .save(writer, loc(getItemName(ModItems.FUMO_25)))
            ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.BIOGAS_GENERATOR)
                .pattern("aba")
                .pattern("cdc")
                .pattern("efe")
                .define('a', INGOTS_STEEL)
                .define('b', Items.HOPPER)
                .define('c', Items.TINTED_GLASS)
                .define('d', Items.CAULDRON)
                .define('e', ModItems.CELL)
                .define('f', Items.BLAST_FURNACE)
                .unlockedBy(getHasName(ModItems.CELL), has(ModItems.CELL))
                .save(writer, loc(getItemName(ModItems.BIOGAS_GENERATOR)))
            ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModItems.BLUEPRINT_RESEARCH_TABLE)
                .pattern("aaa")
                .pattern("dcb")
                .pattern("eee")
                .define('a', Items.IRON_INGOT)
                .define('b', ModItems.BATTERY)
                .define('c', Items.CARTOGRAPHY_TABLE)
                .define('d', ModTags.Items.STORAGE_BLOCKS_REDSTONE)
                .define('e', INGOTS_STEEL)
                .unlockedBy(getHasName(Items.CARTOGRAPHY_TABLE), has(Items.CARTOGRAPHY_TABLE))
                .save(writer, loc(getItemName(ModItems.BLUEPRINT_RESEARCH_TABLE)))
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.RAW_GALENA_BLOCK)
                .pattern("aaa")
                .pattern("aaa")
                .pattern("aaa")
                .define('a', commonItemTag("raw_materials/lead"))
                .unlockedBy(getHasName(ModItems.GALENA), has(commonItemTag("raw_materials/lead")))
                .save(writer, loc(getItemName(ModItems.RAW_GALENA_BLOCK)))
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.RAW_SCHEELITE_BLOCK)
                .pattern("aaa")
                .pattern("aaa")
                .pattern("aaa")
                .define('a', Ingredient.of(commonItemTag("raw_materials/tungsten")))
                .unlockedBy(getHasName(ModItems.SCHEELITE), has(commonItemTag("raw_materials/tungsten")))
                .save(writer, loc(getItemName(ModItems.RAW_SCHEELITE_BLOCK)))
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.RAW_SILVER_BLOCK)
                .pattern("aaa")
                .pattern("aaa")
                .pattern("aaa")
                .define('a', commonItemTag("raw_materials/silver"))
                .unlockedBy(getHasName(ModItems.RAW_SILVER), has(commonItemTag("raw_materials/silver")))
                .save(writer, loc(getItemName(ModItems.RAW_SILVER_BLOCK)))
        }

        private fun buildVehicleRecipes(writer: Consumer<FinishedRecipe>) {
            VehicleAssemblingRecipeBuilder.entity(ModEntities.TOM_6, VehicleAssemblingRecipe.Category.AIRCRAFT)
                .require(ItemTags.PLANKS, 5)
                .require(ModItems.BATTERY)
                .require(Items.MINECART)
                .unlockedBy(getHasName(Items.MINECART), has(Items.MINECART))
                .save(writer, loc(getEntityTypeName(ModEntities.TOM_6)))
            VehicleAssemblingRecipeBuilder.entity(
                ModEntities.ANNIHILATOR,
                VehicleAssemblingRecipe.Category.DEFENSE
            )
                .require(STORAGE_BLOCK_STEEL, 24)
                .require(Items.NETHERITE_BLOCK, 3)
                .require(ModItems.LASER_UNIT, 32)
                .require(ModItems.LARGE_BATTERY_PACK)
                .require(ModItems.ANNIHILATOR_BLUEPRINT)
                .unlockedBy(getHasName(ModItems.ANNIHILATOR_BLUEPRINT), has(ModItems.ANNIHILATOR_BLUEPRINT))
                .save(writer, loc(getEntityTypeName(ModEntities.ANNIHILATOR)))
            VehicleAssemblingRecipeBuilder.entity(ModEntities.BL_132, VehicleAssemblingRecipe.Category.DEFENSE)
                .require(STORAGE_BLOCK_STEEL, 10)
                .require(ModItems.BL_132_BLUEPRINT)
                .require(ModItems.CANNON_CORE, 4)
                .unlockedBy(getHasName(ModItems.BL_132_BLUEPRINT), has(ModItems.BL_132_BLUEPRINT))
                .save(writer, loc(getEntityTypeName(ModEntities.BL_132)))
            VehicleAssemblingRecipeBuilder.entity(ModEntities.MLE_1934, VehicleAssemblingRecipe.Category.DEFENSE)
                .require(STORAGE_BLOCK_STEEL, 8)
                .require(ModItems.MLE_1934_BLUEPRINT)
                .require(ModItems.CANNON_CORE, 2)
                .unlockedBy(getHasName(ModItems.MLE_1934_BLUEPRINT), has(ModItems.MLE_1934_BLUEPRINT))
                .save(writer, loc(getEntityTypeName(ModEntities.MLE_1934)))
            VehicleAssemblingRecipeBuilder.entity(ModEntities.MK_42, VehicleAssemblingRecipe.Category.DEFENSE)
                .require(STORAGE_BLOCK_STEEL, 6)
                .require(ModItems.MK_42_BLUEPRINT)
                .require(ModItems.CANNON_CORE)
                .unlockedBy(getHasName(ModItems.MK_42_BLUEPRINT), has(ModItems.MK_42_BLUEPRINT))
                .save(writer, loc(getEntityTypeName(ModEntities.MK_42)))
            VehicleAssemblingRecipeBuilder.entity(ModEntities.TYPE_63, VehicleAssemblingRecipe.Category.DEFENSE)
                .require(STORAGE_BLOCK_STEEL, 1)
                .require(ModItems.MORTAR_BARREL, 12)
                .require(ModItems.WHEEL, 2)
                .unlockedBy(getHasName(ModItems.MORTAR_BARREL), has(ModItems.MORTAR_BARREL))
                .save(writer, loc(getEntityTypeName(ModEntities.TYPE_63)))
            VehicleAssemblingRecipeBuilder.entity(ModEntities.HPJ_11, VehicleAssemblingRecipe.Category.DEFENSE)
                .require(STORAGE_BLOCK_STEEL, 5)
                .require(ModItems.HPJ_11_BLUEPRINT)
                .require(ModItems.CANNON_CORE)
                .require(ModItems.MEDIUM_BATTERY_PACK)
                .require(ModItems.LARGE_MOTOR)
                .require(Items.OBSERVER)
                .unlockedBy(getHasName(ModItems.HPJ_11_BLUEPRINT), has(ModItems.HPJ_11_BLUEPRINT))
                .save(writer, loc(getEntityTypeName(ModEntities.HPJ_11)))
            VehicleAssemblingRecipeBuilder.entity(
                ModEntities.LASER_TOWER,
                VehicleAssemblingRecipe.Category.DEFENSE
            )
                .require(STORAGE_BLOCK_STEEL, 1)
                .require(ModItems.LASER_UNIT)
                .require(ModItems.SMALL_BATTERY_PACK)
                .require(ModItems.MOTOR)
                .unlockedBy(getHasName(ModItems.LASER_UNIT), has(ModItems.LASER_UNIT))
                .save(writer, loc(getEntityTypeName(ModEntities.LASER_TOWER)))
            VehicleAssemblingRecipeBuilder.entity(
                ModEntities.TOW,
                VehicleAssemblingRecipe.Category.DEFENSE
            )
                .require(Items.DISPENSER)
                .require(ModItems.MORTAR_BARREL)
                .require(ModItems.ARTILLERY_INDICATOR)
                .require(ModItems.MORTAR_BIPOD)
                .unlockedBy(getHasName(ModItems.ARTILLERY_INDICATOR), has(ModItems.ARTILLERY_INDICATOR))
                .save(writer, loc(getEntityTypeName(ModEntities.TOW)))
            VehicleAssemblingRecipeBuilder.entity(
                ModEntities.WAVEFORCE_TOWER,
                VehicleAssemblingRecipe.Category.DEFENSE
            )
                .require(STORAGE_BLOCK_STEEL, 10)
                .require(ModItems.CEMENTED_CARBIDE_BLOCK, 2)
                .require(Items.REDSTONE_BLOCK, 8)
                .require(ModItems.LASER_UNIT, 9)
                .require(ModItems.MEDIUM_BATTERY_PACK, 2)
                .require(ModItems.LARGE_MOTOR)
                .unlockedBy(getHasName(ModItems.LASER_UNIT), has(ModItems.LASER_UNIT))
                .save(writer, loc(getEntityTypeName(ModEntities.WAVEFORCE_TOWER)))
            VehicleAssemblingRecipeBuilder.entity(
                ModEntities.WHEEL_CHAIR,
                VehicleAssemblingRecipe.Category.CIVILIAN
            )
                .require(ModItems.WHEEL, 2)
                .require(ModItems.CELL)
                .require(ModItems.MOTOR)
                .require(Items.MINECART)
                .unlockedBy(getHasName(Items.MINECART), has(Items.MINECART))
                .save(writer, loc(getEntityTypeName(ModEntities.WHEEL_CHAIR)))
            VehicleAssemblingRecipeBuilder.entity(ModEntities.LAV_150, VehicleAssemblingRecipe.Category.LAND)
                .require(STORAGE_BLOCK_STEEL, 6)
                .require(ModItems.LIGHT_ARMAMENT_MODULE)
                .require(ModItems.MEDIUM_BATTERY_PACK)
                .require(ModItems.WHEEL, 4)
                .require(ModItems.LARGE_MOTOR)
                .unlockedBy(getHasName(ModItems.LARGE_MOTOR), has(ModItems.LARGE_MOTOR))
                .save(writer, loc(getEntityTypeName(ModEntities.LAV_150)))
            VehicleAssemblingRecipeBuilder.entity(ModEntities.LAV_AD, VehicleAssemblingRecipe.Category.LAND)
                .require(STORAGE_BLOCK_STEEL, 7)
                .require(ModItems.MEDIUM_ARMAMENT_MODULE)
                .require(ModItems.MEDIUM_BATTERY_PACK)
                .require(ModItems.WHEEL, 8)
                .require(ModItems.LARGE_MOTOR)
                .unlockedBy(getHasName(ModItems.LARGE_MOTOR), has(ModItems.LARGE_MOTOR))
                .save(writer, loc(getEntityTypeName(ModEntities.LAV_AD)))
            VehicleAssemblingRecipeBuilder.entity(ModEntities.LAV_25, VehicleAssemblingRecipe.Category.LAND)
                .require(STORAGE_BLOCK_STEEL, 7)
                .require(ModItems.MEDIUM_ARMAMENT_MODULE)
                .require(ModItems.MEDIUM_BATTERY_PACK)
                .require(ModItems.WHEEL, 8)
                .require(ModItems.LARGE_MOTOR)
                .unlockedBy(getHasName(ModItems.LARGE_MOTOR), has(ModItems.LARGE_MOTOR))
                .save(writer, loc(getEntityTypeName(ModEntities.LAV_25)))
            VehicleAssemblingRecipeBuilder.entity(ModEntities.BMP_2, VehicleAssemblingRecipe.Category.LAND)
                .require(STORAGE_BLOCK_STEEL, 8)
                .require(ModItems.MEDIUM_ARMAMENT_MODULE)
                .require(ModItems.MEDIUM_BATTERY_PACK)
                .require(ModItems.TRACK, 2)
                .require(ModItems.LARGE_MOTOR)
                .unlockedBy(getHasName(ModItems.LARGE_MOTOR), has(ModItems.LARGE_MOTOR))
                .save(writer, loc(getEntityTypeName(ModEntities.BMP_2)))
            VehicleAssemblingRecipeBuilder.entity(ModEntities.BRADLEY, VehicleAssemblingRecipe.Category.LAND)
                .require(STORAGE_BLOCK_STEEL, 8)
                .require(ModItems.MEDIUM_ARMAMENT_MODULE)
                .require(ModItems.MEDIUM_BATTERY_PACK)
                .require(ModItems.TRACK, 2)
                .require(ModItems.LARGE_MOTOR)
                .unlockedBy(getHasName(ModItems.LARGE_MOTOR), has(ModItems.LARGE_MOTOR))
                .save(writer, loc(getEntityTypeName(ModEntities.BRADLEY)))
            VehicleAssemblingRecipeBuilder.entity(ModEntities.PRISM_TANK, VehicleAssemblingRecipe.Category.LAND)
                .require(STORAGE_BLOCK_STEEL, 9)
                .require(ModItems.LASER_UNIT, 16)
                .require(ModItems.LARGE_BATTERY_PACK)
                .require(ModItems.TRACK, 2)
                .require(ModItems.LARGE_MOTOR)
                .unlockedBy(getHasName(ModItems.LARGE_MOTOR), has(ModItems.LARGE_MOTOR))
                .save(writer, loc(getEntityTypeName(ModEntities.PRISM_TANK)))
            VehicleAssemblingRecipeBuilder.entity(ModEntities.T_90A, VehicleAssemblingRecipe.Category.LAND)
                .require(STORAGE_BLOCK_STEEL, 10)
                .require(ModItems.HEAVY_ARMAMENT_MODULE)
                .require(ModItems.MEDIUM_BATTERY_PACK, 2)
                .require(ModItems.TRACK, 2)
                .require(ModItems.LARGE_MOTOR)
                .require(Items.GREEN_DYE)
                .unlockedBy(getHasName(ModItems.LARGE_MOTOR), has(ModItems.LARGE_MOTOR))
                .save(writer, loc(getEntityTypeName(ModEntities.T_90A)))
            VehicleAssemblingRecipeBuilder.entity(ModEntities.ZTZ_99A, VehicleAssemblingRecipe.Category.LAND)
                .require(STORAGE_BLOCK_STEEL, 10)
                .require(ModItems.HEAVY_ARMAMENT_MODULE)
                .require(ModItems.MEDIUM_BATTERY_PACK, 2)
                .require(ModItems.TRACK, 2)
                .require(ModItems.LARGE_MOTOR)
                .require(Items.RED_DYE)
                .unlockedBy(getHasName(ModItems.LARGE_MOTOR), has(ModItems.LARGE_MOTOR))
                .save(writer, loc(getEntityTypeName(ModEntities.ZTZ_99A)))
            VehicleAssemblingRecipeBuilder.entity(ModEntities.M_1A_2, VehicleAssemblingRecipe.Category.LAND)
                .require(STORAGE_BLOCK_STEEL, 10)
                .require(ModItems.HEAVY_ARMAMENT_MODULE)
                .require(ModItems.MEDIUM_BATTERY_PACK, 2)
                .require(ModItems.TRACK, 2)
                .require(ModItems.LARGE_MOTOR)
                .require(Items.SAND)
                .unlockedBy(getHasName(ModItems.LARGE_MOTOR), has(ModItems.LARGE_MOTOR))
                .save(writer, loc(getEntityTypeName(ModEntities.M_1A_2)))
            VehicleAssemblingRecipeBuilder.entity(ModEntities.YX_100, VehicleAssemblingRecipe.Category.LAND)
                .require(STORAGE_BLOCK_STEEL, 8)
                .require(ModItems.CEMENTED_CARBIDE_BLOCK, 24)
                .require(ModItems.HEAVY_ARMAMENT_MODULE)
                .require(ModItems.MEDIUM_ARMAMENT_MODULE)
                .require(ModItems.LARGE_BATTERY_PACK)
                .require(ModItems.TRACK, 2)
                .require(ModItems.LARGE_MOTOR)
                .unlockedBy(getHasName(ModItems.LARGE_MOTOR), has(ModItems.LARGE_MOTOR))
                .save(writer, loc(getEntityTypeName(ModEntities.YX_100)))
            VehicleAssemblingRecipeBuilder.entity(ModEntities.PLZ_05, VehicleAssemblingRecipe.Category.LAND)
                .require(STORAGE_BLOCK_STEEL, 10)
                .require(ModItems.CANNON_CORE, 1)
                .require(ModItems.HEAVY_ARMAMENT_MODULE)
                .require(ModItems.MEDIUM_BATTERY_PACK)
                .require(ModItems.TRACK, 2)
                .require(ModItems.LARGE_MOTOR)
                .unlockedBy(getHasName(ModItems.LARGE_MOTOR), has(ModItems.LARGE_MOTOR))
                .save(writer, loc(getEntityTypeName(ModEntities.PLZ_05)))
            VehicleAssemblingRecipeBuilder.entity(ModEntities.FH_77BW, VehicleAssemblingRecipe.Category.LAND)
                .require(STORAGE_BLOCK_STEEL, 12)
                .require(ModItems.CANNON_CORE, 1)
                .require(ModItems.HEAVY_ARMAMENT_MODULE)
                .require(ModItems.MEDIUM_BATTERY_PACK)
                .require(ModItems.WHEEL, 6)
                .require(ModItems.LARGE_MOTOR)
                .unlockedBy(getHasName(ModItems.LARGE_MOTOR), has(ModItems.LARGE_MOTOR))
                .save(writer, loc(getEntityTypeName(ModEntities.FH_77BW)))
            VehicleAssemblingRecipeBuilder.entity(ModEntities.SPEEDBOAT, VehicleAssemblingRecipe.Category.WATER)
                .require(STORAGE_BLOCK_STEEL, 2)
                .require(ItemTags.BOATS)
                .require(ModItems.LIGHT_ARMAMENT_MODULE)
                .require(ModItems.SMALL_BATTERY_PACK)
                .require(ModItems.LARGE_PROPELLER)
                .require(ModItems.LARGE_MOTOR)
                .unlockedBy(getHasName(ModItems.LIGHT_ARMAMENT_MODULE), has(ModItems.LIGHT_ARMAMENT_MODULE))
                .save(writer, loc(getEntityTypeName(ModEntities.SPEEDBOAT)))
            VehicleAssemblingRecipeBuilder.entity(
                ModEntities.TINY_SPEEDBOAT,
                VehicleAssemblingRecipe.Category.WATER
            )
                .require(Items.IRON_INGOT, 5)
                .require(ItemTags.BOATS)
                .require(ModItems.BATTERY)
                .require(ModItems.PROPELLER)
                .require(ModItems.MOTOR)
                .unlockedBy(getHasName(ModItems.MOTOR), has(ModItems.MOTOR))
                .save(writer, loc(getEntityTypeName(ModEntities.TINY_SPEEDBOAT)))
            VehicleAssemblingRecipeBuilder.entity(ModEntities.AH_6, VehicleAssemblingRecipe.Category.AIRCRAFT)
                .require(STORAGE_BLOCK_STEEL, 3)
                .require(ModItems.LIGHT_ARMAMENT_MODULE)
                .require(ModItems.MEDIUM_BATTERY_PACK)
                .require(ModItems.LARGE_PROPELLER)
                .require(ModItems.PROPELLER)
                .require(ModItems.LARGE_MOTOR)
                .unlockedBy(getHasName(ModItems.LARGE_PROPELLER), has(ModItems.LARGE_PROPELLER))
                .save(writer, loc(getEntityTypeName(ModEntities.AH_6)))
            VehicleAssemblingRecipeBuilder.entity(ModEntities.KV_16, VehicleAssemblingRecipe.Category.AIRCRAFT)
                .require(Items.BUCKET, 2)
                .require(STORAGE_BLOCK_STEEL, 1)
                .require(ItemTags.PLANKS, 2)
                .require(ModItems.LIGHT_ARMAMENT_MODULE)
                .require(ModItems.SMALL_BATTERY_PACK)
                .require(ModItems.PROPELLER, 1)
                .require(ModItems.LARGE_MOTOR)
                .unlockedBy(getHasName(ModItems.LARGE_PROPELLER), has(ModItems.LARGE_PROPELLER))
                .save(writer, loc(getEntityTypeName(ModEntities.KV_16)))
            VehicleAssemblingRecipeBuilder.entity(ModEntities.JU_87, VehicleAssemblingRecipe.Category.AIRCRAFT)
                .require(STORAGE_BLOCK_STEEL, 3)
                .require(ModItems.MEDIUM_ARMAMENT_MODULE, 1)
                .require(Items.GOAT_HORN, 1)
                .require(ModItems.MEDIUM_BATTERY_PACK)
                .require(ModItems.LARGE_PROPELLER, 1)
                .require(ModItems.PROPELLER, 2)
                .require(ModItems.LARGE_MOTOR)
                .unlockedBy(getHasName(ModItems.LARGE_PROPELLER), has(ModItems.LARGE_PROPELLER))
                .save(writer, loc(getEntityTypeName(ModEntities.JU_87)))
            VehicleAssemblingRecipeBuilder.entity(ModEntities.A_10A, VehicleAssemblingRecipe.Category.AIRCRAFT)
                .require(STORAGE_BLOCK_STEEL, 6)
                .require(ModItems.HEAVY_ARMAMENT_MODULE)
                .require(ModItems.LARGE_BATTERY_PACK)
                .require(ModItems.LARGE_PROPELLER, 2)
                .require(ModItems.LARGE_MOTOR, 2)
                .require(ModItems.WHEEL, 3)
                .unlockedBy(getHasName(ModItems.LARGE_PROPELLER), has(ModItems.LARGE_PROPELLER))
                .save(writer, loc(getEntityTypeName(ModEntities.A_10A)))
            VehicleAssemblingRecipeBuilder.entity(ModEntities.TRUCK, VehicleAssemblingRecipe.Category.CIVILIAN)
                .require(STORAGE_BLOCK_STEEL, 8)
                .require(Items.CHEST, 4)
                .require(ModItems.MEDIUM_BATTERY_PACK)
                .require(ModItems.WHEEL, 6)
                .require(ModItems.LARGE_MOTOR)
                .unlockedBy(getHasName(ModItems.LARGE_MOTOR), has(ModItems.LARGE_MOTOR))
                .save(writer, loc(getEntityTypeName(ModEntities.TRUCK)))
            VehicleAssemblingRecipeBuilder.entity(
                ModEntities.SODAYO_PICK_UP,
                VehicleAssemblingRecipe.Category.CIVILIAN
            )
                .require(STORAGE_BLOCK_STEEL, 2)
                .require(Items.CHEST, 1)
                .require(ModItems.MEDIUM_BATTERY_PACK)
                .require(ModItems.WHEEL, 4)
                .require(ModItems.LARGE_MOTOR)
                .unlockedBy(getHasName(ModItems.LARGE_MOTOR), has(ModItems.LARGE_MOTOR))
                .save(writer, loc(getEntityTypeName(ModEntities.SODAYO_PICK_UP)))
            VehicleAssemblingRecipeBuilder.entity(
                ModEntities.SODAYO_PICK_UP_HMG,
                VehicleAssemblingRecipe.Category.CIVILIAN
            )
                .require(STORAGE_BLOCK_STEEL, 2)
                .require(ModItems.MEDIUM_BATTERY_PACK)
                .require(ModItems.WHEEL, 4)
                .require(ModItems.LARGE_MOTOR)
                .require(ModItems.LIGHT_ARMAMENT_MODULE)
                .unlockedBy(getHasName(ModItems.LARGE_MOTOR), has(ModItems.LARGE_MOTOR))
                .save(writer, loc(getEntityTypeName(ModEntities.SODAYO_PICK_UP_HMG)))
            VehicleAssemblingRecipeBuilder.entity(
                ModEntities.SODAYO_PICK_UP_ROCKET,
                VehicleAssemblingRecipe.Category.CIVILIAN
            )
                .require(STORAGE_BLOCK_STEEL, 3)
                .require(ModItems.MEDIUM_BATTERY_PACK)
                .require(ModItems.WHEEL, 4)
                .require(ModItems.LARGE_MOTOR)
                .require(ModItems.MORTAR_BARREL, 12)
                .unlockedBy(getHasName(ModItems.LARGE_MOTOR), has(ModItems.LARGE_MOTOR))
                .save(writer, loc(getEntityTypeName(ModEntities.SODAYO_PICK_UP_ROCKET)))
            VehicleAssemblingRecipeBuilder.entity(
                ModEntities.SODAYO_PICK_UP_TOW,
                VehicleAssemblingRecipe.Category.CIVILIAN
            )
                .require(STORAGE_BLOCK_STEEL, 3)
                .require(ModItems.MEDIUM_BATTERY_PACK)
                .require(ModItems.WHEEL, 4)
                .require(ModItems.LARGE_MOTOR)
                .require(Items.DISPENSER)
                .require(ModItems.MORTAR_BARREL)
                .require(ModItems.ARTILLERY_INDICATOR)
                .require(ModItems.MORTAR_BIPOD)
                .unlockedBy(getHasName(ModItems.LARGE_MOTOR), has(ModItems.LARGE_MOTOR))
                .save(writer, loc(getEntityTypeName(ModEntities.SODAYO_PICK_UP_TOW)))
            VehicleAssemblingRecipeBuilder.entity(ModEntities.MI_28, VehicleAssemblingRecipe.Category.AIRCRAFT)
                .require(STORAGE_BLOCK_STEEL, 5)
                .require(ModItems.HEAVY_ARMAMENT_MODULE)
                .require(ModItems.MEDIUM_BATTERY_PACK, 2)
                .require(ModItems.WHEEL, 3)
                .require(ModItems.LARGE_PROPELLER)
                .require(ModItems.PROPELLER)
                .require(ModItems.LARGE_MOTOR)
                .unlockedBy(getHasName(ModItems.HEAVY_ARMAMENT_MODULE), has(ModItems.HEAVY_ARMAMENT_MODULE))
                .save(writer, loc(getEntityTypeName(ModEntities.MI_28)))
            VehicleAssemblingRecipeBuilder.entity(ModEntities.KIROV, VehicleAssemblingRecipe.Category.AIRCRAFT)
                .require(ModItems.LARGE_BATTERY_PACK, 2)
                .require(STORAGE_BLOCK_STEEL, 24)
                .require(ModItems.HEAVY_ARMAMENT_MODULE, 3)
                .require(ItemTags.WOOL, 640)
                .require(ModItems.LARGE_MOTOR, 5)
                .require(ModItems.LARGE_PROPELLER, 5)
                .require(Items.FLOWER_POT)
                .require(Items.WHITE_TULIP)
                .unlockedBy(getHasName(ModItems.LARGE_PROPELLER), has(ModItems.LARGE_PROPELLER))
                .save(writer, loc(getEntityTypeName(ModEntities.KIROV)))
            VehicleAssemblingRecipeBuilder.entity(ModEntities.AC_130H, VehicleAssemblingRecipe.Category.AIRCRAFT)
                .require(ModItems.LARGE_BATTERY_PACK, 2)
                .require(STORAGE_BLOCK_STEEL, 16)
                .require(ModItems.HEAVY_ARMAMENT_MODULE)
                .require(ModItems.MEDIUM_ARMAMENT_MODULE)
                .require(ModItems.LIGHT_ARMAMENT_MODULE)
                .require(ModItems.LARGE_MOTOR, 4)
                .require(ModItems.LARGE_PROPELLER, 4)
                .require(ModItems.WHEEL, 10)
                .unlockedBy(getHasName(ModItems.HEAVY_ARMAMENT_MODULE), has(ModItems.HEAVY_ARMAMENT_MODULE))
                .save(writer, loc(getEntityTypeName(ModEntities.AC_130H)))

            VehicleAssemblingRecipeBuilder.item(
                ModItems.SMALL_BATTERY_PACK,
                1,
                VehicleAssemblingRecipe.Category.MISC
            )
                .require(PLATES_COPPER, 4)
                .require(ModTags.Items.GLASS_PANES, 8)
                .require(Items.REDSTONE, 4)
                .require(Items.IRON_INGOT, 4)
                .unlockedBy(getHasName(ModItems.COPPER_PLATE), has(ModItems.COPPER_PLATE))
                .save(writer, loc(getItemName(ModItems.SMALL_BATTERY_PACK) + "_assembling"))
            VehicleAssemblingRecipeBuilder.item(
                ModItems.MEDIUM_BATTERY_PACK,
                1,
                VehicleAssemblingRecipe.Category.MISC
            )
                .require(PLATES_COPPER, 36)
                .require(ModTags.Items.GLASS_PANES, 72)
                .require(Items.REDSTONE, 36)
                .require(Items.IRON_INGOT, 36)
                .unlockedBy(getHasName(ModItems.COPPER_PLATE), has(ModItems.COPPER_PLATE))
                .save(writer, loc(getItemName(ModItems.MEDIUM_BATTERY_PACK) + "_assembling"))
            VehicleAssemblingRecipeBuilder.item(
                ModItems.LARGE_BATTERY_PACK,
                1,
                VehicleAssemblingRecipe.Category.MISC
            )
                .require(PLATES_COPPER, 144)
                .require(ModTags.Items.GLASS_PANES, 288)
                .require(Items.REDSTONE, 144)
                .require(Items.IRON_INGOT, 144)
                .unlockedBy(getHasName(ModItems.COPPER_PLATE), has(ModItems.COPPER_PLATE))
                .save(writer, loc(getItemName(ModItems.LARGE_BATTERY_PACK) + "_assembling"))
            VehicleAssemblingRecipeBuilder.item(
                ModItems.VEHICLE_RESET_KIT,
                1,
                VehicleAssemblingRecipe.Category.MISC
            )
                .require(INGOTS_STEEL)
                .require(Items.PAPER, 4)
                .unlockedBy(getHasName(Items.PAPER), has(Items.PAPER))
                .save(writer, loc(getItemName(ModItems.VEHICLE_RESET_KIT) + "_assembling"))
            // TODO 临时配方
            VehicleAssemblingRecipeBuilder.entity(
                ModEntities.AIR_SHEEP,
                VehicleAssemblingRecipe.Category.AIRCRAFT
            )
                .require(ItemTags.WOOL, 16)
                .require(ItemTags.BOATS)
                .require(Items.MUTTON, 8)
                .unlockedBy(getHasName(Items.WHITE_WOOL), has(ItemTags.WOOL))
                .save(writer, loc(getEntityTypeName(ModEntities.AIR_SHEEP)))
        }

        private fun buildGunRecipes(writer: Consumer<FinishedRecipe>) {
            gunSmithing(
                writer,
                ModItems.TRACHELIUM_BLUEPRINT,
                GunRarity.VIRTUAL,
                ModTags.Items.INGOTS_CEMENTED_CARBIDE,
                ModItems.TRACHELIUM
            )
            gunSmithing(
                writer,
                ModItems.GLOCK_17_BLUEPRINT,
                GunRarity.COMMON,
                PLATES_PLASTIC,
                ModItems.GLOCK_17
            )
            gunSmithing(
                writer,
                ModItems.MP_443_BLUEPRINT,
                GunRarity.COMMON,
                Items.IRON_INGOT,
                ModItems.MP_443
            )
            gunSmithing(
                writer,
                ModItems.GLOCK_18_BLUEPRINT,
                GunRarity.RARE,
                PLATES_PLASTIC,
                ModItems.GLOCK_18
            )
            gunSmithing(
                writer,
                ModItems.HUNTING_RIFLE_BLUEPRINT,
                GunRarity.RARE,
                ItemTags.LOGS,
                ModItems.HUNTING_RIFLE
            )
            gunSmithing(writer, ModItems.M_79_BLUEPRINT, GunRarity.RARE, Items.DISPENSER, ModItems.M_79)
            gunSmithing(writer, ModItems.RPG_BLUEPRINT, GunRarity.RARE, Items.DISPENSER, ModItems.RPG)
            gunSmithing(writer, ModItems.BOCEK_BLUEPRINT, GunRarity.EPIC, Items.BOW, ModItems.BOCEK)
            gunSmithing(
                writer,
                ModItems.M_4_BLUEPRINT,
                GunRarity.RARE,
                INGOTS_STEEL,
                ModItems.M_4
            )
            gunSmithing(
                writer,
                ModItems.AA_12_BLUEPRINT,
                GunRarity.LEGENDARY,
                Items.NETHERITE_INGOT,
                ModItems.AA_12
            )
            gunSmithing(
                writer,
                ModItems.HK_416_BLUEPRINT,
                GunRarity.RARE,
                INGOTS_STEEL,
                ModItems.HK_416
            )
            gunSmithing(writer, ModItems.RPK_BLUEPRINT, GunRarity.EPIC, ItemTags.LOGS, ModItems.RPK)
            gunSmithing(writer, ModItems.SKS_BLUEPRINT, GunRarity.RARE, ItemTags.LOGS, ModItems.SKS)
            gunSmithing(
                writer,
                ModItems.NTW_20_BLUEPRINT,
                GunRarity.LEGENDARY,
                Items.SPYGLASS,
                ModItems.NTW_20
            )
            gunSmithing(writer, ModItems.MP_5_BLUEPRINT, GunRarity.RARE, Items.IRON_INGOT, ModItems.MP_5)
            gunSmithing(
                writer,
                ModItems.VECTOR_BLUEPRINT,
                GunRarity.EPIC,
                ModTags.Items.INGOTS_CEMENTED_CARBIDE,
                ModItems.VECTOR
            )
            gunSmithing(
                writer,
                ModItems.MINIGUN_BLUEPRINT,
                GunRarity.LEGENDARY,
                ModItems.MOTOR,
                ModItems.MINIGUN
            )
            gunSmithing(
                writer,
                ModItems.MK_14_BLUEPRINT,
                GunRarity.EPIC,
                ModTags.Items.INGOTS_CEMENTED_CARBIDE,
                ModItems.MK_14
            )
            gunSmithing(
                writer,
                ModItems.SENTINEL_BLUEPRINT,
                GunRarity.EPIC,
                ModItems.CELL,
                ModItems.SENTINEL
            )
            gunSmithing(
                writer,
                ModItems.M_60_BLUEPRINT,
                GunRarity.EPIC,
                ModTags.Items.INGOTS_CEMENTED_CARBIDE,
                ModItems.M_60
            )
            gunSmithing(
                writer,
                ModItems.SVD_BLUEPRINT,
                GunRarity.EPIC,
                ModTags.Items.INGOTS_CEMENTED_CARBIDE,
                ModItems.SVD
            )
            gunSmithing(writer, ModItems.MARLIN_BLUEPRINT, GunRarity.COMMON, ItemTags.LOGS, ModItems.MARLIN)
            gunSmithing(
                writer,
                ModItems.M_870_BLUEPRINT,
                GunRarity.RARE,
                INGOTS_STEEL,
                ModItems.M_870
            )
            gunSmithing(writer, ModItems.M_98B_BLUEPRINT, GunRarity.EPIC, Items.SPYGLASS, ModItems.M_98B)
            gunSmithing(writer, ModItems.AK_47_BLUEPRINT, GunRarity.RARE, ItemTags.LOGS, ModItems.AK_47)
            gunSmithing(
                writer,
                ModItems.AK_12_BLUEPRINT,
                GunRarity.RARE,
                INGOTS_STEEL,
                ModItems.AK_12
            )
            gunSmithing(
                writer,
                ModItems.DEVOTION_BLUEPRINT,
                GunRarity.EPIC,
                ModTags.Items.INGOTS_CEMENTED_CARBIDE,
                ModItems.DEVOTION
            )
            gunSmithing(
                writer,
                ModItems.TASER_BLUEPRINT,
                GunRarity.COMMON,
                PLATES_PLASTIC,
                ModItems.TASER
            )
            gunSmithing(
                writer,
                ModItems.M_1911_BLUEPRINT,
                GunRarity.COMMON,
                INGOTS_STEEL,
                ModItems.M_1911
            )
            gunSmithing(
                writer,
                ModItems.QBZ_95_BLUEPRINT,
                GunRarity.RARE,
                PLATES_PLASTIC,
                ModItems.QBZ_95
            )
            gunSmithing(
                writer,
                ModItems.QBZ_191_BLUEPRINT,
                GunRarity.EPIC,
                ModTags.Items.INGOTS_CEMENTED_CARBIDE,
                ModItems.QBZ_191
            )
            gunSmithing(writer, ModItems.AWM_BLUEPRINT, GunRarity.EPIC, Items.SPYGLASS, ModItems.AWM)
            gunSmithing(writer, ModItems.K_98_BLUEPRINT, GunRarity.RARE, ItemTags.LOGS, ModItems.K_98)
            gunSmithing(
                writer,
                ModItems.MOSIN_NAGANT_BLUEPRINT,
                GunRarity.RARE,
                ItemTags.LOGS,
                ModItems.MOSIN_NAGANT
            )
            gunSmithing(
                writer,
                ModItems.JAVELIN_BLUEPRINT,
                GunRarity.LEGENDARY,
                ModItems.ANCIENT_CPU,
                ModItems.JAVELIN
            )
            gunSmithing(
                writer,
                ModItems.IGLA_BLUEPRINT,
                GunRarity.EPIC,
                ModItems.ANCIENT_CPU,
                ModItems.IGLA_9K38
            )
            gunSmithing(
                writer,
                ModItems.M_2_HB_BLUEPRINT,
                GunRarity.RARE,
                STORAGE_BLOCK_STEEL,
                ModItems.M_2_HB
            )
            gunSmithing(
                writer,
                ModItems.SECONDARY_CATACLYSM_BLUEPRINT,
                GunRarity.VIRTUAL,
                ModItems.KNIFE,
                ModItems.SECONDARY_CATACLYSM
            )
            gunSmithing(
                writer,
                ModItems.INSIDIOUS_BLUEPRINT,
                GunRarity.EPIC,
                ModTags.Items.INGOTS_CEMENTED_CARBIDE,
                ModItems.INSIDIOUS
            )
            gunSmithing(
                writer,
                ModItems.QL_1031_BLUEPRINT,
                GunRarity.VIRTUAL,
                ModItems.BATTERY,
                ModItems.QL_1031
            )
            gunSmithing(
                writer,
                ModItems.SUPER_STAR_SHOOTER_BLUEPRINT,
                GunRarity.SUPERB,
                ModItems.MEDIUM_ARMAMENT_MODULE,
                ModItems.SUPER_STAR_SHOOTER
            )

            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.HOMEMADE_SHOTGUN)
                .pattern("aab")
                .pattern("ccc")
                .pattern(" dc")
                .define('a', ModItems.IRON_MATERIALS.barrel)
                .define('b', Items.FLINT_AND_STEEL)
                .define('c', ItemTags.PLANKS)
                .define('d', ModTags.Items.DUSTS_REDSTONE)
                .unlockedBy(getHasName(ModItems.IRON_MATERIALS.barrel), has(ModItems.IRON_MATERIALS.barrel))
                .save(writer, loc(getItemName(ModItems.HOMEMADE_SHOTGUN)))
        }

        private fun buildBlueprintRecipes(writer: Consumer<FinishedRecipe>) {
            copyBlueprint(writer, ModItems.TRACHELIUM_BLUEPRINT)
            copyBlueprint(writer, ModItems.GLOCK_17_BLUEPRINT)
            copyBlueprint(writer, ModItems.MP_443_BLUEPRINT)
            copyBlueprint(writer, ModItems.GLOCK_18_BLUEPRINT)
            copyBlueprint(writer, ModItems.HUNTING_RIFLE_BLUEPRINT)
            copyBlueprint(writer, ModItems.M_79_BLUEPRINT)
            copyBlueprint(writer, ModItems.RPG_BLUEPRINT)
            copyBlueprint(writer, ModItems.BOCEK_BLUEPRINT)
            copyBlueprint(writer, ModItems.M_4_BLUEPRINT)
            copyBlueprint(writer, ModItems.AA_12_BLUEPRINT)
            copyBlueprint(writer, ModItems.HK_416_BLUEPRINT)
            copyBlueprint(writer, ModItems.RPK_BLUEPRINT)
            copyBlueprint(writer, ModItems.SKS_BLUEPRINT)
            copyBlueprint(writer, ModItems.NTW_20_BLUEPRINT)
            copyBlueprint(writer, ModItems.MP_5_BLUEPRINT)
            copyBlueprint(writer, ModItems.VECTOR_BLUEPRINT)
            copyBlueprint(writer, ModItems.MINIGUN_BLUEPRINT)
            copyBlueprint(writer, ModItems.MK_14_BLUEPRINT)
            copyBlueprint(writer, ModItems.SENTINEL_BLUEPRINT)
            copyBlueprint(writer, ModItems.M_60_BLUEPRINT)
            copyBlueprint(writer, ModItems.SVD_BLUEPRINT)
            copyBlueprint(writer, ModItems.MARLIN_BLUEPRINT)
            copyBlueprint(writer, ModItems.M_870_BLUEPRINT)
            copyBlueprint(writer, ModItems.AWM_BLUEPRINT)
            copyBlueprint(writer, ModItems.M_98B_BLUEPRINT)
            copyBlueprint(writer, ModItems.AK_47_BLUEPRINT)
            copyBlueprint(writer, ModItems.AK_12_BLUEPRINT)
            copyBlueprint(writer, ModItems.DEVOTION_BLUEPRINT)
            copyBlueprint(writer, ModItems.TASER_BLUEPRINT)
            copyBlueprint(writer, ModItems.M_1911_BLUEPRINT)
            copyBlueprint(writer, ModItems.QBZ_95_BLUEPRINT)
            copyBlueprint(writer, ModItems.QBZ_191_BLUEPRINT)
            copyBlueprint(writer, ModItems.K_98_BLUEPRINT)
            copyBlueprint(writer, ModItems.MOSIN_NAGANT_BLUEPRINT)
            copyBlueprint(writer, ModItems.JAVELIN_BLUEPRINT)
            copyBlueprint(writer, ModItems.IGLA_BLUEPRINT)
            copyBlueprint(writer, ModItems.M_2_HB_BLUEPRINT)
            copyBlueprint(writer, ModItems.SECONDARY_CATACLYSM_BLUEPRINT)
            copyBlueprint(writer, ModItems.INSIDIOUS_BLUEPRINT)
            copyBlueprint(writer, ModItems.MK_42_BLUEPRINT)
            copyBlueprint(writer, ModItems.MLE_1934_BLUEPRINT)
            copyBlueprint(writer, ModItems.BL_132_BLUEPRINT)
            copyBlueprint(writer, ModItems.HPJ_11_BLUEPRINT)
            copyBlueprint(writer, ModItems.ANNIHILATOR_BLUEPRINT)
            copyBlueprint(writer, ModItems.QL_1031_BLUEPRINT)
            copyBlueprint(writer, ModItems.SUPER_STAR_SHOOTER_BLUEPRINT)
        }

        private fun buildPerkRecipes(writer: Consumer<FinishedRecipe>) {
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.EMPTY_PERK)
                .pattern("cbc")
                .pattern("bab")
                .pattern("cbc")
                .define('a', Items.PAPER)
                .define('b', Items.LAPIS_LAZULI)
                .define('c', ModTags.Items.INGOTS_IRON)
                .unlockedBy(getHasName(Items.PAPER), has(Items.PAPER))
                .save(writer, loc(getItemName(ModItems.EMPTY_PERK)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PERK_ITEMS[ModPerks.AP_BULLET]!!)
                .pattern("cbc")
                .pattern("bab")
                .pattern("cbc")
                .define('a', ModItems.EMPTY_PERK)
                .define('b', commonItemTag("storage_blocks/tungsten"))
                .define('c', INGOTS_TUNGSTEN)
                .unlockedBy(getHasName(ModItems.EMPTY_PERK), has(ModItems.EMPTY_PERK))
                .save(writer, perkLoc(ModPerks.AP_BULLET))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PERK_ITEMS[ModPerks.CUPID_ARROW]!!)
                .pattern("cbc")
                .pattern("dad")
                .pattern("cbc")
                .define('a', ModItems.EMPTY_PERK)
                .define('b', Items.BOW)
                .define('c', ItemTags.ARROWS)
                .define('d', getPotionIngredient(Potions.HEALING))
                .unlockedBy(getHasName(ModItems.EMPTY_PERK), has(ModItems.EMPTY_PERK))
                .save(writer, perkLoc(ModPerks.CUPID_ARROW))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PERK_ITEMS[ModPerks.FIREFLY]!!)
                .pattern("cbc")
                .pattern("bab")
                .pattern("cbc")
                .define('a', ModItems.EMPTY_PERK)
                .define('b', Ingredient.of(Items.OCHRE_FROGLIGHT, Items.VERDANT_FROGLIGHT, Items.PEARLESCENT_FROGLIGHT))
                .define('c', ModItems.HIGH_ENERGY_EXPLOSIVES)
                .unlockedBy(getHasName(ModItems.EMPTY_PERK), has(ModItems.EMPTY_PERK))
                .save(writer, perkLoc(ModPerks.FIREFLY))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PERK_ITEMS[ModPerks.HE_BULLET]!!)
                .pattern("cbc")
                .pattern("bab")
                .pattern("cbc")
                .define('a', ModItems.EMPTY_PERK)
                .define('b', Items.TNT)
                .define('c', ModItems.HIGH_ENERGY_EXPLOSIVES)
                .unlockedBy(getHasName(ModItems.EMPTY_PERK), has(ModItems.EMPTY_PERK))
                .save(writer, perkLoc(ModPerks.HE_BULLET))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PERK_ITEMS[ModPerks.INCENDIARY_BULLET]!!)
                .pattern("bbb")
                .pattern("cac")
                .pattern("bbb")
                .define('a', ModItems.EMPTY_PERK)
                .define('b', Items.BLAZE_POWDER)
                .define('c', Items.DRAGON_BREATH)
                .unlockedBy(getHasName(ModItems.EMPTY_PERK), has(ModItems.EMPTY_PERK))
                .save(writer, perkLoc(ModPerks.INCENDIARY_BULLET))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PERK_ITEMS[ModPerks.INTELLIGENT_CHIP]!!)
                .pattern("bbb")
                .pattern("bab")
                .pattern("bbb")
                .define('a', ModItems.EMPTY_PERK)
                .define('b', ModItems.ANCIENT_CPU)
                .unlockedBy(getHasName(ModItems.EMPTY_PERK), has(ModItems.EMPTY_PERK))
                .save(writer, perkLoc(ModPerks.INTELLIGENT_CHIP))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PERK_ITEMS[ModPerks.JHP_BULLET]!!)
                .pattern("cbc")
                .pattern("bab")
                .pattern("cbc")
                .define('a', ModItems.EMPTY_PERK)
                .define('b', ModTags.Items.STORAGE_BLOCKS_COPPER)
                .define('c', ModTags.Items.INGOTS_COPPER)
                .unlockedBy(getHasName(ModItems.EMPTY_PERK), has(ModItems.EMPTY_PERK))
                .save(writer, perkLoc(ModPerks.JHP_BULLET))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PERK_ITEMS[ModPerks.LONGER_WIRE]!!)
                .pattern("bbb")
                .pattern("bab")
                .pattern("bbb")
                .define('a', ModItems.EMPTY_PERK)
                .define('b', Items.STRING)
                .unlockedBy(getHasName(ModItems.EMPTY_PERK), has(ModItems.EMPTY_PERK))
                .save(writer, perkLoc(ModPerks.LONGER_WIRE))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PERK_ITEMS[ModPerks.MICRO_MISSILE]!!)
                .pattern("cbc")
                .pattern("bab")
                .pattern("cbc")
                .define('a', ModItems.EMPTY_PERK)
                .define('b', ModItems.GRAIN)
                .define('c', Items.FIREWORK_ROCKET)
                .unlockedBy(getHasName(ModItems.EMPTY_PERK), has(ModItems.EMPTY_PERK))
                .save(writer, perkLoc(ModPerks.MICRO_MISSILE))
            ShapedRecipeBuilder.shaped(
                RecipeCategory.MISC,
                ModItems.PERK_ITEMS[ModPerks.PHASE_PENETRATING_BULLET]!!
            )
                .pattern("cbc")
                .pattern("bab")
                .pattern("cbc")
                .define('a', ModItems.EMPTY_PERK)
                .define('b', ModTags.Items.INGOTS_NETHERITE)
                .define('c', ModItems.AP_HEAD)
                .unlockedBy(getHasName(ModItems.EMPTY_PERK), has(ModItems.EMPTY_PERK))
                .save(writer, perkLoc(ModPerks.PHASE_PENETRATING_BULLET))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PERK_ITEMS[ModPerks.POISONOUS_BULLET]!!)
                .pattern("cbc")
                .pattern("bab")
                .pattern("cbc")
                .define('a', ModItems.EMPTY_PERK)
                .define('b', commonItemTag("storage_blocks/lead"))
                .define('c', Items.SPIDER_EYE)
                .unlockedBy(getHasName(ModItems.EMPTY_PERK), has(ModItems.EMPTY_PERK))
                .save(writer, perkLoc(ModPerks.POISONOUS_BULLET))
            ShapedRecipeBuilder.shaped(
                RecipeCategory.MISC,
                ModItems.PERK_ITEMS[ModPerks.POWERFUL_ATTRACTION]!!
            )
                .pattern("dbe")
                .pattern("cac")
                .pattern(" c ")
                .define('a', ModItems.EMPTY_PERK)
                .define('b', ModTags.Items.ENDER_PEARLS)
                .define('c', ModTags.Items.INGOTS_IRON)
                .define('d', ModTags.Items.DUSTS_REDSTONE)
                .define('e', ModTags.Items.GEMS_LAPIS)
                .unlockedBy(getHasName(ModItems.EMPTY_PERK), has(ModItems.EMPTY_PERK))
                .save(writer, perkLoc(ModPerks.POWERFUL_ATTRACTION))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PERK_ITEMS[ModPerks.REGENERATION]!!)
                .pattern("ccc")
                .pattern("bab")
                .pattern("ddd")
                .define('a', ModItems.EMPTY_PERK)
                .define('b', ModItems.CELL)
                .define('c', Items.DAYLIGHT_DETECTOR)
                .define('d', ModTags.Items.INGOTS_GOLD)
                .unlockedBy(getHasName(ModItems.EMPTY_PERK), has(ModItems.EMPTY_PERK))
                .save(writer, perkLoc(ModPerks.REGENERATION))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PERK_ITEMS[ModPerks.RIOT_BULLET]!!)
                .pattern("cbc")
                .pattern("bab")
                .pattern("cbc")
                .define('a', ModItems.EMPTY_PERK)
                .define('b', Items.SLIME_BLOCK)
                .define('c', Items.COBWEB)
                .unlockedBy(getHasName(ModItems.EMPTY_PERK), has(ModItems.EMPTY_PERK))
                .save(writer, perkLoc(ModPerks.RIOT_BULLET))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PERK_ITEMS[ModPerks.SILVER_BULLET]!!)
                .pattern("cbc")
                .pattern("bab")
                .pattern("cbc")
                .define('a', ModItems.EMPTY_PERK)
                .define('b', commonItemTag("storage_blocks/silver"))
                .define('c', INGOTS_SILVER)
                .unlockedBy(getHasName(ModItems.EMPTY_PERK), has(ModItems.EMPTY_PERK))
                .save(writer, perkLoc(ModPerks.SILVER_BULLET))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PERK_ITEMS[ModPerks.TURBO_CHARGER]!!)
                .pattern("cbc")
                .pattern("bab")
                .pattern("cbc")
                .define('a', ModItems.EMPTY_PERK)
                .define('b', Items.PISTON)
                .define('c', INGOTS_STEEL)
                .unlockedBy(getHasName(ModItems.EMPTY_PERK), has(ModItems.EMPTY_PERK))
                .save(writer, perkLoc(ModPerks.TURBO_CHARGER))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PERK_ITEMS[ModPerks.VOLT_OVERLOAD]!!)
                .pattern("cec")
                .pattern("bab")
                .pattern("bdb")
                .define('a', ModItems.EMPTY_PERK)
                .define('b', ModItems.CELL)
                .define('c', Items.LIGHTNING_ROD)
                .define('d', commonItemTag("dusts/coal_coke"))
                .define('e', ModTags.Items.INGOTS_IRON)
                .unlockedBy(getHasName(ModItems.EMPTY_PERK), has(ModItems.EMPTY_PERK))
                .save(writer, perkLoc(ModPerks.VOLT_OVERLOAD))
            ShapedRecipeBuilder.shaped(
                RecipeCategory.MISC,
                ModItems.PERK_ITEMS[ModPerks.BACKPACK_LINKED_MAGAZINE]!!
            )
                .pattern("cbc")
                .pattern("bab")
                .pattern("cbc")
                .define('a', ModItems.PERK_ITEMS[ModPerks.SUBSISTENCE]!!)
                .define('b', ModTags.Items.CHESTS_ENDER)
                .define('c', ModTags.Items.CHESTS_WOODEN)
                .unlockedBy(
                    getHasName(ModItems.PERK_ITEMS[ModPerks.SUBSISTENCE]!!),
                    has(ModItems.PERK_ITEMS[ModPerks.SUBSISTENCE]!!)
                )
                .save(writer, perkLoc(ModPerks.BACKPACK_LINKED_MAGAZINE))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PERK_ITEMS[ModPerks.POWERFUL_COOLER]!!)
                .pattern("cdc")
                .pattern("bab")
                .pattern("cdc")
                .define('a', ModItems.EMPTY_PERK)
                .define('b', Items.POWDER_SNOW_BUCKET)
                .define('c', Items.BLUE_ICE)
                .define('d', commonItemTag("storage_blocks/silver"))
                .unlockedBy(getHasName(ModItems.EMPTY_PERK), has(ModItems.EMPTY_PERK))
                .unlockedBy(getHasName(Items.POWDER_SNOW_BUCKET), has(Items.POWDER_SNOW_BUCKET))
                .save(writer, perkLoc(ModPerks.POWERFUL_COOLER))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PERK_ITEMS[ModPerks.BLADE_BULLET]!!)
                .pattern("dbd")
                .pattern("cac")
                .pattern("ebe")
                .define('a', ModItems.EMPTY_PERK)
                .define('b', STORAGE_BLOCK_STEEL)
                .define('c', ModItems.BARBED_WIRE)
                .define('d', ModItems.KNIFE)
                .define('e', ModItems.CLAYMORE_MINE)
                .unlockedBy(getHasName(ModItems.EMPTY_PERK), has(ModItems.EMPTY_PERK))
                .unlockedBy(getHasName(ModItems.CLAYMORE_MINE), has(ModItems.CLAYMORE_MINE))
                .save(writer, perkLoc(ModPerks.BLADE_BULLET))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PERK_ITEMS[ModPerks.AQUA_BULLET]!!)
                .pattern("dbd")
                .pattern("cac")
                .pattern("dcd")
                .define('a', ModItems.EMPTY_PERK)
                .define('b', Items.TRIDENT)
                .define('c', Items.PRISMARINE_SHARD)
                .define('d', ItemTags.FISHES)
                .unlockedBy(getHasName(ModItems.EMPTY_PERK), has(ModItems.EMPTY_PERK))
                .unlockedBy(getHasName(Items.TRIDENT), has(Items.TRIDENT))
                .save(writer, perkLoc(ModPerks.AQUA_BULLET))
        }

        private fun buildMiscRecipes(writer: Consumer<FinishedRecipe>) {
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.DOG_TAG)
                .pattern("a")
                .pattern("b")
                .define('a', Items.CHAIN)
                .define('b', Items.NAME_TAG)
                .unlockedBy(getHasName(Items.NAME_TAG), has(Items.NAME_TAG))
                .save(writer, loc(getItemName(ModItems.DOG_TAG)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.DRONE, 4)
                .pattern("a a")
                .pattern("bcb")
                .pattern(" e ")
                .define('a', ModItems.PROPELLER)
                .define('b', ModItems.MOTOR)
                .define('c', PLATES_PLASTIC)
                .define('e', ModItems.CELL)
                .unlockedBy(getHasName(ModItems.PROPELLER), has(ModItems.PROPELLER))
                .unlockedBy(getHasName(ModItems.MOTOR), has(ModItems.MOTOR))
                .save(writer, loc(getItemName(ModItems.DRONE)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.FIRING_PARAMETERS)
                .pattern("a")
                .pattern("b")
                .pattern("c")
                .define('a', Items.TARGET)
                .define('b', Items.PAPER)
                .define('c', ItemTags.PLANKS)
                .unlockedBy(getHasName(Items.TARGET), has(Items.TARGET))
                .save(writer, loc(getItemName(ModItems.FIRING_PARAMETERS)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.IFF)
                .pattern("ab")
                .pattern("c ")
                .define('a', ModTags.Items.DUSTS_REDSTONE)
                .define('b', ModTags.Items.GEMS_LAPIS)
                .define('c', PLATES_COPPER)
                .unlockedBy(getHasName(Items.LAPIS_LAZULI), has(Items.LAPIS_LAZULI))
                .save(writer, loc(getItemName(ModItems.IFF)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.THERMAL_IMAGING_GOGGLES)
                .pattern("aba")
                .pattern("cfc")
                .pattern("ede")
                .define('a', Items.EMERALD)
                .define('b', ModTags.Items.GLASS_PANES)
                .define('c', Items.SPIDER_EYE)
                .define('d', ModItems.CELL)
                .define('e', Items.OBSERVER)
                .define('f', Items.DAYLIGHT_DETECTOR)
                .unlockedBy(getHasName(Items.SPIDER_EYE), has(Items.SPIDER_EYE))
                .save(writer, loc(getItemName(ModItems.THERMAL_IMAGING_GOGGLES)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PARACHUTE)
                .pattern("aaa")
                .pattern("b b")
                .pattern("bcb")
                .define('a', Items.PHANTOM_MEMBRANE)
                .define('b', Items.STRING)
                .define('c', Items.LEATHER)
                .unlockedBy(getHasName(Items.PHANTOM_MEMBRANE), has(Items.PHANTOM_MEMBRANE))
                .save(writer, loc(getItemName(ModItems.PARACHUTE)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.TRANSCRIPT)
                .pattern("a")
                .pattern("b")
                .pattern("c")
                .define('a', ModTags.Items.NUGGETS_IRON)
                .define('b', Items.PAPER)
                .define('c', ItemTags.PLANKS)
                .unlockedBy(getHasName(Items.PAPER), has(Items.PAPER))
                .save(writer, loc(getItemName(ModItems.TRANSCRIPT)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.DATA_CHIP_SUBSTRATE, 4)
                .pattern("dad")
                .pattern("aba")
                .pattern("dad")
                .define('a', PLATES_COPPER)
                .define('b', Items.AMETHYST_SHARD)
                .define('d', Items.IRON_INGOT)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(writer, loc(getItemName(ModItems.DATA_CHIP_SUBSTRATE)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.COMMON_BLUEPRINT_DATA_CHIP)
                .pattern(" a ")
                .pattern("cbc")
                .pattern(" d ")
                .define('a', ModTags.Items.GLASS_BLOCKS)
                .define('b', ModItems.DATA_CHIP_SUBSTRATE)
                .define('c', Items.IRON_INGOT)
                .define('d', ModTags.Items.NUGGETS_GOLD)
                .unlockedBy(getHasName(ModItems.DATA_CHIP_SUBSTRATE), has(ModItems.DATA_CHIP_SUBSTRATE))
                .save(writer, loc(getItemName(ModItems.COMMON_BLUEPRINT_DATA_CHIP)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.RARE_BLUEPRINT_DATA_CHIP)
                .pattern(" a ")
                .pattern("cbc")
                .pattern(" d ")
                .define('a', ModTags.Items.GLASS_BLOCKS)
                .define('b', ModItems.DATA_CHIP_SUBSTRATE)
                .define('c', INGOTS_STEEL)
                .define('d', ModTags.Items.NUGGETS_GOLD)
                .unlockedBy(getHasName(ModItems.DATA_CHIP_SUBSTRATE), has(ModItems.DATA_CHIP_SUBSTRATE))
                .save(writer, loc(getItemName(ModItems.RARE_BLUEPRINT_DATA_CHIP)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.EPIC_BLUEPRINT_DATA_CHIP)
                .pattern(" a ")
                .pattern("cbc")
                .pattern(" d ")
                .define('a', ModTags.Items.GLASS_BLOCKS)
                .define('b', ModItems.DATA_CHIP_SUBSTRATE)
                .define('c', ModTags.Items.INGOTS_CEMENTED_CARBIDE)
                .define('d', ModTags.Items.NUGGETS_GOLD)
                .unlockedBy(getHasName(ModItems.DATA_CHIP_SUBSTRATE), has(ModItems.DATA_CHIP_SUBSTRATE))
                .save(writer, loc(getItemName(ModItems.EPIC_BLUEPRINT_DATA_CHIP)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.LEGENDARY_BLUEPRINT_DATA_CHIP)
                .pattern(" a ")
                .pattern("cbc")
                .pattern(" d ")
                .define('a', ModTags.Items.GLASS_BLOCKS)
                .define('b', ModItems.DATA_CHIP_SUBSTRATE)
                .define('c', Items.NETHERITE_SCRAP)
                .define('d', ModTags.Items.NUGGETS_GOLD)
                .unlockedBy(getHasName(ModItems.DATA_CHIP_SUBSTRATE), has(ModItems.DATA_CHIP_SUBSTRATE))
                .save(writer, loc(getItemName(ModItems.LEGENDARY_BLUEPRINT_DATA_CHIP)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.VIRTUAL_BLUEPRINT_DATA_CHIP)
                .pattern("eae")
                .pattern("cbc")
                .pattern("fdf")
                .define('a', ModTags.Items.GLASS_BLOCKS)
                .define('b', ModItems.DATA_CHIP_SUBSTRATE)
                .define('c', ModTags.Items.INGOTS_CEMENTED_CARBIDE)
                .define('d', ModTags.Items.NUGGETS_GOLD)
                .define('e', ModTags.Items.GEMS_AMETHYST)
                .define('f', ModTags.Items.GEMS_DIAMOND)
                .unlockedBy(getHasName(ModItems.DATA_CHIP_SUBSTRATE), has(ModItems.DATA_CHIP_SUBSTRATE))
                .save(writer, loc(getItemName(ModItems.VIRTUAL_BLUEPRINT_DATA_CHIP)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BOOST_RESEARCH_MODULE)
                .pattern("ada")
                .pattern("bcb")
                .pattern("ada")
                .define('a', ModTags.Items.INGOTS_CEMENTED_CARBIDE)
                .define('b', ModTags.Items.INGOTS_GOLD)
                .define('c', ModItems.DATA_CHIP_SUBSTRATE)
                .define('d', ModTags.Items.GEMS_EMERALD)
                .unlockedBy(getHasName(ModItems.DATA_CHIP_SUBSTRATE), has(ModItems.DATA_CHIP_SUBSTRATE))
                .save(writer, loc(getItemName(ModItems.BOOST_RESEARCH_MODULE)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.EFFECTIVE_RESEARCH_MODULE)
                .pattern("ada")
                .pattern("bcb")
                .pattern("ada")
                .define('a', ModTags.Items.INGOTS_CEMENTED_CARBIDE)
                .define('b', ModTags.Items.INGOTS_GOLD)
                .define('c', ModItems.DATA_CHIP_SUBSTRATE)
                .define('d', ModTags.Items.STORAGE_BLOCKS_REDSTONE)
                .unlockedBy(getHasName(ModItems.DATA_CHIP_SUBSTRATE), has(ModItems.DATA_CHIP_SUBSTRATE))
                .save(writer, loc(getItemName(ModItems.EFFECTIVE_RESEARCH_MODULE)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.DIRECTIONAL_RESEARCH_MODULE)
                .pattern("ada")
                .pattern("bcb")
                .pattern("ada")
                .define('a', ModTags.Items.INGOTS_CEMENTED_CARBIDE)
                .define('b', ModTags.Items.INGOTS_GOLD)
                .define('c', ModItems.ANCIENT_CPU)
                .define('d', ModTags.Items.GEMS_DIAMOND)
                .unlockedBy(getHasName(ModItems.ANCIENT_CPU), has(ModItems.ANCIENT_CPU))
                .save(writer, loc(getItemName(ModItems.DIRECTIONAL_RESEARCH_MODULE)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ENLARGEMENT_RESEARCH_MODULE)
                .pattern("ada")
                .pattern("bcb")
                .pattern("ada")
                .define('a', ModTags.Items.INGOTS_CEMENTED_CARBIDE)
                .define('b', ModTags.Items.INGOTS_GOLD)
                .define('c', ModItems.ANCIENT_CPU)
                .define('d', ModTags.Items.STORAGE_BLOCKS_IRON)
                .unlockedBy(getHasName(ModItems.ANCIENT_CPU), has(ModItems.ANCIENT_CPU))
                .save(writer, loc(getItemName(ModItems.ENLARGEMENT_RESEARCH_MODULE)))

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.AMMO_PERK_DATA_CHIP)
                .pattern(" a ")
                .pattern("cbc")
                .pattern(" d ")
                .define('a', ModTags.Items.GLASS_BLOCKS)
                .define('b', ModItems.DATA_CHIP_SUBSTRATE)
                .define('c', INGOTS_LEAD)
                .define('d', ModTags.Items.NUGGETS_GOLD)
                .unlockedBy(getHasName(ModItems.DATA_CHIP_SUBSTRATE), has(ModItems.DATA_CHIP_SUBSTRATE))
                .save(writer, loc(getItemName(ModItems.AMMO_PERK_DATA_CHIP)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.FUNCTIONAL_PERK_DATA_CHIP)
                .pattern(" a ")
                .pattern("cbc")
                .pattern(" d ")
                .define('a', ModTags.Items.GLASS_BLOCKS)
                .define('b', ModItems.DATA_CHIP_SUBSTRATE)
                .define('c', INGOTS_SILVER)
                .define('d', ModTags.Items.NUGGETS_GOLD)
                .unlockedBy(getHasName(ModItems.DATA_CHIP_SUBSTRATE), has(ModItems.DATA_CHIP_SUBSTRATE))
                .save(writer, loc(getItemName(ModItems.FUNCTIONAL_PERK_DATA_CHIP)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.DAMAGE_PERK_DATA_CHIP)
                .pattern(" a ")
                .pattern("cbc")
                .pattern(" d ")
                .define('a', ModTags.Items.GLASS_BLOCKS)
                .define('b', ModItems.DATA_CHIP_SUBSTRATE)
                .define('c', INGOTS_TUNGSTEN)
                .define('d', ModTags.Items.NUGGETS_GOLD)
                .unlockedBy(getHasName(ModItems.DATA_CHIP_SUBSTRATE), has(ModItems.DATA_CHIP_SUBSTRATE))
                .save(writer, loc(getItemName(ModItems.DAMAGE_PERK_DATA_CHIP)))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SKIN_SPRAY)
                .pattern(" ab")
                .pattern("cdc")
                .pattern(" c ")
                .define('a', Items.IRON_NUGGET)
                .define('b', Items.TRIPWIRE_HOOK)
                .define('c', PLATES_STEEL)
                .define('d', commonItemTag("dyes"))
                .unlockedBy(getHasName(Items.TRIPWIRE_HOOK), has(Items.TRIPWIRE_HOOK))
                .save(writer, loc(getItemName(ModItems.SKIN_SPRAY)))
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.GALENA, 9)
                .requires(commonItemTag("storage_blocks/raw_lead"))
                .unlockedBy(getHasName(ModItems.RAW_GALENA_BLOCK), has(commonItemTag("storage_blocks/raw_lead")))
                .save(writer, loc("${getItemName(ModItems.GALENA)}_from_raw_block"))
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SCHEELITE, 9)
                .requires(commonItemTag("storage_blocks/raw_tungsten"))
                .unlockedBy(
                    getHasName(ModItems.RAW_SCHEELITE_BLOCK),
                    has(commonItemTag("storage_blocks/raw_tungsten"))
                )
                .save(writer, loc("${getItemName(ModItems.SCHEELITE)}_from_raw_block"))
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.RAW_SILVER, 9)
                .requires(commonItemTag("storage_blocks/raw_silver"))
                .unlockedBy(
                    getHasName(ModItems.RAW_SILVER_BLOCK),
                    has(commonItemTag("storage_blocks/raw_silver"))
                )
                .save(writer, loc("${getItemName(ModItems.RAW_SILVER)}_from_raw_block"))

            VehicleAssemblingRecipeBuilder.item(
                ModItems.VEHICLE_KEY,
                1,
                VehicleAssemblingRecipe.Category.MISC
            )
                .require(INGOTS_STEEL, 1)
                .require(ModTags.Items.INGOTS_IRON, 2)
                .require(ModTags.Items.INGOTS_COPPER, 2)
                .unlockedBy(getHasName(ModItems.STEEL_INGOT), has(INGOTS_STEEL))
                .save(writer, loc(getItemName(ModItems.VEHICLE_KEY) + "_assembling"))
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.VEHICLE_KEY, 1)
                .requires(ModItems.VEHICLE_KEY)
                .unlockedBy(getHasName(ModItems.VEHICLE_KEY), has(ModItems.VEHICLE_KEY))
                .save(writer, loc("${getItemName(ModItems.VEHICLE_KEY)}_reset"))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.TOWLINE, 1)
                .pattern(" a ")
                .pattern("cbc")
                .pattern(" a ")
                .define('a', Items.CHAIN)
                .define('b', Items.LEAD)
                .define('c', INGOTS_STEEL)
                .unlockedBy(getHasName(Items.LEAD), has(Items.LEAD))
                .save(writer, loc("${getItemName(ModItems.TOWLINE)}"))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.TOW_BAR, 1)
                .pattern("a")
                .pattern("b")
                .pattern("a")
                .define('a', Items.TRIPWIRE_HOOK)
                .define('b', INGOTS_STEEL)
                .unlockedBy(getHasName(Items.TRIPWIRE_HOOK), has(Items.TRIPWIRE_HOOK))
                .save(writer, loc("${getItemName(ModItems.TOW_BAR)}"))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.CATAPULT_SHUTTLE, 1)
                .pattern("ba ")
                .pattern("bbb")
                .pattern("dcd")
                .define('a', Items.TRIPWIRE_HOOK)
                .define('b', INGOTS_STEEL)
                .define('c', Items.COPPER_INGOT)
                .define('d', Items.REDSTONE)
                .unlockedBy(getHasName(Items.TRIPWIRE_HOOK), has(Items.TRIPWIRE_HOOK))
                .save(writer, loc("${getItemName(ModItems.CATAPULT_SHUTTLE)}"))
        }

        private fun buildSpecialRecipes(writer: Consumer<FinishedRecipe>) {
            SpecialRecipeBuilder.special(ModRecipes.POTION_MORTAR_SHELL_SERIALIZER)
                .save(writer, "superbwarfare:potion_mortar_shell")
            SpecialRecipeBuilder.special(ModRecipes.SMOKE_DYE_SERIALIZER).save(writer, "superbwarfare:smoke_dye")
            SpecialRecipeBuilder.special(ModRecipes.VEHICLE_RESET_SERIALIZER)
                .save(writer, "superbwarfare:vehicle_reset")
        }

        private fun buildResearchRecipes(writer: Consumer<FinishedRecipe>) {
            this.generateBlueprintResearchingRecipe(writer, Rarity.COMMON)
            this.generateBlueprintResearchingRecipe(writer, Rarity.RARE)
            this.generateBlueprintResearchingRecipe(writer, Rarity.EPIC)
            this.generateBlueprintResearchingRecipe(writer, ModRarities.LEGENDARY)
            this.generateBlueprintResearchingRecipe(writer, ModRarities.SUPERB)
            this.generateBlueprintResearchingRecipe(writer, ModRarities.VIRTUAL)

            Perk.Type.entries.forEach { this.generatePerkResearchingRecipe(writer, it) }
        }

        fun copyBlueprint(writer: Consumer<FinishedRecipe>, result: ItemLike) {
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, 2)
                .pattern("ABA")
                .pattern("ACA")
                .pattern("AAA")
                .define('A', Items.LAPIS_LAZULI)
                .define('B', Items.PAPER)
                .define('C', result)
                .unlockedBy(getHasName(result), has(result))
                .save(writer, loc("${getItemName(result)}_copy"))
            ResearchingRecipeBuilder.item(result.asItem(), 2, result)
                .base(Items.PAPER)
                .addition(Items.LAPIS_LAZULI)
                .time(600)
                .unlockedBy(getHasName(result), has(result))
                .save(writer, loc("${getItemName(result)}_copy_researching"))
        }

        fun gunSmithing(
            writer: Consumer<FinishedRecipe>,
            blueprint: ItemLike,
            rarity: GunRarity,
            tagKey: TagKey<Item>,
            pResultItem: Item
        ) {
            gunSmithing(writer, blueprint, rarity, Ingredient.of(tagKey), pResultItem)
        }

        fun gunSmithing(
            writer: Consumer<FinishedRecipe>,
            blueprint: ItemLike,
            rarity: GunRarity,
            ingredient: ItemLike,
            pResultItem: Item
        ) {
            gunSmithing(writer, blueprint, rarity, Ingredient.of(ingredient), pResultItem)
        }

        fun gunSmithing(
            writer: Consumer<FinishedRecipe>,
            blueprint: ItemLike,
            rarity: GunRarity,
            ingredient: Ingredient,
            pResultItem: Item
        ) {
            val pack: ItemLike = when (rarity) {
                GunRarity.COMMON -> ModItems.COMMON_MATERIAL_PACK
                GunRarity.RARE -> ModItems.RARE_MATERIAL_PACK
                GunRarity.EPIC -> ModItems.EPIC_MATERIAL_PACK
                GunRarity.LEGENDARY -> ModItems.LEGENDARY_MATERIAL_PACK
                GunRarity.SUPERB -> ModItems.SUPERB_MATERIAL_PACK
                GunRarity.VIRTUAL -> ModItems.VIRTUAL_MATERIAL_PACK
            }

            SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(blueprint),
                Ingredient.of(pack),
                ingredient,
                RecipeCategory.COMBAT,
                pResultItem
            )
                .unlocks(getHasName(blueprint), has(blueprint))
                .save(writer, loc(getItemName(pResultItem) + "_smithing"))
        }

        fun perkLoc(perk: Perk): ResourceLocation {
            return loc("perk/" + getItemName(ModItems.PERK_ITEMS[perk]!!))
        }

        fun getEntityTypeName(entityType: EntityType<*>): String {
            return EntityType.getKey(entityType).path
        }

        // 生成材料包所有材料的配方
        fun generateMaterialRecipes(writer: Consumer<FinishedRecipe>, material: Materials, ingredient: ItemLike) {
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, material.barrel)
                .pattern("AAA")
                .define('A', ingredient)
                .unlockedBy(getHasName(ingredient), has(ingredient))
                .save(writer, loc(getItemName(material.barrel)))

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, material.action)
                .pattern("AAA")
                .pattern("  A")
                .define('A', ingredient)
                .unlockedBy(getHasName(ingredient), has(ingredient))
                .save(writer, loc(getItemName(material.action)))

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, material.spring)
                .pattern("A")
                .pattern("A")
                .pattern("A")
                .define('A', ingredient)
                .unlockedBy(getHasName(ingredient), has(ingredient))
                .save(writer, loc(getItemName(material.spring)))

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, material.trigger)
                .pattern("BA")
                .pattern(" A")
                .define('A', ingredient)
                .define('B', Items.TRIPWIRE_HOOK)
                .unlockedBy(getHasName(ingredient), has(ingredient))
                .save(writer, loc(getItemName(material.trigger)))
        }

        fun generateMaterialRecipes(
            writer: Consumer<FinishedRecipe>,
            material: Materials,
            tagKey: TagKey<Item>,
            name: Item
        ) {
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, material.barrel)
                .pattern("AAA")
                .define('A', tagKey)
                .unlockedBy(getHasName(name), has(tagKey))
                .save(writer, loc(getItemName(material.barrel)))

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, material.action)
                .pattern("AAA")
                .pattern("  A")
                .define('A', tagKey)
                .unlockedBy(getHasName(name), has(tagKey))
                .save(writer, loc(getItemName(material.action)))

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, material.spring)
                .pattern("A")
                .pattern("A")
                .pattern("A")
                .define('A', tagKey)
                .unlockedBy(getHasName(name), has(tagKey))
                .save(writer, loc(getItemName(material.spring)))

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, material.trigger)
                .pattern("BA")
                .pattern(" A")
                .define('A', tagKey)
                .define('B', Items.TRIPWIRE_HOOK)
                .unlockedBy(getHasName(name), has(tagKey))
                .save(writer, loc(getItemName(material.trigger)))
        }

        fun generateSmithingMaterialRecipe(
            writer: Consumer<FinishedRecipe>,
            material: Materials,
            result: Materials,
            template: Item,
            ingredient: Item
        ) {
            SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(template),
                Ingredient.of(material.barrel),
                Ingredient.of(ingredient),
                RecipeCategory.MISC,
                result.barrel
            )
                .unlocks(getHasName(template), has(template))
                .unlocks(getHasName(material.barrel), has(material.barrel))
                .save(writer, loc(getItemName(result.barrel)))

            SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(template),
                Ingredient.of(material.action),
                Ingredient.of(ingredient),
                RecipeCategory.MISC,
                result.action
            )
                .unlocks(getHasName(template), has(template))
                .unlocks(getHasName(material.action), has(material.action))
                .save(writer, loc(getItemName(result.action)))

            SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(template),
                Ingredient.of(material.spring),
                Ingredient.of(ingredient),
                RecipeCategory.MISC,
                result.spring
            )
                .unlocks(getHasName(template), has(template))
                .unlocks(getHasName(material.spring), has(material.spring))
                .save(writer, loc(getItemName(result.spring)))

            SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(template),
                Ingredient.of(material.trigger),
                Ingredient.of(ingredient),
                RecipeCategory.MISC,
                result.trigger
            )
                .unlocks(getHasName(template), has(template))
                .unlocks(getHasName(material.trigger), has(material.trigger))
                .save(writer, loc(getItemName(result.trigger)))
        }

        fun generateMaterialPackRecipe(writer: Consumer<FinishedRecipe>, material: Materials, pack: Item) {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, pack)
                .requires(material.barrel)
                .requires(material.action)
                .requires(material.spring)
                .requires(material.trigger)
                .unlockedBy(getHasName(material.barrel), has(material.barrel))
                .unlockedBy(getHasName(material.action), has(material.action))
                .unlockedBy(getHasName(material.spring), has(material.spring))
                .unlockedBy(getHasName(material.trigger), has(material.trigger))
                .save(writer, loc(getItemName(pack)))
        }

        fun getPotionIngredient(potion: Potion): Ingredient {
            val stack = ItemStack(Items.POTION)
            PotionUtils.setPotion(stack, potion)
            return DefaultCustomIngredients.nbt(stack, true)
        }

        fun generateBlueprintResearchingRecipe(writer: Consumer<FinishedRecipe>, rarity: Rarity) {
            val tag: TagKey<Item>
            val enlargedTag: TagKey<Item>?
            val input: Item
            val time: Int
            when (rarity) {
                Rarity.RARE -> {
                    tag = ModTags.Items.RARE_BLUEPRINT
                    enlargedTag = ModTags.Items.ENLARGED_RARE_BLUEPRINT
                    input = ModItems.RARE_BLUEPRINT_DATA_CHIP
                    time = 600
                }

                Rarity.EPIC -> {
                    tag = ModTags.Items.EPIC_BLUEPRINT
                    enlargedTag = ModTags.Items.ENLARGED_EPIC_BLUEPRINT
                    input = ModItems.EPIC_BLUEPRINT_DATA_CHIP
                    time = 1500
                }

                ModRarities.LEGENDARY -> {
                    tag = ModTags.Items.LEGENDARY_BLUEPRINT
                    enlargedTag = ModTags.Items.ENLARGED_LEGENDARY_BLUEPRINT
                    input = ModItems.LEGENDARY_BLUEPRINT_DATA_CHIP
                    time = 3000
                }

                ModRarities.SUPERB -> {
                    tag = ModTags.Items.SUPERB_BLUEPRINT
                    enlargedTag = null
                    input = ModItems.SUPERB_BLUEPRINT_DATA_CHIP
                    time = 6000
                }

                ModRarities.VIRTUAL -> {
                    tag = ModTags.Items.VIRTUAL_BLUEPRINT
                    enlargedTag = null
                    input = ModItems.VIRTUAL_BLUEPRINT_DATA_CHIP
                    time = 2400
                }

                else -> {
                    tag = ModTags.Items.COMMON_BLUEPRINT
                    enlargedTag = ModTags.Items.ENLARGED_COMMON_BLUEPRINT
                    input = ModItems.COMMON_BLUEPRINT_DATA_CHIP
                    time = 300
                }
            }

            ResearchingRecipeBuilder.tag(tag, input = input)
                .base(Items.PAPER)
                .addition(Items.LAPIS_LAZULI)
                .time(time)
                .unlockedBy(getHasName(input), has(input))
                .save(writer, loc(getItemName(input) + "_researching"))
            ResearchingRecipeBuilder.tag(tag, 2, input)
                .base(Items.PAPER)
                .addition(Items.LAPIS_LAZULI)
                .special(ModItems.BOOST_RESEARCH_MODULE)
                .time(time)
                .color(1)
                .unlockedBy(getHasName(input), has(input))
                .unlockedBy(getHasName(ModItems.BOOST_RESEARCH_MODULE), has(ModItems.BOOST_RESEARCH_MODULE))
                .save(writer, loc(getItemName(input) + "_researching_boost"))
            ResearchingRecipeBuilder.tag(tag, input = input)
                .base(Items.PAPER)
                .addition(Items.LAPIS_LAZULI)
                .special(ModItems.DIRECTIONAL_RESEARCH_MODULE)
                .time(time)
                .color(2)
                .selectable()
                .unlockedBy(getHasName(input), has(input))
                .unlockedBy(
                    getHasName(ModItems.DIRECTIONAL_RESEARCH_MODULE),
                    has(ModItems.DIRECTIONAL_RESEARCH_MODULE)
                )
                .save(writer, loc(getItemName(input) + "_researching_directional"))
            ResearchingRecipeBuilder.tag(tag, input = input)
                .base(Items.PAPER)
                .addition(Items.LAPIS_LAZULI)
                .special(ModItems.EFFECTIVE_RESEARCH_MODULE)
                .time(time / 5)
                .color(3)
                .unlockedBy(getHasName(input), has(input))
                .unlockedBy(
                    getHasName(ModItems.EFFECTIVE_RESEARCH_MODULE),
                    has(ModItems.EFFECTIVE_RESEARCH_MODULE)
                )
                .save(writer, loc(getItemName(input) + "_researching_effective"))
            if (enlargedTag != null) {
                ResearchingRecipeBuilder.tag(enlargedTag, input = input)
                    .base(Items.PAPER)
                    .addition(Items.LAPIS_LAZULI)
                    .special(ModItems.ENLARGEMENT_RESEARCH_MODULE)
                    .time(time * 2)
                    .color(4)
                    .unlockedBy(getHasName(input), has(input))
                    .unlockedBy(
                        getHasName(ModItems.ENLARGEMENT_RESEARCH_MODULE),
                        has(ModItems.ENLARGEMENT_RESEARCH_MODULE)
                    )
                    .save(writer, loc(getItemName(input) + "_researching_enlargement"))
            }

            ResearchingRecipeBuilder.item(input, input = tag)
                .base(ModItems.DATA_CHIP_SUBSTRATE)
                .addition(Items.AMETHYST_SHARD)
                .time(200)
                .unlockedBy("has_${tag.location.path}", has(tag))
                .save(writer, loc(getItemName(input) + "_from_blueprint"))
            ResearchingRecipeBuilder.item(input, 2, tag)
                .base(ModItems.DATA_CHIP_SUBSTRATE)
                .addition(Items.AMETHYST_SHARD)
                .time(200)
                .special(ModItems.BOOST_RESEARCH_MODULE)
                .color(1)
                .unlockedBy("has_${tag.location.path}", has(tag))
                .unlockedBy(getHasName(ModItems.BOOST_RESEARCH_MODULE), has(ModItems.BOOST_RESEARCH_MODULE))
                .save(writer, loc(getItemName(input) + "_from_blueprint_boost"))
        }

        fun generatePerkResearchingRecipe(writer: Consumer<FinishedRecipe>, type: Perk.Type) {
            val inputPerk: Item
            val resTag: TagKey<Item>
            when (type) {
                Perk.Type.AMMO -> {
                    inputPerk = ModItems.AMMO_PERK_DATA_CHIP
                    resTag = ModTags.Items.RESEARCHABLE_AMMO_PERK
                }

                Perk.Type.FUNCTIONAL -> {
                    inputPerk = ModItems.FUNCTIONAL_PERK_DATA_CHIP
                    resTag = ModTags.Items.RESEARCHABLE_FUNCTIONAL_PERK
                }

                Perk.Type.DAMAGE -> {
                    inputPerk = ModItems.DAMAGE_PERK_DATA_CHIP
                    resTag = ModTags.Items.RESEARCHABLE_DAMAGE_PERK
                }
            }

            ResearchingRecipeBuilder.tag(resTag, 1, inputPerk)
                .base(ModItems.EMPTY_PERK)
                .time(600)
                .unlockedBy(getHasName(inputPerk), has(inputPerk))
                .save(writer, loc(getItemName(inputPerk) + "_researching"))
            ResearchingRecipeBuilder.tag(resTag, 2, inputPerk)
                .base(ModItems.EMPTY_PERK)
                .special(ModItems.BOOST_RESEARCH_MODULE)
                .time(600)
                .color(1)
                .unlockedBy(getHasName(inputPerk), has(inputPerk))
                .unlockedBy(getHasName(ModItems.BOOST_RESEARCH_MODULE), has(ModItems.BOOST_RESEARCH_MODULE))
                .save(writer, loc(getItemName(inputPerk) + "_researching_boost"))
            ResearchingRecipeBuilder.tag(resTag, 1, inputPerk)
                .base(ModItems.EMPTY_PERK)
                .special(ModItems.DIRECTIONAL_RESEARCH_MODULE)
                .time(600)
                .color(2)
                .selectable()
                .unlockedBy(getHasName(inputPerk), has(inputPerk))
                .unlockedBy(
                    getHasName(ModItems.DIRECTIONAL_RESEARCH_MODULE),
                    has(ModItems.DIRECTIONAL_RESEARCH_MODULE)
                )
                .save(writer, loc(getItemName(inputPerk) + "_researching_directional"))
            ResearchingRecipeBuilder.tag(resTag, 1, inputPerk)
                .base(ModItems.EMPTY_PERK)
                .special(ModItems.EFFECTIVE_RESEARCH_MODULE)
                .time(120)
                .color(3)
                .unlockedBy(getHasName(inputPerk), has(inputPerk))
                .unlockedBy(
                    getHasName(ModItems.EFFECTIVE_RESEARCH_MODULE),
                    has(ModItems.EFFECTIVE_RESEARCH_MODULE)
                )
                .save(writer, loc(getItemName(inputPerk) + "_researching_effective"))

            ResearchingRecipeBuilder.item(inputPerk, 1, resTag)
                .base(ModItems.DATA_CHIP_SUBSTRATE)
                .addition(Items.AMETHYST_SHARD)
                .time(200)
                .unlockedBy("has_${resTag.location.path}", has(resTag))
                .save(writer, loc(getItemName(inputPerk) + "_from_blueprint"))
            ResearchingRecipeBuilder.item(inputPerk, 2, resTag)
                .base(ModItems.DATA_CHIP_SUBSTRATE)
                .addition(Items.AMETHYST_SHARD)
                .time(200)
                .special(ModItems.BOOST_RESEARCH_MODULE)
                .color(1)
                .unlockedBy("has_${resTag.location.path}", has(resTag))
                .unlockedBy(getHasName(ModItems.BOOST_RESEARCH_MODULE), has(ModItems.BOOST_RESEARCH_MODULE))
                .save(writer, loc(getItemName(inputPerk) + "_from_blueprint_boost"))
        }
    }
}
