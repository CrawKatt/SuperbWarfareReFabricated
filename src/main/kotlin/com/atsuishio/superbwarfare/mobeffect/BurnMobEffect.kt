package com.atsuishio.superbwarfare.mobeffect

import com.atsuishio.superbwarfare.capability.PersistentDataAccessor
import com.atsuishio.superbwarfare.init.ModDamageTypes
import com.atsuishio.superbwarfare.init.ModMobEffects
import com.atsuishio.superbwarfare.init.ModSounds
import com.atsuishio.superbwarfare.network.message.receive.ClientIndicatorMessage
import com.atsuishio.superbwarfare.tools.DamageHandler
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.core.registries.Registries
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundSource
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

object BurnMobEffect : MobEffect(MobEffectCategory.HARMFUL, -12708330) {
    const val TAG_ATTACKER: String = "BurnAttacker"

    override fun applyEffectTick(entity: LivingEntity, amplifier: Int): Boolean {
        val attacker = getBurnAttacker(entity)

        DamageHandler.doDamage(
            entity,
            ModDamageTypes.causeBurnDamage(entity.level().registryAccess(), attacker),
            0.6f + 0.3f * amplifier
        )

        entity.invulnerableTime = 0

        val level = attacker?.level() ?: return false
        val player = attacker as? ServerPlayer ?: return false
        if (level is ServerLevel) {
            level.playSound(
                null,
                player.blockPosition(),
                ModSounds.INDICATION,
                SoundSource.VOICE,
                1f,
                1f
            )

            ServerPlayNetworking.send(player, ClientIndicatorMessage(0, 5))
        }

        return true
    }

    override fun shouldApplyEffectTickThisTick(duration: Int, amplifier: Int): Boolean {
        return duration % 20 == 0
    }

    @JvmStatic
    fun onBurnAdded(living: LivingEntity, instance: MobEffectInstance, source: Entity?) {
        if (instance.effect != ModMobEffects.BURN) return

        DamageHandler.doDamage(
            living,
            DamageSource(
                living.level().registryAccess()
                    .registryOrThrow(Registries.DAMAGE_TYPE)
                    .getHolderOrThrow(DamageTypes.IN_FIRE),
                source
            ),
            0.6f + 0.3f * instance.amplifier
        )

        living.invulnerableTime = 0

        if (source is LivingEntity) {
            persistentData(living).putInt(TAG_ATTACKER, source.id)
        }
    }

    @JvmStatic
    fun onBurnRemoved(living: LivingEntity, instance: MobEffectInstance?) {
        if (instance == null) return
        if (instance.effect != ModMobEffects.BURN) return

        persistentData(living).remove(TAG_ATTACKER)
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

    private fun getBurnAttacker(entity: LivingEntity): Entity? {
        val data = persistentData(entity)

        return if (data.contains(TAG_ATTACKER)) {
            entity.level().getEntity(data.getInt(TAG_ATTACKER))
        } else {
            null
        }
    }

    private fun persistentData(entity: LivingEntity) =
        (entity as PersistentDataAccessor).`superbwarfare$getPersistentData`()
}
