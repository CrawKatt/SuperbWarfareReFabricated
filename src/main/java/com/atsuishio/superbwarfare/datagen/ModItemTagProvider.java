package com.atsuishio.superbwarfare.datagen;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

import static com.atsuishio.superbwarfare.init.ModTags.commonItemTag;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {

    public ModItemTagProvider(FabricDataOutput packOutput, CompletableFuture<HolderLookup.Provider> providerCompletableFuture,
                              FabricTagProvider.BlockTagProvider blockTagProvider) {
        super(packOutput, providerCompletableFuture, blockTagProvider);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        getOrCreateTagBuilder(commonItemTag("dusts")).addTag(commonItemTag("dusts/coal_coke")).addTag(commonItemTag("dusts/tungsten"));
        getOrCreateTagBuilder(commonItemTag("dusts/coal_coke")).add(ModItems.COAL_POWDER);
        getOrCreateTagBuilder(commonItemTag("dusts/iron")).add(ModItems.IRON_POWDER);
        getOrCreateTagBuilder(commonItemTag("dusts/tungsten")).add(ModItems.TUNGSTEN_POWDER);

        getOrCreateTagBuilder(commonItemTag("ingots")).addTag(commonItemTag("ingots/lead")).addTag(commonItemTag("ingots/steel")).addTag(commonItemTag("ingots/tungsten")).addTag(commonItemTag("ingots/silver"));
        getOrCreateTagBuilder(commonItemTag("ingots/lead")).add(ModItems.LEAD_INGOT);
        getOrCreateTagBuilder(commonItemTag("ingots/steel")).add(ModItems.STEEL_INGOT);
        getOrCreateTagBuilder(commonItemTag("ingots/tungsten")).add(ModItems.TUNGSTEN_INGOT);
        getOrCreateTagBuilder(commonItemTag("ingots/silver")).add(ModItems.SILVER_INGOT);

        getOrCreateTagBuilder(ItemTags.ARMOR_ENCHANTABLE).add(ModItems.GE_HELMET_M_35, ModItems.RU_CHEST_6B43, ModItems.RU_HELMET_6B47, ModItems.US_CHEST_IOTV, ModItems.US_HELMET_PASGT);
        getOrCreateTagBuilder(ItemTags.HEAD_ARMOR).add(ModItems.GE_HELMET_M_35, ModItems.RU_HELMET_6B47, ModItems.US_HELMET_PASGT);
        getOrCreateTagBuilder(ItemTags.HEAD_ARMOR_ENCHANTABLE).add(ModItems.GE_HELMET_M_35, ModItems.RU_HELMET_6B47, ModItems.US_HELMET_PASGT);
        getOrCreateTagBuilder(ItemTags.CHEST_ARMOR).add(ModItems.RU_CHEST_6B43, ModItems.US_CHEST_IOTV);
        getOrCreateTagBuilder(ItemTags.CHEST_ARMOR_ENCHANTABLE).add(ModItems.RU_CHEST_6B43, ModItems.US_CHEST_IOTV);

        getOrCreateTagBuilder(ItemTags.SWORD_ENCHANTABLE).add(ModItems.CROWBAR, ModItems.HAMMER, ModItems.KNIFE, ModItems.T_BATON,
                ModItems.ELECTRIC_BATON, ModItems.STEEL_PIPE, ModItems.GOLDEN_HAMMER, ModItems.STEEL_HAMMER, ModItems.DIAMOND_HAMMER,
                ModItems.CEMENTED_CARBIDE_HAMMER, ModItems.NETHERITE_HAMMER);

        getOrCreateTagBuilder(ModTags.Items.INGOTS_STEEL).addTag(commonItemTag("ingots/steel"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("dreamaticvoyage", "fukamizu_bread_ingot"));
        getOrCreateTagBuilder(ModTags.Items.INGOTS_CEMENTED_CARBIDE).add(ModItems.CEMENTED_CARBIDE_INGOT)
                .addOptional(ResourceLocation.fromNamespaceAndPath("dreamaticvoyage", "hqss_bread_ingot"));

        getOrCreateTagBuilder(commonItemTag("storage_blocks")).addTag(commonItemTag("storage_blocks/lead")).addTag(commonItemTag("storage_blocks/steel")).addTag(commonItemTag("storage_blocks/tungsten")).addTag(commonItemTag("storage_blocks/silver"));
        getOrCreateTagBuilder(commonItemTag("storage_blocks/lead")).add(ModItems.LEAD_BLOCK);
        getOrCreateTagBuilder(commonItemTag("storage_blocks/steel")).add(ModItems.STEEL_BLOCK);
        getOrCreateTagBuilder(commonItemTag("storage_blocks/tungsten")).add(ModItems.TUNGSTEN_BLOCK);
        getOrCreateTagBuilder(commonItemTag("storage_blocks/silver")).add(ModItems.SILVER_BLOCK);

        getOrCreateTagBuilder(ModTags.Items.STORAGE_BLOCK_STEEL).addTag(commonItemTag("storage_blocks/steel"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("dreamaticvoyage", "fukamizu_bread_bricks"));
        getOrCreateTagBuilder(ModTags.Items.STORAGE_BLOCK_CEMENTED_CARBIDE).add(ModItems.CEMENTED_CARBIDE_BLOCK)
                .addOptional(ResourceLocation.fromNamespaceAndPath("dreamaticvoyage", "hqss_bread_bricks"));

        getOrCreateTagBuilder(commonItemTag("ores")).addTag(commonItemTag("ores/lead")).addTag(commonItemTag("ores/tungsten")).addTag(commonItemTag("ores/silver"));
        getOrCreateTagBuilder(commonItemTag("ores/lead")).add(ModItems.GALENA_ORE, ModItems.DEEPSLATE_GALENA_ORE);
        getOrCreateTagBuilder(commonItemTag("ores/tungsten")).add(ModItems.SCHEELITE_ORE, ModItems.DEEPSLATE_SCHEELITE_ORE);
        getOrCreateTagBuilder(commonItemTag("ores/silver")).add(ModItems.SILVER_ORE, ModItems.DEEPSLATE_SILVER_ORE);

        getOrCreateTagBuilder(commonItemTag("raw_materials")).addTag(commonItemTag("raw_materials/lead")).addTag(commonItemTag("raw_materials/tungsten")).addTag(commonItemTag("raw_materials/silver"));
        getOrCreateTagBuilder(commonItemTag("raw_materials/lead")).add(ModItems.GALENA);
        getOrCreateTagBuilder(commonItemTag("raw_materials/tungsten")).add(ModItems.SCHEELITE);
        getOrCreateTagBuilder(commonItemTag("raw_materials/silver")).add(ModItems.RAW_SILVER);

        getOrCreateTagBuilder(commonItemTag("ingots/scheelite")).add(ModItems.TUNGSTEN_INGOT);
        getOrCreateTagBuilder(commonItemTag("ores/scheelite")).add(ModItems.SCHEELITE_ORE, ModItems.DEEPSLATE_SCHEELITE_ORE);
        getOrCreateTagBuilder(commonItemTag("raw_materials/scheelite")).add(ModItems.SCHEELITE);
        getOrCreateTagBuilder(commonItemTag("dusts/scheelite")).add(ModItems.TUNGSTEN_POWDER);
        getOrCreateTagBuilder(commonItemTag("storage_blocks/scheelite")).add(ModItems.TUNGSTEN_BLOCK);

        getOrCreateTagBuilder(commonItemTag("ore_rates/singular")).add(ModItems.GALENA_ORE, ModItems.DEEPSLATE_GALENA_ORE,
                ModItems.SCHEELITE_ORE, ModItems.DEEPSLATE_SCHEELITE_ORE,
                ModItems.SILVER_ORE, ModItems.DEEPSLATE_SILVER_ORE);

        getOrCreateTagBuilder(commonItemTag("ores_in_ground_stone")).add(ModItems.GALENA_ORE, ModItems.SCHEELITE_ORE, ModItems.SILVER_ORE);
        getOrCreateTagBuilder(commonItemTag("ores_in_ground_deepslate")).add(ModItems.DEEPSLATE_GALENA_ORE, ModItems.DEEPSLATE_SCHEELITE_ORE, ModItems.DEEPSLATE_SILVER_ORE);

        getOrCreateTagBuilder(commonItemTag("plates")).addTag(commonItemTag("plates/copper"));
        getOrCreateTagBuilder(commonItemTag("plates/copper")).add(ModItems.COPPER_PLATE);

        getOrCreateTagBuilder(commonItemTag("tools/crowbar")).add(ModItems.CROWBAR);

        getOrCreateTagBuilder(ModTags.Items.HAMMER).add(ModItems.HAMMER, ModItems.GOLDEN_HAMMER, ModItems.STEEL_HAMMER, ModItems.DIAMOND_HAMMER,
                ModItems.CEMENTED_CARBIDE_HAMMER, ModItems.NETHERITE_HAMMER);
        getOrCreateTagBuilder(ModTags.Items.TOOLS_HAMMER).addTag(ModTags.Items.HAMMER);

        getOrCreateTagBuilder(commonItemTag("armors")).add(ModItems.RU_HELMET_6B47, ModItems.US_HELMET_PASGT, ModItems.GE_HELMET_M_35);
        getOrCreateTagBuilder(commonItemTag("armors")).add(ModItems.RU_CHEST_6B43, ModItems.US_CHEST_IOTV);

        getOrCreateTagBuilder(ModTags.Items.ANIMATED_PISTOL).add(
                ModItems.TASER,
                ModItems.GLOCK_17,
                ModItems.GLOCK_18,
                ModItems.MP_443,
                ModItems.M_1911,
                ModItems.TRACHELIUM,
                ModItems.REPAIR_TOOL);
        getOrCreateTagBuilder(ModTags.Items.ANIMATED_SNIPER).add(
                ModItems.MOSIN_NAGANT,
                ModItems.SVD,
                ModItems.AWM,
                ModItems.NTW_20);
        getOrCreateTagBuilder(ModTags.Items.ANIMATED_RIFLE).add(
                ModItems.AK_47,
                ModItems.AK_12,
                ModItems.SKS,
                ModItems.M_4,
                ModItems.HK_416,
                ModItems.QBZ_95,
                ModItems.QBZ_191,
                ModItems.INSIDIOUS,
                ModItems.MK_14,
                ModItems.MARLIN,
                ModItems.K_98,
                ModItems.M_98B,
                ModItems.SENTINEL,
                ModItems.HUNTING_RIFLE,
                ModItems.QL_1031);
        getOrCreateTagBuilder(ModTags.Items.ANIMATED_SHOTGUN).add(
                ModItems.HOMEMADE_SHOTGUN,
                ModItems.M_870,
                ModItems.AA_12,
                ModItems.M_79,
                ModItems.SECONDARY_CATACLYSM);
        getOrCreateTagBuilder(ModTags.Items.ANIMATED_SMG).add(
                ModItems.MP_5,
                ModItems.VECTOR);
        getOrCreateTagBuilder(ModTags.Items.ANIMATED_RPG).add(
                ModItems.RPG,
                ModItems.JAVELIN,
                ModItems.IGLA_9K38);
        getOrCreateTagBuilder(ModTags.Items.ANIMATED_MG).add(
                ModItems.DEVOTION,
                ModItems.RPK,
                ModItems.M_60,
                ModItems.M_2_HB);
        getOrCreateTagBuilder(ModTags.Items.ANIMATED_MINIGUN).add(
                ModItems.MINIGUN);

        getOrCreateTagBuilder(ModTags.Items.GUN).add(
                ModItems.REPAIR_TOOL,
                ModItems.TASER,
                ModItems.GLOCK_17,
                ModItems.GLOCK_18,
                ModItems.MP_443,
                ModItems.M_1911,
                ModItems.HOMEMADE_SHOTGUN,
                ModItems.TRACHELIUM,
                ModItems.MP_5,
                ModItems.VECTOR,
                ModItems.AK_47,
                ModItems.AK_12,
                ModItems.SKS,
                ModItems.M_4,
                ModItems.HK_416,
                ModItems.QBZ_95,
                ModItems.QBZ_191,
                ModItems.INSIDIOUS,
                ModItems.MK_14,
                ModItems.QL_1031,
                ModItems.MARLIN,
                ModItems.K_98,
                ModItems.MOSIN_NAGANT,
                ModItems.SVD,
                ModItems.AWM,
                ModItems.M_98B,
                ModItems.SENTINEL,
                ModItems.HUNTING_RIFLE,
                ModItems.NTW_20,
                ModItems.M_870,
                ModItems.AA_12,
                ModItems.DEVOTION,
                ModItems.RPK,
                ModItems.M_60,
                ModItems.M_2_HB,
                ModItems.MINIGUN,
                ModItems.M_79,
                ModItems.SECONDARY_CATACLYSM,
                ModItems.RPG,
                ModItems.JAVELIN,
                ModItems.IGLA_9K38,
                ModItems.AURELIA_SCEPTRE,
                ModItems.BOCEK,
                ModItems.VEHICLE_GUN);

        getOrCreateTagBuilder(ModTags.Items.SMG).add(ModItems.VECTOR, ModItems.MP_5);
        getOrCreateTagBuilder(ModTags.Items.RIFLE).add(ModItems.M_4, ModItems.HK_416, ModItems.SKS,
                ModItems.MK_14, ModItems.MARLIN, ModItems.AK_47, ModItems.AK_12, ModItems.QBZ_95, ModItems.QBZ_191);
        getOrCreateTagBuilder(ModTags.Items.SNIPER_RIFLE).add(ModItems.HUNTING_RIFLE, ModItems.SENTINEL, ModItems.NTW_20,
                ModItems.SVD, ModItems.M_98B, ModItems.K_98, ModItems.MOSIN_NAGANT, ModItems.AWM, ModItems.QL_1031);
        getOrCreateTagBuilder(ModTags.Items.SHOTGUN).add(ModItems.HOMEMADE_SHOTGUN, ModItems.M_870, ModItems.AA_12);
        getOrCreateTagBuilder(ModTags.Items.MACHINE_GUN).add(ModItems.MINIGUN, ModItems.M_2_HB);
        getOrCreateTagBuilder(ModTags.Items.LAUNCHER).add(ModItems.RPG, ModItems.JAVELIN, ModItems.IGLA_9K38,
                ModItems.M_79, ModItems.SECONDARY_CATACLYSM);

        getOrCreateTagBuilder(ModTags.Items.MILITARY_ARMOR).add(ModItems.RU_CHEST_6B43, ModItems.US_CHEST_IOTV);

        getOrCreateTagBuilder(ModTags.Items.BLUEPRINT).addTag(ModTags.Items.COMMON_BLUEPRINT).addTag(ModTags.Items.RARE_BLUEPRINT).addTag(ModTags.Items.EPIC_BLUEPRINT)
                .addTag(ModTags.Items.LEGENDARY_BLUEPRINT).addTag(ModTags.Items.CANNON_BLUEPRINT);

        getOrCreateTagBuilder(ModTags.Items.COMMON_BLUEPRINT).add(ModItems.GLOCK_17_BLUEPRINT, ModItems.MP_443_BLUEPRINT, ModItems.MARLIN_BLUEPRINT,
                ModItems.TASER_BLUEPRINT, ModItems.M_1911_BLUEPRINT);

        getOrCreateTagBuilder(ModTags.Items.RARE_BLUEPRINT).add(ModItems.GLOCK_18_BLUEPRINT, ModItems.M_79_BLUEPRINT, ModItems.M_4_BLUEPRINT,
                ModItems.SKS_BLUEPRINT, ModItems.M_870_BLUEPRINT, ModItems.AK_47_BLUEPRINT, ModItems.K_98_BLUEPRINT,
                ModItems.MOSIN_NAGANT_BLUEPRINT, ModItems.M_2_HB_BLUEPRINT, ModItems.HK_416_BLUEPRINT, ModItems.AK_12_BLUEPRINT,
                ModItems.QBZ_95_BLUEPRINT, ModItems.RPG_BLUEPRINT, ModItems.HUNTING_RIFLE_BLUEPRINT);

        getOrCreateTagBuilder(ModTags.Items.EPIC_BLUEPRINT).add(ModItems.TRACHELIUM_BLUEPRINT, ModItems.BOCEK_BLUEPRINT, ModItems.RPK_BLUEPRINT,
                ModItems.VECTOR_BLUEPRINT, ModItems.MK_14_BLUEPRINT, ModItems.M_60_BLUEPRINT, ModItems.SVD_BLUEPRINT,
                ModItems.M_98B_BLUEPRINT, ModItems.DEVOTION_BLUEPRINT, ModItems.INSIDIOUS_BLUEPRINT, ModItems.QBZ_191_BLUEPRINT,
                ModItems.AWM_BLUEPRINT, ModItems.IGLA_BLUEPRINT, ModItems.SENTINEL_BLUEPRINT);

        getOrCreateTagBuilder(ModTags.Items.LEGENDARY_BLUEPRINT).add(ModItems.AA_12_BLUEPRINT, ModItems.NTW_20_BLUEPRINT, ModItems.MINIGUN_BLUEPRINT,
                ModItems.JAVELIN_BLUEPRINT, ModItems.SECONDARY_CATACLYSM_BLUEPRINT, ModItems.MK_42_BLUEPRINT,
                ModItems.MLE_1934_BLUEPRINT, ModItems.ANNIHILATOR_BLUEPRINT, ModItems.HPJ_11_BLUEPRINT, ModItems.AURELIA_SCEPTRE_BLUEPRINT,
                ModItems.BL_132_BLUEPRINT, ModItems.QL_1031_BLUEPRINT);

        getOrCreateTagBuilder(ModTags.Items.CANNON_BLUEPRINT).add(ModItems.MK_42_BLUEPRINT, ModItems.MLE_1934_BLUEPRINT, ModItems.ANNIHILATOR_BLUEPRINT,
                ModItems.HPJ_11_BLUEPRINT, ModItems.BL_132_BLUEPRINT);
    }
}
