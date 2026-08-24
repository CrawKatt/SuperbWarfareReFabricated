package com.atsuishio.superbwarfare.item.gun.special

import com.atsuishio.superbwarfare.client.renderer.gun.GeoGunRenderer
import com.atsuishio.superbwarfare.item.gun.GunItem
import com.atsuishio.superbwarfare.registerToEventBus
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent

object TaserV2Item : GunItem(Properties()) {

    init {
        registerToEventBus(this)
    }

    @SubscribeEvent
    fun initializeClient(event: RegisterClientExtensionsEvent) {
        event.registerItem(object : IClientItemExtensions {
            private val renderer by lazy { GeoGunRenderer() }

            override fun getCustomRenderer() = renderer
        }, this)
    }
}
