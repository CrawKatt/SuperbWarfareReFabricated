package com.atsuishio.superbwarfare.compat.tacz

import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import com.tacz.guns.api.TimelessAPI
import com.tacz.guns.api.item.IGun
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.Version
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

object TACZGunEventHandler {
    fun hasMod(): Boolean {
        return FabricLoader.getInstance().isModLoaded("tacz")
    }

    fun compatCondition(): Boolean {
        val container = FabricLoader.getInstance().getModContainer("tacz").orElse(null) ?: return false
        return container.metadata.version >= Version.parse("1.1.4")
    }

    fun getTaczCompatIcon(stack: ItemStack): ResourceLocation? {
        val item = stack.item
        if (item is IGun) {
            val gunId: ResourceLocation = item.getGunId(stack)
            val gunData = TimelessAPI.getClientGunIndex(gunId)
                .map { obj -> obj.gunData }.orElse(null)
            val display = TimelessAPI.getGunDisplay(stack).orElse(null)
            if (gunData != null && display != null) {
                return display.hudTexture
            }
        }
        return null
    }
}
