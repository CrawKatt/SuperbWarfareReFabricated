package com.atsuishio.superbwarfare.mobeffect;

import com.atsuishio.superbwarfare.init.ModMobEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class TraumaMobEffect extends MobEffect {

    public TraumaMobEffect() {
        super(MobEffectCategory.HARMFUL, 0xF4ADB4);
    }

    public static float modifyHeal(LivingEntity entity, float amount) {
        var effect = entity.getEffect(ModMobEffects.TRAUMA);
        if (effect == null) return amount;

        int amp = effect.getAmplifier() + 1;
        if (amp >= 10) return 0;

        return amount * (1 - amp * 0.1f);
    }

    public static float modifyIncomingDamage(LivingEntity entity, float amount) {
        var effect = entity.getEffect(ModMobEffects.TRAUMA);
        if (effect == null) return amount;

        int amp = effect.getAmplifier() + 1;
        return amount * (1 + amp * 0.15f);
    }
}
