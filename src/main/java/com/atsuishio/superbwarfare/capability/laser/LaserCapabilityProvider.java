package com.atsuishio.superbwarfare.capability.laser;

import net.fabricmc.fabric.api.lookup.v1.entity.EntityApiLookup;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LaserCapabilityProvider implements EntityApiLookup.EntityApiProvider<LaserCapability, Void> {

    private final LaserCapability instance = new LaserCapability();

    @Override
    public @Nullable LaserCapability find(@NotNull Entity entity, @Nullable Void context) {
        return instance;
    }
}
