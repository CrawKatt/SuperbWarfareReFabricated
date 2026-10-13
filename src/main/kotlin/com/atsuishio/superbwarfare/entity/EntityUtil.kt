package com.atsuishio.superbwarfare.entity

import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.world.entity.Entity
import kotlin.reflect.KProperty

// EntityDataAccessor代理
operator fun <T : Any> EntityDataAccessor<T>.getValue(entity: Any?, prop: KProperty<*>) =
    (entity as Entity).entityData.get(this) as T

operator fun <T : Any> EntityDataAccessor<T>.setValue(entity: Any?, prop: KProperty<*>, value: T) {
    (entity as Entity).entityData.set(this, value)
}
