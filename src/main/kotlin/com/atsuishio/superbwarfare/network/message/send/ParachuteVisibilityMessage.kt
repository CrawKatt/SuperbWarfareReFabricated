package com.atsuishio.superbwarfare.network.message.send

import com.atsuishio.superbwarfare.init.ModItems
import com.atsuishio.superbwarfare.item.curio.ParachuteItem
import com.atsuishio.superbwarfare.network.PayloadContext
import com.atsuishio.superbwarfare.network.ServerPacketPayload
import dev.emi.trinkets.api.TrinketsApi

object ParachuteVisibilityMessage : ServerPacketPayload() {
    override fun PayloadContext.handler() {
        val player = sender()
        TrinketsApi.getTrinketComponent(player)
            .flatMap { it.getEquipped(ModItems.PARACHUTE).stream().findFirst() }
            .ifPresent {
                it.b.orCreateTag.putBoolean(ParachuteItem.TAG_VISIBLE, !ParachuteItem.isVisible(it.b))
                TrinketsApi.TRINKET_COMPONENT.sync(player)
            }
    }
}
