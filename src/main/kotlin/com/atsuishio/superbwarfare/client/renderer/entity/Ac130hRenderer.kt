package com.atsuishio.superbwarfare.client.renderer.entity

import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import com.atsuishio.superbwarfare.event.ClientEventHandler
import com.atsuishio.superbwarfare.tools.localPlayer
import com.atsuishio.superbwarfare.tools.options
import com.github.mcmodderanchor.simplebedrockmodel.v2.common.model.runtime.BakedModelInstance
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.CameraType
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.util.Mth

class Ac130hRenderer(manager: EntityRendererProvider.Context) : BasicVehicleRenderer(manager) {
    override fun transformCustomModelPart(
        entity: VehicleEntity,
        instance: BakedModelInstance,
        poseStack: PoseStack,
        entityYaw: Float,
        partialTicks: Float
    ) {
        super.transformCustomModelPart(entity, instance, poseStack, entityYaw, partialTicks)

        val wingFL = instance.getBone("move_wingFL")
        val wingFR = instance.getBone("move_wingFR")
        val xRotL = -1.5f * Mth.lerp(partialTicks, entity.flap2RRotO, entity.flap2RRot) * Mth.DEG_TO_RAD
        val xRotR = -1.5f * Mth.lerp(partialTicks, entity.flap2RRotO, entity.flap2RRot) * Mth.DEG_TO_RAD

        wingFL?.rotation?.rotateX(xRotL)
        wingFR?.rotation?.rotateX(xRotR)

        val tailWingHL = instance.getBone("move_tailWingHL")

        tailWingHL?.rotation?.rotateX(Mth.lerp(partialTicks, entity.flap2LRotO, entity.flap2LRot) * Mth.DEG_TO_RAD)

        val tailWingHR = instance.getBone("move_tailWingHR")

        tailWingHR?.rotation?.rotateX(Mth.lerp(partialTicks, entity.flap2RRotO, entity.flap2RRot) * Mth.DEG_TO_RAD)

        val tailWingV = instance.getBone("move_tailWingV")

        tailWingV?.rotation?.rotateY(
            Mth.clamp(
                Mth.lerp(partialTicks, entity.flap3RotO, entity.flap3Rot),
                -20f,
                20f
            ) * Mth.DEG_TO_RAD
        )

        val propeller = instance.getBone("move_prop1")
        val propeller2 = instance.getBone("move_prop2")
        val propeller3 = instance.getBone("move_prop3")
        val propeller4 = instance.getBone("move_prop4")
        val rot = Mth.lerp(partialTicks, entity.propellerRotO, entity.propellerRot)

        propeller?.rotation?.rotateZ(rot)
        propeller2?.rotation?.rotateZ(rot)
        propeller3?.rotation?.rotateZ(rot)
        propeller4?.rotation?.rotateZ(rot)

        val player = localPlayer
        val hide =
            player != null && entity === player.vehicle && entity.hasWeapon(entity.getSeatIndex(player)) && (options.cameraType == CameraType.FIRST_PERSON || ClientEventHandler.zoomVehicle)

        val gd = instance.getBone("move_gd")
        gd?.visible = !hide
    }
}