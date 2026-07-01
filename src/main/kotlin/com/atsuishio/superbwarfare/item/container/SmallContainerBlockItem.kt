package com.atsuishio.superbwarfare.item.container

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.client.renderer.item.SmallContainerBlockItemRenderer
import com.atsuishio.superbwarfare.init.ModBlocks
import com.atsuishio.superbwarfare.init.ModItems
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
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
import software.bernie.geckolib.animatable.GeoItem
import software.bernie.geckolib.animatable.client.GeoRenderProvider
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.animation.AnimatableManager
import software.bernie.geckolib.animation.AnimationController
import software.bernie.geckolib.animation.AnimationState
import software.bernie.geckolib.animation.PlayState
import software.bernie.geckolib.renderer.GeoItemRenderer
import software.bernie.geckolib.util.GeckoLibUtil
import java.util.function.Consumer

class SmallContainerBlockItem : BlockItem(ModBlocks.SMALL_CONTAINER, Properties().stacksTo(1).fireResistant()),
    GeoItem {
    private val cache: AnimatableInstanceCache = GeckoLibUtil.createInstanceCache(this)

    private fun predicate(event: AnimationState<SmallContainerBlockItem>): PlayState {
        return PlayState.CONTINUE
    }

    @Environment(EnvType.CLIENT)
    override fun createGeoRenderer(consumer: Consumer<GeoRenderProvider>) {
        consumer.accept(object : GeoRenderProvider {
            private var renderer: SmallContainerBlockItemRenderer? = null

            override fun getGeoItemRenderer(): GeoItemRenderer<*> {
                if (this.renderer == null) {
                    this.renderer = SmallContainerBlockItemRenderer()
                }
                return this.renderer!!
            }
        })
    }

    override fun registerControllers(data: AnimatableManager.ControllerRegistrar) {
        data.add(
            AnimationController(this, "controller", 0) { this.predicate(it) }
        )
    }

    override fun getAnimatableInstanceCache(): AnimatableInstanceCache {
        return this.cache
    }

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
