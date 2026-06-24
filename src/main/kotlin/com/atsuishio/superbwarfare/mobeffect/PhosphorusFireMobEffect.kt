package com.atsuishio.superbwarfare.mobeffect

import com.atsuishio.superbwarfare.capability.PersistentDataAccessor
import com.atsuishio.superbwarfare.init.ModDamageTypes
import com.atsuishio.superbwarfare.tools.DamageHandler
import net.minecraft.core.registries.Registries
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.item.enchantment.Enchantments

open class PhosphorusFireMobEffect : MobEffect(MobEffectCategory.HARMFUL, 0xB1C1F2) {

    override fun applyEffectTick(entity: LivingEntity, amplifier: Int): Boolean {
        val data = (entity as PersistentDataAccessor).superbwarfare$getPersistentData()
        val attacker = if (data.contains(TAG_PHOSPHORUS_FIRE_ATTACKER)) {
            entity.level().getEntity(data.getInt(TAG_PHOSPHORUS_FIRE_ATTACKER))
        } else null

        val fireCount = data.getInt(TAG_PHOSPHORUS_FIRE_COUNT)
        val fireLevel = fireCount / 4

        var damage = 1f + 0.5f * amplifier + ((amplifier + 1) * 5f).coerceAtMost(fireLevel * (amplifier * 0.6f + 1.2f))
        if (entity.isInWater) {
            damage /= 1.5f
        }
        if (entity.hasEffect(MobEffects.FIRE_RESISTANCE)) {
            damage /= 2f
        }

        val enchantment = entity.level().registryAccess()
            .lookupOrThrow(Registries.ENCHANTMENT)
            .get(Enchantments.FIRE_PROTECTION)
            .orElse(null)
        if (enchantment != null) {
            val fireResLevel = EnchantmentHelper.getEnchantmentLevel(enchantment, entity)
            damage /= 1 + fireResLevel * 0.1f
        }

        DamageHandler.doDamage(
            entity,
            ModDamageTypes.causePhosphorusFireDamage(entity.level().registryAccess(), null, attacker),
            damage
        )
        entity.invulnerableTime = 0
        data.putInt(TAG_PHOSPHORUS_FIRE_COUNT, fireCount + 1)
        return true
    }

    override fun shouldApplyEffectTickThisTick(duration: Int, amplifier: Int): Boolean {
        return duration % 10 == 0
    }

    companion object {
        const val TAG_PHOSPHORUS_FIRE_COUNT = "SbwPhosphorusFireCount"
        const val TAG_PHOSPHORUS_FIRE_ATTACKER = "SbwPhosphorusFireAttacker"
    }
}
