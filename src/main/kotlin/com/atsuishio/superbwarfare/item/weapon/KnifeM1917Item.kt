package com.atsuishio.superbwarfare.item.weapon

import com.atsuishio.superbwarfare.client.renderer.item.KnifeM1917Renderer
import com.atsuishio.superbwarfare.init.ModItems
import com.atsuishio.superbwarfare.init.RegistryName
import com.atsuishio.superbwarfare.tools.mc
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent

@RegistryName("knife_m1917")
class KnifeM1917Item : KnifeItem() {

    @EventBusSubscriber
    companion object {
        @SubscribeEvent
        private fun registerGunExtensions(event: RegisterClientExtensionsEvent) {
            event.registerItem(object : IClientItemExtensions {
                private val renderer by lazy { KnifeM1917Renderer(mc.blockEntityRenderDispatcher, mc.entityModels) }
                override fun getCustomRenderer() = renderer
            }, ModItems.KNIFE_M1917)
        }
    }
}
