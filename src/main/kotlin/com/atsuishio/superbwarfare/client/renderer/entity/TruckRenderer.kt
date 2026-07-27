package com.atsuishio.superbwarfare.client.renderer.entity

import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import com.github.mcmodderanchor.simplebedrockmodel.v2.common.model.runtime.BakedModelInstance
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.util.Mth

class TruckRenderer(manager: EntityRendererProvider.Context) : BasicVehicleRenderer(manager) {
    override fun transformCustomModelPart(
        vehicle: VehicleEntity,
        instance: BakedModelInstance,
        poseStack: PoseStack,
        entityYaw: Float,
        partialTicks: Float
    ) {
        super.transformCustomModelPart(vehicle, instance, poseStack, entityYaw, partialTicks)
        val control = instance.getBone("move_control")

        control?.rotation?.rotationY(12 * Mth.lerp(partialTicks, vehicle.rudderRotO, vehicle.rudderRot))
    }
}