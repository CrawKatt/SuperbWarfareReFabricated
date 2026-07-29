package com.atsuishio.superbwarfare.client.renderer.entity

import com.atsuishio.superbwarfare.client.model.entity.VehicleModelInstance
import com.atsuishio.superbwarfare.entity.vehicle.KirovEntity
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.util.Mth

class KirovRenderer(manager: EntityRendererProvider.Context) : GeoVehicleRenderer<KirovEntity>(manager) {
    override fun hideForTurretControllerWhileZooming(): Boolean {
        return true
    }

    override fun transformCustomModelPart(
        entity: KirovEntity,
        instance: VehicleModelInstance,
        poseStack: PoseStack,
        entityYaw: Float,
        partialTicks: Float
    ) {
        super.transformCustomModelPart(entity, instance, poseStack, entityYaw, partialTicks)

        val propeller = instance.getBone("move_prop1")
        val propeller2 = instance.getBone("move_prop2")
        val propeller3 = instance.getBone("move_prop3")

        val propeller4 = instance.getBone("move_prop4")
        val propeller5 = instance.getBone("move_prop5")

        val rot = Mth.lerp(partialTicks, entity.propellerO, entity.propeller)
        propeller?.rotation?.rotateZ(rot)

        val rotL = Mth.lerp(partialTicks, entity.propellerLO, entity.propellerL)
        propeller2?.rotation?.rotateZ(rotL)

        val rotR = Mth.lerp(partialTicks, entity.propellerRO, entity.propellerR)
        propeller3?.rotation?.rotateZ(rotR)

        val rotV = Mth.lerp(partialTicks, entity.propellerVO, entity.propellerV)

        propeller4?.rotation?.rotateZ(rotV)
        propeller5?.rotation?.rotateZ(rotV)

        val turretRight = instance.getBone("move_turret_right")
        if (turretRight != null) {
            turretRight.rotation.rotationY(turretYRot * Mth.DEG_TO_RAD)
            turretRight.visible = !(entity.isWreck && entity.hasTurret() && entity.sympatheticDetonated)
        }

        val controlP = instance.getBone("move_controlP")
        controlP?.rotation?.rotationX(Mth.clamp(-entity.power * 48, -20f, 20f) * Mth.DEG_TO_RAD)

        val rudder = instance.getBone("move_rudder")
        rudder?.rotation?.rotationZ(12 * Mth.lerp(partialTicks, entity.rudderRotO, entity.rudderRot))
    }
}
