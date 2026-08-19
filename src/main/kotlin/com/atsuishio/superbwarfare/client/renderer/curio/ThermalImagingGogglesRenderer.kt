package com.atsuishio.superbwarfare.client.renderer.curio

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.client.model.curio.ThermalImagingGogglesModel
import com.atsuishio.superbwarfare.tools.mc
import com.mojang.blaze3d.vertex.PoseStack
import dev.emi.trinkets.api.SlotReference
import dev.emi.trinkets.api.client.TrinketRenderer
import net.minecraft.client.model.EntityModel
import net.minecraft.client.model.HumanoidModel
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.ItemRenderer
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack

class ThermalImagingGogglesRenderer : TrinketRenderer {
    private var model: ThermalImagingGogglesModel? = null

    private fun getModel(): ThermalImagingGogglesModel {
        if (model == null) {
            model = ThermalImagingGogglesModel(mc.entityModels.bakeLayer(ThermalImagingGogglesModel.LAYER_LOCATION))
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
        matrices.pushPose()

        val goggles = getModel()
        goggles.prepareMobModel(entity, limbAngle, limbDistance, tickDelta)
        goggles.setupAnim(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch)

        if (contextModel is HumanoidModel<*>) {
            goggles.bone.copyFrom(contextModel.head)
        }

        val vertexConsumer = ItemRenderer.getArmorFoilBuffer(
            vertexConsumers,
            RenderType.armorCutoutNoCull(TEXTURE),
            false,
            stack.hasFoil()
        )
        goggles.renderToBuffer(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f)
        matrices.popPose()
    }

    companion object {
        private val TEXTURE = loc("textures/curio/thermal_imaging_goggles.png")
    }
}
