package com.atsuishio.superbwarfare.capability.living

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.init.ModComponents
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.LivingEntity
import org.ladysnake.cca.api.v3.component.Component

class PhosphorusFireCapability(var isOnFire: Boolean = false) : Component {

    override fun readFromNbt(tag: CompoundTag, registryLookup: HolderLookup.Provider) {
        if (tag.contains(TAG_PHOSPHORUS_FIRE)) {
            this.isOnFire = tag.getBoolean(TAG_PHOSPHORUS_FIRE)
        }
    }

    override fun writeToNbt(tag: CompoundTag, registryLookup: HolderLookup.Provider) {
        tag.putBoolean(TAG_PHOSPHORUS_FIRE, this.isOnFire)
    }

    companion object {
        @JvmField
        val ID: ResourceLocation = loc("phosphorus_fire_capability")
        const val TAG_PHOSPHORUS_FIRE: String = "SbwPhosphorusFire"

        @JvmStatic
        fun of(living: LivingEntity): PhosphorusFireCapability {
            return ModComponents.PHOSPHORUS_FIRE.get(living)
        }
    }
}
