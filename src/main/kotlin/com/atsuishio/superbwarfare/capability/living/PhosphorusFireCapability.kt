package com.atsuishio.superbwarfare.capability.living

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.capability.CapabilityWrapper
import com.atsuishio.superbwarfare.capability.ModCapabilities
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity

/**
 * 白磷弹燃烧状态。
 *
 * 只描述数据；存档读写与网络同步由 [CapabilityWrapper] 代劳，见 [ModCapabilities.PHOSPHORUS_FIRE]。
 */
@Serializable
data class PhosphorusFireCapability(
    @SerialName("SbwPhosphorusFire")
    val isOnFire: Boolean = false,
) {
    companion object {
        val ID: ResourceLocation = loc("phosphorus_fire_capability")

        @JvmStatic
        fun get(entity: Entity): PhosphorusFireCapability = ModCapabilities.PHOSPHORUS_FIRE.get(entity)

        @JvmStatic
        fun set(entity: Entity, value: Boolean) {
            ModCapabilities.PHOSPHORUS_FIRE.set(entity, PhosphorusFireCapability(value))
        }
    }

    /** 只为了让 Forge 有一个唯一的 capability 实现类而存在，见 [CapabilityWrapper]。 */
    class Holder : CapabilityWrapper<PhosphorusFireCapability>(
        serializer(),
        PhosphorusFireCapability(),
    )
}
