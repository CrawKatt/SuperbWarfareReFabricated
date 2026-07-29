package com.atsuishio.superbwarfare.client.renderer.entity

import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import com.github.mcmodderanchor.simplebedrockmodel.v2.common.model.runtime.BakedModelInstance
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.util.Mth

class Ah6Renderer(manager: EntityRendererProvider.Context) : BasicVehicleRenderer(manager) {
    override fun transformCustomModelPart(
        entity: VehicleEntity,
        instance: BakedModelInstance,
        poseStack: PoseStack,
        entityYaw: Float,
        partialTicks: Float
    ) {
        super.transformCustomModelPart(entity, instance, poseStack, entityYaw, partialTicks)
        val propeller = instance.getBone("move_propeller")
        val tailPropeller = instance.getBone("move_tailPropeller")

        propeller?.rotation?.rotationY(Mth.lerp(partialTicks, entity.propellerRotO, entity.propellerRot))
        tailPropeller?.rotation?.rotationX(-6 * Mth.lerp(partialTicks, entity.propellerRotO, entity.propellerRot))
    }
}
