package com.atsuishio.superbwarfare.capability.entity

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.init.ModComponents
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.Entity
import org.ladysnake.cca.api.v3.component.Component

@Serializable
data class InfiniteAmmoCapability(
    @SerialName("SbwInfiniteAmmo")
    var hasInfiniteAmmo: Boolean = false
) : Component {

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

        @JvmStatic
        fun set(entity: Entity, value: Boolean) {
            modify(entity) { it.hasInfiniteAmmo = value }
        }

        @JvmStatic
        fun set(entity: Entity, value: InfiniteAmmoCapability) {
            set(entity, value.hasInfiniteAmmo)
        }

        @JvmStatic
        fun toggle(entity: Entity): Boolean {
            val enabled = !get(entity).hasInfiniteAmmo
            set(entity, enabled)
            return enabled
        }
    }
}
