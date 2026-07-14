package com.atsuishio.superbwarfare.compat.valkyrienskies

import com.atsuishio.superbwarfare.compat.CompatHolder
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import net.minecraftforge.fml.ModList
import org.valkyrienskies.mod.common.util.EntityShipCollisionUtils
import java.lang.reflect.Method

object ValkyrienSkiesCompat {

    @JvmStatic
    fun hasMod(): Boolean {
        return ModList.get().isLoaded(CompatHolder.VALKYRIEN_SKIES)
    }

    /**
     * Adjust entity movement for Valkyrien Skies ship collisions.
     */
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
        } catch (e: Exception) {
            movement
        }
    }

    // =========================================================================
    // Ship block helpers — reflection-based because VS core API is Jar-in-Jar
    // =========================================================================

    /** Reflective handles, populated lazily. */
    @Suppress("PrivatePropertyName")
    private object VS {
        val SHIP: Class<*>? by lazy { cls("org.valkyrienskies.core.api.ships.Ship") }
        val MATRIX4DC: Class<*>? by lazy { cls("org.joml.Matrix4dc") }
        val VECTOR3D: Class<*>? by lazy { cls("org.joml.Vector3d") }
        val VECTOR3DC: Class<*>? by lazy { cls("org.joml.Vector3dc") }
        val AABBDC: Class<*>? by lazy { cls("org.joml.primitives.AABBdc") }
        val AABBD: Class<*>? by lazy { cls("org.joml.primitives.AABBd") }

        // VSGameUtilsKt.getShipsIntersecting(Level, AABBdc)
        val getShipsIntersecting: Method? by lazy {
            cls("org.valkyrienskies.mod.common.VSGameUtilsKt")
                ?.getMethod("getShipsIntersecting", Level::class.java, AABBDC)
        }
        // Ship.getWorldToShip()
        val shipWorldToShip: Method? by lazy { SHIP?.getMethod("getWorldToShip") }
        // Ship.getWorldAABB()
        val shipWorldAABB: Method? by lazy { SHIP?.getMethod("getWorldAABB") }
        // Matrix4dc.transformPosition(Vector3d)
        val matrixTransform: Method? by lazy { MATRIX4DC?.getMethod("transformPosition", VECTOR3D) }
        // VectorConversionsMCKt.toJOML(Vec3) → Vector3d
        val toJOML: Method? by lazy {
            cls("org.valkyrienskies.mod.common.util.VectorConversionsMCKt")
                ?.getMethod("toJOML", Vec3::class.java)
        }
        // VectorConversionsMCKt.toMinecraft(Vector3dc) → Vec3
        val toMinecraft: Method? by lazy {
            cls("org.valkyrienskies.mod.common.util.VectorConversionsMCKt")
                ?.getMethod("toMinecraft", VECTOR3DC)
        }
        // AABBdc.containsPoint(Vector3dc) → boolean
        val aabbContainsPoint: Method? by lazy { AABBDC?.getMethod("containsPoint", VECTOR3DC) }
        // Vector3d.set(x, y, z) → Vector3d (constructor replacement)
        val vector3dSet: Method? by lazy {
            VECTOR3D?.getMethod("set", Double::class.javaPrimitiveType,
                Double::class.javaPrimitiveType, Double::class.javaPrimitiveType)
        }

        // --- JOML AABBd construction helpers ---
        // AABBd(Vector3dc min, Vector3dc max)
        val aabbdCtor: java.lang.reflect.Constructor<*>? by lazy { AABBD?.getConstructor(VECTOR3DC, VECTOR3DC) }
        // AABBd.correctBounds()
        val aabbdCorrect: Method? by lazy { AABBD?.getMethod("correctBounds") }

        val ready: Boolean by lazy {
            getShipsIntersecting != null && shipWorldToShip != null && shipWorldAABB != null &&
                matrixTransform != null && toJOML != null && toMinecraft != null &&
                aabbContainsPoint != null && vector3dSet != null && aabbdCtor != null &&
                aabbdCorrect != null && VECTOR3D != null && AABBD != null
        }

        private fun cls(name: String): Class<*>? = try { Class.forName(name) } catch (_: Exception) { null }
    }

    // =========================================================================
    // Ship-transform cache — pre-computed once per explosion
    // =========================================================================

    /**
     * Pre-computed ship data for a single explosion.
     * Each entry is a triple: (worldToShip: Matrix4dc, worldAABB: AABBdc, shipVec: Vector3d — reusable instance).
     */
    class ShipTransformCache(private val entries: List<Triple<Any, Any, Any>>) {
        val isEmpty: Boolean get() = entries.isEmpty()

        companion object {
            /** Create a cache for all ships whose physics AABB intersects [explosionAABB]. */
            @JvmStatic
            fun create(level: Level, explosionAABB: AABB): ShipTransformCache {
                if (!hasMod() || level !is ServerLevel || !VS.ready) return ShipTransformCache(emptyList())

                return try {
                    // Build an AABBd from the explosion AABB
                    val minVec = VS.toJOML!!.invoke(null, Vec3(explosionAABB.minX, explosionAABB.minY, explosionAABB.minZ))
                    val maxVec = VS.toJOML!!.invoke(null, Vec3(explosionAABB.maxX, explosionAABB.maxY, explosionAABB.maxZ))
                    val queryAabb = VS.aabbdCtor!!.newInstance(minVec, maxVec)
                    VS.aabbdCorrect!!.invoke(queryAabb)

                    @Suppress("UNCHECKED_CAST")
                    val ships = VS.getShipsIntersecting!!.invoke(null, level, queryAabb) as? Iterable<*> ?: return ShipTransformCache(emptyList())

                    val entries = mutableListOf<Triple<Any, Any, Any>>()
                    for (ship in ships) {
                        if (ship == null) continue
                        val wts = VS.shipWorldToShip!!.invoke(ship) ?: continue
                        val aabb = VS.shipWorldAABB!!.invoke(ship) ?: continue
                        val tmpVec = VS.VECTOR3D!!.getDeclaredConstructor().newInstance()
                        entries.add(Triple(wts, aabb, tmpVec))
                    }
                    ShipTransformCache(entries)
                } catch (_: Exception) {
                    ShipTransformCache(emptyList())
                }
            }
        }

        /**
         * If [worldPos] is inside any cached ship's physics AABB, returns the
         * ship-space BlockPos where blocks are stored.
         */
        fun toShipSpace(worldPos: BlockPos): BlockPos? {
            if (isEmpty) return null

            return try {
                val jomlWorld = VS.toJOML!!.invoke(null, Vec3.atCenterOf(worldPos)) ?: return null
                for ((worldToShip, worldAABB, tmpVec) in entries) {
                    // Check AABB containment
                    val contained = VS.aabbContainsPoint!!.invoke(worldAABB, jomlWorld) as? Boolean ?: continue
                    if (!contained) continue

                    // Transform to ship space
                    VS.matrixTransform!!.invoke(worldToShip, jomlWorld)
                    val mcVec = VS.toMinecraft!!.invoke(null, jomlWorld) as? Vec3 ?: continue
                    return BlockPos.containing(mcVec)
                }
                null
            } catch (_: Exception) {
                null
            }
        }
    }
}
