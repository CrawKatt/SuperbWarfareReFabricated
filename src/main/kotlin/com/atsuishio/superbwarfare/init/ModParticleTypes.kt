package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.client.particle.*
import com.mojang.serialization.MapCodec
import net.minecraft.core.Registry
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec

object ModParticleTypes {
    @JvmField
    val FIRE_STAR: SimpleParticleType = registerSimpleParticle("fire_star")

    @JvmField
    val EXPLOSION_DEBRIS: ParticleType<ExplosionDebrisOption> = register(
        "explosion_debris",
        createOptions(ExplosionDebrisOption.CODEC, true, ExplosionDebrisOption.STREAM_CODEC)
    )

    @JvmField
    val WHITE_STAR: SimpleParticleType = registerSimpleParticle("white_star")

    @JvmField
    val RISING_SMOKE: SimpleParticleType = registerSimpleParticle("rising_smoke")

    @JvmField
    val BULLET_DECAL: ParticleType<BulletDecalOption> = register(
        "bullet_decal",
        createOptions(BulletDecalOption.CODEC, true, BulletDecalOption.STREAM_CODEC)
    )

    @JvmField
    val CUSTOM_SMOKE: ParticleType<CustomSmokeOption> = register(
        "custom_smoke",
        createOptions(CustomSmokeOption.CODEC, true, CustomSmokeOption.STREAM_CODEC)
    )

    @JvmField
    val CANNON_MUZZLE_FLARE: ParticleType<CannonMuzzleFlareOption> = register(
        "cannon_muzzle_flare",
        createOptions(CannonMuzzleFlareOption.CODEC, true, CannonMuzzleFlareOption.STREAM_CODEC)
    )

    @JvmField
    val CUSTOM_FLARE: ParticleType<CustomFlareOption> = register(
        "custom_flare",
        createOptions(CustomFlareOption.CODEC, true, CustomFlareOption.STREAM_CODEC)
    )

    @JvmField
    val CUSTOM_CLOUD: ParticleType<CustomCloudOption> = register(
        "custom_cloud",
        createOptions(CustomCloudOption.CODEC, true, CustomCloudOption.STREAM_CODEC)
    )

    private fun <T : ParticleType<*>> register(name: String, type: T): T {
        return Registry.register(
            BuiltInRegistries.PARTICLE_TYPE,
            Mod.loc(name),
            type
        )
    }

    fun <T : ParticleOptions> createOptions(
        codec: MapCodec<T>,
        overrideLimiter: Boolean,
        streamCodec: StreamCodec<in RegistryFriendlyByteBuf, T>
    ): ParticleType<T> {
        return object : ParticleType<T>(overrideLimiter) {
            override fun codec(): MapCodec<T> {
                return codec
            }

            override fun streamCodec(): StreamCodec<in RegistryFriendlyByteBuf, T> {
                return streamCodec
            }
        }
    }

    fun registerSimpleParticle(
        name: String,
        limit: Boolean = true
    ): SimpleParticleType {
        return register(
            name,
            object : SimpleParticleType(limit) {}
        )
    }

    @JvmStatic
    fun init() {
    }
}
