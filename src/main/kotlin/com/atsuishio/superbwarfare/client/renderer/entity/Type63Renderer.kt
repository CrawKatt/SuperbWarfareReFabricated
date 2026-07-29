package com.atsuishio.superbwarfare.client.renderer.entity

import com.atsuishio.superbwarfare.client.model.entity.VehicleModelInstance
import com.atsuishio.superbwarfare.entity.vehicle.Type63Entity
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.entity.EntityRendererProvider

class Type63Renderer(manager: EntityRendererProvider.Context) : BasicVehicleRenderer(manager) {

    override fun transformCustomModelPart(entity: VehicleEntity, instance: VehicleModelInstance, poseStack: PoseStack, entityYaw: Float, partialTicks: Float) {
        super.transformCustomModelPart(entity, instance, poseStack, entityYaw, partialTicks)
        val shouLunX = instance.getBone("move_shoulunx")
        val shouLunY = instance.getBone("move_shouluny")

        if (shouLunX != null && shouLunY != null) {
            shouLunX.rotation.rotationX(-turretXRot * 3)
            shouLunY.rotation.rotationZ(turretYRot * 6)
        }

        instance.boneGroups.shell.forEachIndexed { index, bone ->
            val items = entity.entityData.get(Type63Entity.LOADED_AMMO)
            bone.visible = items[index] != -1
        }
    }
}
