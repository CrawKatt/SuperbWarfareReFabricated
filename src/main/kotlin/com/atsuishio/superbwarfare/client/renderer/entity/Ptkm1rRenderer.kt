package com.atsuishio.superbwarfare.client.renderer.entity

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.entity.projectile.Ptkm1rEntity
import com.maydaymemory.mae.basic.ArrayPoseBuilder
import com.maydaymemory.mae.basic.ZYXBoneTransformFactory
import com.maydaymemory.mae.blend.EulerAdditiveBlender
import com.maydaymemory.mae.blend.SimpleEulerAdditiveBlender
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth

class Ptkm1rRenderer(renderManager: EntityRendererProvider.Context) : EntityRenderer<Ptkm1rEntity>(renderManager) {
    override fun shouldShowName(pEntity: Ptkm1rEntity): Boolean {
        return false
    }

    override fun getTextureLocation(pEntity: Ptkm1rEntity): ResourceLocation {
        return TEXTURE
    }

    override fun render(
        entity: Ptkm1rEntity,
        entityYaw: Float,
        partialTick: Float,
        poseStack: PoseStack,
        buffer: MultiBufferSource,
        packedLight: Int
    ) {
        val instance = entity.modelInstance ?: return
        val ani = entity.animationInstance ?: return

        poseStack.pushPose()

        val renderType = RenderType.entityTranslucent(getTextureLocation(entity))
        val vertexConsumer = buffer.getBuffer(renderType)

        ani.context.partialTick = partialTick
        ani.tick()
        instance.applyPose(BLENDER.blend(instance.bindPose, ani.getPose()))

        val bodyBone = instance.getBone("move_body")
        bodyBone?.rotation?.mul(Axis.YP.rotationDegrees(-entityYaw))

        val zhuBone = instance.getBone("move_zhu2")
        zhuBone?.rotation?.mul(Axis.XP.rotationDegrees(-0.5f * Mth.lerp(partialTick, entity.xRotO, entity.xRot)))

        instance.renderToBuffer(
            poseStack,
            vertexConsumer,
            packedLight,
            OverlayTexture.NO_OVERLAY
        )

        poseStack.popPose()
    }

    companion object {
        val TEXTURE = loc("textures/bedrock/projectile/ptkm_1r.png")
        val BLENDER: EulerAdditiveBlender = SimpleEulerAdditiveBlender(ZYXBoneTransformFactory()) { ArrayPoseBuilder() }
    }
}
