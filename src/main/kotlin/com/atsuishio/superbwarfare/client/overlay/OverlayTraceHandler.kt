package com.atsuishio.superbwarfare.client.overlay

import com.atsuishio.superbwarfare.config.server.VehicleConfig
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import com.atsuishio.superbwarfare.tools.TraceTool
import com.atsuishio.superbwarfare.tools.getEntityReach
import com.atsuishio.superbwarfare.tools.localPlayer
import com.atsuishio.superbwarfare.tools.mc
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.ClientTickEvent

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(Dist.CLIENT)
object OverlayTraceHandler {
    @JvmField
    var playerReachEntity: Entity? = null

    @JvmField
    var maxRangeEntity: Entity? = null

    @JvmField
    var cameraEntity: Entity? = null

    @SubscribeEvent
    fun onOverlayTraceClientTick(event: ClientTickEvent.Post) {
        val player = localPlayer
        if (player == null) {
            clear()
            return
        }

        handlePlayerTrace(player)
        handleCameraTrace(player)
    }

    @JvmStatic
    fun handlePlayerTrace(player: Player) {
        val reachDistance = player.getEntityReach()
        val reachEntity = TraceTool.findLookingEntity(player, reachDistance)
        playerReachEntity = reachEntity
        if (reachEntity != null) {
            maxRangeEntity = reachEntity
            return
        }

        val maxEntity = TraceTool.findLookingEntity(player, reachDistance)
        maxRangeEntity = maxEntity
    }

    @JvmStatic
    fun handleCameraTrace(player: Player) {
        val camera = mc.gameRenderer.mainCamera
        var viewPos = camera.position
        var viewVec = Vec3(camera.lookVector)
        val distance = try {
            VehicleConfig.VEHICLE_INFO_DISPLAY_DISTANCE.get().toDouble()
        } catch (_: Exception) {
            196.0
        }

        val vehicle = player.vehicle
        if (vehicle is VehicleEntity && vehicle.hasWeapon(vehicle.getSeatIndex(player))) {
            viewVec = vehicle.getShootDirectionForHud(player, 1f)
            viewPos = vehicle.getShootPosForHud(player, 1f)
        }

        val cameraRes = TraceTool.cameraFindLookingEntity(player, viewPos, viewVec, distance)
        if (cameraRes is VehicleEntity) {
            val decoy = TraceTool.findLookDecoy(player, viewPos, viewVec, distance)
            if (decoy != null) {
                cameraEntity = null
                return
            }
        }
        cameraEntity = cameraRes
    }

    @JvmStatic
    fun clear() {
        playerReachEntity = null
        maxRangeEntity = null
        cameraEntity = null
    }
}