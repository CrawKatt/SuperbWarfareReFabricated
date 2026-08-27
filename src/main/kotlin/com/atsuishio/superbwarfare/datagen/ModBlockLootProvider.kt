package com.atsuishio.superbwarfare.datagen

import com.atsuishio.superbwarfare.block.BlueprintResearchTableBlock
import com.atsuishio.superbwarfare.block.VehicleAssemblingTableBlock
import com.atsuishio.superbwarfare.block.property.BlockPart
import com.atsuishio.superbwarfare.init.ModBlocks
import com.atsuishio.superbwarfare.init.ModItems
import com.mojang.datafixers.util.Pair
import net.minecraft.advancements.critereon.StatePropertiesPredicate
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.properties.BedPart
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator
import java.util.function.BiConsumer

class ModBlockLootProvider : BlockLootSubProvider(mutableSetOf<Item>(), FeatureFlags.REGISTRY.allFlags()) {
    override fun generate() {
        this.dropSelf(ModBlocks.SANDBAG)
        this.dropSelf(ModBlocks.BARBED_WIRE)
        this.dropSelf(ModBlocks.JUMP_PAD)
        this.dropSelf(ModBlocks.DRAGON_TEETH)
        this.dropSelf(ModBlocks.REFORGING_TABLE)
        this.dropSelf(ModBlocks.LEAD_BLOCK)
        this.dropSelf(ModBlocks.STEEL_BLOCK)
        this.dropSelf(ModBlocks.TUNGSTEN_BLOCK)
        this.dropSelf(ModBlocks.CEMENTED_CARBIDE_BLOCK)
        this.dropSelf(ModBlocks.SILVER_BLOCK)
        this.dropSelf(ModBlocks.CREATIVE_CHARGING_STATION)
        this.dropSelf(ModBlocks.FUMO_25)
        this.dropSelf(ModBlocks.VEHICLE_DEPLOYER)
        this.dropSelf(ModBlocks.AIRCRAFT_CATAPULT)
        this.dropSelf(ModBlocks.CATAPULT_CONTROLLER)
        this.dropSelf(ModBlocks.SUPERB_ITEM_INTERFACE)
        this.dropSelf(ModBlocks.CREATIVE_SUPERB_ITEM_INTERFACE)
        this.dropSelf(ModBlocks.RAW_GALENA_BLOCK)
        this.dropSelf(ModBlocks.RAW_SCHEELITE_BLOCK)
        this.dropSelf(ModBlocks.RAW_SILVER_BLOCK)
        this.dropSelf(ModBlocks.URANIUM_BLOCK)
        this.dropSelf(ModBlocks.SULFUR_BLOCK)
        this.dropSelf(ModBlocks.NITER_BLOCK)
        this.dropSelf(ModBlocks.RAW_URANIUM_BLOCK)
        this.add(
            ModBlocks.BLUEPRINT_RESEARCH_TABLE,
            this.applyExplosionDecay(
                ModBlocks.BLUEPRINT_RESEARCH_TABLE, LootTable.lootTable().withPool(
                    LootPool.lootPool().add(
                        LootItem.lootTableItem(ModBlocks.BLUEPRINT_RESEARCH_TABLE).`when`(
                            LootItemBlockStatePropertyCondition.hasBlockStateProperties(
                                ModBlocks.BLUEPRINT_RESEARCH_TABLE
                            ).setProperties(
                                StatePropertiesPredicate.Builder.properties()
                                    .hasProperty(BlueprintResearchTableBlock.PART, BedPart.FOOT)
                            )
                        ).otherwise(LootItem.lootTableItem(Blocks.AIR))
                    )
                )
            )
        )
        this.add(
            ModBlocks.VEHICLE_ASSEMBLING_TABLE,
            this.applyExplosionDecay(
                ModBlocks.VEHICLE_ASSEMBLING_TABLE, LootTable.lootTable().withPool(
                    LootPool.lootPool().add(
                        LootItem.lootTableItem(ModBlocks.VEHICLE_ASSEMBLING_TABLE).`when`(
                            LootItemBlockStatePropertyCondition.hasBlockStateProperties(
                                ModBlocks.VEHICLE_ASSEMBLING_TABLE
                            ).setProperties(
                                StatePropertiesPredicate.Builder.properties()
                                    .hasProperty(VehicleAssemblingTableBlock.BLOCK_PART, BlockPart.FLB)
                            )
                        ).otherwise(LootItem.lootTableItem(Blocks.AIR))
                    )
                )
            )
        )
        this.dropSelf(ModBlocks.BIOGAS_GENERATOR)

        this.add(
            ModBlocks.CHARGING_STATION, createCopyNBTDrops(
                ModBlocks.CHARGING_STATION,
                listOf(
                    Pair.of("Energy", "BlockEntityTag.Energy"),
                    Pair.of("id", "BlockEntityTag.id")
                )
            )
        )

        this.add(ModBlocks.GALENA_ORE, this.createOreDrop(ModBlocks.GALENA_ORE, ModItems.GALENA))
        this.add(
            ModBlocks.SCHEELITE_ORE,
            this.createOreDrop(ModBlocks.SCHEELITE_ORE, ModItems.SCHEELITE)
        )
        this.add(ModBlocks.SILVER_ORE, this.createOreDrop(ModBlocks.SILVER_ORE, ModItems.RAW_SILVER))
        this.add(ModBlocks.URANIUM_ORE, this.createOreDrop(ModBlocks.URANIUM_ORE, ModItems.RAW_URANIUM))
        this.add(ModBlocks.SULFUR_ORE, this.createSulfurDrop(ModBlocks.SULFUR_ORE))
        this.add(ModBlocks.NITER_ORE, this.createNiterDrop(ModBlocks.NITER_ORE))
        this.add(
            ModBlocks.DEEPSLATE_GALENA_ORE,
            this.createOreDrop(ModBlocks.DEEPSLATE_GALENA_ORE, ModItems.GALENA)
        )
        this.add(
            ModBlocks.DEEPSLATE_SCHEELITE_ORE,
            this.createOreDrop(ModBlocks.DEEPSLATE_SCHEELITE_ORE, ModItems.SCHEELITE)
        )
        this.add(
            ModBlocks.DEEPSLATE_SILVER_ORE,
            this.createOreDrop(ModBlocks.DEEPSLATE_SILVER_ORE, ModItems.RAW_SILVER)
        )
        this.add(
            ModBlocks.DEEPSLATE_URANIUM_ORE,
            this.createOreDrop(ModBlocks.DEEPSLATE_URANIUM_ORE, ModItems.RAW_URANIUM)
        )
        this.add(ModBlocks.DEEPSLATE_SULFUR_ORE, this.createSulfurDrop(ModBlocks.DEEPSLATE_SULFUR_ORE))
        this.add(ModBlocks.DEEPSLATE_NITER_ORE, this.createNiterDrop(ModBlocks.DEEPSLATE_NITER_ORE))

        this.add(
            ModBlocks.CONTAINER, LootTable.lootTable().withPool(
                this.applyExplosionCondition(
                    ModBlocks.CONTAINER,
                    LootPool.lootPool().setRolls(ConstantValue.exactly(1f))
                        .add(LootItem.lootTableItem(ModBlocks.CONTAINER))
                        .apply(
                            CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                                .copy("Entity", "BlockEntityTag.Entity")
                                .copy("EntityType", "BlockEntityTag.EntityType")
                        )
                )
            )
        )
        this.add(
            ModBlocks.SMALL_CONTAINER, LootTable.lootTable().withPool(
                this.applyExplosionCondition(
                    ModBlocks.SMALL_CONTAINER,
                    LootPool.lootPool().setRolls(ConstantValue.exactly(1f))
                        .add(LootItem.lootTableItem(ModBlocks.SMALL_CONTAINER))
                        .apply(
                            CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                                .copy("LootTable", "BlockEntityTag.LootTable")
                                .copy("LootTableSeed", "BlockEntityTag.LootTableSeed")
                        )
                )
            )
        )
        this.add(
            ModBlocks.LUCKY_CONTAINER, LootTable.lootTable().withPool(
                this.applyExplosionCondition(
                    ModBlocks.LUCKY_CONTAINER,
                    LootPool.lootPool().setRolls(ConstantValue.exactly(1f))
                        .add(LootItem.lootTableItem(ModBlocks.LUCKY_CONTAINER))
                        .apply(
                            CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                                .copy("Location", "BlockEntityTag.Location")
                                .copy("Icon", "BlockEntityTag.Icon")
                        )
                )
            )
        )
    }

