package com.atsuishio.superbwarfare.client.renderer.entity

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.entity.vehicle.WheelChairEntity
import com.atsuishio.superbwarfare.resource.model.VehicleModelReloadListenerV2
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
        // Fetch model every frame to properly handle resource reloads
        val model = VehicleModelReloadListenerV2.getModel(MODEL) ?: return
        val instance = model.createInstance()

        poseStack.pushPose()

        // Vehicle axis rotation (same as GeoVehicleRenderer.rotateVehicleAxis)
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

        // Reset instance to bind pose before applying per-frame transforms
        instance.resetPose()

        // Wheel rotation
        val rightWheelRot = Mth.lerp(partialTicks, entity.rightWheelRotO, entity.rightWheelRot)
        val leftWheelRot = Mth.lerp(partialTicks, entity.leftWheelRotO, entity.leftWheelRot)

        instance.getBone("w_rb")?.rotation?.rotationX(rightWheelRot)
        instance.getBone("w_lb")?.rotation?.rotationX(leftWheelRot)
        instance.getBone("w_rr")?.rotation?.rotationX(4f * rightWheelRot)
        instance.getBone("w_lr")?.rotation?.rotationX(4f * leftWheelRot)

        // Render with dual render types (translucent + cutout)
        model.renderToBuffer(
            instance,
            poseStack,
            buffer,
            RenderType.entityTranslucent(TEXTURE),
            BedrockModelRenderTypes.polyMeshCutout(TEXTURE),
            packedLight,
            OverlayTexture.NO_OVERLAY
        )

        poseStack.popPose()
    }

    companion object {
        val TEXTURE: ResourceLocation = Mod.loc("textures/bedrock/vehicle/wheel_chair.png")
        val MODEL: ResourceLocation = Mod.loc("models/bedrock/vehicle_v2/wheel_chair.geo.json")
    }
}
