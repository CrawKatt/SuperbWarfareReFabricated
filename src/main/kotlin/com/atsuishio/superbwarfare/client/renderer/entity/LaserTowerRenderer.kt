package com.atsuishio.superbwarfare.client.renderer.entity

import com.atsuishio.superbwarfare.client.model.entity.VehicleModelInstance
import com.atsuishio.superbwarfare.entity.vehicle.base.AutoAimableEntity
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.EntityRendererProvider

class LaserTowerRenderer(manager: EntityRendererProvider.Context) : BasicAutoAimableRenderer(manager) {

    override fun renderEmissive(
        entity: AutoAimableEntity,
        instance: VehicleModelInstance,
        yaw: Float,
        partialTick: Float,
        poseStack: PoseStack,
        buffer: MultiBufferSource,
        packedLight: Int
    ) {
        if (entity.energy <= 0 || !entity.active) return
        super.renderEmissive(entity, instance, yaw, partialTick, poseStack, buffer, packedLight)
    }
}
