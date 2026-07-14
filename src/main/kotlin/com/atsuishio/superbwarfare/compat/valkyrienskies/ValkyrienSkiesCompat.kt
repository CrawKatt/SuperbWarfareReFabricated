package com.atsuishio.superbwarfare.compat.valkyrienskies

import com.atsuishio.superbwarfare.compat.CompatHolder
import com.atsuishio.superbwarfare.entity.projectile.IAdvancedHitDetection
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import net.minecraftforge.fml.ModList
import org.joml.Matrix4dc
import org.joml.Vector3d
import org.joml.primitives.AABBd
import org.joml.primitives.AABBdc
import org.valkyrienskies.core.api.ships.Ship
import org.valkyrienskies.mod.api.getShipsIntersecting
import org.valkyrienskies.mod.api.toJOML
import org.valkyrienskies.mod.api.toMinecraft
import org.valkyrienskies.mod.common.getLevelFromDimensionId
import org.valkyrienskies.mod.common.util.EntityShipCollisionUtils
import java.util.function.Predicate
import kotlin.math.max
import kotlin.math.min

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

    /**
     * 对瓦尔基里天空模组的船体方块进行射线检测。
     * 将射线从世界坐标转换到船舶空间，在船舶维度中执行与原版世界相同的方块射线追踪（DDA 遍历），
     * 然后将命中结果转换回世界坐标。
     *
     * @param level 当前世界（必须是 ServerLevel）
     * @param startVec 射线起点（世界坐标）
     * @param endVec 射线终点（世界坐标）
     * @param ignorePredicate 忽略特定方块的谓词（在船舶空间对方块状态进行测试）
     * @return 命中位置和方块坐标（世界空间），如果未命中则返回 null
     */
    @JvmStatic
    fun rayTraceShipBlocks(
        level: Level,
        startVec: Vec3,
        endVec: Vec3,
        ignorePredicate: Predicate<BlockState>
    ): Pair<Vec3, BlockPos>? {
        if (!hasMod() || level !is ServerLevel) return null

        return try {
            // 构建射线包围盒，用于查询相交的船舶
            val minVec = Vec3(
                min(startVec.x, endVec.x) - 1.0,
                min(startVec.y, endVec.y) - 1.0,
                min(startVec.z, endVec.z) - 1.0
            ).toJOML()
            val maxVec = Vec3(
                max(startVec.x, endVec.x) + 1.0,
                max(startVec.y, endVec.y) + 1.0,
                max(startVec.z, endVec.z) + 1.0
            ).toJOML()
            val queryAabb = AABBd(minVec, maxVec).correctBounds()
            val ships = level.getShipsIntersecting(queryAabb)

            val server = level.server
            var closestDistSqr = Double.MAX_VALUE
            var closestHit: Pair<Vec3, BlockPos>? = null

            for (ship in ships) {
                val shipHit = rayTraceSingleShip(ship, server, startVec, endVec, ignorePredicate)
                    ?: continue
                val (hitPos, _) = shipHit
                val distSqr = startVec.distanceToSqr(hitPos)
                if (distSqr < closestDistSqr) {
                    closestDistSqr = distSqr
                    closestHit = shipHit
                }
            }

            closestHit
        } catch (_: Exception) {
            null
        }
    }

    /**
     * 对单个船舶进行射线检测。
     * 将射线转换到船舶空间，在船舶维度中使用与原版世界相同的 DDA 方块遍历进行射线追踪，
     * 然后将命中转换回世界空间。
     *
     * 使用 [com.atsuishio.superbwarfare.entity.projectile.IAdvancedHitDetection.Companion.performRayTrace]
     * 在船舶维度上进行射线追踪，以避免 VS 对 Level.clip() 的 mixin 拦截产生递归。
     */
    private fun rayTraceSingleShip(
        ship: Ship,
        server: net.minecraft.server.MinecraftServer,
        startVec: Vec3,
        endVec: Vec3,
        ignorePredicate: Predicate<BlockState>
    ): Pair<Vec3, BlockPos>? {
        return try {
            val worldToShip = ship.worldToShip
            val shipToWorld = ship.shipToWorld

            // 将射线转换到船舶空间
            val shipStart = startVec.toJOML().also { worldToShip.transformPosition(it) }.toMinecraft()
            val shipEnd = endVec.toJOML().also { worldToShip.transformPosition(it) }.toMinecraft()

            // 获取船舶所在维度的 ServerLevel
            val shipLevel = server.getLevelFromDimensionId(ship.chunkClaimDimension) ?: return null

            // 在船舶维度中进行方块射线追踪（使用与原版世界相同的 DDA 遍历逻辑）
            val shipContext = ClipContext(
                shipStart, shipEnd,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                null
            )
            val shipResult = IAdvancedHitDetection.performRayTrace(
                shipContext,
                { rayTraceContext, blockPos ->
                    val blockState: BlockState = shipLevel.getBlockState(blockPos)
                    if (ignorePredicate.test(blockState)) return@performRayTrace null
                    val fluidState = shipLevel.getFluidState(blockPos)
                    val blockShape = rayTraceContext.getBlockShape(blockState, shipLevel, blockPos)
                    val blockResult = shipLevel.clipWithInteractionOverride(
                        rayTraceContext.from, rayTraceContext.to, blockPos, blockShape, blockState
                    )
                    val fluidShape = rayTraceContext.getFluidShape(fluidState, shipLevel, blockPos)
                    val fluidResult = fluidShape.clip(rayTraceContext.from, rayTraceContext.to, blockPos)
                    val blockDistance = blockResult?.let { rayTraceContext.from.distanceToSqr(it.location) }
                        ?: Double.MAX_VALUE
                    val fluidDistance = fluidResult?.let { rayTraceContext.from.distanceToSqr(it.location) }
                        ?: Double.MAX_VALUE
                    if (blockDistance <= fluidDistance) blockResult else fluidResult
                },
                { rayTraceContext ->
                    val vec3 = rayTraceContext.from.subtract(rayTraceContext.to)
                    BlockHitResult.miss(
                        rayTraceContext.to,
                        net.minecraft.core.Direction.getNearest(vec3.x, vec3.y, vec3.z),
                        BlockPos.containing(rayTraceContext.to)
                    )
                }
            )

            if (shipResult.type == HitResult.Type.MISS) return null

            // 将命中位置转换回世界空间
            val worldHitPos = shipToWorld
                .transformPosition(shipResult.location.toJOML())
                .toMinecraft()
            val worldBlockPos = BlockPos.containing(worldHitPos)

            Pair(worldHitPos, worldBlockPos)
        } catch (_: Exception) {
            null
        }
    }
}
