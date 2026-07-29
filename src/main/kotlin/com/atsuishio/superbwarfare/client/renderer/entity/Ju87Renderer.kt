package com.atsuishio.superbwarfare.client.renderer.entity

import com.atsuishio.superbwarfare.entity.vehicle.Ju87Entity
import com.atsuishio.superbwarfare.event.ClientEventHandler
import com.github.mcmodderanchor.simplebedrockmodel.v2.common.model.runtime.BakedModelInstance
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.util.Mth

class Ju87Renderer(manager: EntityRendererProvider.Context) : GeoVehicleRenderer<Ju87Entity>(manager) {
    override fun transformCustomModelPart(
        entity: Ju87Entity,
        instance: BakedModelInstance,
        poseStack: PoseStack,
        entityYaw: Float,
        partialTicks: Float
    ) {
        super.transformCustomModelPart(entity, instance, poseStack, entityYaw, partialTicks)

        val root = instance.getBone("root")
        root?.visible = !(ClientEventHandler.zoomVehicle && entity.firstPassenger == Minecraft.getInstance().player
                && (entity.getWeaponIndex(0) == 1
                || entity.getWeaponIndex(0) == 2))

        val wingLR = instance.getBone("move_wingLR")
        val wingLR2 = instance.getBone("move_wingLR2")
        val wingLR3 = instance.getBone("move_wingLR3")

        wingLR?.rotation?.rotateX(
            1.5f * Mth.lerp(
                partialTicks,
                entity.flap2LRotO,
                entity.flap2LRot
            ) * Mth.DEG_TO_RAD
        )

        wingLR2?.rotation?.rotateX(
            1.5f * Mth.lerp(
                partialTicks,
                entity.flap2LRotO,
                entity.flap2LRot
            ) * Mth.DEG_TO_RAD
        )

        wingLR3?.rotation?.rotateX(
            1.5f * Mth.lerp(
                partialTicks,
                entity.flap2LRotO,
                entity.flap2LRot
            ) * Mth.DEG_TO_RAD
        )

        val wingRR = instance.getBone("move_wingRR")
        val wingRR2 = instance.getBone("move_wingRR2")
        val wingRR3 = instance.getBone("move_wingRR3")

        wingRR?.rotation?.rotateX(
            1.5f * Mth.lerp(
                partialTicks,
                entity.flap2RRotO,
                entity.flap2RRot
            ) * Mth.DEG_TO_RAD
        )

        wingRR2?.rotation?.rotateX(
            1.5f * Mth.lerp(
                partialTicks,
                entity.flap2RRotO,
                entity.flap2RRot
            ) * Mth.DEG_TO_RAD
        )

        wingRR3?.rotation?.rotateX(
            1.5f * Mth.lerp(
                partialTicks,
                entity.flap2RRotO,
                entity.flap2RRot
            ) * Mth.DEG_TO_RAD
        )

        val wingLB = instance.getBone("move_wingLB")
        wingLB?.rotation?.rotateX(Mth.lerp(partialTicks, entity.flap2LRotO, entity.flap2LRot) * Mth.DEG_TO_RAD)

        val wingRB = instance.getBone("move_wingRB")
        wingRB?.rotation?.rotateX(Mth.lerp(partialTicks, entity.flap2RRotO, entity.flap2RRot) * Mth.DEG_TO_RAD)

        val breakerL = instance.getBone("move_breakerL")
        val breakerR = instance.getBone("move_breakerR")

        breakerL?.rotation?.rotateX(2 * entity.planeBreak * Mth.DEG_TO_RAD)
        breakerR?.rotation?.rotateX(2 * entity.planeBreak * Mth.DEG_TO_RAD)

        val tailWing = instance.getBone("move_tailWing")
        tailWing?.rotation?.rotateY(
            Mth.clamp(
                Mth.lerp(partialTicks, entity.flap3RotO, entity.flap3Rot),
                -20f,
                20f
            ) * Mth.DEG_TO_RAD
        )

        val propeller = instance.getBone("move_propeller")
        val propeller2 = instance.getBone("move_propeller2")
        val propeller3 = instance.getBone("move_propeller3")

        propeller?.rotation?.rotateZ(-Mth.lerp(partialTicks, entity.propellerRotO, entity.propellerRot))

        val rot = Mth.lerp(partialTicks, entity.smallPropellerO, entity.smallPropeller)

        propeller2?.rotation?.rotateZ(-rot)
        propeller3?.rotation?.rotateZ(rot)
    }
}
