package com.atsuishio.superbwarfare.capability.entity

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.capability.CapabilityWrapper
import com.atsuishio.superbwarfare.capability.ModCapabilities
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.world.entity.Entity

/**
 * 无限弹药状态。
 *
 * 只描述数据；存档读写与网络同步由 [CapabilityWrapper] 代劳，见 [ModCapabilities.INFINITE_AMMO]。
 */
@Serializable
data class InfiniteAmmoCapability(
    @SerialName("SbwInfiniteAmmo")
    val hasInfiniteAmmo: Boolean = false,
) {
    companion object {
        val ID = Mod.loc("infinite_ammo_capability")

        @JvmStatic
        fun get(entity: Entity): InfiniteAmmoCapability = ModCapabilities.INFINITE_AMMO.get(entity)

        @JvmStatic
        fun set(entity: Entity, value: Boolean) {
            ModCapabilities.INFINITE_AMMO.set(entity, InfiniteAmmoCapability(value))
        }

        @JvmStatic
        fun toggle(entity: Entity): Boolean {
            val enabled = !get(entity).hasInfiniteAmmo
            set(entity, enabled)
            return enabled
        }
    }

    /** 只为了让 Forge 有一个唯一的 capability 实现类而存在，见 [CapabilityWrapper]。 */
    class Holder : CapabilityWrapper<InfiniteAmmoCapability>(
        serializer(),
        InfiniteAmmoCapability(),
    )
}
