package com.atsuishio.superbwarfare.capability.entity

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.capability.ModCapabilities
import dev.onyxstudios.cca.api.v3.component.Component
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.Entity

class InfiniteAmmoCapability(var hasInfinityAmmo: Boolean = false) : Component {

    override fun readFromNbt(tag: CompoundTag) {
        if (tag.contains(TAG_INFINITY_AMMO)) {
            this.hasInfinityAmmo = tag.getBoolean(TAG_INFINITY_AMMO)
        }
    }

    override fun writeToNbt(tag: CompoundTag) {
        tag.putBoolean(TAG_INFINITY_AMMO, hasInfinityAmmo)
    }

    companion object {
        val ID = Mod.loc("infinite_ammo_capability")
        const val TAG_INFINITY_AMMO = "SbwInfiniteAmmo"

        @JvmStatic
        fun get(entity: Entity): InfiniteAmmoCapability {
            return ModCapabilities.INFINITY_AMMO.get(entity)
        }

        @JvmStatic
        fun modify(entity: Entity, modifier: (InfiniteAmmoCapability) -> Unit) {
            val data = get(entity)
            data.apply(modifier)
        }
    }
}