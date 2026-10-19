package com.atsuishio.superbwarfare.item.blockitem

import com.atsuishio.superbwarfare.client.renderer.item.TripodBlockItemRenderer
import com.atsuishio.superbwarfare.init.ModBlocks
import com.atsuishio.superbwarfare.init.ModItems
import com.atsuishio.superbwarfare.init.RegistryName
import com.atsuishio.superbwarfare.tools.mc
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
                private val renderer by lazy {
                    TripodBlockItemRenderer(mc.blockEntityRenderDispatcher, mc.entityModels)
                }

                override fun getCustomRenderer() = renderer
            }, ModItems.TRIPOD)
        }

    }
}
