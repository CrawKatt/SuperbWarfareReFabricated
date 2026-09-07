package com.atsuishio.superbwarfare.capability.entity

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.capability.ModCapabilities
import com.atsuishio.superbwarfare.serialization.decodeFromCompoundTag
import com.atsuishio.superbwarfare.serialization.encodeToCompoundTag
import dev.onyxstudios.cca.api.v3.component.Component
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.Entity

@Serializable
data class InfiniteAmmoCapability(
    @SerialName("SbwInfiniteAmmo")
    var hasInfiniteAmmo: Boolean = false,
) : Component {

    override fun readFromNbt(tag: CompoundTag) {
        hasInfiniteAmmo = decodeFromCompoundTag(serializer<InfiniteAmmoCapability>(), tag).hasInfiniteAmmo
    }

    override fun writeToNbt(tag: CompoundTag) {
        val written = encodeToCompoundTag(serializer<InfiniteAmmoCapability>(), this)
        for (key in written.allKeys) written.get(key)?.let { tag.put(key, it) }
    }

    companion object {
        val ID = Mod.loc("infinite_ammo_capability")

        @JvmStatic
        fun get(entity: Entity): InfiniteAmmoCapability {
            return ModCapabilities.INFINITE_AMMO.get(entity)
        }

        @JvmStatic
        fun set(entity: Entity, value: Boolean) {
            get(entity).hasInfiniteAmmo = value
        }

        @JvmStatic
        fun toggle(entity: Entity): Boolean {
            val enabled = !get(entity).hasInfiniteAmmo
            set(entity, enabled)
            return enabled
        }
    }
}
