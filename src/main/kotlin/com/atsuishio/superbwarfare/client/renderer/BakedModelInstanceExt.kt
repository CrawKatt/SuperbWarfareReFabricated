package com.atsuishio.superbwarfare.client.renderer

import com.github.mcmodderanchor.simplebedrockmodel.v2.common.model.runtime.BakedModelInstance
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer

/**
 * Renders a single bone (and its children) of a [BakedModelInstance] using the given
 * [VertexConsumer], mirroring the behaviour of GeckoLib's removed renderSingleBonePass.
 */
fun BakedModelInstance.renderSingleBonePass(
    poseStack: PoseStack,
    boneIndex: Int,
    consumer: VertexConsumer,
    packedLight: Int,
    packedOverlay: Int,
    r: Float,
    g: Float,
    b: Float,
    a: Float,
    checkQuadsInTree: Boolean,
    shade: Boolean
) {
    baseModel().renderBone(
        this,
        boneIndex,
        poseStack,
        consumer,
        packedLight,
        packedOverlay,
        r, g, b, a,
        checkQuadsInTree,
        shade
    )
}
