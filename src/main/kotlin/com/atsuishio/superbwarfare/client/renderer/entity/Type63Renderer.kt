package com.atsuishio.superbwarfare.client.renderer.entity

import com.atsuishio.superbwarfare.entity.vehicle.Type63Entity
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import com.github.mcmodderanchor.simplebedrockmodel.v2.common.model.runtime.BakedModelInstance
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.entity.EntityRendererProvider

class Type63Renderer(manager: EntityRendererProvider.Context) : BasicVehicleRenderer(manager) {

    override fun transformCustomModelPart(vehicle: VehicleEntity, instance: BakedModelInstance, poseStack: PoseStack, entityYaw: Float, partialTicks: Float) {
        super.transformCustomModelPart(vehicle, instance, poseStack, entityYaw, partialTicks)
        val shouLunX = instance.getBone("move_shoulunx")
        val shouLunY = instance.getBone("move_shouluny")

        if (shouLunX != null && shouLunY != null) {
            shouLunX.rotation.rotationX(-turretXRot * 3)
            shouLunY.rotation.rotationZ(turretYRot * 6)
        }

        getOrComputeBoneGroups(instance).shell.forEachIndexed { index, bone ->
            val items = vehicle.entityData.get(Type63Entity.LOADED_AMMO)
            bone.visible = items[index] != -1
        }
    }
}
