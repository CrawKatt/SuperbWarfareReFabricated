package com.atsuishio.superbwarfare.client.renderer.entity

import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import com.github.mcmodderanchor.simplebedrockmodel.v2.common.model.runtime.BakedModelInstance
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.util.Mth

class A10Renderer(manager: EntityRendererProvider.Context) : BasicVehicleRenderer(manager) {
    override fun transformCustomModelPart(
        vehicle: VehicleEntity,
        instance: BakedModelInstance,
        poseStack: PoseStack,
        entityYaw: Float,
        partialTicks: Float
    ) {
        super.transformCustomModelPart(vehicle, instance, poseStack, entityYaw, partialTicks)

        val root = instance.getBone("root")
        root?.visible = !(hideForTurretControllerWhileZooming && vehicle.getWeaponIndex(0) == 2)

        val wingLR = instance.getBone("move_wingLR")
        wingLR?.rotation?.rotateX(
            1.5f * Mth.lerp(
                partialTicks,
                vehicle.flap1LRotO,
                vehicle.flap1LRot
            ) * Mth.DEG_TO_RAD
        )

        val wingRR = instance.getBone("move_wingRR")
        wingRR?.rotation?.rotateX(
            1.5f * Mth.lerp(
                partialTicks,
                vehicle.flap1RRotO,
                vehicle.flap1RRot
            ) * Mth.DEG_TO_RAD
        )

        val wingLR2 = instance.getBone("move_wingLR2")
        wingLR2?.rotation?.rotateX(
            1.5f * Mth.lerp(
                partialTicks,
                vehicle.flap1L2RotO,
                vehicle.flap1L2Rot
            ) * Mth.DEG_TO_RAD
        )

        val wingRR2 = instance.getBone("move_wingRR2")
        wingRR2?.rotation?.rotateX(
            1.5f * Mth.lerp(
                partialTicks,
                vehicle.flap1R2RotO,
                vehicle.flap1R2Rot
            ) * Mth.DEG_TO_RAD
        )

        val wingLB = instance.getBone("move_wingLB")
        wingLB?.rotation?.rotateX(Mth.lerp(partialTicks, vehicle.flap2LRotO, vehicle.flap2LRot) * Mth.DEG_TO_RAD)

        val wingRB = instance.getBone("move_wingRB")
        wingRB?.rotation?.rotateX(Mth.lerp(partialTicks, vehicle.flap2RRotO, vehicle.flap2RRot) * Mth.DEG_TO_RAD)

        val weiyiL = instance.getBone("move_weiyiL")
        val weiyiR = instance.getBone("move_weiyiR")

        weiyiL?.rotation?.rotateY(
            Mth.clamp(
                Mth.lerp(partialTicks, vehicle.flap3RotO, vehicle.flap3Rot),
                -20f,
                20f
            ) * Mth.DEG_TO_RAD
        )
        weiyiR?.rotation?.rotateY(
            Mth.clamp(
                Mth.lerp(partialTicks, vehicle.flap3RotO, vehicle.flap3Rot),
                -20f,
                20f
            ) * Mth.DEG_TO_RAD
        )

        val qianzhou = instance.getBone("move_qianzhou")
        val qianzhou2 = instance.getBone("move_qianzhou2")

        qianzhou?.rotation?.rotateZ(Mth.lerp(partialTicks, vehicle.propellerRotO, vehicle.propellerRot))
        qianzhou2?.rotation?.rotateZ(Mth.lerp(partialTicks, vehicle.propellerRotO, vehicle.propellerRot))
    }
}
