package com.atsuishio.superbwarfare.api.event

import com.atsuishio.superbwarfare.data.gun.GunData
import com.atsuishio.superbwarfare.event.ShieldRuntime
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.ItemStack
import net.minecraft.world.phys.Vec3
import org.jetbrains.annotations.ApiStatus

/**
 * 枪盾即将尝试抵挡一次命中时触发
 */
@ApiStatus.AvailableSince("0.8.10")
open class ShieldHitEvent private constructor(
    val shooter: LivingEntity,
    val gunStack: ItemStack,
    val gunData: GunData,
    val shields: List<ShieldRuntime.Instance>,
    var damage: Float,
) {
    var isCanceled: Boolean = false

    fun interface DeflectCallback {
        fun post(event: Deflect)
    }

    class Deflect(
        shooter: LivingEntity,
        gunStack: ItemStack,
        gunData: GunData,
        shields: List<ShieldRuntime.Instance>,
        damage: Float,
        val projectile: Projectile?,
        val victim: Entity,
        val hitVec: Vec3,
        val travel: Vec3,
    ) : ShieldHitEvent(shooter, gunStack, gunData, shields, damage)

    companion object {
        @JvmField
        val DEFLECT: Event<DeflectCallback> = EventFactory.createArrayBacked(DeflectCallback::class.java) { callbacks ->
            DeflectCallback { event ->
                callbacks.forEach {
                    it.post(event)
                    if (event.isCanceled) return@DeflectCallback
                }
            }
        }
    }
}
