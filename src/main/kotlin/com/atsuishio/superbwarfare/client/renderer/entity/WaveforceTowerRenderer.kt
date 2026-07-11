package com.atsuishio.superbwarfare.client.renderer.entity

import com.atsuishio.superbwarfare.client.model.entity.BedrockVehicleModel
import com.atsuishio.superbwarfare.client.renderer.ModRenderTypes
import com.atsuishio.superbwarfare.entity.vehicle.base.AutoAimableEntity
import com.github.mcmodderanchor.simplebedrockmodel.v1.client.renderer.BedrockModelRenderTypes
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.texture.OverlayTexture

class WaveforceTowerRenderer(manager: EntityRendererProvider.Context) : BasicAutoAimableRenderer(manager){

    @Suppress("unused")
    var energy0: Float = 0f

    override fun renderCustomPart(
        vehicle: AutoAimableEntity,
        model: BedrockVehicleModel,
        poseStack: PoseStack,
        entityYaw: Float,
        partialTicks: Float,
        buffer: MultiBufferSource,
        packedLight: Int
    ) {
        super.renderCustomPart(vehicle, model, poseStack, entityYaw, partialTicks, buffer, packedLight)

        if (vehicle.energy > 0 && vehicle.active) {
            val emissive = this.getEmissiveTextureLocation(poseStack, vehicle)
            model.renderToBuffer(
                poseStack,
                buffer,
                ModRenderTypes.LASER.apply(emissive),
                BedrockModelRenderTypes.polyMeshCutout(emissive),
                packedLight,
                OverlayTexture.NO_OVERLAY
            )
        }
    }
}
