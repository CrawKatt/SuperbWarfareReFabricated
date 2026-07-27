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
        vehicle: VehicleEntity,
        instance: BakedModelInstance,
        poseStack: PoseStack,
        entityYaw: Float,
        partialTicks: Float
    ) {
        super.transformCustomModelPart(vehicle, instance, poseStack, entityYaw, partialTicks)

        val wingFL = instance.getBone("move_wingFL")
        val wingFR = instance.getBone("move_wingFR")
        val xRotL = -1.5f * Mth.lerp(partialTicks, vehicle.flap2RRotO, vehicle.flap2RRot) * Mth.DEG_TO_RAD
        val xRotR = -1.5f * Mth.lerp(partialTicks, vehicle.flap2RRotO, vehicle.flap2RRot) * Mth.DEG_TO_RAD

        wingFL?.rotation?.rotateX(xRotL)
        wingFR?.rotation?.rotateX(xRotR)

        val tailWingHL = instance.getBone("move_tailWingHL")

        tailWingHL?.rotation?.rotateX(Mth.lerp(partialTicks, vehicle.flap2LRotO, vehicle.flap2LRot) * Mth.DEG_TO_RAD)

        val tailWingHR = instance.getBone("move_tailWingHR")

        tailWingHR?.rotation?.rotateX(Mth.lerp(partialTicks, vehicle.flap2RRotO, vehicle.flap2RRot) * Mth.DEG_TO_RAD)

        val tailWingV = instance.getBone("move_tailWingV")

        tailWingV?.rotation?.rotateY(
            Mth.clamp(
                Mth.lerp(partialTicks, vehicle.flap3RotO, vehicle.flap3Rot),
                -20f,
                20f
            ) * Mth.DEG_TO_RAD
        )

        val propeller = instance.getBone("move_prop1")
        val propeller2 = instance.getBone("move_prop2")
        val propeller3 = instance.getBone("move_prop3")
        val propeller4 = instance.getBone("move_prop4")
        val rot = Mth.lerp(partialTicks, vehicle.propellerRotO, vehicle.propellerRot)

        propeller?.rotation?.rotateZ(rot)
        propeller2?.rotation?.rotateZ(rot)
        propeller3?.rotation?.rotateZ(rot)
        propeller4?.rotation?.rotateZ(rot)

        val player = localPlayer
        val hide =
            player != null && vehicle === player.vehicle && vehicle.hasWeapon(vehicle.getSeatIndex(player)) && (options.cameraType == CameraType.FIRST_PERSON || ClientEventHandler.zoomVehicle)

        val gd = instance.getBone("move_gd")
        gd?.visible = !hide
    }
}