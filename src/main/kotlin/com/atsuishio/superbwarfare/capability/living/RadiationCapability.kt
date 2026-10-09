package com.atsuishio.superbwarfare.capability.living

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.init.ModComponents
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity

@Serializable
data class RadiationCapability(
    @SerialName("SbwRadiationDosage")
    val dosage: Float = 0f,
) {

    companion object {
        @JvmField
        val ID: ResourceLocation = loc("radiation_capability")

        const val MAX_DOSAGE = 20000f

        @JvmStatic
        fun get(entity: Entity): RadiationCapability {
            return ModComponents.RADIATION.get(entity).value
        }

        @JvmStatic
        fun getOrNull(entity: Entity): RadiationCapability? {
            return ModComponents.RADIATION.maybeGet(entity).orElse(null)?.value
        }

        @JvmStatic
        fun getDosage(entity: Entity): Float {
            return get(entity).dosage
        }

        @JvmStatic
        fun setDosage(entity: Entity, value: Float): Float {
            val capability = get(entity)
            val new = capability.copy(dosage = value.coerceIn(0f, MAX_DOSAGE))
            ModComponents.RADIATION.get(entity).value = new
            if (!entity.level().isClientSide) ModComponents.RADIATION.sync(entity)
            return new.dosage
        }

        @JvmStatic
        fun addDosage(entity: Entity, amount: Float): Float {
            return setDosage(entity, getDosage(entity) + amount)
        }
    }
}
