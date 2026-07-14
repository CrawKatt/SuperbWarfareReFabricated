package com.atsuishio.superbwarfare.compat.valkyrienskies

import com.atsuishio.superbwarfare.compat.CompatHolder
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import net.minecraftforge.fml.ModList
import org.valkyrienskies.mod.common.util.EntityShipCollisionUtils

object ValkyrienSkiesCompat {

    @JvmStatic
    fun hasMod(): Boolean {
        return ModList.get().isLoaded(CompatHolder.VALKYRIEN_SKIES)
    }

    /**
     * Adjust entity movement for Valkyrien Skies ship collisions.
     * Mirrors what VS's MixinEntity does — applies ship collision before
     * world collision resolution.
     *
     * @return adjusted movement, or the original movement if VS is not loaded
     *         or an error occurs
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
}
