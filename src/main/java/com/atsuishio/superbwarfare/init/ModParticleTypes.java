package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.client.particle.BulletDecalOption;
import com.atsuishio.superbwarfare.client.particle.CannonMuzzleFlareOption;
import com.atsuishio.superbwarfare.client.particle.CustomCloudOption;
import com.atsuishio.superbwarfare.client.particle.CustomSmokeOption;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

public class ModParticleTypes {
    public static final SimpleParticleType FIRE_STAR = register("fire_star", new SimpleParticleType(true) {});
    public static final SimpleParticleType RISING_SMOKE = register("rising_smoke", new SimpleParticleType(true) {});
    public static final ParticleType<BulletDecalOption> BULLET_DECAL = register("bullet_decal",
            createOptions(BulletDecalOption.CODEC, true, BulletDecalOption.STREAM_CODEC));
    public static final ParticleType<CustomSmokeOption> CUSTOM_SMOKE = register("custom_smoke",
            createOptions(CustomSmokeOption.CODEC, true, CustomSmokeOption.STREAM_CODEC));
    public static final ParticleType<CannonMuzzleFlareOption> CANNON_MUZZLE_FLARE = register("cannon_muzzle_flare",
            createOptions(CannonMuzzleFlareOption.CODEC, true, CannonMuzzleFlareOption.STREAM_CODEC));
    public static final ParticleType<CustomCloudOption> CUSTOM_CLOUD = register("custom_cloud",
            createOptions(CustomCloudOption.CODEC, true, CustomCloudOption.STREAM_CODEC));

    private static <T extends ParticleType<?>> T register(String name, T type) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, Mod.loc(name), type);
    }

    public static <T extends ParticleOptions> ParticleType<T> createOptions(MapCodec<T> codec, boolean overrideLimiter, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return new ParticleType<>(overrideLimiter) {
            public @NotNull MapCodec<T> codec() {
                return codec;
            }

            public @NotNull StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
                return streamCodec;
            }
        };
    }

    public static void init() {

    }
}
