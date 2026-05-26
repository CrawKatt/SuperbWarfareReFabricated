package com.atsuishio.superbwarfare.api.event.wrapper;

import io.github.lounode.eventwrapper.eventbus.api.Cancelable;
import io.github.lounode.eventwrapper.eventbus.api.EventWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

@Cancelable
public class ProjectileHitEventWrapper extends EventWrapper {

    @Nullable
    private final Entity owner;
    private final Projectile projectile;
    private final Vec3 hitVec;

    public ProjectileHitEventWrapper(@Nullable Entity owner, Projectile projectile, Vec3 hitVec) {
        this.owner = owner;
        this.projectile = projectile;
        this.hitVec = hitVec;
    }

    @Nullable
    public Entity getOwner() {
        return owner;
    }

    public Projectile getProjectile() {
        return projectile;
    }

    public Vec3 getHitVec() {
        return hitVec;
    }

    @Cancelable
    public static class HitEntity extends ProjectileHitEventWrapper {
        private final Entity target;
        private final boolean isHeadshot;
        private final boolean isLegShot;

        public HitEntity(@Nullable Entity owner, Projectile projectile, Entity target, Vec3 hitVec, boolean isHeadshot, boolean isLegShot) {
            super(owner, projectile, hitVec);
            this.target = target;
            this.isHeadshot = isHeadshot;
            this.isLegShot = isLegShot;
        }

        public Entity getTarget() {
            return target;
        }

        public boolean isHeadshot() {
            return isHeadshot;
        }

        public boolean isLegShot() {
            return isLegShot;
        }
    }

    @Cancelable
    public static class HitBlock extends ProjectileHitEventWrapper {
        private final BlockPos pos;
        private final BlockState state;
        private final Direction face;

        public HitBlock(BlockPos pos, BlockState state, Direction face, @Nullable Entity owner, Projectile projectile, Vec3 hitVec) {
            super(owner, projectile, hitVec);
            this.pos = pos;
            this.state = state;
            this.face = face;
        }

        public BlockPos getPos() {
            return pos;
        }

        public BlockState getState() {
            return state;
        }

        public Direction getFace() {
            return face;
        }
    }
}
