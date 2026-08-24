package com.atsuishio.superbwarfare.client.renderer.gun

import com.atsuishio.superbwarfare.client.animation.gun.GeoGunAnimationInstance
import com.atsuishio.superbwarfare.client.model.gun.GeoGunModel
import com.atsuishio.superbwarfare.config.client.DisplayConfig
import com.atsuishio.superbwarfare.resource.gun.DefaultGunResource
import com.atsuishio.superbwarfare.resource.gun.GunResource
import com.atsuishio.superbwarfare.tools.RenderDistanceHelper
import com.github.mcmodderanchor.simplebedrockmodel.v1.client.animation.IFPAnimationInstance
import com.github.mcmodderanchor.simplebedrockmodel.v1.client.handler.FirstPersonRenderHandler
import com.github.mcmodderanchor.simplebedrockmodel.v2.client.renderer.AbstractGeoItemRendererV2
import com.maydaymemory.mae.basic.ArrayPoseBuilder
import com.maydaymemory.mae.basic.ZYXBoneTransformFactory
import com.maydaymemory.mae.blend.EulerAdditiveBlender
import com.maydaymemory.mae.blend.SimpleEulerAdditiveBlender
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import net.minecraft.client.player.LocalPlayer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack

open class GeoGunRenderer : AbstractGeoItemRendererV2() {

    override fun createAnimationInstance(stack: ItemStack, entity: Entity): IFPAnimationInstance {
        return GeoGunAnimationInstance(stack, entity, InteractionHand.MAIN_HAND)
    }

    override fun createAnimationInstance(
        stack: ItemStack,
        entity: Entity,
        hand: InteractionHand
    ): IFPAnimationInstance {
        return GeoGunAnimationInstance(stack, entity, hand)
    }

    override fun getSlotTexture(stack: ItemStack): ResourceLocation? {
        val resource = GunResource.compute(stack)
        val slotIcon = if (!resource.slotIcon.isNullOrEmpty()) resource.slotIcon else resource.icon
        return ResourceLocation.tryParse(slotIcon)
    }

    override fun hasModel(stack: ItemStack): Boolean {
        val modelResource = GunResource.compute(stack).model ?: return false
        return GeoGunModel.create(modelResource) != null
    }

    override fun renderFirstPerson(
        player: LocalPlayer,
        stack: ItemStack,
        transformType: ItemDisplayContext,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        partialTick: Float
    ) {
        if (GunResource.compute(stack).itemDisplay[displayKey(transformType)] == null) {
            super.renderFirstPerson(player, stack, transformType, poseStack, bufferSource, packedLight, partialTick)
            return
        }

        render(stack, transformType, poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY, partialTick)
    }

    override fun beforeRender(
        poseStack: PoseStack,
        transformType: ItemDisplayContext,
        stack: ItemStack,
        partialTick: Float
    ) {
        val display = GunResource.compute(stack).itemDisplay[displayKey(transformType)]
        if (display != null) {
            applyItemDisplayTransform(poseStack, display)
        }
        super.beforeRender(poseStack, transformType, stack, partialTick)
    }

    override fun renderModel(
        poseStack: PoseStack,
        transformType: ItemDisplayContext,
        stack: ItemStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int,
        partialTick: Float
    ) {
        val resource = GunResource.compute(stack)
        val modelResource = resource.model ?: return

        val useLod = transformType != ItemDisplayContext.FIRST_PERSON_RIGHT_HAND
                && DisplayConfig.ENABLE_GUN_LOD.get()
                && !RenderDistanceHelper.isInGui()
        val model = if (useLod) {
            GeoGunModel.create(modelResource, 1)
        } else {
            GeoGunModel.create(modelResource)
        } ?: return

        val texture = if (useLod) {
            modelResource.getLODTexture(1)
        } else {
            modelResource.texture
        } ?: return

        model.renderHand = transformType.firstPerson()
        if (transformType.firstPerson()) {
            val hand = if (transformType == ItemDisplayContext.FIRST_PERSON_LEFT_HAND) {
                InteractionHand.OFF_HAND
            } else {
                InteractionHand.MAIN_HAND
            }
            val pose = FirstPersonRenderHandler.getActiveAnimationInstance(hand)?.cachedPose
            if (pose != null) {
                model.applyPose(BLENDER.blend(model.getBindPose(), pose))
            }
        }
        model.renderToBuffer(poseStack, bufferSource, texture, packedLight, packedOverlay)
        model.resetPose()
    }

    private fun applyItemDisplayTransform(poseStack: PoseStack, display: DefaultGunResource.ItemDisplayInfo) {
        val translation = display.translation
        if (translation.size >= 3) {
            poseStack.translate(translation[0] / 16f, translation[1] / 16f, translation[2] / 16f)
        }

        val rotation = display.rotation
        if (rotation.size >= 3) {
            poseStack.mulPose(Axis.XP.rotationDegrees(rotation[0]))
            poseStack.mulPose(Axis.YP.rotationDegrees(rotation[1]))
            poseStack.mulPose(Axis.ZP.rotationDegrees(rotation[2]))
        }

        val scale = display.scale
        if (scale.size >= 3) {
            poseStack.scale(scale[0], scale[1], scale[2])
        }
    }

    private fun displayKey(transformType: ItemDisplayContext): String {
        return when (transformType) {
            ItemDisplayContext.FIRST_PERSON_RIGHT_HAND -> "firstperson_righthand"
            ItemDisplayContext.FIRST_PERSON_LEFT_HAND -> "firstperson_lefthand"
            ItemDisplayContext.THIRD_PERSON_RIGHT_HAND -> "thirdperson_righthand"
            ItemDisplayContext.THIRD_PERSON_LEFT_HAND -> "thirdperson_lefthand"
            ItemDisplayContext.GUI -> "gui"
            ItemDisplayContext.GROUND -> "ground"
            ItemDisplayContext.HEAD -> "head"
            ItemDisplayContext.FIXED -> "fixed"
            else -> ""
        }
    }

    companion object {
        private val BLENDER: EulerAdditiveBlender =
            SimpleEulerAdditiveBlender(ZYXBoneTransformFactory()) { ArrayPoseBuilder() }
    }
}
