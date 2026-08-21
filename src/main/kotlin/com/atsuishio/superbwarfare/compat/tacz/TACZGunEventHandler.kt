package com.atsuishio.superbwarfare.compat.tacz

import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import com.tacz.guns.api.TimelessAPI
import com.tacz.guns.api.event.common.EntityHurtByGunEvent
import com.tacz.guns.api.item.IGun
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

object TACZGunEventHandler {
    fun registerEvents() {
        EntityHurtByGunEvent.PRE.register(::entityHurtByTACZGun)
    }

    fun entityHurtByTACZGun(event: EntityHurtByGunEvent.Pre) {
        if (event.hurtEntity is VehicleEntity) {
            event.setHeadshot(false)
        }
    }

    fun hasMod(): Boolean {
        return FabricLoader.getInstance().isModLoaded("tacz")
    }

    fun compatCondition(): Boolean {
        return hasMod()
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
