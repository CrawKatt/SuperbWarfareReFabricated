package com.atsuishio.superbwarfare.capability.living

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.init.ModComponents
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.Entity
import org.ladysnake.cca.api.v3.component.Component

class InfinityAmmoCapability(var hasInfinityAmmo: Boolean = false) : Component {

    override fun writeToNbt(tag: CompoundTag, registryLookup: HolderLookup.Provider) {
        tag.putBoolean(TAG_INFINITY_AMMO, hasInfinityAmmo)
    }

    override fun readFromNbt(tag: CompoundTag, registryLookup: HolderLookup.Provider) {
        if (tag.contains(TAG_INFINITY_AMMO)) {
            this.hasInfinityAmmo = tag.getBoolean(TAG_INFINITY_AMMO)
        }
    }

    companion object {
        @JvmField
        val ID = loc("infinity_ammo_capability")

        const val TAG_INFINITY_AMMO = "SbwInfinityAmmo"

        @JvmStatic
        fun get(entity: Entity): InfinityAmmoCapability {
            return ModComponents.INFINITY_AMMO.get(entity)
        }

        @JvmStatic
        fun modify(entity: Entity, modifier: (InfinityAmmoCapability) -> Unit) {
            val data = get(entity)
            data.apply(modifier)
        }
    }
}