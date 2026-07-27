package com.atsuishio.superbwarfare.client.renderer.entity

import com.atsuishio.superbwarfare.entity.vehicle.TowEntity
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import com.atsuishio.superbwarfare.event.ClientEventHandler
import com.atsuishio.superbwarfare.tools.localPlayer
import com.atsuishio.superbwarfare.tools.options
import com.github.mcmodderanchor.simplebedrockmodel.v2.common.model.runtime.BakedModelInstance
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.CameraType
import net.minecraft.client.renderer.entity.EntityRendererProvider

class TowRenderer(manager: EntityRendererProvider.Context) : BasicVehicleRenderer(manager) {
    override fun transformCustomModelPart(
        vehicle: VehicleEntity,
        instance: BakedModelInstance,
        poseStack: PoseStack,
        entityYaw: Float,
        partialTicks: Float
    ) {
        super.transformCustomModelPart(vehicle, instance, poseStack, entityYaw, partialTicks)
        val guanMiao = instance.getBone("move_guanmiao")
        val missile = instance.getBone("move_missile")

        guanMiao?.visible = !(vehicle.turretControllerIndex == vehicle.getSeatIndex(localPlayer)
                && (options.cameraType == CameraType.FIRST_PERSON || ClientEventHandler.zoomVehicle))

        missile?.visible = vehicle.entityData.get(TowEntity.LOADED)

    }
}
