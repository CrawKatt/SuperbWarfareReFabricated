package com.atsuishio.superbwarfare.api.event.wrapper;

import com.atsuishio.superbwarfare.data.gun.GunData;
import com.atsuishio.superbwarfare.data.gun.ShootParameters;
import io.github.lounode.eventwrapper.eventbus.api.EventWrapper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShootEventWrapper extends EventWrapper {

    private final ShootParameters parameters;

    public ShootEventWrapper(@NotNull ShootParameters parameters) {
        this.parameters = parameters;
    }

    @NotNull
    public ShootParameters getShootParameters() {
        return this.parameters;
    }

    @Nullable
    public Entity getShooter() {
        return parameters.shooter();
    }

    public ServerLevel getLevel() {
        return parameters.level();
    }

    public GunData getData() {
        return parameters.data();
    }

    public double getSpread() {
        return parameters.spread();
    }

    public boolean isZoom() {
        return parameters.zoom();
    }

    public static class Pre extends ShootEventWrapper {
        public Pre(@NotNull ShootParameters parameters) {
            super(parameters);
        }
    }

    public static class Post extends ShootEventWrapper {
        public Post(@NotNull ShootParameters parameters) {
            super(parameters);
        }
    }
}
