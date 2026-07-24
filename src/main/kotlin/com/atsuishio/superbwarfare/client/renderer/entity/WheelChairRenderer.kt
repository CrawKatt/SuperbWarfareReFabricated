package com.atsuishio.superbwarfare.client.renderer.entity

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.entity.vehicle.VehicleModelEntry
import com.atsuishio.superbwarfare.entity.vehicle.WheelChairEntity
import com.atsuishio.superbwarfare.tools.RenderDistanceHelper
import com.github.mcmodderanchor.simplebedrockmodel.v1.client.renderer.BedrockModelRenderTypes
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraft.world.phys.Vec3

class WheelChairRenderer(manager: EntityRendererProvider.Context) : EntityRenderer<WheelChairEntity>(manager) {

    init {
        this.shadowRadius = 0.5f
    }

    override fun getTextureLocation(entity: WheelChairEntity): ResourceLocation = TEXTURE

    override fun shouldShowName(entity: WheelChairEntity): Boolean = false

    override fun render(
        entity: WheelChairEntity,
        entityYaw: Float,
        partialTicks: Float,
        poseStack: PoseStack,
        buffer: MultiBufferSource,
        packedLight: Int
    ) {
        val entries = entity.getModelEntries()
        if (entries.isEmpty()) return
        val entry = selectModelEntry(entries, poseStack) ?: return

        poseStack.pushPose()

        val root = Vec3(0.0, entity.rotateOffsetHeight, 0.0)
        poseStack.rotateAround(
            Axis.YP.rotationDegrees(-entityYaw + 180f),
            root.x.toFloat(), root.y.toFloat(), root.z.toFloat()
        )
        poseStack.rotateAround(
            Axis.XP.rotationDegrees(
                -Mth.lerp(partialTicks, entity.xRotO, entity.xRot)
            ),
            root.x.toFloat(), root.y.toFloat(), root.z.toFloat()
        )
        poseStack.rotateAround(
            Axis.ZP.rotationDegrees(
                -Mth.lerp(partialTicks, entity.prevRoll, entity.roll)
            ),
            root.x.toFloat(), root.y.toFloat(), root.z.toFloat()
        )

        val rightWheelRot = Mth.lerp(partialTicks, entity.rightWheelRotO, entity.rightWheelRot)
        val leftWheelRot = Mth.lerp(partialTicks, entity.leftWheelRotO, entity.leftWheelRot)

        entry.instance.getBone("w_rb")?.rotation?.rotationX(rightWheelRot)
        entry.instance.getBone("w_lb")?.rotation?.rotationX(leftWheelRot)
        entry.instance.getBone("w_rr")?.rotation?.rotationX(4f * rightWheelRot)
        entry.instance.getBone("w_lr")?.rotation?.rotationX(4f * leftWheelRot)

        entry.instance.renderToBuffer(
            poseStack,
            buffer,
            RenderType.entityTranslucent(entry.texture),
            BedrockModelRenderTypes.polyMeshCutout(entry.texture),
            packedLight,
            OverlayTexture.NO_OVERLAY
        )

        poseStack.popPose()
    }

    companion object {
        val TEXTURE: ResourceLocation = Mod.loc("textures/bedrock/vehicle/wheel_chair.png")
        val MODEL: ResourceLocation = Mod.loc("models/bedrock/vehicle_v2/wheel_chair.geo.json")

        // TODO 合并至geo renderer
        @JvmStatic
        fun selectModelEntry(entries: List<VehicleModelEntry>, poseStack: PoseStack): VehicleModelEntry? {
            if (entries.isEmpty()) return null
            entries.forEachIndexed { index, entry ->
                if (index == 0) return@forEachIndexed  // skip main model (distance = 0)
                if (RenderDistanceHelper.shouldRenderLOD(poseStack, entry.lodDistance.toDouble())) {
                    return entry
                }
            }
            return entries.firstOrNull()
        }
    }
}
