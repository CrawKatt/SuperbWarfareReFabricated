package com.atsuishio.superbwarfare.item.gun.special

import com.atsuishio.superbwarfare.client.renderer.gun.GeoGunRenderer
import com.atsuishio.superbwarfare.client.renderer.gun.KnifeRenderer
import com.atsuishio.superbwarfare.item.gun.GeoGunItem
import net.minecraft.world.inventory.tooltip.TooltipComponent
import net.minecraft.world.item.ItemStack
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import java.util.*

open class KnifeItem(properties: Properties) : GeoGunItem(properties) {

    override fun getTooltipImage(pStack: ItemStack): Optional<TooltipComponent> {
        return Optional.empty()
    }

    @Environment(EnvType.CLIENT)
    override fun createRenderer(): GeoGunRenderer = KnifeRenderer()
}
