package com.atsuishio.superbwarfare.event

import com.atsuishio.superbwarfare.block.TripodBlock
import com.atsuishio.superbwarfare.capability.player.PlayerVariable
import com.atsuishio.superbwarfare.data.gun.GunData
import com.atsuishio.superbwarfare.item.gun.GunItem
import com.atsuishio.superbwarfare.tools.getBlockReach
import net.minecraft.core.BlockPos
import net.minecraft.util.Mth
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.ClipContext
import net.minecraft.world.phys.Vec3

object DeployedWeaponHandler {

    const val PIVOT_HEIGHT = 0.65625

    const val STAND_BEHIND = 1.0

    const val MIN_STAND_BEHIND = 0.5

    const val PITCH_LIMIT_UP = 45f

    const val PITCH_LIMIT_DOWN = 30f

    const val MAX_DRIFT = 1.0

    @JvmStatic
    fun clampPitch(xRot: Float): Float = Mth.clamp(xRot, -PITCH_LIMIT_UP, PITCH_LIMIT_DOWN)

    @JvmStatic
    fun orbitRadius(xRot: Float): Double =
        (STAND_BEHIND * Mth.cos(xRot * Mth.DEG_TO_RAD)).coerceAtLeast(MIN_STAND_BEHIND)

    @JvmStatic
    fun isDeployed(player: Player): Boolean = pivotOf(player) != null

    @JvmStatic
    fun pivotOf(player: Player): Vec3? = Vec3.ZERO //PlayerVariable.getOrDefault(player).deployPivot

    @JvmStatic
    fun deployTarget(player: Player): BlockPos? {
        val stack = player.mainHandItem
        val item = stack.item as? GunItem ?: return null
        if (!item.deployable(GunData.from(stack))) return null
        if (player.isSpectator || player.isPassenger || !player.isAlive) return null

        return lookingTripod(player)
    }

    @JvmStatic
    fun deploy(player: Player): Boolean {
        if (player.level().isClientSide) return false

        val tripod = deployTarget(player) ?: return false
        val data = GunData.from(player.mainHandItem)

        val pivot = Vec3(tripod.x + 0.5, tripod.y + PIVOT_HEIGHT, tripod.z + 0.5)

        val forward = horizontalForward(player)
        val radius = orbitRadius(player.xRot)
        val behindX = pivot.x - forward.x * radius
        val behindZ = pivot.z - forward.z * radius
        val behindY = pivot.y - PIVOT_HEIGHT


        player.teleportTo(behindX, behindY, behindZ)
        player.deltaMovement = Vec3.ZERO

//        val uuid = data.uuid?.toString()
//        PlayerVariable.modify(player) {
//            it.deployPivot = pivot
//            it.deployGunUuid = uuid
//        }
        return true
    }

    @JvmStatic
    fun lookingTripod(player: Player): BlockPos? {
        val level = player.level()
        val start = player.eyePosition
        val end = start.add(player.getViewVector(1f).scale(player.getBlockReach()))
        val hit = level.clip(ClipContext(start, end, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, player))

        return hit.blockPos.takeIf { level.getBlockState(it).block is TripodBlock }
    }

    @JvmStatic
    fun undeploy(player: Player) {
        if (player.level().isClientSide) return

//        PlayerVariable.modify(player) {
//            it.deployPivot = null
//            it.deployGunUuid = null
//        }
    }

    @JvmStatic
    fun applyOrbit(player: Player) {
        val pivot = pivotOf(player) ?: return

        val forward = horizontalForward(player)
        val radius = orbitRadius(player.xRot)
        val x = pivot.x - forward.x * radius
        val z = pivot.z - forward.z * radius
        val y = player.y

        player.setPos(x, y, z)

        player.xo = x
        player.yo = y
        player.zo = z
        player.xOld = x
        player.yOld = y
        player.zOld = z
    }

    fun serverTick(player: Player) {
        if (player.level().isClientSide) return

        val cap = PlayerVariable.getOrDefault(player)
//        val pivot = cap.deployPivot ?:
        return

//        val stack = player.mainHandItem
//        val item = stack.item as? GunItem
//        val data = if (item == null) null else GunData.from(stack)
//        val stillHolding = item != null && data != null && item.deployable(data) &&
//                cap.deployGunUuid != null && cap.deployGunUuid == data.uuid?.toString()
//
//        if (!stillHolding || !player.isAlive || player.isPassenger || player.isSpectator) {
//            undeploy(player)
//            return
//        }
//
//        val clamped = clampPitch(player.xRot)
//        if (clamped != player.xRot) {
//            player.xRot = clamped
//            player.xRotO = clamped
//        }
//
//        val dx = player.x - pivot.x
//        val dz = player.z - pivot.z
//        val radius = sqrt(dx * dx + dz * dz)
//        val dy = player.y - (pivot.y - PIVOT_HEIGHT)
//
//        if (abs(radius - orbitRadius(clamped)) > MAX_DRIFT ||
//            abs(dy) > MAX_DRIFT ||
//            player.level().getBlockState(player.blockPosition()).canOcclude()
//        ) {
//            undeploy(player)
//            return
//        }
//
//        player.deltaMovement = Vec3.ZERO
//        player.fallDistance = 0f
    }

    fun clientTick(player: Player) {
        if (!isDeployed(player)) return

        val clamped = clampPitch(player.xRot)
        if (clamped != player.xRot) {
            player.xRot = clamped
            player.xRotO = clamped
        }

        applyOrbit(player)
        player.deltaMovement = Vec3.ZERO

        if (player.isSprinting) {
            player.setSprinting(false)
        }
    }

    @JvmStatic
    fun horizontalForward(player: Player): Vec3 =
        Vec3.directionFromRotation(0f, player.yRot)
}
