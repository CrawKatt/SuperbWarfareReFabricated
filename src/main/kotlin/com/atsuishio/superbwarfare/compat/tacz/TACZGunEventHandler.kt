package com.atsuishio.superbwarfare.compat.tacz

import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import com.tacz.guns.api.TimelessAPI
import com.tacz.guns.api.event.common.EntityHurtByGunEvent
import com.tacz.guns.api.item.IGun
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.metadata.version.VersionPredicate

object TACZGunEventHandler {
    fun register() {
        EntityHurtByGunEvent.PRE.register(::entityHurtByTACZGun)
    }

    private fun entityHurtByTACZGun(event: EntityHurtByGunEvent.Pre) {
        if (event.getHurtEntity() is VehicleEntity) {
            event.setHeadshot(false)
        }
    }

    fun hasMod(): Boolean {
        return FabricLoader.getInstance().isModLoaded("tacz")
    }

    fun compatCondition(): Boolean {
        val mod = FabricLoader.getInstance().getModContainer("tacz").orElse(null) ?: return false
        return VersionPredicate.parse(">=1.0.0-neo1.2.0").test(mod.metadata.version)
    }

    fun getTaczCompatIcon(stack: ItemStack): ResourceLocation? {
        val item = stack.item
        if (item is IGun) {
            val gunId: ResourceLocation = item.getGunId(stack)
            val gunData = TimelessAPI.getClientGunIndex(gunId)
                .map { obj -> obj.gunData }.orElse(null)
            val display = TimelessAPI.getGunDisplay(stack).orElse(null)
            if (gunData != null && display != null) {
                return display.getHUDTexture()
            }
        }
        return null
    }
}
