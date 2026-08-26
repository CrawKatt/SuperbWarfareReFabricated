package com.atsuishio.superbwarfare.client.renderer.entity

import com.atsuishio.superbwarfare.client.model.entity.VehicleModelInstance
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.util.Mth

class Mi28Renderer(manager: EntityRendererProvider.Context) : BasicVehicleRenderer(manager) {
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
        val tailPropeller = instance.getBone("move_tailPropeller")

        propeller?.rotation?.rotateY(-Mth.lerp(partialTicks, entity.propellerRotO, entity.propellerRot))
        tailPropeller?.rotation?.rotateX(6 * Mth.lerp(partialTicks, entity.propellerRotO, entity.propellerRot))
    }
}
