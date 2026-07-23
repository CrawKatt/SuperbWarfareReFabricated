package com.atsuishio.superbwarfare.client.renderer.entity

import com.atsuishio.superbwarfare.client.model.entity.BedrockVehicleModel
import com.atsuishio.superbwarfare.entity.vehicle.base.ArtilleryEntity
import com.atsuishio.superbwarfare.event.ClientEventHandler
import com.atsuishio.superbwarfare.tools.localPlayer
import com.atsuishio.superbwarfare.tools.options
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.CameraType
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.util.Mth
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class Fh88bwRenderer(manager: EntityRendererProvider.Context) : BasicArtilleryRenderer(manager) {
    override fun hideForTurretControllerWhileZooming(): Boolean {
        return true
    }

    override fun transformCustomModelPart(
        vehicle: ArtilleryEntity,
        model: BedrockVehicleModel,
        poseStack: PoseStack,
        entityYaw: Float,
        partialTicks: Float
    ) {
        super.transformCustomModelPart(vehicle, model, poseStack, entityYaw, partialTicks)

        val pitch = Mth.clamp(-turretXRot, vehicle.turretMinPitch, vehicle.turretMaxPitch) * Mth.DEG_TO_RAD

        val barrel = model.getBone("barrel")
        val angle = if (!vehicle.lockTurret) {
            pitch
        } else {
            0f
        }

        barrel.rotation.rotationX(angle)

        val b = atan2(11.8113, -14.0761) -
                atan2(
                    32.1847 * sin(pitch) + 9.4012 * cos(pitch) + 2.4101,
                    -32.1847 * cos(pitch) + 9.4012 * sin(pitch) + 18.1086
                )

        model.getBone("yeyagan")?.rotation?.rotationX(b.toFloat())
        model.getBone("yeya")?.rotation?.rotationX((b - angle).toFloat())
        model.getBone("control")?.rotation?.rotationY(12 * Mth.lerp(partialTicks, vehicle.rudderRotO, vehicle.rudderRot))

        model.getBone("hmg")?.visible =
            !(localPlayer == vehicle.getNthEntity(2) && (options.cameraType == CameraType.FIRST_PERSON || ClientEventHandler.zoomVehicle))
    }
}