    override fun generate(output: BiConsumer<ResourceLocation, LootTable.Builder>) {
        this.generate()

        for ((key, builder) in this.map) {
            output.accept(key, builder)
        }

        this.map.clear()
    }

    fun createCopyNBTDrops(pBlock: Block, paths: List<Pair<String, String>>): LootTable.Builder {
        val pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1f)).add(LootItem.lootTableItem(pBlock))
        if (!paths.isEmpty()) {
            val copy = CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
            for (path in paths) {
                copy.copy(path.getFirst(), path.getSecond())
            }
            pool.apply(copy)
        }
        return LootTable.lootTable().withPool(this.applyExplosionCondition(pBlock, pool))
    }

    private fun createSulfurDrop(block: Block): LootTable.Builder {
        return createSilkTouchDispatchTable(
            block,
            this.applyExplosionDecay(
                block,
                LootItem.lootTableItem(ModItems.SULFUR)
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0f, 5.0f)))
                    .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE))
            )
        )
    }

    private fun createNiterDrop(block: Block): LootTable.Builder {
        return createSilkTouchDispatchTable(
            block,
            this.applyExplosionDecay(
                block,
                LootItem.lootTableItem(ModItems.NITER)
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0f, 9.0f)))
                    .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE))
            )
        )
    }
}
