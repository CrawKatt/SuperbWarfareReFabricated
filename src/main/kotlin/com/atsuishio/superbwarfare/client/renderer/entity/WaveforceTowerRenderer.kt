package com.atsuishio.superbwarfare.client.renderer.entity

import com.atsuishio.superbwarfare.entity.vehicle.base.AutoAimableEntity
import com.github.mcmodderanchor.simplebedrockmodel.v2.common.model.runtime.BakedModelInstance
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.EntityRendererProvider

class WaveforceTowerRenderer(manager: EntityRendererProvider.Context) : BasicAutoAimableRenderer(manager) {

    @Suppress("unused")
    var energy0: Float = 0f

//    override fun renderCustomPart(
//        entity: AutoAimableEntity,
//        instance: BakedModelInstance,
//        poseStack: PoseStack,
//        entityYaw: Float,
//        partialTicks: Float,
//        buffer: MultiBufferSource,
//        packedLight: Int
//    ) {
//        super.renderCustomPart(entity, instance, poseStack, entityYaw, partialTicks, buffer, packedLight)
//
//        if (entity.energy > 0 && entity.active) {
//            val emissive = this.getEmissiveTextureLocation(poseStack, entity)
//            instance.renderToBuffer(
//                poseStack,
//                buffer,
//                ModRenderTypes.LASER.apply(emissive),
//                BedrockModelRenderTypes.polyMeshCutout(emissive),
//                packedLight,
//                OverlayTexture.NO_OVERLAY
//            )
//        }
//    }

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
