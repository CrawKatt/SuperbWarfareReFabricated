package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.mobeffect.BurnMobEffect;
import com.atsuishio.superbwarfare.mobeffect.ShockMobEffect;
import com.atsuishio.superbwarfare.mobeffect.StrikeProtectionMobEffect;
import com.atsuishio.superbwarfare.mobeffect.TraumaMobEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;

public class ModMobEffects {
    public static final Holder<MobEffect> SHOCK = register("shock", new ShockMobEffect());
    public static final Holder<MobEffect> BURN = register("burn", new BurnMobEffect());
    public static final Holder<MobEffect> STRIKE_PROTECTION = register("strike_protection", new StrikeProtectionMobEffect());
    public static final Holder<MobEffect> TRAUMA = register("trauma", new TraumaMobEffect());

    private static Holder<MobEffect> register(String name, MobEffect effect) {
        var id = Mod.loc(name);
        Registry.register(BuiltInRegistries.MOB_EFFECT, id, effect);
        return BuiltInRegistries.MOB_EFFECT.getHolder(ResourceKey.create(Registries.MOB_EFFECT, id)).orElseThrow();
    }

    public static void init() {

    }
}
