package com.atsuishio.superbwarfare.entity.projectile

import com.atsuishio.superbwarfare.client.animation.entity.BasicProjectileAnimationInstance
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level

open class Kh39Entity(type: EntityType<out Kh39Entity>, level: Level) : Agm65Entity(type, level),
    BasicGeoProjectileEntity {
    val anim: BasicProjectileAnimationInstance<*>? =
        if (this.level().isClientSide) BasicProjectileAnimationInstance(this) else null

    override fun getAnimationInstance(): BasicProjectileAnimationInstance<*>? {
        return this.anim
    }
}
