package com.atsuishio.superbwarfare.mobeffect

import com.atsuishio.superbwarfare.event.custom.LivingHealCallback
import com.atsuishio.superbwarfare.event.custom.LivingHurtCallback
import com.atsuishio.superbwarfare.init.ModMobEffects
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.LivingEntity

open class TraumaMobEffect : MobEffect(MobEffectCategory.HARMFUL, 0xF4ADB4) {

    companion object {
        @JvmStatic
        fun registerEvents() {
            LivingHealCallback.EVENT.register { event ->
                val effect = event.entity.getEffect(ModMobEffects.TRAUMA) ?: return@register
                if (effect.amplifier + 1 >= 10) {
                    event.isCanceled = true
                } else {
                    event.amount = modifyHeal(event.entity, event.amount)
                }
            }
            LivingHurtCallback.EVENT.register { event ->
                event.amount = modifyIncomingDamage(event.entity, event.amount)
            }
        }

        @JvmStatic
        fun modifyHeal(entity: LivingEntity, amount: Float): Float {
            val effect = entity.getEffect(ModMobEffects.TRAUMA) ?: return amount
            val amp = effect.amplifier + 1
            if (amp >= 10) return 0f
            return amount * (1 - amp * 0.1f)
        }

        @JvmStatic
        fun modifyIncomingDamage(entity: LivingEntity, amount: Float): Float {
            val effect = entity.getEffect(ModMobEffects.TRAUMA) ?: return amount
            val amp = effect.amplifier + 1
            return amount * (1 + amp * 0.15f)
        }
    }
}
