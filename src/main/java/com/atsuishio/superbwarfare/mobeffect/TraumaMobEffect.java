package com.atsuishio.superbwarfare.mobeffect;

import com.atsuishio.superbwarfare.event.custom.LivingHealCallback;
import com.atsuishio.superbwarfare.event.custom.LivingHurtCallback;
import com.atsuishio.superbwarfare.init.ModMobEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class TraumaMobEffect extends MobEffect {

    public TraumaMobEffect() {
        super(MobEffectCategory.HARMFUL, 0xF4ADB4);
    }

    public static void registerEvents() {
        LivingHealCallback.EVENT.register(TraumaMobEffect::onLivingHeal);
        LivingHurtCallback.EVENT.register(TraumaMobEffect::onLivingHurt);
    }

    public static void onLivingHeal(LivingHealCallback.Event event) {
        var entity = event.getEntity();
        var effect = entity.getEffect(ModMobEffects.TRAUMA.get());
        if (effect == null) return;

        int amp = effect.getAmplifier() + 1;
        if (amp >= 10) {
            event.setCanceled(true);
            return;
        }

        float amount = event.getAmount();
        event.setAmount(amount * (1 - amp * 0.1f));
    }

    public static void onLivingHurt(LivingHurtCallback.Event event) {
        var entity = event.getEntity();
        var effect = entity.getEffect(ModMobEffects.TRAUMA.get());
        if (effect == null) return;

        int amp = effect.getAmplifier() + 1;
        float amount = event.getAmount();
        event.setAmount(amount * (1 + amp * 0.15f));
    }
}