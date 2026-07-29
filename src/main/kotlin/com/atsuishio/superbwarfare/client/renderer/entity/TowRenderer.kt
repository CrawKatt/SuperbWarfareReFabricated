package com.atsuishio.superbwarfare.client.renderer.entity

import com.atsuishio.superbwarfare.client.model.entity.VehicleModelInstance
import com.atsuishio.superbwarfare.entity.vehicle.TowEntity
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import com.atsuishio.superbwarfare.event.ClientEventHandler
import com.atsuishio.superbwarfare.tools.localPlayer
import com.atsuishio.superbwarfare.tools.options
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.CameraType
import net.minecraft.client.renderer.entity.EntityRendererProvider

class TowRenderer(manager: EntityRendererProvider.Context) : BasicVehicleRenderer(manager) {
    override fun transformCustomModelPart(
        entity: VehicleEntity,
        instance: VehicleModelInstance,
        poseStack: PoseStack,
        entityYaw: Float,
        partialTicks: Float
    ) {
        super.transformCustomModelPart(entity, instance, poseStack, entityYaw, partialTicks)
        val guanMiao = instance.getBone("move_guanmiao")
        val missile = instance.getBone("move_missile")

        guanMiao?.visible = !(entity.turretControllerIndex == entity.getSeatIndex(localPlayer)
                && (options.cameraType == CameraType.FIRST_PERSON || ClientEventHandler.zoomVehicle))

        missile?.visible = entity.entityData.get(TowEntity.LOADED)
    }
}
