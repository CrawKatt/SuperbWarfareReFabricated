package com.atsuishio.superbwarfare.mobeffect

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.event.custom.LivingHurtCallback
import com.atsuishio.superbwarfare.init.ModMobEffects
import net.minecraft.tags.DamageTypeTags
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes

object AcidEtchingMobEffect : MobEffect(MobEffectCategory.HARMFUL, 0x8FBF3F) {

    init {
        addAttributeModifier(
            Attributes.ARMOR,
            loc("effect.shock"),
            -0.05,
            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        )
        addAttributeModifier(
            Attributes.ARMOR_TOUGHNESS,
            loc("effect.shock"),
            -0.05,
            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        )

        LivingHurtCallback.EVENT.register(::onLivingHurt)
    }

    fun onLivingHurt(event: LivingHurtCallback.Event) {
        val effect = event.entity.getEffect(ModMobEffects.ACID_ETCHING) ?: return
        if (event.source.`is`(DamageTypeTags.BYPASSES_ARMOR)) return

        val level = effect.amplifier + 1
        event.amount *= 1f + 0.025f * level
    }
}
