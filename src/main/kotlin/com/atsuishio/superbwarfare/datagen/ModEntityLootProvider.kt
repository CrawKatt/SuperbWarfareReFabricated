package com.atsuishio.superbwarfare.datagen

import com.atsuishio.superbwarfare.init.ModEntities
import com.atsuishio.superbwarfare.init.ModItems
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Items
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.data.loot.LootTableSubProvider
import java.util.function.BiConsumer

class ModEntityLootProvider : LootTableSubProvider {
    override fun generate(output: BiConsumer<ResourceKey<LootTable>, LootTable.Builder>) {
        output.accept(
            ModEntities.STEEL_COIL.defaultLootTable,
            LootTable.lootTable().withPool(
                LootPool.lootPool().setRolls(ConstantValue.exactly(1f))
                    .add(
                        LootItem.lootTableItem(ModItems.STEEL_BLOCK)
                            .apply(
                                SetItemCountFunction.setCount(ConstantValue.exactly(3f))
                            )
                    )
            ).withPool(
                LootPool.lootPool().setRolls(ConstantValue.exactly(1f))
                    .add(
                        LootItem.lootTableItem(ModItems.STEEL_BLOCK)
                            .`when`(LootItemRandomChanceCondition.randomChance(0.5f))
                    )
            )
        )

        output.accept(
            ModEntities.TARGET.defaultLootTable,
            LootTable.lootTable()
        )

        output.accept(
            ModEntities.DPS_GENERATOR.defaultLootTable,
            LootTable.lootTable()
        )

        output.accept(
            ModEntities.SENPAI.defaultLootTable,
            LootTable.lootTable().withPool(
                LootPool.lootPool().setRolls(ConstantValue.exactly(1f))
                    .add(LootItem.lootTableItem(Items.APPLE).setWeight(80))
                    .add(LootItem.lootTableItem(Items.GOLDEN_APPLE).setWeight(19))
                    .add(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE).setWeight(1))
            )
        )
    }
}
