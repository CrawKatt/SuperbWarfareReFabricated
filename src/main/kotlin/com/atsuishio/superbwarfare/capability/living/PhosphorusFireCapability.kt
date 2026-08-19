package com.atsuishio.superbwarfare.capability.living

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.capability.ModCapabilities
import dev.onyxstudios.cca.api.v3.component.Component
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.LivingEntity

class PhosphorusFireCapability : Component {
    var isOnFire: Boolean = false

    override fun readFromNbt(tag: CompoundTag) {
        if (tag.contains(TAG_PHOSPHORUS_FIRE)) isOnFire = tag.getBoolean(TAG_PHOSPHORUS_FIRE)
    }

    override fun writeToNbt(tag: CompoundTag) {
        tag.putBoolean(TAG_PHOSPHORUS_FIRE, isOnFire)
    }

    companion object {
        @JvmField
        val ID: ResourceLocation = loc("phosphorus_fire_capability")
        const val TAG_PHOSPHORUS_FIRE = "SbwPhosphorusFire"

        @JvmStatic
        fun of(living: LivingEntity): PhosphorusFireCapability = ModCapabilities.PHOSPHORUS_FIRE.get(living)
    }
}
