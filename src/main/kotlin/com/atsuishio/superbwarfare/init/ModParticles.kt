package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.client.particle.*
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry

object ModParticles {
    @JvmStatic
    fun init() {
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.FIRE_STAR, FireStarParticle::provider)
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.EXPLOSION_DEBRIS, ExplosionDebrisParticle::Provider)
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.WHITE_STAR, WhiteStarParticle::provider)
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.RISING_SMOKE, RisingSmokeParticle::provider)
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.BULLET_DECAL, BulletDecalParticle.Provider())
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.CUSTOM_CLOUD, CustomCloudParticle::Provider)
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.CUSTOM_SMOKE, CustomSmokeParticle::Provider)
        ParticleFactoryRegistry.getInstance()
            .register(ModParticleTypes.CANNON_MUZZLE_FLARE, CannonMuzzleFlareParticle::Provider)
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.CUSTOM_FLARE, CustomFlareParticle::Provider)
    }
}