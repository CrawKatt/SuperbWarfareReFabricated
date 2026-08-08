package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.Mod
import com.mojang.serialization.MapCodec
import net.fabricmc.fabric.api.loot.v3.LootTableEvents
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.storage.loot.BuiltInLootTables
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.NestedLootTable
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType

object ModLootModifier {
    private val COMMON = lootTable("chests/blue_print_common")
    private val RARE = lootTable("chests/blue_print_rare")
    private val EPIC = lootTable("chests/blue_print_epic")
    private val ANCIENT_CPU = lootTable("chests/ancient_cpu")

    private val COMMON_TARGETS = setOf(
        BuiltInLootTables.SIMPLE_DUNGEON,
        BuiltInLootTables.ABANDONED_MINESHAFT,
        BuiltInLootTables.SHIPWRECK_MAP,
        BuiltInLootTables.SHIPWRECK_SUPPLY,
        BuiltInLootTables.SHIPWRECK_TREASURE,
        BuiltInLootTables.RUINED_PORTAL
    )

    private val RARE_TARGETS = setOf(
        BuiltInLootTables.ANCIENT_CITY,
        BuiltInLootTables.ANCIENT_CITY_ICE_BOX,
        BuiltInLootTables.BASTION_BRIDGE,
        BuiltInLootTables.BASTION_HOGLIN_STABLE,
        BuiltInLootTables.BASTION_OTHER,
        BuiltInLootTables.BURIED_TREASURE,
        BuiltInLootTables.DESERT_PYRAMID,
        BuiltInLootTables.IGLOO_CHEST,
        BuiltInLootTables.JUNGLE_TEMPLE
    )

    private val EPIC_TARGETS = setOf(
        BuiltInLootTables.PILLAGER_OUTPOST,
        BuiltInLootTables.STRONGHOLD_LIBRARY,
        BuiltInLootTables.WOODLAND_MANSION,
        BuiltInLootTables.END_CITY_TREASURE
    )

    private val CONDITION_TYPE = Registry.register(
        BuiltInRegistries.LOOT_CONDITION_TYPE,
        Mod.loc("generate_loot"),
        LootItemConditionType(GenerateLootCondition.CODEC)
    )

    @JvmStatic
    fun init() {
        LootTableEvents.MODIFY.register { key, builder, _, _ ->
            if (key in COMMON_TARGETS) builder.add(COMMON)
            if (key in RARE_TARGETS) builder.add(RARE)
            if (key in EPIC_TARGETS) builder.add(EPIC)
            if (key == BuiltInLootTables.ANCIENT_CITY) builder.add(ANCIENT_CPU)
        }
    }

    private fun LootTable.Builder.add(table: ResourceKey<LootTable>) {
        withPool(
            LootPool.lootPool()
                .add(NestedLootTable.lootTableReference(table))
                .`when`(LootItemCondition.Builder { GenerateLootCondition.INSTANCE })
        )
    }

    private fun lootTable(path: String): ResourceKey<LootTable> =
        ResourceKey.create(Registries.LOOT_TABLE, Mod.loc(path))

    private class GenerateLootCondition private constructor() : LootItemCondition {
        override fun test(context: LootContext): Boolean =
            context.level.gameRules.getBoolean(ModGameRules.MOD_RULE_DO_GENERATE_LOOTS)

        override fun getType(): LootItemConditionType = CONDITION_TYPE

        companion object {
            val INSTANCE = GenerateLootCondition()
            val CODEC: MapCodec<GenerateLootCondition> = MapCodec.unit(INSTANCE)
        }
    }
}
