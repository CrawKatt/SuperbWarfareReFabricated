package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.client.particle.BulletDecalOption;
import com.atsuishio.superbwarfare.client.particle.CannonMuzzleFlareOption;
import com.atsuishio.superbwarfare.client.particle.CustomCloudOption;
import com.atsuishio.superbwarfare.client.particle.CustomSmokeOption;
import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ModParticleTypes {

    public static final Supplier<SimpleParticleType> FIRE_STAR = Registration.particle("fire_star", () -> new SimpleParticleType(true));

    public static final Supplier<SimpleParticleType> RISING_SMOKE = Registration.particle("rising_smoke", () -> new SimpleParticleType(true));

    public static final Supplier<ParticleType<BulletDecalOption>> BULLET_DECAL = Registration.particle("bullet_decal",
            () -> createOptions(BulletDecalOption.CODEC, true, BulletDecalOption.DESERIALIZER));

    public static final Supplier<ParticleType<CustomSmokeOption>> CUSTOM_SMOKE = Registration.particle("custom_smoke",
            () -> createOptions(CustomSmokeOption.CODEC, true, CustomSmokeOption.DESERIALIZER));

    public static final Supplier<ParticleType<CannonMuzzleFlareOption>> CANNON_MUZZLE_FLARE = Registration.particle("cannon_muzzle_flare",
            () -> createOptions(CannonMuzzleFlareOption.CODEC, true, CannonMuzzleFlareOption.DESERIALIZER));

    public static final Supplier<ParticleType<CustomCloudOption>> CUSTOM_CLOUD = Registration.particle("custom_cloud",
            () -> createOptions(CustomCloudOption.CODEC, true, CustomCloudOption.DESERIALIZER));

    @SuppressWarnings("deprecation")
    public static <T extends ParticleOptions> ParticleType<T> createOptions(Codec<T> codec, boolean pOverrideLimiter, ParticleOptions.Deserializer<T> deserializer) {
        return new ParticleType<>(pOverrideLimiter, deserializer) {
            public @NotNull Codec<T> codec() {
                return codec;
            }
        };
    }

    public static void register() {

    }
}
