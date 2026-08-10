package com.atsuishio.superbwarfare.tools

import com.atsuishio.superbwarfare.config.client.DisplayConfig
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import com.mojang.blaze3d.vertex.PoseStack
import net.createmod.ponder.api.level.PonderLevel
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
object RenderDistanceHelper {
    private var GUI_RENDER_TIMESTAMP: Long = -1

    @JvmStatic
    fun markGuiRenderTimestamp() {
        GUI_RENDER_TIMESTAMP = System.currentTimeMillis()
    }

    @JvmStatic
    fun isInGui(): Boolean {
        return System.currentTimeMillis() - GUI_RENDER_TIMESTAMP < 100L
    }

    @JvmStatic
    fun shouldRenderLOD(entity: VehicleEntity, poseStack: PoseStack, distance: Double): Boolean {
        if (isInGui()) return false
        if (entity.level() is PonderLevel) return false
        val globalLODDistance = try {
            DisplayConfig.VEHICLE_LOD_DISTANCE.get()
        } catch (_: Exception) {
            -1
        }
        if (globalLODDistance < 0) return false
        if (distance < globalLODDistance) return false
        val matrix = poseStack.last().pose()
        val viewDistance = matrix.m30() * matrix.m30() + matrix.m31() * matrix.m31() + matrix.m32() * matrix.m32()
        return viewDistance >= distance * distance
    }
}