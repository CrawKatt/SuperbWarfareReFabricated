package com.atsuishio.superbwarfare.world.phys

import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3

class EntityResult(
    @JvmField val entity: Entity,
    @JvmField val hitVec: Vec3,
    @JvmField val headshot: Boolean,
    @JvmField val legShot: Boolean,
) {
    fun getHitPos(): Vec3 = hitVec

    fun isHeadshot(): Boolean = headshot

    fun isLegShot(): Boolean = legShot
}
