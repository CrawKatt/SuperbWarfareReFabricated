package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.Mod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;

@SuppressWarnings("unused")
public class ModPotions {
    public static final Potion SHOCK = register("superbwarfare_shock",
            new Potion(new MobEffectInstance(ModMobEffects.SHOCK, 100, 0)));
    public static final Potion STRONG_SHOCK = register("superbwarfare_strong_shock",
            new Potion(new MobEffectInstance(ModMobEffects.SHOCK, 100, 1)));
    public static final Potion LONG_SHOCK = register("superbwarfare_long_shock",
            new Potion(new MobEffectInstance(ModMobEffects.SHOCK, 400, 0)));

    private static Potion register(String name, Potion potion) {
        return Registry.register(BuiltInRegistries.POTION, Mod.loc(name), potion);
    }
}
