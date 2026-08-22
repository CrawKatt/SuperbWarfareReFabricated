package com.atsuishio.superbwarfare.client.renderer.entity

import com.atsuishio.superbwarfare.client.model.entity.VehicleModelInstance
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.util.Mth

class WheelChairRenderer(manager: EntityRendererProvider.Context) : BasicVehicleRenderer(manager) {
    init {
        this.shadowRadius = 0.5f
    }

    override fun transformCustomModelPart(
        entity: VehicleEntity,
        instance: VehicleModelInstance,
        poseStack: PoseStack,
        entityYaw: Float,
        partialTicks: Float
    ) {
        val rightWheelRot = Mth.lerp(partialTicks, entity.rightWheelRotO, entity.rightWheelRot)
        val leftWheelRot = Mth.lerp(partialTicks, entity.leftWheelRotO, entity.leftWheelRot)

        instance.getBone("w_rb")?.rotation?.rotationX(rightWheelRot)
        instance.getBone("w_lb")?.rotation?.rotationX(leftWheelRot)
        instance.getBone("w_rr")?.rotation?.rotationX(4f * rightWheelRot)
        instance.getBone("w_lr")?.rotation?.rotationX(4f * leftWheelRot)
    }
}
