package com.atsuishio.superbwarfare.client.renderer.entity

import com.atsuishio.superbwarfare.client.model.entity.VehicleModelInstance
import com.atsuishio.superbwarfare.entity.vehicle.SodayoPickUpRocketEntity
import com.atsuishio.superbwarfare.entity.vehicle.SodayoPickUpTowEntity
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import com.atsuishio.superbwarfare.event.ClientEventHandler
import com.atsuishio.superbwarfare.tools.localPlayer
import com.atsuishio.superbwarfare.tools.options
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.CameraType
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.util.Mth

class SodayoPickUpRenderer(manager: EntityRendererProvider.Context) : BasicVehicleRenderer(manager) {
    override fun transformCustomModelPart(
        entity: VehicleEntity,
        instance: VehicleModelInstance,
        poseStack: PoseStack,
        entityYaw: Float,
        partialTicks: Float
    ) {
        super.transformCustomModelPart(entity, instance, poseStack, entityYaw, partialTicks)
        val control = instance.getBone("move_control")
        val head = instance.getBone("move_head")

        control?.rotation?.rotationZ(8 * Mth.lerp(partialTicks, entity.rudderRotO, entity.rudderRot))

        val pitch = -5f * entity.getAcceleration().toFloat() * Mth.DEG_TO_RAD
        val roll = 0.5f * Mth.lerp(partialTicks, entity.rudderRotO, entity.rudderRot) * entity.deltaMovement.horizontalDistance().toFloat() * Mth.DEG_TO_RAD
        head?.rotation?.rotateX(pitch)
        head?.rotation?.rotateZ(roll)

        if (entity is SodayoPickUpRocketEntity) {
            instance.boneGroups.shell.forEachIndexed { index, bone ->
                val items = entity.entityData.get(SodayoPickUpRocketEntity.LOADED_AMMO)
                bone.visible = items[index] != -1
            }
        }

        if (entity is SodayoPickUpTowEntity) {
            val guanMiao = instance.getBone("move_guanmiao")
            guanMiao?.visible =
                !(entity.turretControllerIndex == entity.getSeatIndex(localPlayer) && (options.cameraType == CameraType.FIRST_PERSON || ClientEventHandler.zoomVehicle))
        }
    }
}


