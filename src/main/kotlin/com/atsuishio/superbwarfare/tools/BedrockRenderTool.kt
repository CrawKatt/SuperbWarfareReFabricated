package com.atsuishio.superbwarfare.tools

import com.github.mcmodderanchor.simplebedrockmodel.v2.common.model.runtime.BakedModelInstance
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType

fun BakedModelInstance.renderSingleBonePass(
    poseStack: PoseStack,
    boneIndex: Int,
    buffer: VertexConsumer,
    packedLight: Int,
    packedOverlay: Int,
    red: Float,
    green: Float,
    blue: Float,
    alpha: Float,
    quadsPass: Boolean,
    skipNormalVisibilityCull: Boolean
) {
    poseStack.pushPose()
    getBone(boneIndex)?.parentIndex()?.takeIf { it >= 0 }?.let { poseStack.mulPose(getGlobalTransform(it)) }
    baseModel().renderBone(
        this, boneIndex, poseStack, buffer, packedLight, packedOverlay,
        red, green, blue, alpha, quadsPass, skipNormalVisibilityCull
    )
    poseStack.popPose()
}

fun BakedModelInstance.renderSingleBone(
    poseStack: PoseStack,
    boneIndex: Int,
    bufferSource: MultiBufferSource,
    quadRenderType: RenderType,
    triangleRenderType: RenderType,
    packedLight: Int,
    packedOverlay: Int,
    red: Float,
    green: Float,
    blue: Float,
    alpha: Float,
    skipNormalVisibilityCull: Boolean
) {
    renderSingleBonePass(
        poseStack, boneIndex, bufferSource.getBuffer(quadRenderType), packedLight, packedOverlay,
        red, green, blue, alpha, true, skipNormalVisibilityCull
    )
    renderSingleBonePass(
        poseStack, boneIndex, bufferSource.getBuffer(triangleRenderType), packedLight, packedOverlay,
        red, green, blue, alpha, false, skipNormalVisibilityCull
    )
}
