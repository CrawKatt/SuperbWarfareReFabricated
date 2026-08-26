package com.atsuishio.superbwarfare.client.renderer.entity

import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.util.Mth
import net.minecraft.world.phys.Vec3

class VehicleAssemblingTableVehicleRenderer(manager: EntityRendererProvider.Context) : BasicVehicleRenderer(manager) {
    override fun rotateVehicleAxis(entity: VehicleEntity, poseStack: PoseStack, entityYaw: Float, partialTicks: Float) {
        poseStack.translate(0.5, 0.0, -0.5)

        val root = Vec3(-0.5, 0.5, 0.5)
        poseStack.rotateAround(
            Axis.YP.rotationDegrees(-entityYaw + 180),
            root.x.toFloat(),
            root.y.toFloat(),
            root.z.toFloat()
        )
        poseStack.rotateAround(
            Axis.XP.rotationDegrees(-Mth.lerp(partialTicks, entity.xRotO, entity.xRot)),
            root.x.toFloat(),
            root.y.toFloat(),
            root.z.toFloat()
        )
        poseStack.rotateAround(
            Axis.ZP.rotationDegrees(-Mth.lerp(partialTicks, entity.prevRoll, entity.roll)),
            root.x.toFloat(),
            root.y.toFloat(),
            root.z.toFloat()
        )
    }
}
