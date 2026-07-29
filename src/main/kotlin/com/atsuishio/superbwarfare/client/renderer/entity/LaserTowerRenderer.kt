package com.atsuishio.superbwarfare.client.renderer.entity

import com.atsuishio.superbwarfare.entity.vehicle.base.AutoAimableEntity
import com.github.mcmodderanchor.simplebedrockmodel.v2.common.model.runtime.BakedModelInstance
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.EntityRendererProvider

class LaserTowerRenderer(manager: EntityRendererProvider.Context) : BasicAutoAimableRenderer(manager) {

    override fun renderEmissive(
        entity: AutoAimableEntity,
        instance: BakedModelInstance,
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
