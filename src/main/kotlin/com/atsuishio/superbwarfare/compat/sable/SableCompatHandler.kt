package com.atsuishio.superbwarfare.compat.sable

import com.atsuishio.superbwarfare.compat.CompatHolder
import dev.ryanhcode.sable.companion.SableCompanion
import dev.ryanhcode.sable.companion.math.BoundingBox3d
import dev.ryanhcode.sable.sublevel.SubLevel
import net.minecraft.core.BlockPos
import net.minecraft.core.Position
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.neoforged.fml.ModList
import org.joml.Vector3d

object SableCompatHandler {

    @JvmStatic
    fun hasMod(): Boolean {
        return ModList.get().isLoaded(CompatHolder.SABLE)
    }

    @JvmStatic
    fun collectSubLevelBlockCollisions(level: Level, worldSearchBox: AABB, targetList: MutableList<AABB>) {
        try {
            val searchBounds = BoundingBox3d(
                worldSearchBox.minX, worldSearchBox.minY, worldSearchBox.minZ,
                worldSearchBox.maxX, worldSearchBox.maxY, worldSearchBox.maxZ
            )
            val subLevels = SableCompanion.INSTANCE.getAllIntersecting(level, searchBounds)
            for (subLevelAccess in subLevels) {
                val subLevel = subLevelAccess as? SubLevel ?: continue
                val subLevelWorld = subLevel.level ?: continue
                val pose = subLevelAccess.logicalPose()

                val localCenter = JOMLConversion.toJOML(worldSearchBox.center).let {
                    pose.transformPositionInverse(it)
                }
                val hw = worldSearchBox.xsize / 2.0
                val hh = worldSearchBox.ysize / 2.0
                val hd = worldSearchBox.zsize / 2.0
                val localSearchBox = AABB(
                    localCenter.x - hw, localCenter.y - hh, localCenter.z - hd,
                    localCenter.x + hw, localCenter.y + hh, localCenter.z + hd
                )

                for (shape in subLevelWorld.getBlockCollisions(null, localSearchBox)) {
                    for (aabb in shape.toAabbs()) {
                        targetList.add(transformAabbToWorld(pose, aabb))
                    }
                }
            }
        } catch (_: Exception) {
        }
    }

    @JvmStatic
    fun isOnSubLevelGround(level: Level, testObbAabb: AABB): Boolean {
        return try {
            val searchBounds = BoundingBox3d(
                testObbAabb.minX, testObbAabb.minY, testObbAabb.minZ,
                testObbAabb.maxX, testObbAabb.maxY, testObbAabb.maxZ
            )
            val subLevels = SableCompanion.INSTANCE.getAllIntersecting(level, searchBounds)
            for (subLevelAccess in subLevels) {
                val subLevel = subLevelAccess as? SubLevel ?: continue
                val subLevelWorld = subLevel.level ?: continue
                val pose = subLevelAccess.logicalPose()

                val localCenter = JOMLConversion.toJOML(testObbAabb.center).let {
                    pose.transformPositionInverse(it)
                }
                val localBox = AABB(
                    localCenter.x - testObbAabb.xsize / 2.0,
                    localCenter.y - testObbAabb.ysize / 2.0,
                    localCenter.z - testObbAabb.zsize / 2.0,
                    localCenter.x + testObbAabb.xsize / 2.0,
                    localCenter.y + testObbAabb.ysize / 2.0,
                    localCenter.z + testObbAabb.zsize / 2.0
                )

                for (pos in BlockPos.betweenClosedStream(
                    BlockPos.containing(localBox.minX, localBox.minY, localBox.minZ),
                    BlockPos.containing(localBox.maxX, localBox.maxY, localBox.maxZ)
                )) {
                    val state = subLevelWorld.getBlockState(pos)
                    if (!state.isAir) {
                        val shape = state.getCollisionShape(subLevelWorld, pos)
                        if (!shape.isEmpty) return true
                    }
                }
            }
            false
        } catch (_: Exception) {
            false
        }
    }

    private fun transformAabbToWorld(pose: dev.ryanhcode.sable.companion.math.Pose3dc, local: AABB): AABB {
        val corners = arrayOf(
            Vector3d(local.minX, local.minY, local.minZ),
            Vector3d(local.minX, local.minY, local.maxZ),
            Vector3d(local.minX, local.maxY, local.minZ),
            Vector3d(local.minX, local.maxY, local.maxZ),
            Vector3d(local.maxX, local.minY, local.minZ),
            Vector3d(local.maxX, local.minY, local.maxZ),
            Vector3d(local.maxX, local.maxY, local.minZ),
            Vector3d(local.maxX, local.maxY, local.maxZ),
        )
        var wxMin = Double.MAX_VALUE; var wyMin = Double.MAX_VALUE; var wzMin = Double.MAX_VALUE
        var wxMax = -Double.MAX_VALUE; var wyMax = -Double.MAX_VALUE; var wzMax = -Double.MAX_VALUE
        for (c in corners) {
            val w = pose.transformPosition(c)
            if (w.x < wxMin) wxMin = w.x
            if (w.y < wyMin) wyMin = w.y
            if (w.z < wzMin) wzMin = w.z
            if (w.x > wxMax) wxMax = w.x
            if (w.y > wyMax) wyMax = w.y
            if (w.z > wzMax) wzMax = w.z
        }
        return AABB(wxMin, wyMin, wzMin, wxMax, wyMax, wzMax)
    }

    private object JOMLConversion {
        fun toJOML(pos: Position): Vector3d {
            return Vector3d(pos.x(), pos.y(), pos.z())
        }
    }
}
