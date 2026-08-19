package com.atsuishio.superbwarfare.item.blockitem

import com.atsuishio.superbwarfare.client.renderer.item.BlueprintResearchingTableBlockItemRenderer
import com.atsuishio.superbwarfare.init.ModBlocks
import com.atsuishio.superbwarfare.tools.mc
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.minecraft.world.item.BlockItem
import software.bernie.geckolib.animatable.GeoItem
import software.bernie.geckolib.animatable.client.RenderProvider
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.core.animation.AnimatableManager
import software.bernie.geckolib.util.GeckoLibUtil
import java.util.function.Consumer
import java.util.function.Supplier

class BlueprintResearchTableBlockItem : BlockItem(ModBlocks.BLUEPRINT_RESEARCH_TABLE, Properties()), GeoItem {
    private val cache: AnimatableInstanceCache = GeckoLibUtil.createInstanceCache(this)
    private val renderProvider: Supplier<Any> = GeoItem.makeRenderer(this)

    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {}

    @Environment(EnvType.CLIENT)
    override fun createRenderer(consumer: Consumer<Any>) {
        consumer.accept(object : RenderProvider {
            private var renderer: BlockEntityWithoutLevelRenderer? = null

            override fun getCustomRenderer(): BlockEntityWithoutLevelRenderer {
                if (this.renderer == null) {
                    this.renderer = BlueprintResearchingTableBlockItemRenderer(mc.blockEntityRenderDispatcher, mc.entityModels)
                }
                return this.renderer!!
            }
        })
    }

    override fun getRenderProvider(): Supplier<Any> = renderProvider

    override fun getAnimatableInstanceCache(): AnimatableInstanceCache = this.cache
}
