package com.atsuishio.superbwarfare.client.renderer.curio

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.client.model.curio.ParachuteModel
import com.atsuishio.superbwarfare.item.curio.ParachuteItem
import com.atsuishio.superbwarfare.tools.mc
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import dev.emi.trinkets.api.SlotReference
import dev.emi.trinkets.api.client.TrinketRenderer
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.minecraft.client.CameraType
import net.minecraft.client.model.EntityModel
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderBuffers
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.ItemRenderer
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack

class ParachuteRenderer : TrinketRenderer {
    private var model: ParachuteModel? = null

    private fun getModel(): ParachuteModel {
        if (model == null) {
            model = ParachuteModel(mc.entityModels.bakeLayer(ParachuteModel.LAYER_LOCATION))
        }
        return model!!
    }

    override fun render(
        stack: ItemStack,
        slotReference: SlotReference,
        contextModel: EntityModel<out LivingEntity>,
        matrices: PoseStack,
        vertexConsumers: MultiBufferSource,
        light: Int,
        entity: LivingEntity,
        limbAngle: Float,
        limbDistance: Float,
        tickDelta: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
        if (!ParachuteItem.isVisible(stack)) return

        matrices.pushPose()
        matrices.scale(0.5f, 0.5f, 0.5f)
        matrices.translate(0.0, 1.25, 0.0)

        if (stack.getOrCreateTag().getBoolean(ParachuteItem.TAG_OPEN)) {
            val parachute = getModel()
            parachute.prepareMobModel(entity, limbAngle, limbDistance, tickDelta)
            parachute.setupAnim(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch)

            val vertexConsumer = ItemRenderer.getArmorFoilBuffer(
                vertexConsumers,
                RenderType.armorCutoutNoCull(TEXTURE),
                false,
                stack.hasFoil()
            )

            parachute.renderToBuffer(
                matrices,
                vertexConsumer,
                light,
                OverlayTexture.NO_OVERLAY,
                1f,
                1f,
                1f,
                1f
            )
        }

        matrices.popPose()
    }

    companion object {
        private var firstPersonModel: ParachuteModel? = null
        private val TEXTURE = loc("textures/curio/parachute.png")

        @JvmStatic
        fun onRenderLevelStage() {
            WorldRenderEvents.AFTER_TRANSLUCENT.register { context ->
                val player = mc.player ?: return@register
                if (!ParachuteItem.isParachuteOpen(player)) return@register
                if (!ParachuteItem.isParachuteVisible(player)) return@register
                if (mc.options.cameraType != CameraType.FIRST_PERSON) return@register

                val matrices = context.matrixStack()
                val buffers: RenderBuffers = mc.renderBuffers()
                matrices.pushPose()

                if (firstPersonModel == null) {
                    firstPersonModel = ParachuteModel(mc.entityModels.bakeLayer(ParachuteModel.LAYER_LOCATION))
                }

                val tickDelta = context.tickDelta()
                matrices.mulPose(Axis.XP.rotationDegrees(180f))
                matrices.mulPose(Axis.YP.rotationDegrees(player.getViewYRot(tickDelta)))
                matrices.translate(0.0, 1.5, 0.0)

                firstPersonModel!!.prepareMobModel(player, 0f, 0f, tickDelta)
                firstPersonModel!!.setupAnim(player, 0f, 0f, player.tickCount.toFloat(), 0f, 0f)
                firstPersonModel!!.renderToBuffer(
                    matrices,
                    buffers.bufferSource().getBuffer(RenderType.armorCutoutNoCull(TEXTURE)),
                    0xFFFFFF,
                    OverlayTexture.NO_OVERLAY,
                    1f,
                    1f,
                    1f,
                    1f
                )

                matrices.popPose()
            }
        }
    }
}
