package com.atsuishio.superbwarfare.item.gun.special

import com.atsuishio.superbwarfare.data.gun.GunData
import com.atsuishio.superbwarfare.data.gun.ShootParameters
import com.atsuishio.superbwarfare.capability.energy.EnergyStorageHelper
import com.atsuishio.superbwarfare.item.gun.GeoGunItemV2
import net.minecraft.world.entity.Entity
import team.reborn.energy.api.EnergyStorage

object TaserItem : GeoGunItemV2(Properties()) {

    override fun afterShoot(parameters: ShootParameters) {
        super.afterShoot(parameters)

        val data = parameters.data
        val stack = data.stack
        EnergyStorage.ITEM.find(stack, null)?.let { EnergyStorageHelper.extract(it, 400L) }
    }

    override fun canShoot(data: GunData, shooter: Entity?): Boolean {
        val stack = data.stack
        val hasEnoughEnergy = (EnergyStorage.ITEM.find(stack, null)?.amount ?: 0L) >= 400L

        if (!hasEnoughEnergy) return false

        return super.canShoot(data, shooter)
    }
}
