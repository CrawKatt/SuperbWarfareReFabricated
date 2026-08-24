package com.atsuishio.superbwarfare.api.event

import com.atsuishio.superbwarfare.world.phys.EntityResult
import com.atsuishio.superbwarfare.world.phys.ExtendedEntityRayTraceResult
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3
import org.jetbrains.annotations.ApiStatus

/**
 * 子弹等投射物在命中实体或方块时触发的事件
 */
@ApiStatus.AvailableSince("0.8.7")
open class ProjectileHitEvent private constructor(val owner: Entity?, val projectile: Projectile, val hitVec: Vec3?) {
    var isCanceled: Boolean = false

    fun interface HitEntityCallback {
        fun post(event: HitEntity)
    }

    fun interface HitBlockCallback {
        fun post(event: HitBlock)
    }

    class HitEntity(owner: Entity?, projectile: Projectile, val result: ExtendedEntityRayTraceResult) :
        ProjectileHitEvent(owner, projectile, result.location) {
        val target: Entity = result.entity

        @get:JvmName("isHeadshot")
        val isHeadshot = result.headshot

        @get:JvmName("isLegShot")
        val isLegShot = result.legShot

        constructor(owner: Entity?, projectile: Projectile, target: Entity, hitVec: Vec3) : this(
            owner,
            projectile,
            ExtendedEntityRayTraceResult(EntityResult(target, hitVec, headshot = false, legShot = false))
        )
    }

    class HitBlock(
        val pos: BlockPos?,
        val state: BlockState?,
        val face: Direction?,
        owner: Entity?,
        projectile: Projectile,
        hitVec: Vec3?
    ) : ProjectileHitEvent(owner, projectile, hitVec)

    companion object {
        @JvmField
        val HIT_ENTITY: Event<HitEntityCallback> = EventFactory.createArrayBacked(HitEntityCallback::class.java) { callbacks ->
            HitEntityCallback { event ->
                callbacks.forEach {
                    it.post(event)
                    if (event.isCanceled) return@HitEntityCallback
                }
            }
        }

        @JvmField
        val HIT_BLOCK: Event<HitBlockCallback> = EventFactory.createArrayBacked(HitBlockCallback::class.java) { callbacks ->
            HitBlockCallback { event ->
                callbacks.forEach {
                    it.post(event)
                    if (event.isCanceled) return@HitBlockCallback
                }
            }
        }
    }
}
