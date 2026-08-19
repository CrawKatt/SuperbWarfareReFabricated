package com.atsuishio.superbwarfare.entity.mixin

import net.minecraft.world.level.Explosion

@Suppress("FunctionName")
interface ExplosionAccess {
    fun `superbwarfare$getRadius`(): Float

    fun `superbwarfare$getX`(): Double

    fun `superbwarfare$getY`(): Double

    fun `superbwarfare$getZ`(): Double

    companion object {
        fun of(explosion: Explosion): ExplosionAccess {
            return explosion as ExplosionAccess
        }
    }
}
