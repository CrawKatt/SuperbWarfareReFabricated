package com.atsuishio.superbwarfare.datagen;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.init.ModBlocks;
import com.atsuishio.superbwarfare.init.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unchecked")
public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {

    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL).add(
                ModBlocks.GALENA_ORE,
                ModBlocks.SCHEELITE_ORE,
                ModBlocks.DEEPSLATE_GALENA_ORE,
                ModBlocks.DEEPSLATE_SCHEELITE_ORE,
                ModBlocks.DRAGON_TEETH,
                ModBlocks.SILVER_ORE,
                ModBlocks.DEEPSLATE_SILVER_ORE
        );

        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.BARBED_WIRE);
        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE).add(
                ModBlocks.GALENA_ORE,
                ModBlocks.SCHEELITE_ORE,
                ModBlocks.DEEPSLATE_GALENA_ORE,
                ModBlocks.DEEPSLATE_SCHEELITE_ORE,
                ModBlocks.DRAGON_TEETH,
                ModBlocks.REFORGING_TABLE,
                ModBlocks.LEAD_BLOCK,
                ModBlocks.STEEL_BLOCK,
                ModBlocks.TUNGSTEN_BLOCK,
                ModBlocks.CEMENTED_CARBIDE_BLOCK,
                ModBlocks.SILVER_ORE,
                ModBlocks.DEEPSLATE_SILVER_ORE,
                ModBlocks.SILVER_BLOCK,
                ModBlocks.JUMP_PAD,
                ModBlocks.CONTAINER,
                ModBlocks.CHARGING_STATION,
                ModBlocks.FUMO_25,
                ModBlocks.SMALL_CONTAINER,
                ModBlocks.VEHICLE_DEPLOYER,
                ModBlocks.AIRCRAFT_CATAPULT,
                ModBlocks.SUPERB_ITEM_INTERFACE,
                ModBlocks.CREATIVE_SUPERB_ITEM_INTERFACE,
                ModBlocks.LUCKY_CONTAINER,
                ModBlocks.VEHICLE_ASSEMBLING_TABLE
        );
        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_SHOVEL).add(ModBlocks.SANDBAG);

        getOrCreateTagBuilder(ModTags.Blocks.SOFT_COLLISION)
                .addTag(BlockTags.LEAVES)
                .add(Blocks.LILY_PAD, Blocks.COBWEB, Blocks.CACTUS, Blocks.MANGROVE_ROOTS);
        getOrCreateTagBuilder(ModTags.Blocks.NORMAL_COLLISION)
                .addTag(BlockTags.FENCES).addTag(BlockTags.FENCE_GATES).addTag(BlockTags.DOORS).addTag(BlockTags.TRAPDOORS).addTag(BlockTags.WALLS).addTag(BlockTags.WOOL)
                        .addTag(BlockTags.STAIRS).addTag(BlockTags.SLABS).addTag(cTag("glass_panes"))
                .add(Blocks.BAMBOO, Blocks.MELON, Blocks.PUMPKIN, Blocks.HAY_BLOCK, Blocks.BELL, Blocks.CHAIN, Blocks.SNOW_BLOCK,
                        Blocks.MUSHROOM_STEM, Blocks.BROWN_MUSHROOM_BLOCK, Blocks.RED_MUSHROOM_BLOCK);
        getOrCreateTagBuilder(ModTags.Blocks.HARD_COLLISION)
                .addTag(BlockTags.LOGS).addTag(BlockTags.PLANKS).addTag(cTag("glass_blocks"))
                .add(Blocks.ICE, Blocks.FROSTED_ICE, Blocks.PACKED_ICE, Blocks.BLUE_ICE);
        getOrCreateTagBuilder(ModTags.Blocks.BULLET_IGNORE)
                .addTag(BlockTags.FENCES).addTag(BlockTags.FENCE_GATES).addTag(BlockTags.DOORS).addTag(BlockTags.TRAPDOORS).addTag(BlockTags.WALLS).addTag(BlockTags.LEAVES).addTag(cTag("glass_panes"))
                .add(Blocks.IRON_BARS, ModBlocks.BARBED_WIRE);
        getOrCreateTagBuilder(ModTags.Blocks.BULLET_CAN_DESTROY)
                .addTag(cTag("glass_panes")).addTag(cTag("glass_blocks"));
        getOrCreateTagBuilder(ModTags.Blocks.CANNON_SHOT_CAN_DESTROY)
                .addTag(ModTags.Blocks.BULLET_CAN_DESTROY).addTag(BlockTags.LEAVES).addTag(BlockTags.BAMBOO_BLOCKS).addTag(BlockTags.WOOL)
                        .addTag(BlockTags.SIGNS).addTag(BlockTags.LOGS).addTag(BlockTags.PLANKS).addTag(BlockTags.SAPLINGS)
                .add(Blocks.LANTERN, Blocks.SOUL_LANTERN, Blocks.CHAIN);
        getOrCreateTagBuilder(ModTags.Blocks.AUTO_LANDING)
                .add(ModBlocks.CHARGING_STATION, ModBlocks.CREATIVE_CHARGING_STATION);

        getOrCreateTagBuilder(cTag("ores")).addTag(cTag("ores/lead")).addTag(cTag("ores/tungsten")).addTag(cTag("ores/silver"));
        getOrCreateTagBuilder(cTag("ores/lead")).add(ModBlocks.GALENA_ORE, ModBlocks.DEEPSLATE_GALENA_ORE);
        getOrCreateTagBuilder(cTag("ores/tungsten")).add(ModBlocks.SCHEELITE_ORE, ModBlocks.DEEPSLATE_SCHEELITE_ORE);
        getOrCreateTagBuilder(cTag("ores/silver")).add(ModBlocks.SILVER_ORE, ModBlocks.DEEPSLATE_SILVER_ORE);

        getOrCreateTagBuilder(cTag("ores/scheelite")).add(ModBlocks.SCHEELITE_ORE, ModBlocks.DEEPSLATE_SCHEELITE_ORE);

        getOrCreateTagBuilder(cTag("ores_in_ground_stone")).add(ModBlocks.GALENA_ORE, ModBlocks.SCHEELITE_ORE, ModBlocks.SILVER_ORE);
        getOrCreateTagBuilder(cTag("ores_in_ground_deepslate")).add(ModBlocks.DEEPSLATE_GALENA_ORE, ModBlocks.DEEPSLATE_SCHEELITE_ORE, ModBlocks.DEEPSLATE_SILVER_ORE);
    }

    public static TagKey<Block> cTag(String name) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", name));
    }
}
