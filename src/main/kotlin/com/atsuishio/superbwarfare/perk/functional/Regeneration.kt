package com.atsuishio.superbwarfare.perk.functional

import com.atsuishio.superbwarfare.init.ModCapabilities

import com.atsuishio.superbwarfare.data.gun.GunData
import com.atsuishio.superbwarfare.perk.Perk
import com.atsuishio.superbwarfare.perk.PerkInstance
import net.minecraft.world.entity.Entity

object Regeneration : Perk("regeneration", Type.FUNCTIONAL) {
    override fun tick(
        data: GunData,
        instance: PerkInstance,
        entity: Entity?
    ) {
        val stack = data.stack
        ModCapabilities.ENERGY_ITEM.find(stack, null)?.let {
            it.receiveEnergy(instance.level * it.maxEnergyStored / 2000, false)
        }
    }
}
