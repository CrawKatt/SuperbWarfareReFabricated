package com.atsuishio.superbwarfare.client.renderer.entity

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.entity.living.DPSGeneratorEntity
import com.atsuishio.superbwarfare.resource.model.EntityModelReloadListener
import com.atsuishio.superbwarfare.tools.mulPoseMatrix
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

class DPSGeneratorRenderer(renderManager: EntityRendererProvider.Context) :
    EntityRenderer<DPSGeneratorEntity>(renderManager) {
    override fun getTextureLocation(pEntity: DPSGeneratorEntity): ResourceLocation {
        return TEXTURES[pEntity.generatorLevel.coerceIn(0, 7)]
    }

    override fun render(
        entity: DPSGeneratorEntity,
        entityYaw: Float,
        partialTick: Float,
        poseStack: PoseStack,
        buffer: MultiBufferSource,
        packedLight: Int
    ) {
        val model = EntityModelReloadListener.getModel(MODEL) ?: return
        val ani = entity.animationInstance ?: return
        val instance = model.createInstance()

        poseStack.pushPose()
        poseStack.mulPose(Axis.YP.rotationDegrees(180f))
        poseStack.mulPose(Axis.YP.rotationDegrees(-entity.getViewYRot(partialTick)))

        val renderType = RenderType.entityTranslucent(getTextureLocation(entity))
        val vertexConsumer = buffer.getBuffer(renderType)

        ani.context.partialTick = partialTick
        ani.tick()
        instance.applyPose(BLENDER.blend(model.bindPose, ani.getPose()))

        instance.renderToBuffer(
            poseStack,
            vertexConsumer,
            packedLight,
            OverlayTexture.pack(0f, entity.hurtTime > 0 || entity.deathTime > 0)
        )

        poseStack.pushPose()
        val boneIndex = instance.getIndex("move_ba")
        val global = instance.getGlobalTransform(boneIndex)

        val boneConsumer = buffer.getBuffer(RenderType.eyes(TEXTURE_E))
        poseStack.mulPoseMatrix(global)
        model.renderBone(
            instance,
            boneIndex,
            poseStack,
            boneConsumer,
            packedLight,
            OverlayTexture.NO_OVERLAY,
            1f,
            1f,
            1f,
            1f,
            false
        )
        poseStack.popPose()

        poseStack.popPose()
    }

    companion object {
        val TEXTURES =
            ArrayList<ResourceLocation>((0..7).map { loc("textures/bedrock/entity/dps_generator_tier_${it}.png") })
        val TEXTURE_E = loc("textures/bedrock/entity/dps_generator_e.png")
        val BLENDER: EulerAdditiveBlender = SimpleEulerAdditiveBlender(ZYXBoneTransformFactory()) { ArrayPoseBuilder() }
        val MODEL = loc("models/bedrock/entity/dps_generator.geo.json")
    }
}
