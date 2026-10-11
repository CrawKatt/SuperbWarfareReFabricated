package com.atsuishio.superbwarfare.item.blockitem

import com.atsuishio.superbwarfare.client.renderer.item.TripodBlockItemRenderer
import com.atsuishio.superbwarfare.init.ModBlocks
import com.atsuishio.superbwarfare.init.ModItems
import com.atsuishio.superbwarfare.init.RegistryName
import com.atsuishio.superbwarfare.tools.mc
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.minecraft.world.item.BlockItem
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent

@RegistryName("tripod")
class TripodBlockItem : BlockItem(ModBlocks.TRIPOD.get(), Properties()) {

    @EventBusSubscriber
    companion object {
        @SubscribeEvent
        fun registerRender(event: RegisterClientExtensionsEvent) {
            event.registerItem(object : IClientItemExtensions {
                private var renderer: BlockEntityWithoutLevelRenderer? = null

                override fun getCustomRenderer(): BlockEntityWithoutLevelRenderer {
                    if (renderer == null) {
                        renderer = TripodBlockItemRenderer(mc.blockEntityRenderDispatcher, mc.entityModels)
                    }
                    return renderer!!
                }
            }, ModItems.TRIPOD)
        }

    }
}
