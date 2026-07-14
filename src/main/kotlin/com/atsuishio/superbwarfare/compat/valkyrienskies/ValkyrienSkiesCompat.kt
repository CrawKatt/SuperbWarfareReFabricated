package com.atsuishio.superbwarfare.compat.valkyrienskies

import com.atsuishio.superbwarfare.compat.CompatHolder
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import net.minecraftforge.fml.ModList
import org.joml.Matrix4dc
import org.joml.Vector3d
import org.joml.primitives.AABBd
import org.joml.primitives.AABBdc
import org.valkyrienskies.mod.api.getShipsIntersecting
import org.valkyrienskies.mod.api.toJOML
import org.valkyrienskies.mod.api.toMinecraft
import org.valkyrienskies.mod.common.util.EntityShipCollisionUtils
import java.util.function.Predicate

object ValkyrienSkiesCompat {

    @JvmStatic
    fun hasMod(): Boolean {
        return ModList.get().isLoaded(CompatHolder.VALKYRIEN_SKIES)
    }

    @JvmStatic
    fun adjustMovementForShipCollisions(
        entity: Entity,
        movement: Vec3,
        boundingBox: AABB,
        level: Level
    ): Vec3 {
        if (!hasMod()) return movement
        return try {
            EntityShipCollisionUtils.adjustEntityMovementForShipCollisions(
                entity, movement, boundingBox, level
            )
        } catch (_: Exception) {
            movement
        }
    }

    class ShipTransformCache(private val entries: List<Triple<Matrix4dc, AABBdc, Vector3d>>) {
        /** (worldToShip: Matrix4dc, worldAABB: AABBdc, tmpVec: Vector3d) */
        val isEmpty: Boolean get() = entries.isEmpty()

        companion object {
            @JvmStatic
            fun create(level: Level, explosionAABB: AABB): ShipTransformCache {
                if (!hasMod() || level !is ServerLevel) return ShipTransformCache(emptyList())

                return try {
                    val minVec = Vec3(explosionAABB.minX, explosionAABB.minY, explosionAABB.minZ).toJOML()
                    val maxVec = Vec3(explosionAABB.maxX, explosionAABB.maxY, explosionAABB.maxZ).toJOML()
                    val queryAabb = AABBd(minVec, maxVec).correctBounds()

                    val ships = level.getShipsIntersecting(queryAabb)

                    val entries = mutableListOf<Triple<Matrix4dc, AABBdc, Vector3d>>()
                    for (ship in ships) {
                        val wts = ship.worldToShip
                        val aabb = ship.worldAABB
                        entries.add(Triple(wts, aabb, Vector3d()))
                    }
                    ShipTransformCache(entries)
                } catch (_: Exception) {
                    ShipTransformCache(emptyList())
                }
            }
        }

        fun toShipSpace(worldPos: BlockPos): BlockPos? {
            if (isEmpty) return null

            return try {
                val jomlWorld = Vec3.atCenterOf(worldPos).toJOML()
                for ((worldToShip, worldAABB, _) in entries) {
                    val contained = worldAABB.containsPoint(jomlWorld)
                    if (!contained) continue

                    worldToShip.transformPosition(jomlWorld)
                    return BlockPos.containing(jomlWorld.toMinecraft())
                }
                null
            } catch (_: Exception) {
                null
            }
        }
    }

    // TODO 命中判定
    @JvmStatic
    fun rayTraceShipBlocks(
        level: Level,
        startVec: Vec3,
        endVec: Vec3,
        @Suppress("UNUSED_PARAMETER") ignorePredicate: Predicate<BlockState>
    ): Pair<Vec3, BlockPos>? {
        if (!hasMod() || level !is ServerLevel) return null
        return null

//        return try {
//            val minVec = Vec3(
//                min(startVec.x, endVec.x) - 1.0,
//                min(startVec.y, endVec.y) - 1.0,
//                min(startVec.z, endVec.z) - 1.0
//            ).toJOML()
//            val maxVec = Vec3(
//                max(startVec.x, endVec.x) + 1.0,
//                max(startVec.y, endVec.y) + 1.0,
//                max(startVec.z, endVec.z) + 1.0
//            ).toJOML()
//            val queryAabb = AABBd(minVec, maxVec).correctBounds()
//            val ships = level.getShipsIntersecting(queryAabb)
//
//            val dirX = endVec.x - startVec.x
//            val dirY = endVec.y - startVec.y
//            val dirZ = endVec.z - startVec.z
//
//            var closestHitWorld: Vec3? = null
//            var closestBlockWorld: BlockPos? = null
//            var closestT = Double.MAX_VALUE
//
//            for (ship in ships) {
//                val worldAABB = ship.worldAABB
//
//                val t = worldAABB.intersectsRay(
//                    startVec.x, startVec.y, startVec.z,
//                    dirX, dirY, dirZ
//                )
//                if (t !in 0.0..1.0) continue
//
//                if (t < closestT) {
//                    closestT = t
//                    closestHitWorld = Vec3(
//                        startVec.x + dirX * t,
//                        startVec.y + dirY * t,
//                        startVec.z + dirZ * t
//                    )
//                    closestBlockWorld = BlockPos.containing(closestHitWorld)
//                }
//            }
//
//            if (closestHitWorld != null && closestBlockWorld != null)
//                Pair(closestHitWorld, closestBlockWorld)
//            else
//                null
//        } catch (_: Exception) {
//            null
//        }
    }
}
