package com.atsuishio.superbwarfare.init

import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.alchemy.Potion
import java.util.function.Supplier

@Suppress("unused")
object ModPotions {
    @JvmField
    val SHOCK: Potion = Registration.potion(
        "superbwarfare_shock",
    ) { Potion(MobEffectInstance(ModMobEffects.SHOCK, 100, 0)) }

    @JvmField
    val STRONG_SHOCK: Potion = Registration.potion(
        "superbwarfare_strong_shock",
    ) { Potion(MobEffectInstance(ModMobEffects.SHOCK, 100, 1)) }

    @JvmField
    val LONG_SHOCK: Potion = Registration.potion(
        "superbwarfare_long_shock",
    ) { Potion(MobEffectInstance(ModMobEffects.SHOCK, 400, 0)) }

    @JvmStatic
    fun init() {
    }
}
