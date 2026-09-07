package com.atsuishio.superbwarfare.capability.living

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.capability.ModCapabilities
import com.atsuishio.superbwarfare.serialization.decodeFromCompoundTag
import com.atsuishio.superbwarfare.serialization.encodeToCompoundTag
import dev.onyxstudios.cca.api.v3.component.Component
import net.minecraft.world.entity.LivingEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation

@Serializable
data class PhosphorusFireCapability(
    @SerialName("SbwPhosphorusFire")
    var isOnFire: Boolean = false,
) : Component {

    override fun readFromNbt(tag: CompoundTag) {
        isOnFire = decodeFromCompoundTag(serializer<PhosphorusFireCapability>(), tag).isOnFire
    }

    override fun writeToNbt(tag: CompoundTag) {
        val written = encodeToCompoundTag(serializer<PhosphorusFireCapability>(), this)
        for (key in written.allKeys) written.get(key)?.let { tag.put(key, it) }
    }

    companion object {
        @JvmField
        val ID: ResourceLocation = loc("phosphorus_fire_capability")

        @JvmStatic
        fun get(entity: LivingEntity): PhosphorusFireCapability = ModCapabilities.PHOSPHORUS_FIRE.get(entity)

        @JvmStatic
        fun set(entity: LivingEntity, value: Boolean) {
            get(entity).isOnFire = value
        }
    }
}
