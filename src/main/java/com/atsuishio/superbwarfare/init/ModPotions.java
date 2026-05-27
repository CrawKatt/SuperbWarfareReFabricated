package com.atsuishio.superbwarfare.init;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;

import java.util.function.Supplier;

public class ModPotions {

    public static final Supplier<Potion> SHOCK = Registration.potion("superbwarfare_shock",
            () -> new Potion(new MobEffectInstance(ModMobEffects.SHOCK.get(), 100, 0)));
    public static final Supplier<Potion> STRONG_SHOCK = Registration.potion("superbwarfare_strong_shock",
            () -> new Potion(new MobEffectInstance(ModMobEffects.SHOCK.get(), 100, 1)));
    public static final Supplier<Potion> LONG_SHOCK = Registration.potion("superbwarfare_long_shock",
            () -> new Potion(new MobEffectInstance(ModMobEffects.SHOCK.get(), 400, 0)));
}
