package com.atsuishio.superbwarfare.item.container

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.client.renderer.item.SmallContainerBlockItemRenderer
import com.atsuishio.superbwarfare.init.ModBlockEntities
import com.atsuishio.superbwarfare.init.ModBlocks
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.DamageTypeTags
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import software.bernie.geckolib.animatable.GeoItem
import software.bernie.geckolib.animatable.client.RenderProvider
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.core.animation.AnimatableManager
import software.bernie.geckolib.core.animation.AnimationController
import software.bernie.geckolib.core.animation.AnimationState
import software.bernie.geckolib.core.`object`.PlayState
import software.bernie.geckolib.util.GeckoLibUtil
import java.util.function.Consumer
import java.util.function.Supplier

class SmallContainerBlockItem : BlockItem(ModBlocks.SMALL_CONTAINER, Properties().stacksTo(1).fireResistant()),
    GeoItem {
    private val cache: AnimatableInstanceCache = GeckoLibUtil.createInstanceCache(this)
    private val renderProvider: Supplier<Any> = GeoItem.makeRenderer(this)

    override fun canBeHurtBy(pDamageSource: DamageSource): Boolean {
        return super.canBeHurtBy(pDamageSource)
                && !pDamageSource.`is`(DamageTypeTags.IS_EXPLOSION)
                && !pDamageSource.`is`(DamageTypes.CACTUS)
    }

    @Environment(EnvType.CLIENT)
    private fun predicate(event: AnimationState<SmallContainerBlockItem>): PlayState {
        return PlayState.CONTINUE
    }

    @Environment(EnvType.CLIENT)
    override fun createRenderer(consumer: Consumer<Any>) {
        consumer.accept(object : RenderProvider {
            private var renderer: BlockEntityWithoutLevelRenderer? = null

            override fun getCustomRenderer(): BlockEntityWithoutLevelRenderer {
                if (renderer == null) renderer = SmallContainerBlockItemRenderer()
                return renderer!!
            }
        })
    }

    override fun getRenderProvider(): Supplier<Any> = renderProvider

    override fun registerControllers(data: AnimatableManager.ControllerRegistrar) {
        if (FabricLoader.getInstance().environmentType == EnvType.SERVER) return
        data.add(
            AnimationController<SmallContainerBlockItem>(
                this,
                "controller",
                0
            ) { this.predicate(it) }
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
            val stack = ItemStack(ModBlocks.SMALL_CONTAINER)
            val tag = CompoundTag()
            tag.putString("LootTable", lootTable.toString())
            if (lootTableSeed != 0L) {
                tag.putLong("LootTableSeed", lootTableSeed)
            }
            setBlockEntityData(stack, ModBlockEntities.SMALL_CONTAINER, tag)
            return stack
        }
    }
}
