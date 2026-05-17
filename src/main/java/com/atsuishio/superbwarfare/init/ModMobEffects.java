package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.mobeffect.BurnMobEffect;
import com.atsuishio.superbwarfare.mobeffect.ShockMobEffect;
import com.atsuishio.superbwarfare.mobeffect.StrikeProtectionMobEffect;
import com.atsuishio.superbwarfare.mobeffect.TraumaMobEffect;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;

public class ModMobEffects {
    public static final MobEffect SHOCK = register("shock", new ShockMobEffect());
    public static final MobEffect BURN = register("burn", new BurnMobEffect());
    public static final MobEffect STRIKE_PROTECTION = register("strike_protection", new StrikeProtectionMobEffect());
    public static final MobEffect TRAUMA = register("trauma", new TraumaMobEffect());

    private static MobEffect register(String name, MobEffect effect) {
        return Registry.register(BuiltInRegistries.MOB_EFFECT, Mod.loc(name), effect);
    }
}
