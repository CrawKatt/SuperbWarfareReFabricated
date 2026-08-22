package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.mobeffect.BurnMobEffect
import com.atsuishio.superbwarfare.mobeffect.PhosphorusFireMobEffect
import com.atsuishio.superbwarfare.mobeffect.ShockMobEffect
import com.atsuishio.superbwarfare.mobeffect.StrikeProtectionMobEffect
import com.atsuishio.superbwarfare.mobeffect.TraumaMobEffect
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.effect.MobEffect

@Suppress("unused")
object ModMobEffects {
    @JvmField
    val SHOCK: Holder<MobEffect> = register("shock", ShockMobEffect())

    @JvmField
    val BURN: Holder<MobEffect> = register("burn", BurnMobEffect)

    @JvmField
    val STRIKE_PROTECTION: Holder<MobEffect> = register("strike_protection", StrikeProtectionMobEffect)

    @JvmField
    val TRAUMA: Holder<MobEffect> = register("trauma", TraumaMobEffect())

    @JvmField
    val PHOSPHORUS_FIRE: Holder<MobEffect> = register("phosphorus_fire", PhosphorusFireMobEffect)

    private fun register(name: String, effect: MobEffect): Holder<MobEffect> {
        val id = Mod.loc(name)

        Registry.register(
            BuiltInRegistries.MOB_EFFECT,
            id,
            effect
        )

        return BuiltInRegistries.MOB_EFFECT
            .getHolder(ResourceKey.create(Registries.MOB_EFFECT, id))
            .orElseThrow()
    }

    @JvmStatic
    fun init() {
    }
}
