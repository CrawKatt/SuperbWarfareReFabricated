package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.mobeffect.*
import net.minecraft.world.effect.MobEffect
import java.util.function.Supplier

@Suppress("unused")
object ModMobEffects {
    @JvmField
    val SHOCK: MobEffect = Registration.effect("shock") { ShockMobEffect() }

    @JvmField
    val BURN: MobEffect = Registration.effect("burn") { BurnMobEffect }

    @JvmField
    val STRIKE_PROTECTION: MobEffect = Registration.effect("strike_protection") { StrikeProtectionMobEffect }

    @JvmField
    val TRAUMA: MobEffect = Registration.effect("trauma") { TraumaMobEffect() }

    @JvmField
    val PHOSPHORUS_FIRE: MobEffect = Registration.effect("phosphorus_fire") { PhosphorusFireMobEffect }

    @JvmStatic
    fun init() {
    }
}
