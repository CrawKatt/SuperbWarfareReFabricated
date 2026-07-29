package com.atsuishio.superbwarfare.entity.vehicle

import com.atsuishio.superbwarfare.client.animation.entity.VehicleAnimationInstance
import com.atsuishio.superbwarfare.client.model.entity.VehicleModelInstance
import net.minecraft.resources.ResourceLocation

data class VehicleModelEntry(
    val instance: VehicleModelInstance,
    val texture: ResourceLocation,
    val emissiveTexture: ResourceLocation?,
    val lodDistance: Int,  // 0 = main model, > 0 = LOD
) {
    fun isLOD() = lodDistance > 0
}

interface BasicGeoVehicleEntity {
    fun getAnimationInstance(): VehicleAnimationInstance<*>? = null

    fun getModelEntries(): List<VehicleModelEntry> = emptyList()
}
