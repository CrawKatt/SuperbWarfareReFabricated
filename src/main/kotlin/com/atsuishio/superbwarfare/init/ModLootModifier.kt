package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.Mod
import com.mojang.serialization.MapCodec
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.LootTable

object ModLootModifier {

    class TargetModLootTableModifier(
        private val lootTable: ResourceKey<LootTable>
    ) {
        fun apply(
            generatedLoot: ObjectArrayList<ItemStack>,
            context: LootContext
        ) {
            if (context.level.gameRules.getBoolean(ModGameRules.MOD_RULE_DO_GENERATE_LOOTS)) {
                context.resolver.get(Registries.LOOT_TABLE, this.lootTable).ifPresent { table ->
                    table.value().getRandomItemsRaw(
                        context,
                        LootTable.createStackSplitter(context.level) { generatedLoot.add(it) }
                    )
                }
            }
        }

        fun table(): ResourceKey<LootTable> {
            return this.lootTable
        }
    }
}