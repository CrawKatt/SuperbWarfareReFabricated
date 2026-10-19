package com.atsuishio.superbwarfare.entity

import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.boss.EnderDragonPart
import net.minecraft.world.entity.boss.enderdragon.EnderDragon
import net.minecraft.world.level.entity.EntityTypeTest
import net.minecraft.world.phys.AABB
import java.util.function.Predicate
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
        while (root is EnderDragonPart) root = root.parentMob
        return root
    }

fun ServerLevel.getEntitiesWithParts(
    except: Entity?,
    bounds: AABB,
    predicate: Predicate<in Entity>
): List<Entity> {
    val entities = getEntities(except, bounds, predicate)
    // Vanilla discovers parts through the parent's bounds; NeoForge queries each part independently.
    entities.removeAll { it is EnderDragonPart }
    for (dragon in getEntities(EntityTypeTest.forClass(EnderDragon::class.java)) { true }) {
        for (part in dragon.subEntities) {
            if (part !== except && part.boundingBox.intersects(bounds) && predicate.test(part)) {
                entities.add(part)
            }
        }
    }
    return entities
}
