package com.atsuishio.superbwarfare.datagen

import com.atsuishio.superbwarfare.init.ModBlocks
import com.atsuishio.superbwarfare.init.ModTags
import com.atsuishio.superbwarfare.init.ModTags.commonBlockTag
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.VanillaBlockTagsProvider
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Blocks
import java.util.concurrent.CompletableFuture

class ModBlockTagProvider(
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>
) : VanillaBlockTagsProvider(output, lookupProvider) {
    override fun addTags(pProvider: HolderLookup.Provider) {
        super.addTags(pProvider)
        this.tag(BlockTags.NEEDS_IRON_TOOL).add(
            ModBlocks.GALENA_ORE, ModBlocks.SCHEELITE_ORE,
            ModBlocks.DEEPSLATE_GALENA_ORE, ModBlocks.DEEPSLATE_SCHEELITE_ORE,
            ModBlocks.SILVER_ORE, ModBlocks.DEEPSLATE_SILVER_ORE,
            ModBlocks.RAW_GALENA_BLOCK, ModBlocks.RAW_SCHEELITE_BLOCK,
            ModBlocks.RAW_SILVER_BLOCK, ModBlocks.DRAGON_TEETH
        )

        this.tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.BARBED_WIRE)
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
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
            ModBlocks.VEHICLE_ASSEMBLING_TABLE,
            ModBlocks.BIOGAS_GENERATOR,
            ModBlocks.BLUEPRINT_RESEARCH_TABLE,
            ModBlocks.RAW_GALENA_BLOCK,
            ModBlocks.RAW_SCHEELITE_BLOCK,
            ModBlocks.RAW_SILVER_BLOCK
        )
        this.tag(BlockTags.MINEABLE_WITH_SHOVEL).add(ModBlocks.SANDBAG)

        this.tag(ModTags.Blocks.SOFT_COLLISION)
            .addTag(BlockTags.LEAVES)
            .add(Blocks.LILY_PAD, Blocks.COBWEB, Blocks.CACTUS, Blocks.MANGROVE_ROOTS)
        this.tag(ModTags.Blocks.NORMAL_COLLISION)
            .addTag(BlockTags.FENCES)
            .addTag(BlockTags.FENCE_GATES)
            .addTag(BlockTags.DOORS)
            .addTag(BlockTags.TRAPDOORS)
            .addTag(BlockTags.WALLS)
            .addTag(BlockTags.WOOL)
            .addTag(BlockTags.STAIRS)
            .addTag(BlockTags.SLABS)
            .addTag(ModTags.Blocks.GLASS_PANES)
            .add(
                Blocks.BAMBOO,
                Blocks.MELON,
                Blocks.PUMPKIN,
                Blocks.HAY_BLOCK,
                Blocks.BELL,
                Blocks.CHAIN,
                Blocks.SNOW_BLOCK,
                Blocks.MUSHROOM_STEM,
                Blocks.BROWN_MUSHROOM_BLOCK,
                Blocks.RED_MUSHROOM_BLOCK
            )
        this.tag(ModTags.Blocks.HARD_COLLISION)
            .addTag(BlockTags.LOGS)
            .addTag(BlockTags.PLANKS)
            .addTag(ModTags.Blocks.GLASS_BLOCKS)
            .add(Blocks.ICE, Blocks.FROSTED_ICE, Blocks.PACKED_ICE, Blocks.BLUE_ICE)
        this.tag(ModTags.Blocks.BULLET_IGNORE)
            .addTag(BlockTags.FENCES)
            .addTag(BlockTags.FENCE_GATES)
            .addTag(BlockTags.DOORS)
            .addTag(BlockTags.TRAPDOORS)
            .addTag(BlockTags.WALLS)
            .addTag(BlockTags.LEAVES)
            .addTag(ModTags.Blocks.GLASS_PANES)
            .add(Blocks.IRON_BARS, ModBlocks.BARBED_WIRE)
        this.tag(ModTags.Blocks.BULLET_CAN_DESTROY)
            .addTag(ModTags.Blocks.GLASS_PANES)
            .addTag(ModTags.Blocks.GLASS_BLOCKS)
        this.tag(ModTags.Blocks.CANNON_SHOT_CAN_DESTROY)
            .addTag(ModTags.Blocks.BULLET_CAN_DESTROY)
            .addTag(BlockTags.LEAVES)
            .addTag(BlockTags.BAMBOO_BLOCKS)
            .addTag(BlockTags.WOOL)
            .addTag(BlockTags.SIGNS)
            .addTag(BlockTags.LOGS)
            .addTag(BlockTags.PLANKS)
            .addTag(BlockTags.SAPLINGS)
            .add(Blocks.LANTERN, Blocks.SOUL_LANTERN, Blocks.CHAIN)
        this.tag(ModTags.Blocks.AUTO_LANDING)
            .add(ModBlocks.CHARGING_STATION, ModBlocks.CREATIVE_CHARGING_STATION)
        this.tag(ModTags.Blocks.VEHICLE_PASS_THROUGH)
            .addTag(BlockTags.SWORD_EFFICIENT)

        this.tag(ModTags.Blocks.ORES)
            .addTag(commonBlockTag("ores/lead"))
            .addTag(commonBlockTag("ores/tungsten"))
            .addTag(commonBlockTag("ores/silver"))
        this.tag(commonBlockTag("ores/lead")).add(ModBlocks.GALENA_ORE, ModBlocks.DEEPSLATE_GALENA_ORE)
        this.tag(commonBlockTag("ores/tungsten")).add(ModBlocks.SCHEELITE_ORE, ModBlocks.DEEPSLATE_SCHEELITE_ORE)
        this.tag(commonBlockTag("ores/silver")).add(ModBlocks.SILVER_ORE, ModBlocks.DEEPSLATE_SILVER_ORE)

        // 这个tag仅用于其他mod配方兼容，自己家配方不用这个
        this.tag(commonBlockTag("ores/scheelite")).add(ModBlocks.SCHEELITE_ORE, ModBlocks.DEEPSLATE_SCHEELITE_ORE)

        this.tag(commonBlockTag("storage_blocks/raw_lead")).add(ModBlocks.RAW_GALENA_BLOCK)
        this.tag(commonBlockTag("storage_blocks/raw_tungsten")).add(ModBlocks.RAW_SCHEELITE_BLOCK)
        this.tag(commonBlockTag("storage_blocks/raw_silver")).add(ModBlocks.RAW_SILVER_BLOCK)

        this.tag(commonBlockTag("storage_blocks/raw_scheelite")).add(ModBlocks.RAW_SCHEELITE_BLOCK)

        this.tag(ModTags.Blocks.GLASS_BLOCKS).add(
            Blocks.GLASS, Blocks.WHITE_STAINED_GLASS, Blocks.ORANGE_STAINED_GLASS,
            Blocks.MAGENTA_STAINED_GLASS, Blocks.LIGHT_BLUE_STAINED_GLASS,
            Blocks.LIME_STAINED_GLASS, Blocks.PINK_STAINED_GLASS,
            Blocks.GRAY_STAINED_GLASS, Blocks.LIGHT_GRAY_STAINED_GLASS,
            Blocks.CYAN_STAINED_GLASS, Blocks.PURPLE_STAINED_GLASS,
            Blocks.BLUE_STAINED_GLASS, Blocks.BROWN_STAINED_GLASS,
            Blocks.GREEN_STAINED_GLASS, Blocks.RED_STAINED_GLASS,
            Blocks.BLACK_STAINED_GLASS, Blocks.TINTED_GLASS
        )
        this.tag(ModTags.Blocks.GLASS_PANES).add(
            Blocks.GLASS_PANE, Blocks.WHITE_STAINED_GLASS_PANE,
            Blocks.ORANGE_STAINED_GLASS_PANE, Blocks.MAGENTA_STAINED_GLASS_PANE,
            Blocks.LIGHT_BLUE_STAINED_GLASS_PANE, Blocks.LIME_STAINED_GLASS_PANE,
            Blocks.PINK_STAINED_GLASS_PANE, Blocks.GRAY_STAINED_GLASS_PANE,
            Blocks.LIGHT_GRAY_STAINED_GLASS_PANE, Blocks.CYAN_STAINED_GLASS_PANE,
            Blocks.PURPLE_STAINED_GLASS_PANE, Blocks.BLUE_STAINED_GLASS_PANE,
            Blocks.BROWN_STAINED_GLASS_PANE, Blocks.GREEN_STAINED_GLASS_PANE,
            Blocks.RED_STAINED_GLASS_PANE, Blocks.BLACK_STAINED_GLASS_PANE
        )

        this.tag(ModTags.Blocks.ORES_IN_GROUND_STONE)
            .add(ModBlocks.GALENA_ORE, ModBlocks.SCHEELITE_ORE, ModBlocks.SILVER_ORE)
        this.tag(ModTags.Blocks.ORES_IN_GROUND_DEEPSLATE).add(
            ModBlocks.DEEPSLATE_GALENA_ORE,
            ModBlocks.DEEPSLATE_SCHEELITE_ORE,
            ModBlocks.DEEPSLATE_SILVER_ORE
        )
    }
}
