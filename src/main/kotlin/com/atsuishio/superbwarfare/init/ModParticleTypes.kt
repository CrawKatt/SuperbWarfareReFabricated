package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.client.particle.*
import com.mojang.serialization.Codec
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.core.particles.SimpleParticleType
import java.util.function.Supplier

object ModParticleTypes {
    @JvmField
    val FIRE_STAR: SimpleParticleType = registerSimpleParticle("fire_star")

    @JvmField
    @JvmField
    val EXPLOSION_DEBRIS: ParticleType<ExplosionDebrisOption> = Registration.particle(
        "explosion_debris",
    ) { createOptions(ExplosionDebrisOption.CODEC, true, ExplosionDebrisOption.DESERIALIZER) }

    @JvmField
    val WHITE_STAR: SimpleParticleType = registerSimpleParticle("white_star")

    @JvmField
    val RISING_SMOKE: SimpleParticleType = registerSimpleParticle("rising_smoke")

    @JvmField
    @JvmField
    val BULLET_DECAL: ParticleType<BulletDecalOption> = Registration.particle(
        "bullet_decal",
    ) { createOptions(BulletDecalOption.CODEC, true, BulletDecalOption.DESERIALIZER) }

    @JvmField
    val CUSTOM_SMOKE: ParticleType<CustomSmokeOption> = Registration.particle(
        "custom_smoke",
    ) { createOptions(CustomSmokeOption.CODEC, true, CustomSmokeOption.DESERIALIZER) }

    @JvmField
    val CANNON_MUZZLE_FLARE: ParticleType<CannonMuzzleFlareOption> = Registration.particle(
        "cannon_muzzle_flare",
    ) { createOptions(CannonMuzzleFlareOption.CODEC, true, CannonMuzzleFlareOption.DESERIALIZER) }

    @JvmField
    val CUSTOM_FLARE: ParticleType<CustomFlareOption> = Registration.particle(
        "custom_flare",
    ) { createOptions(CustomFlareOption.CODEC, true, CustomFlareOption.DESERIALIZER) }

    @JvmField
    val CUSTOM_CLOUD: ParticleType<CustomCloudOption> = Registration.particle(
        "custom_cloud",
    ) { createOptions(CustomCloudOption.CODEC, true, CustomCloudOption.DESERIALIZER) }

    @Suppress("DEPRECATION")
    fun <T : ParticleOptions> createOptions(
        codec: Codec<T>,
        pOverrideLimiter: Boolean,
        deserializer: ParticleOptions.Deserializer<T>
    ): ParticleType<T> {
        return object : ParticleType<T>(pOverrideLimiter, deserializer) {
            override fun codec(): Codec<T> {
                return codec
            }
        }
    }

    fun registerSimpleParticle(
        name: String,
        limit: Boolean = true
    ): SimpleParticleType =
        Registration.particle(name) { object : SimpleParticleType(limit) {} }

    @JvmStatic
    fun init() {
    }
}
