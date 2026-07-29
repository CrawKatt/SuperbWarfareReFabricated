package com.atsuishio.superbwarfare.client.renderer.entity

import com.atsuishio.superbwarfare.client.model.entity.VehicleModelInstance
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import com.atsuishio.superbwarfare.event.ClientEventHandler
import com.atsuishio.superbwarfare.tools.localPlayer
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.util.Mth

class SpeedBoatRenderer(manager: EntityRendererProvider.Context) : BasicVehicleRenderer(manager) {
    override fun hideForTurretControllerWhileZooming(): Boolean {
        return true
    }

    override fun transformCustomModelPart(
        entity: VehicleEntity,
        instance: VehicleModelInstance,
        poseStack: PoseStack,
        entityYaw: Float,
        partialTicks: Float
    ) {
        super.transformCustomModelPart(entity, instance, poseStack, entityYaw, partialTicks)

        if (entity.isWreck) return

        val propeller = instance.getBone("move_propeller")
        val propeller2 = instance.getBone("move_propeller2")
        val turret = instance.getBone("turret")
        val control = instance.getBone("move_control")
        val rudder = instance.getBone("move_rudder")

        propeller?.rotation?.rotationZ(Mth.lerp(partialTicks, entity.propellerRotO, entity.propellerRot))
        propeller2?.rotation?.rotationZ(-Mth.lerp(partialTicks, entity.propellerRotO, entity.propellerRot))
        turret?.visible = !(entity.getNthEntity(entity.turretControllerIndex) === localPlayer && ClientEventHandler.zoomVehicle)
        control?.rotation?.rotationZ(-4 * Mth.lerp(partialTicks, entity.rudderRotO, entity.rudderRot))
        rudder?.rotation?.rotationY(Mth.lerp(partialTicks, entity.rudderRotO, entity.rudderRot))
    }
}
