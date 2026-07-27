package com.atsuishio.superbwarfare.client.renderer.entity

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.client.renderer.ModRenderTypes
import com.atsuishio.superbwarfare.entity.vehicle.AnnihilatorEntity
import com.atsuishio.superbwarfare.entity.vehicle.base.ArtilleryEntity
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import com.github.mcmodderanchor.simplebedrockmodel.v1.client.renderer.BedrockModelRenderTypes
import com.github.mcmodderanchor.simplebedrockmodel.v2.common.model.runtime.BakedModelInstance
import com.github.mcmodderanchor.simplebedrockmodel.v2.common.model.runtime.BoneState
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.util.Mth

class AnnihilatorRenderer(manager: EntityRendererProvider.Context) : BasicArtilleryRenderer(manager) {
    companion object {
        val TEXTURE_GLOW = Mod.loc("textures/bedrock/vehicle/annihilator_glow.png")
        val TEXTURE_POWER = Mod.loc("textures/bedrock/vehicle/annihilator_power.png")
    }

    override fun transformCustomModelPart(
        vehicle: ArtilleryEntity,
        instance: BakedModelInstance,
        poseStack: PoseStack,
        entityYaw: Float,
        partialTicks: Float
    ) {
        super.transformCustomModelPart(vehicle, instance, poseStack, entityYaw, partialTicks)

        val laser1 = instance.getBone("laser1")
        val laser2 = instance.getBone("laser2")
        val laser3 = instance.getBone("laser3")

        laser1?.zScale = vehicle.entityData.get(AnnihilatorEntity.LASER_LEFT_LENGTH) * 10
        laser2?.zScale = vehicle.entityData.get(AnnihilatorEntity.LASER_MIDDLE_LENGTH) * 10
        laser3?.zScale = vehicle.entityData.get(AnnihilatorEntity.LASER_RIGHT_LENGTH) * 10

        val energy = vehicle.chargeProgress

        for (i in 1..5) {
            val greenBoneName = "light_on$i"
            val redBoneName = "light_off$i"
            val greenBone = instance.getBone(greenBoneName)
            val redBone = instance.getBone(redBoneName)

            if (greenBone != null && redBone != null) {
                greenBone.visible = energy >= (i / 5.0)
                redBone.visible = energy < (i / 5.0)
            }
        }
    }

    override fun customLaserLength(laserBones: List<BoneState>, entity: VehicleEntity, partialTicks: Float) {
        for (laser in laserBones) {
            laser.visible = false

            val scale = Mth.lerp(
                partialTicks,
                entity.laserScaleO,
                entity.laserScale
            ).coerceAtMost(1.2f)

            laser.xScale = scale
            laser.yScale = scale
        }
    }

    override fun renderCustomPart(
        vehicle: ArtilleryEntity,
        instance: BakedModelInstance,
        poseStack: PoseStack,
        entityYaw: Float,
        partialTicks: Float,
        buffer: MultiBufferSource,
        packedLight: Int
    ) {
        super.renderCustomPart(vehicle, instance, poseStack, entityYaw, partialTicks, buffer, packedLight)

        // power

        val red = 1 - Mth.clamp(2.5f * vehicle.energy / vehicle.maxEnergy, 0f, 1f)
        val green = Mth.clamp(2.5f * vehicle.energy / vehicle.maxEnergy, 0f, 1f)

        instance.renderToBuffer(
            poseStack,
            buffer,
            RenderType.entityTranslucent(TEXTURE_POWER),
            BedrockModelRenderTypes.polyMeshCutout(TEXTURE_POWER),
            packedLight,
            OverlayTexture.NO_OVERLAY, red, green, 0f, 1f
        )

        instance.renderToBuffer(
            poseStack,
            buffer,
            ModRenderTypes.LASER.apply(TEXTURE_POWER),
            BedrockModelRenderTypes.polyMeshCutout(TEXTURE_POWER),
            packedLight,
            OverlayTexture.NO_OVERLAY, red, green, 0f, 1f
        )

        instance.renderToBuffer(
            poseStack,
            buffer,
            ModRenderTypes.LASER.apply(TEXTURE_GLOW),
            BedrockModelRenderTypes.polyMeshCutout(TEXTURE_GLOW),
            packedLight,
            OverlayTexture.NO_OVERLAY
        )
    }
}
