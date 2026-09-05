package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.capability.living.InfinityAmmoCapability;
import com.atsuishio.superbwarfare.capability.living.PhosphorusFireCapability;
import com.atsuishio.superbwarfare.capability.player.PlayerVariable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

public class ModComponents implements EntityComponentInitializer {

    public static final ComponentKey<PlayerVariable> PLAYER_VARIABLE = ComponentRegistry.getOrCreate(
            Mod.loc("player_variable"),
            PlayerVariable.class
    );

    public static final ComponentKey<PhosphorusFireCapability> PHOSPHORUS_FIRE = ComponentRegistry.getOrCreate(
            PhosphorusFireCapability.ID,
            PhosphorusFireCapability.class
    );

    public static final ComponentKey<InfinityAmmoCapability> INFINITY_AMMO = ComponentRegistry.getOrCreate(
            InfinityAmmoCapability.ID,
            InfinityAmmoCapability.class
    );

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(
                PLAYER_VARIABLE,
                player -> new PlayerVariable(),
                RespawnCopyStrategy.ALWAYS_COPY
        );

        registry.registerFor(
                LivingEntity.class,
                PHOSPHORUS_FIRE,
                living -> new PhosphorusFireCapability()
        );

        registry.registerFor(
                Entity.class,
                INFINITY_AMMO,
                entity -> new InfinityAmmoCapability()
        );
    }
}