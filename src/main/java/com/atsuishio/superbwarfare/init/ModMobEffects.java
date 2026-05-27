package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.mobeffect.BurnMobEffect;
import com.atsuishio.superbwarfare.mobeffect.ShockMobEffect;
import com.atsuishio.superbwarfare.mobeffect.StrikeProtectionMobEffect;
import com.atsuishio.superbwarfare.mobeffect.TraumaMobEffect;
import net.minecraft.world.effect.MobEffect;

import java.util.function.Supplier;

public class ModMobEffects {
    public static final Supplier<MobEffect> SHOCK = Registration.effect("shock", ShockMobEffect::new);
    public static final Supplier<MobEffect> BURN = Registration.effect("burn", BurnMobEffect::new);
    public static final Supplier<MobEffect> STRIKE_PROTECTION = Registration.effect("strike_protection", StrikeProtectionMobEffect::new);
    public static final Supplier<MobEffect> TRAUMA = Registration.effect("trauma", TraumaMobEffect::new);
}
