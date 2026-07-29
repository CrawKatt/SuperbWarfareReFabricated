package com.atsuishio.superbwarfare.client.renderer.entity

import com.atsuishio.superbwarfare.client.model.entity.VehicleModelInstance
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.util.Mth

class Kv16Renderer(manager: EntityRendererProvider.Context) : BasicVehicleRenderer(manager) {
    override fun transformCustomModelPart(
        entity: VehicleEntity,
        instance: VehicleModelInstance,
        poseStack: PoseStack,
        entityYaw: Float,
        partialTicks: Float
    ) {
        super.transformCustomModelPart(entity, instance, poseStack, entityYaw, partialTicks)

        val root = instance.getBone("root")
        root?.visible = !(hideForTurretControllerWhileZooming && entity.getWeaponIndex(0) == 1)

        val propeller = instance.getBone("move_propeller")

        propeller?.rotation?.rotateZ(Mth.lerp(partialTicks, entity.propellerRotO, entity.propellerRot))
    }
}
