package com.atsuishio.superbwarfare.perk.functional

import com.atsuishio.superbwarfare.data.PMC
import com.atsuishio.superbwarfare.data.gun.DefaultGunData
import com.atsuishio.superbwarfare.data.gun.GunData
import com.atsuishio.superbwarfare.data.gun.GunProp
import com.atsuishio.superbwarfare.perk.Perk

object Regeneration : Perk("regeneration", Type.FUNCTIONAL) {
    override fun modifyProperty(modifier: PMC<GunData, DefaultGunData>) {
        val maxEnergy = modifier[GunProp.MAX_ENERGY]
        if (maxEnergy <= 0) return

        val level = modifier.data.perk.getLevel(this)
        modifier[GunProp.ENERGY_REGENERATION] += ((1 + level * 0.4) * maxEnergy / 4000).toInt()
    }
}
