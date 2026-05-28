package com.atsuishio.superbwarfare.capability;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.capability.player.PlayerVariable;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;

public final class ModCapabilities implements EntityComponentInitializer {
    public static final ComponentKey<LaserCapability.ILaserCapability> LASER_CAPABILITY = ComponentRegistry.getOrCreate(Mod.loc("laser_capability"), LaserCapability.ILaserCapability.class);
    public static final ComponentKey<PlayerVariable> PLAYER_VARIABLE = ComponentRegistry.getOrCreate(Mod.loc("player_variables"), PlayerVariable.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(PLAYER_VARIABLE, player -> new PlayerVariable(), RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(LASER_CAPABILITY, player -> new LaserCapability.LaserCapabilityImpl(), RespawnCopyStrategy.LOSSLESS_ONLY);
    }
}
