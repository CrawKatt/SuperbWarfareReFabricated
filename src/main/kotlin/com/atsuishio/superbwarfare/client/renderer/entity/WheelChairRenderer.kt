package com.atsuishio.superbwarfare.client.renderer.entity

import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import com.github.mcmodderanchor.simplebedrockmodel.v2.common.model.runtime.BakedModelInstance
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.util.Mth

class WheelChairRenderer(manager: EntityRendererProvider.Context) : BasicVehicleRendererV2(manager) {
    init {
        this.shadowRadius = 0.5f
    }

    override fun transformCustomModelPart(
        vehicle: VehicleEntity,
        instance: BakedModelInstance,
        poseStack: PoseStack,
        entityYaw: Float,
        partialTicks: Float
    ) {
        val rightWheelRot = Mth.lerp(partialTicks, vehicle.rightWheelRotO, vehicle.rightWheelRot)
        val leftWheelRot = Mth.lerp(partialTicks, vehicle.leftWheelRotO, vehicle.leftWheelRot)

        instance.getBone("move_w_rb")?.rotation?.rotationX(rightWheelRot)
        instance.getBone("move_w_lb")?.rotation?.rotationX(leftWheelRot)
        instance.getBone("move_w_rr")?.rotation?.rotationX(4f * rightWheelRot)
        instance.getBone("move_w_lr")?.rotation?.rotationX(4f * leftWheelRot)
    }
}
