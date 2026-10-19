package com.atsuishio.superbwarfare.item.blockitem

import com.atsuishio.superbwarfare.client.renderer.item.BlueprintResearchingTableBlockItemRenderer
import com.atsuishio.superbwarfare.init.ModBlocks
import com.atsuishio.superbwarfare.init.ModItems
import com.atsuishio.superbwarfare.init.RegistryName
import com.atsuishio.superbwarfare.tools.mc
import net.minecraft.world.item.BlockItem
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent
import software.bernie.geckolib.animatable.GeoItem
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.animation.AnimatableManager
import software.bernie.geckolib.util.GeckoLibUtil

@RegistryName("blueprint_research_table")
class BlueprintResearchTableBlockItem : BlockItem(ModBlocks.BLUEPRINT_RESEARCH_TABLE.get(), Properties()), GeoItem {
    private val cache: AnimatableInstanceCache = GeckoLibUtil.createInstanceCache(this)

    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {}

    override fun getAnimatableInstanceCache(): AnimatableInstanceCache = this.cache

    @EventBusSubscriber
    companion object {
        @SubscribeEvent
        fun registerRenderer(event: RegisterClientExtensionsEvent) {
            event.registerItem(object : IClientItemExtensions {
                private val renderer by lazy {
                    BlueprintResearchingTableBlockItemRenderer(mc.blockEntityRenderDispatcher, mc.entityModels)
                }

                override fun getCustomRenderer() = renderer
            }, ModItems.BLUEPRINT_RESEARCH_TABLE.get())
        }
    }
}
