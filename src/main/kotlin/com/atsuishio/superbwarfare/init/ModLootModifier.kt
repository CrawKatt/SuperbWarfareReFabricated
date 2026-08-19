package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.Mod
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import net.fabricmc.fabric.api.loot.v2.LootTableEvents
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.storage.loot.BuiltInLootTables
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.Serializer
import net.minecraft.world.level.storage.loot.entries.LootTableReference
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue

object ModLootModifier {
    private val COMMON = Mod.loc("chests/blue_print_common")
    private val RARE = Mod.loc("chests/blue_print_rare")
    private val EPIC = Mod.loc("chests/blue_print_epic")
    private val ANCIENT_CPU = Mod.loc("chests/ancient_cpu")

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

    private val CONDITION_TYPE: LootItemConditionType = Registry.register(
        BuiltInRegistries.LOOT_CONDITION_TYPE,
        Mod.loc("generate_loot"),
        LootItemConditionType(GenerateLootConditionSerializer)
    )

    @JvmStatic
    fun register() {
        LootTableEvents.MODIFY.register { _, _, id, builder, _ ->
            if (id in COMMON_TARGETS) builder.add(COMMON)
            if (id in RARE_TARGETS) builder.add(RARE)
            if (id in EPIC_TARGETS) builder.add(EPIC)
            if (id == BuiltInLootTables.ANCIENT_CITY) builder.add(ANCIENT_CPU)
        }
    }

    private fun LootTable.Builder.add(table: ResourceLocation) {
        withPool(
            LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1f))
                .add(LootTableReference.lootTableReference(table))
                .`when`(LootItemCondition.Builder { GenerateLootCondition })
        )
    }

    private object GenerateLootCondition : LootItemCondition {
        override fun test(context: LootContext): Boolean {
            return context.level.gameRules.getBoolean(ModGameRules.MOD_RULE_DO_GENERATE_LOOTS)
        }

        override fun getType(): LootItemConditionType = CONDITION_TYPE
    }

    private object GenerateLootConditionSerializer : Serializer<GenerateLootCondition> {
        override fun serialize(
            json: JsonObject,
            value: GenerateLootCondition,
            context: JsonSerializationContext
        ) {
        }

        override fun deserialize(
            json: JsonObject,
            context: JsonDeserializationContext
        ): GenerateLootCondition = GenerateLootCondition
    }
}
