package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.Mod
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.alchemy.Potion

@Suppress("unused")
object ModPotions {
    @JvmField
    val SHOCK: Potion = register(
        "superbwarfare_shock",
        Potion(MobEffectInstance(ModMobEffects.SHOCK, 100, 0))
    )

    @JvmField
    val STRONG_SHOCK: Potion = register(
        "superbwarfare_strong_shock",
        Potion(MobEffectInstance(ModMobEffects.SHOCK, 100, 1))
    )

    @JvmField
    val LONG_SHOCK: Potion = register(
        "superbwarfare_long_shock",
        Potion(MobEffectInstance(ModMobEffects.SHOCK, 400, 0))
    )

    private fun register(name: String, potion: Potion): Potion {
        return Registry.register(BuiltInRegistries.POTION, Mod.loc(name), potion)
    }

    @JvmStatic
    fun init() {
    }
}
