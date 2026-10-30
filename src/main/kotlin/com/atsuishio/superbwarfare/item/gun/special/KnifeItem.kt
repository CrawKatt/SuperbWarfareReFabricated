package com.atsuishio.superbwarfare.item.gun.special

import com.atsuishio.superbwarfare.client.renderer.gun.GeoGunRenderer
import com.atsuishio.superbwarfare.client.renderer.gun.KnifeRenderer
import com.atsuishio.superbwarfare.item.gun.GeoGunItem
import net.minecraft.world.item.ItemStack
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment

open class KnifeItem(properties: Properties) : GeoGunItem(properties) {

    override fun isDamageable(stack: ItemStack?): Boolean = true

    @Environment(EnvType.CLIENT)
    override fun createRenderer(): GeoGunRenderer = KnifeRenderer()
}
