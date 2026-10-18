package com.atsuishio.superbwarfare.client.overlay

import com.atsuishio.superbwarfare.client.overlay.ammo.AmmoBarHud
import com.atsuishio.superbwarfare.config.client.DisplayConfig
import com.atsuishio.superbwarfare.data.gun.ActiveGun
import com.atsuishio.superbwarfare.data.gun.GunData
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import com.atsuishio.superbwarfare.item.gun.GunItem
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
object AmmoBarOverlay : CommonOverlay("ammo_bar") {
    override fun shouldRender(): Boolean {
        val result = super.shouldRender() && DisplayConfig.AMMO_HUD.get()
        if (!result) AmmoBarHud.close()
        return result
    }

    override fun RenderContext.render() {
        val stack = ActiveGun.stackOf(player)
        val vehicle = player.vehicle
        if (stack.item !is GunItem || !GunItem.isOperable(stack) || vehicle is VehicleEntity && vehicle.banHand(player)) {
            AmmoBarHud.close()
            return
        }
        AmmoBarHud.render(GunData.from(stack), player)
    }
}
