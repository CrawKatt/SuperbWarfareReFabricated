package com.atsuishio.superbwarfare.capability.entity

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.init.ModComponents
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.Entity
import org.ladysnake.cca.api.v3.component.Component

class InfiniteAmmoCapability(var hasInfiniteAmmo: Boolean = false) : Component {

    override fun writeToNbt(tag: CompoundTag, registryLookup: HolderLookup.Provider) {
        tag.putBoolean(TAG_INFINITE_AMMO, hasInfiniteAmmo)
    }

    override fun readFromNbt(tag: CompoundTag, registryLookup: HolderLookup.Provider) {
        if (tag.contains(TAG_INFINITE_AMMO)) {
            this.hasInfiniteAmmo = tag.getBoolean(TAG_INFINITE_AMMO)
        }
    }

    companion object {
        @JvmField
        val ID = Mod.loc("infinite_ammo_capability")

        const val TAG_INFINITE_AMMO = "SbwInfiniteAmmo"

        @JvmStatic
        fun get(entity: Entity): InfiniteAmmoCapability {
            return ModComponents.INFINITE_AMMO.get(entity)
        }

        @JvmStatic
        fun modify(entity: Entity, modifier: (InfiniteAmmoCapability) -> Unit) {
            val data = get(entity)
            data.apply(modifier)
        }
    }
}