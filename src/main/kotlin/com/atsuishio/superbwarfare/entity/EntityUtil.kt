package com.atsuishio.superbwarfare.entity

import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.world.entity.Entity
import net.neoforged.neoforge.entity.PartEntity
import kotlin.reflect.KProperty

// EntityDataAccessor代理
operator fun <T : Any> EntityDataAccessor<T>.getValue(entity: Any?, prop: KProperty<*>) =
    (entity as Entity).entityData.get(this) as T

operator fun <T : Any> EntityDataAccessor<T>.setValue(entity: Any?, prop: KProperty<*>, value: T) {
    (entity as Entity).entityData.set(this, value)
}

/** Owner of a multipart hitbox, used for eligibility, cooldown resets and damage deduplication. */
val Entity.multipartRoot: Entity
    get() {
        var root = this
        while (root is PartEntity<*>) root = root.parent
        return root
    }
