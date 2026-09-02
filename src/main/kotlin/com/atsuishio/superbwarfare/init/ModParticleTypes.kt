package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.client.particle.*
import com.atsuishio.superbwarfare.tools.createStreamCodec
import com.atsuishio.superbwarfare.tools.generateMapCodec
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
    val EXPLOSION_DEBRIS: ParticleType<ExplosionDebrisOption> =
        registerParticle("explosion_debris", true, ExplosionDebrisOption.CODEC)

    @JvmField
    val WHITE_STAR: SimpleParticleType = registerSimpleParticle("white_star")

    @JvmField
    val RISING_SMOKE: SimpleParticleType = registerSimpleParticle("rising_smoke")

    @JvmField
    val BULLET_DECAL: ParticleType<BulletDecalOption> =
        registerParticle("bullet_decal", true, BulletDecalOption.CODEC)

    @JvmField
    val CUSTOM_SMOKE: ParticleType<CustomSmokeOption> = registerParticle("custom_smoke")

    @JvmField
    val CANNON_MUZZLE_FLARE: ParticleType<CannonMuzzleFlareOption> =
        registerParticle("cannon_muzzle_flare")

    @JvmField
    val CUSTOM_FLARE: ParticleType<CustomFlareOption> = registerParticle("custom_flare")

    @JvmField
    val CUSTOM_CLOUD: ParticleType<CustomCloudOption> = registerParticle("custom_cloud")

    private fun <T : ParticleType<*>> register(name: String, type: T): T {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, Mod.loc(name), type)
    }

    inline fun <reified T : ParticleOptions> registerParticle(
        name: String,
        overrideLimiter: Boolean = true,
        codec: MapCodec<T> = generateMapCodec<T>(),
        streamCodec: StreamCodec<in RegistryFriendlyByteBuf, T> = createStreamCodec<T>(),
    ): ParticleType<T> = Registry.register(
        BuiltInRegistries.PARTICLE_TYPE,
        Mod.loc(name),
        createOptions(overrideLimiter, codec, streamCodec)
    )

    fun <T : ParticleOptions> createOptions(
        overrideLimiter: Boolean,
        codec: MapCodec<T>,
        streamCodec: StreamCodec<in RegistryFriendlyByteBuf, T>,
    ) = object : ParticleType<T>(overrideLimiter) {
        override fun codec() = codec

        override fun streamCodec() = streamCodec
    }

    fun registerSimpleParticle(
        name: String,
        limit: Boolean = true
    ): SimpleParticleType {
        return register(name, object : SimpleParticleType(limit) {})
    }

    @JvmStatic
    fun init() {
    }
}
