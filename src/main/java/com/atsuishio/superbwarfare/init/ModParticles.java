package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.client.particle.*;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

public class ModParticles {

    public static void init() {
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.FIRE_STAR, FireStarParticle::provider);
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.RISING_SMOKE, RisingSmokeParticle::provider);
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.BULLET_DECAL, new BulletDecalParticle.Provider());
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.CUSTOM_CLOUD, CustomCloudParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.CUSTOM_SMOKE, CustomSmokeParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.CANNON_MUZZLE_FLARE, CannonMuzzleFlareParticle.Provider::new);
    }
}
