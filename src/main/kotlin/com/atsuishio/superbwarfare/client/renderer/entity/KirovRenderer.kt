package com.atsuishio.superbwarfare.client.renderer.entity

import com.atsuishio.superbwarfare.entity.vehicle.KirovEntity
import com.github.mcmodderanchor.simplebedrockmodel.v2.common.model.runtime.BakedModelInstance
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.util.Mth

class KirovRenderer(manager: EntityRendererProvider.Context) : GeoVehicleRenderer<KirovEntity>(manager) {
    override fun hideForTurretControllerWhileZooming(): Boolean {
        return true
    }

    override fun transformCustomModelPart(
        vehicle: KirovEntity,
        instance: BakedModelInstance,
        poseStack: PoseStack,
        entityYaw: Float,
        partialTicks: Float
    ) {
        super.transformCustomModelPart(vehicle, instance, poseStack, entityYaw, partialTicks)

        val propeller = instance.getBone("move_prop1")
        val propeller2 = instance.getBone("move_prop2")
        val propeller3 = instance.getBone("move_prop3")

        val propeller4 = instance.getBone("move_prop4")
        val propeller5 = instance.getBone("move_prop5")

        val rot = Mth.lerp(partialTicks, vehicle.propellerO, vehicle.propeller)
        propeller?.rotation?.rotateZ(rot)

        val rotL = Mth.lerp(partialTicks, vehicle.propellerLO, vehicle.propellerL)
        propeller2?.rotation?.rotateZ(rotL)

        val rotR = Mth.lerp(partialTicks, vehicle.propellerRO, vehicle.propellerR)
        propeller3?.rotation?.rotateZ(rotR)

        val rotV = Mth.lerp(partialTicks, vehicle.propellerVO, vehicle.propellerV)

        propeller4?.rotation?.rotateZ(rotV)
        propeller5?.rotation?.rotateZ(rotV)

        val turretRight = instance.getBone("move_turret_right")
        if (turretRight != null) {
            turretRight.rotation.rotationY(turretYRot * Mth.DEG_TO_RAD)
            turretRight.visible = !(vehicle.isWreck && vehicle.hasTurret() && vehicle.sympatheticDetonated)
        }

        val controlP = instance.getBone("move_controlP")
        controlP?.rotation?.rotationX(Mth.clamp(-vehicle.power * 48, -20f, 20f) * Mth.DEG_TO_RAD)

        val rudder = instance.getBone("move_rudder")
        rudder?.rotation?.rotationZ(12 * Mth.lerp(partialTicks, vehicle.rudderRotO, vehicle.rudderRot))
    }
}
