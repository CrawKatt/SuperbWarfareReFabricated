package com.atsuishio.superbwarfare.mobeffect

import com.atsuishio.superbwarfare.event.custom.LivingHealCallback
import com.atsuishio.superbwarfare.event.custom.LivingHurtCallback
import com.atsuishio.superbwarfare.init.ModDamageTypes
import com.atsuishio.superbwarfare.init.ModMobEffects
import com.atsuishio.superbwarfare.tools.forceHurt
import com.atsuishio.superbwarfare.tools.persistentData
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import kotlin.math.max
import kotlin.math.min

object RadiationMobEffect : MobEffect(MobEffectCategory.HARMFUL, 0x55FF55) {
    const val MAX_EFFECTS_LEVEL = 20

    private const val MAX_HEALTH_MODIFIER_UUID = "E560C8B4-8E5D-4C59-BD01-8C50E9C1EDB7"
    private const val MOVEMENT_SPEED_MODIFIER_UUID = "7D5F7E7E-67DD-48D7-90E3-64C3410E1A80"
    private const val ATTACK_SPEED_MODIFIER_UUID = "B49D7F68-566A-48C6-BFB4-19F7D4AC25EB"
    private const val ATTACK_DAMAGE_MODIFIER_UUID = "AD6EE9CB-6037-40AF-B122-10A4590A55E3"

    private const val LAST_BLEED_TIME_TAG = "SbwRadiationLastBleedTime"

    init {
        addAttributeModifier(
            Attributes.MAX_HEALTH,
            MAX_HEALTH_MODIFIER_UUID,
            -0.9,
            AttributeModifier.Operation.MULTIPLY_TOTAL
        )
        addAttributeModifier(
            Attributes.MOVEMENT_SPEED,
            MOVEMENT_SPEED_MODIFIER_UUID,
            -0.9,
            AttributeModifier.Operation.MULTIPLY_TOTAL
        )
        addAttributeModifier(
            Attributes.ATTACK_SPEED,
            ATTACK_SPEED_MODIFIER_UUID,
            -0.9,
            AttributeModifier.Operation.MULTIPLY_TOTAL
        )
        addAttributeModifier(
            Attributes.ATTACK_DAMAGE,
            ATTACK_DAMAGE_MODIFIER_UUID,
            -0.9,
            AttributeModifier.Operation.MULTIPLY_TOTAL
        )
        LivingHealCallback.EVENT.register(::onLivingHeal)
        LivingHurtCallback.EVENT.register(::onLivingHurt)
    }

    override fun getAttributeModifierValue(amplifier: Int, modifier: AttributeModifier): Double {
        return -getAttributeReduction(getLevel(amplifier))
    }

    override fun getCurativeItems(): List<ItemStack> = emptyList()

    override fun isDurationEffectTick(duration: Int, amplifier: Int): Boolean {
        return duration % 40 == 0
    }

    override fun applyEffectTick(entity: LivingEntity, amplifier: Int) {
        if (entity.level().isClientSide) return

        val level = getLevel(amplifier)
        if (entity is Player) {
            entity.causeFoodExhaustion((0.005f * level).coerceAtMost(0.1f))
        }

        entity.forceHurt(
            ModDamageTypes.causeRadiationDamage(entity.level().registryAccess(), null),
            0.25f + 0.125f * (level - 1)
        )
    }

    fun onLivingHeal(event: LivingHealCallback.Event) {
        val level = getLevel(event.entity.getEffect(ModMobEffects.RADIATION) ?: return)
        when (level) {
            1 -> event.amount *= 0.5f
            2 -> event.amount *= 0.25f
            else -> event.isCanceled = true
        }
    }

    fun onLivingHurt(event: LivingHurtCallback.Event) {
        val entity = event.entity
        val source = event.source
        if (!canCauseBleeding(source)) return

        val level = getLevel(entity.getEffect(ModMobEffects.RADIATION) ?: return)
        val gameTime = entity.level().gameTime
        val lastBleedTime = entity.persistentData.getLong(LAST_BLEED_TIME_TAG)
        val cooldown = max(10, 200 / level)
        if (gameTime - lastBleedTime < cooldown) return

        val chance = min(0.75, 0.1 + 0.035 * (level - 1))
        if (entity.random.nextDouble() >= chance) return

        val multiplier = min(0.5, 0.1 + 0.4 * (level - 1) / 19.0)
        entity.persistentData.putLong(LAST_BLEED_TIME_TAG, gameTime)
        entity.forceHurt(
            ModDamageTypes.causeRadiationDamage(entity.level().registryAccess(), source.entity),
            (event.amount * multiplier).toFloat()
        )
    }

    @JvmStatic
    fun reduceLevel(entity: LivingEntity): Boolean {
        val instance = entity.getEffect(ModMobEffects.RADIATION) ?: return false
        val level = getLevel(instance)
        if (level <= 1) {
            return entity.removeEffect(ModMobEffects.RADIATION)
        } else {
            entity.forceAddEffect(
                MobEffectInstance(
                    ModMobEffects.RADIATION,
                    MobEffectInstance.INFINITE_DURATION,
                    level - 2,
                    false,
                    true,
                    true
                ),
                null
            )
        }
        return true
    }

    @JvmStatic
    fun getLevel(instance: MobEffectInstance?): Int {
        return instance?.let { (it.amplifier + 1).coerceIn(1, MAX_EFFECTS_LEVEL) } ?: 0
    }

    @JvmStatic
    fun getLevel(entity: LivingEntity): Int {
        return getLevel(entity.getEffect(ModMobEffects.RADIATION))
    }

    @JvmStatic
    fun getAttributeReduction(level: Int): Double {
        if (level <= 1) return 0.0
        return 0.9 * (level.coerceAtMost(MAX_EFFECTS_LEVEL) - 1) / (MAX_EFFECTS_LEVEL - 1)
    }

    private fun getLevel(amplifier: Int): Int {
        return (amplifier + 1).coerceIn(1, MAX_EFFECTS_LEVEL)
    }

    private fun canCauseBleeding(source: net.minecraft.world.damagesource.DamageSource): Boolean {
        return !source.`is`(ModDamageTypes.RADIATION)
    }
}
