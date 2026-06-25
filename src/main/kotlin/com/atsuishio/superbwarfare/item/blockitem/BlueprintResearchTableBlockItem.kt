package com.atsuishio.superbwarfare.item.blockitem

import com.atsuishio.superbwarfare.client.renderer.item.BlueprintResearchingTableBlockItemRenderer
import com.atsuishio.superbwarfare.init.ModBlocks
import com.atsuishio.superbwarfare.tools.mc
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.minecraft.world.item.BlockItem
import software.bernie.geckolib.animatable.GeoItem
import software.bernie.geckolib.animatable.client.GeoRenderProvider
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.animation.AnimatableManager
import software.bernie.geckolib.util.GeckoLibUtil
import java.util.function.Consumer

class BlueprintResearchTableBlockItem : BlockItem(ModBlocks.BLUEPRINT_RESEARCH_TABLE, Properties()), GeoItem {
    private val cache: AnimatableInstanceCache = GeckoLibUtil.createInstanceCache(this)

    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {}

    @Environment(EnvType.CLIENT)
    override fun createGeoRenderer(consumer: Consumer<GeoRenderProvider>) {
        consumer.accept(object : GeoRenderProvider {
            private var renderer: BlockEntityWithoutLevelRenderer? = null

            override fun getGeoItemRenderer(): BlockEntityWithoutLevelRenderer {
                if (this.renderer == null) {
                    this.renderer = BlueprintResearchingTableBlockItemRenderer(mc.blockEntityRenderDispatcher, mc.entityModels)
                }
                return this.renderer!!
            }
        })
    }

    override fun getAnimatableInstanceCache(): AnimatableInstanceCache = this.cache
}
