package com.atsuishio.superbwarfare.item.container

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.init.ModBlocks
import com.atsuishio.superbwarfare.init.ModItems
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.DamageTypeTags
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.SeededContainerLoot
import net.minecraft.world.level.storage.loot.LootTable

class SmallContainerBlockItem : BlockItem(ModBlocks.SMALL_CONTAINER, Properties().stacksTo(1).fireResistant()) {

    companion object {
        @JvmField
        val SMALL_CONTAINERS: MutableList<() -> ItemStack> = mutableListOf(
            { createInstance(loc("containers/blueprints")) },
            { createInstance(loc("containers/common")) }
        )

        @JvmOverloads
        fun createInstance(lootTable: ResourceLocation, lootTableSeed: Long = 0L): ItemStack {
            return createInstance(ResourceKey.create(Registries.LOOT_TABLE, lootTable), lootTableSeed)
        }

        @JvmOverloads
        fun createInstance(lootTable: ResourceKey<LootTable>, lootTableSeed: Long = 0L): ItemStack {
            val stack = ItemStack(ModItems.SMALL_CONTAINER)
            stack.set(
                DataComponents.CONTAINER_LOOT,
                SeededContainerLoot(lootTable, lootTableSeed)
            )
            return stack
        }
    }
}
