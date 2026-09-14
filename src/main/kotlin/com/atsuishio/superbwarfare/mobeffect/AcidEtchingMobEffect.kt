package com.atsuishio.superbwarfare.mobeffect

import com.atsuishio.superbwarfare.init.ModMobEffects
import net.minecraft.tags.DamageTypeTags
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraftforge.event.entity.living.LivingHurtEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
object AcidEtchingMobEffect : MobEffect(MobEffectCategory.HARMFUL, 0x8FBF3F) {
    private const val ARMOR_MODIFIER_UUID = "8C7A09BD-6E7F-4E0A-AEB7-C85D837D0EB1"
    private const val ARMOR_TOUGHNESS_MODIFIER_UUID = "A9D8BEA9-6E1F-4D5E-9B47-1B5FA4F9D1D2"

    init {
        addAttributeModifier(
            Attributes.ARMOR,
            ARMOR_MODIFIER_UUID,
            -0.05,
            AttributeModifier.Operation.MULTIPLY_TOTAL
        )
        addAttributeModifier(
            Attributes.ARMOR_TOUGHNESS,
            ARMOR_TOUGHNESS_MODIFIER_UUID,
            -0.05,
            AttributeModifier.Operation.MULTIPLY_TOTAL
        )
    }

    @SubscribeEvent
    fun onLivingHurt(event: LivingHurtEvent) {
        val effect = event.entity.getEffect(ModMobEffects.ACID_ETCHING.get()) ?: return
        if (event.source.`is`(DamageTypeTags.BYPASSES_ARMOR)) return

        val level = effect.amplifier + 1
        event.amount *= 1f + 0.025f * level
    }
}
