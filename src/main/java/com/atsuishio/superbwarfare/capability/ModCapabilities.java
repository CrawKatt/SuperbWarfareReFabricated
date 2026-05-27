package com.atsuishio.superbwarfare.capability;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.capability.player.PlayerVariable;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;

public final class ModCapabilities {
    public static final ComponentKey<LaserCapability.ILaserCapability> LASER_CAPABILITY = ComponentRegistry.getOrCreate(Mod.loc("laser_capability"), LaserCapability.ILaserCapability.class);
    public static final ComponentKey<PlayerVariable> PLAYER_VARIABLE = ComponentRegistry.getOrCreate(Mod.loc("player_variables"), PlayerVariable.class);
}
