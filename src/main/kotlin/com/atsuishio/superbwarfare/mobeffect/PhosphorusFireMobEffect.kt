package com.atsuishio.superbwarfare.mobeffect

import com.atsuishio.superbwarfare.entity.mixin.EntityPersistentDataAccess
import com.atsuishio.superbwarfare.init.ModDamageTypes
import com.atsuishio.superbwarfare.init.ModMobEffects
import com.atsuishio.superbwarfare.network.NetworkRegistry
import com.atsuishio.superbwarfare.network.message.receive.ClientPhosphorusFireMessage
import com.atsuishio.superbwarfare.tools.DamageHandler
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.item.enchantment.Enchantments

object PhosphorusFireMobEffect : MobEffect(MobEffectCategory.HARMFUL, 0xB1C1F2) {
    const val TAG_PHOSPHORUS_FIRE_COUNT = "SbwPhosphorusFireCount"
    const val TAG_PHOSPHORUS_FIRE_ATTACKER = "SbwPhosphorusFireAttacker"

    @JvmStatic
    fun registerEvents() {
        EntityTrackingEvents.START_TRACKING.register { entity, player ->
            if (entity is LivingEntity && entity.hasEffect(ModMobEffects.PHOSPHORUS_FIRE)) {
                NetworkRegistry.sendToPlayer(player, ClientPhosphorusFireMessage(entity.id, true))
            }
        }
    }

    override fun applyEffectTick(entity: LivingEntity, amplifier: Int) {
        val data = persistentData(entity)
        val attacker = if (data.contains(TAG_PHOSPHORUS_FIRE_ATTACKER)) {
            entity.level().getEntity(data.getInt(TAG_PHOSPHORUS_FIRE_ATTACKER))
        } else {
            null
        }

        val fireCount = data.getInt(TAG_PHOSPHORUS_FIRE_COUNT)
        val fireLevel = fireCount / 4
        var damage = 1f + 0.5f * amplifier +
                ((amplifier + 1) * 5f).coerceAtMost(fireLevel * (amplifier * 0.6f + 1.2f))

        if (entity.isInWater) {
            damage /= 1.5f
        }
        if (entity.hasEffect(MobEffects.FIRE_RESISTANCE)) {
            damage /= 2f
        }

        val fireResLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_PROTECTION, entity)
        damage /= 1 + fireResLevel * 0.1f

        DamageHandler.doDamage(
            entity,
            ModDamageTypes.causePhosphorusFireDamage(entity.level().registryAccess(), null, attacker),
            damage
        )
        entity.invulnerableTime = 0
        data.putInt(TAG_PHOSPHORUS_FIRE_COUNT, fireCount + 1)
    }

    override fun isDurationEffectTick(duration: Int, amplifier: Int): Boolean {
        return duration % 10 == 0
    }

    @JvmStatic
    fun onPhosphorusFireAdded(living: LivingEntity, instance: MobEffectInstance, source: Entity?) {
        if (instance.effect != ModMobEffects.PHOSPHORUS_FIRE) return

        if (source is LivingEntity) {
            persistentData(living).putInt(TAG_PHOSPHORUS_FIRE_ATTACKER, source.id)
        }

        NetworkRegistry.sendToTracking(living, ClientPhosphorusFireMessage(living.id, true))
    }

    @JvmStatic
    fun onPhosphorusFireRemoved(living: LivingEntity, instance: MobEffectInstance?) {
        if (instance == null || instance.effect != ModMobEffects.PHOSPHORUS_FIRE) return

        val data = persistentData(living)
        data.remove(TAG_PHOSPHORUS_FIRE_ATTACKER)
        data.remove(TAG_PHOSPHORUS_FIRE_COUNT)

        NetworkRegistry.sendToTracking(living, ClientPhosphorusFireMessage(living.id, false))
    }

    @JvmStatic
    fun onLivingTick(living: LivingEntity) {
        if (
            !living.level().isClientSide &&
            living.hasEffect(ModMobEffects.PHOSPHORUS_FIRE) &&
            living.level().gameTime % 1000L == 0L
        ) {
            NetworkRegistry.sendToTracking(living, ClientPhosphorusFireMessage(living.id, true))
        }
    }

    private fun persistentData(entity: LivingEntity) =
        EntityPersistentDataAccess.of(entity).`superbwarfare$getPersistentData`()
}
