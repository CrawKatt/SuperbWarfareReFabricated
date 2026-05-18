package com.atsuishio.superbwarfare.datagen;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.block.VehicleAssemblingTableBlock;
import com.atsuishio.superbwarfare.block.property.BlockPart;
import com.atsuishio.superbwarfare.component.ModDataComponents;
import com.atsuishio.superbwarfare.init.ModBlocks;
import com.atsuishio.superbwarfare.init.ModItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class ModBlockLootProvider extends BlockLootSubProvider {

    public ModBlockLootProvider(HolderLookup.Provider provider) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
    }

    @Override
    public void generate() {
        this.dropSelf(ModBlocks.SANDBAG);
        this.dropSelf(ModBlocks.BARBED_WIRE);
        this.dropSelf(ModBlocks.JUMP_PAD);
        this.dropSelf(ModBlocks.DRAGON_TEETH);
        this.dropSelf(ModBlocks.REFORGING_TABLE);
        this.dropSelf(ModBlocks.LEAD_BLOCK);
        this.dropSelf(ModBlocks.STEEL_BLOCK);
        this.dropSelf(ModBlocks.TUNGSTEN_BLOCK);
        this.dropSelf(ModBlocks.CEMENTED_CARBIDE_BLOCK);
        this.dropSelf(ModBlocks.SILVER_BLOCK);
        this.dropSelf(ModBlocks.CREATIVE_CHARGING_STATION);
        this.dropSelf(ModBlocks.FUMO_25);
        this.dropSelf(ModBlocks.VEHICLE_DEPLOYER);
        this.dropSelf(ModBlocks.AIRCRAFT_CATAPULT);
        this.dropSelf(ModBlocks.SUPERB_ITEM_INTERFACE);
        this.dropSelf(ModBlocks.CREATIVE_SUPERB_ITEM_INTERFACE);
        this.add(ModBlocks.VEHICLE_ASSEMBLING_TABLE,
                this.applyExplosionDecay(ModBlocks.VEHICLE_ASSEMBLING_TABLE, LootTable.lootTable().withPool(LootPool.lootPool().add(
                        LootItem.lootTableItem(ModBlocks.VEHICLE_ASSEMBLING_TABLE).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.VEHICLE_ASSEMBLING_TABLE)
                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(VehicleAssemblingTableBlock.BLOCK_PART, BlockPart.FLB))).otherwise(LootItem.lootTableItem(Blocks.AIR)))
                ))
        );

        this.add(ModBlocks.CHARGING_STATION, createCopyComponentsDrops(
                ModBlocks.CHARGING_STATION,
                List.of(ModDataComponents.ENERGY))
        );

        this.add(ModBlocks.GALENA_ORE, this.createOreDrop(ModBlocks.GALENA_ORE, ModItems.GALENA));
        this.add(ModBlocks.SCHEELITE_ORE, this.createOreDrop(ModBlocks.SCHEELITE_ORE, ModItems.SCHEELITE));
        this.add(ModBlocks.SILVER_ORE, this.createOreDrop(ModBlocks.SILVER_ORE, ModItems.RAW_SILVER));
        this.add(ModBlocks.DEEPSLATE_GALENA_ORE, this.createOreDrop(ModBlocks.DEEPSLATE_GALENA_ORE, ModItems.GALENA));
        this.add(ModBlocks.DEEPSLATE_SCHEELITE_ORE, this.createOreDrop(ModBlocks.DEEPSLATE_SCHEELITE_ORE, ModItems.SCHEELITE));
        this.add(ModBlocks.DEEPSLATE_SILVER_ORE, this.createOreDrop(ModBlocks.DEEPSLATE_SILVER_ORE, ModItems.RAW_SILVER));

        this.add(ModBlocks.CONTAINER, LootTable.lootTable().withPool(this.applyExplosionCondition(
                ModBlocks.CONTAINER,
                LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1F))
                        .add(LootItem.lootTableItem(ModBlocks.CONTAINER))
                        .apply(CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
                                .include(DataComponents.BLOCK_ENTITY_DATA)
                        )
        )));
        this.add(ModBlocks.SMALL_CONTAINER, LootTable.lootTable().withPool(this.applyExplosionCondition(
                ModBlocks.SMALL_CONTAINER,
                LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1F))
                        .add(LootItem.lootTableItem(ModBlocks.SMALL_CONTAINER))
                        .apply(CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
                                .include(DataComponents.CONTAINER_LOOT)
                        )
        )));
        this.add(ModBlocks.LUCKY_CONTAINER, LootTable.lootTable().withPool(this.applyExplosionCondition(ModBlocks.LUCKY_CONTAINER,
                LootPool.lootPool().setRolls(ConstantValue.exactly(1F)).add(LootItem.lootTableItem(ModBlocks.LUCKY_CONTAINER))
                        .apply(CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY).include(DataComponents.BLOCK_ENTITY_DATA)))));
    }

    protected @NotNull Iterable<Block> getKnownBlocks() {
        return BuiltInRegistries.BLOCK.stream()
                .filter(b -> Mod.MODID.equals(BuiltInRegistries.BLOCK.getKey(b).getNamespace()))
                .toList();
    }

    public LootTable.Builder createCopyComponentsDrops(Block pBlock, List<DataComponentType<?>> components) {
        var pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1F)).add(LootItem.lootTableItem(pBlock));
        if (!components.isEmpty()) {
            var copy = CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY);
            for (var type : components) {
                copy.include(type);
            }
            pool.apply(copy);
        }
        return LootTable.lootTable().withPool(this.applyExplosionCondition(pBlock, pool));
    }
}
