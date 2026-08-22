package com.atsuishio.superbwarfare.client.renderer.entity

import com.atsuishio.superbwarfare.client.model.entity.VehicleModelInstance
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.util.Mth

class A10Renderer(manager: EntityRendererProvider.Context) : BasicVehicleRenderer(manager) {
    override fun transformCustomModelPart(
        entity: VehicleEntity,
        instance: VehicleModelInstance,
        poseStack: PoseStack,
        entityYaw: Float,
        partialTicks: Float
    ) {
        super.transformCustomModelPart(entity, instance, poseStack, entityYaw, partialTicks)

        val root = instance.getBone("root")
        root?.visible = !(hideForTurretControllerWhileZooming && entity.getWeaponIndex(0) == 2)

        if (entity.isWreck) return

        val wingLR = instance.getBone("move_wingLR")
        wingLR?.rotation?.rotateX(
            1.5f * Mth.lerp(
                partialTicks,
                entity.flap1LRotO,
                entity.flap1LRot
            ) * Mth.DEG_TO_RAD
        )

        val wingRR = instance.getBone("move_wingRR")
        wingRR?.rotation?.rotateX(
            1.5f * Mth.lerp(
                partialTicks,
                entity.flap1RRotO,
                entity.flap1RRot
            ) * Mth.DEG_TO_RAD
        )

        val wingLR2 = instance.getBone("move_wingLR2")
        wingLR2?.rotation?.rotateX(
            1.5f * Mth.lerp(
                partialTicks,
                entity.flap1L2RotO,
                entity.flap1L2Rot
            ) * Mth.DEG_TO_RAD
        )

        val wingRR2 = instance.getBone("move_wingRR2")
        wingRR2?.rotation?.rotateX(
            1.5f * Mth.lerp(
                partialTicks,
                entity.flap1R2RotO,
                entity.flap1R2Rot
            ) * Mth.DEG_TO_RAD
        )

        val wingLB = instance.getBone("move_wingLB")
        wingLB?.rotation?.rotateX(Mth.lerp(partialTicks, entity.flap2LRotO, entity.flap2LRot) * Mth.DEG_TO_RAD)

        val wingRB = instance.getBone("move_wingRB")
        wingRB?.rotation?.rotateX(Mth.lerp(partialTicks, entity.flap2RRotO, entity.flap2RRot) * Mth.DEG_TO_RAD)

        val weiyiL = instance.getBone("move_weiyiL")
        val weiyiR = instance.getBone("move_weiyiR")

        weiyiL?.rotation?.rotateY(
            Mth.clamp(
                Mth.lerp(partialTicks, entity.flap3RotO, entity.flap3Rot),
                -20f,
                20f
            ) * Mth.DEG_TO_RAD
        )
        weiyiR?.rotation?.rotateY(
            Mth.clamp(
                Mth.lerp(partialTicks, entity.flap3RotO, entity.flap3Rot),
                -20f,
                20f
            ) * Mth.DEG_TO_RAD
        )

        val qianzhou = instance.getBone("move_qianzhou")
        val qianzhou2 = instance.getBone("move_qianzhou2")

        qianzhou?.rotation?.rotateZ(Mth.lerp(partialTicks, entity.propellerRotO, entity.propellerRot))
        qianzhou2?.rotation?.rotateZ(Mth.lerp(partialTicks, entity.propellerRotO, entity.propellerRot))
    }
}
