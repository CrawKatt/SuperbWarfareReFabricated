package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.client.particle.*;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

public class ModParticles {

    public static void register() {
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.FIRE_STAR.get(), FireStarParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.RISING_SMOKE.get(), RisingSmokeParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.BULLET_DECAL.get(), new BulletDecalParticle.Provider());
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.CUSTOM_CLOUD.get(), CustomCloudParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.CUSTOM_SMOKE.get(), CustomSmokeParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.CANNON_MUZZLE_FLARE.get(), CannonMuzzleFlareParticle.Provider::new);
    }
}
