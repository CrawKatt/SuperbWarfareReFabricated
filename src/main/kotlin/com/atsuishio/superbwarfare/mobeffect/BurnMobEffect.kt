package com.atsuishio.superbwarfare.mobeffect

import com.atsuishio.superbwarfare.init.ModDamageTypes
import com.atsuishio.superbwarfare.init.ModMobEffects
import com.atsuishio.superbwarfare.init.ModSounds
import com.atsuishio.superbwarfare.network.message.receive.ClientIndicatorMessage
import com.atsuishio.superbwarfare.tools.forceHurt
import com.atsuishio.superbwarfare.tools.persistentData
import com.atsuishio.superbwarfare.tools.sendPacket
import net.minecraft.core.registries.Registries
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundSource
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.AreaEffectCloud
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

object BurnMobEffect : MobEffect(MobEffectCategory.HARMFUL, -12708330) {
    const val TAG_ATTACKER = "BurnAttacker"

    override fun applyEffectTick(entity: LivingEntity, amplifier: Int) {
        val attacker = if (!entity.persistentData.contains(TAG_ATTACKER)) {
            null
        } else {
            entity.level().getEntity(entity.persistentData.getInt(TAG_ATTACKER))
        }

        entity.forceHurt(
            ModDamageTypes.causeBurnDamage(entity.level().registryAccess(), attacker),
            0.6f + (0.3f * amplifier)
        )
        entity.invulnerableTime = 0

        val level = attacker?.level() ?: return
        val player = attacker as? ServerPlayer ?: return
        if (level is ServerLevel) {
            level.playSound(null, player.blockPosition(), ModSounds.INDICATION, SoundSource.VOICE, 1f, 1f)
            player.sendPacket(ClientIndicatorMessage(0, 5))
        }
    }

    override fun isDurationEffectTick(pDuration: Int, pAmplifier: Int): Boolean {
        return pDuration % 20 == 0
    }

    @JvmStatic
    fun onBurnAdded(living: LivingEntity, instance: MobEffectInstance, source: Entity?) {
        if (!instance.effect.equals(ModMobEffects.BURN)) {
            return
        }

        val attacker = if (source is AreaEffectCloud) source.owner else source as? LivingEntity
        living.forceHurt(
            DamageSource(
                living.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE)
                    .getHolderOrThrow(DamageTypes.IN_FIRE),
                attacker ?: source
            ), 0.6f + (0.3f * instance.amplifier)
        )
        living.invulnerableTime = 0

        if (attacker != null) {
            living.persistentData.putInt(TAG_ATTACKER, attacker.id)
        }
    }

    @JvmStatic
    fun onBurnRemoved(living: LivingEntity, instance: MobEffectInstance?) {
        if (instance == null) return

        if (instance.effect.equals(ModMobEffects.BURN)) {
            living.persistentData.remove(TAG_ATTACKER)
        }
    }

    @JvmStatic
    fun onLivingTick(living: LivingEntity) {
        if (living.hasEffect(ModMobEffects.BURN)) {
            living.remainingFireTicks = 2
        }
        if (living.isInWater) {
            living.removeEffect(ModMobEffects.BURN)
        }
    }
}
