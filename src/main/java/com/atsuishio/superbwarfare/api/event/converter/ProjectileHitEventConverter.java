package com.atsuishio.superbwarfare.api.event.converter;

import com.atsuishio.superbwarfare.api.event.ProjectileHitEvent;
import com.atsuishio.superbwarfare.api.event.wrapper.ProjectileHitEventWrapper;
import io.github.lounode.eventwrapper.forge.event.converter.ForgeEventConverter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;

public class ProjectileHitEventConverter implements ForgeEventConverter<ProjectileHitEvent, ProjectileHitEventWrapper> {

    @Override
    @NotNull
    public ProjectileHitEvent toEvent(@NotNull ProjectileHitEventWrapper wrapper) {
        if (wrapper instanceof ProjectileHitEventWrapper.HitEntity he) {
            var event = new ProjectileHitEvent.HitEntity(he.getOwner(), he.getProjectile(), he.getTarget(), he.getHitVec());
            event.setCanceled(wrapper.isCanceled());
            return event;
        }
        if (wrapper instanceof ProjectileHitEventWrapper.HitBlock hb) {
            var event = new ProjectileHitEvent.HitBlock(hb.getPos(), hb.getState(), hb.getFace(), hb.getOwner(), hb.getProjectile(), hb.getHitVec());
            event.setCanceled(wrapper.isCanceled());
            return event;
        }
        var event = new ProjectileHitEvent.HitBlock(
                BlockPos.ZERO, null, Direction.UP,
                wrapper.getOwner(), wrapper.getProjectile(), wrapper.getHitVec()
        );
        event.setCanceled(wrapper.isCanceled());
        return event;
    }

    @Override
    @NotNull
    public ProjectileHitEventWrapper toWrapper(@NotNull ProjectileHitEvent event) {
        if (event instanceof ProjectileHitEvent.HitEntity he) {
            var wrapper = new ProjectileHitEventWrapper.HitEntity(
                    he.getOwner(), he.getProjectile(), he.getTarget(),
                    he.getHitVec(), he.isHeadshot(), he.isLegShot()
            );
            wrapper.setCanceled(event.isCanceled());
            return wrapper;
        }
        if (event instanceof ProjectileHitEvent.HitBlock hb) {
            var wrapper = new ProjectileHitEventWrapper.HitBlock(
                    hb.getPos(), hb.getState(), hb.getFace(),
                    hb.getOwner(), hb.getProjectile(), hb.getHitVec()
            );
            wrapper.setCanceled(event.isCanceled());
            return wrapper;
        }
        var wrapper = new ProjectileHitEventWrapper(event.getOwner(), event.getProjectile(), event.getHitVec());
        wrapper.setCanceled(event.isCanceled());
        return wrapper;
    }

    public static class HitEntityConverter implements ForgeEventConverter<ProjectileHitEvent.HitEntity, ProjectileHitEventWrapper.HitEntity> {
        @Override
        @NotNull
        public ProjectileHitEvent.HitEntity toEvent(ProjectileHitEventWrapper.HitEntity wrapper) {
            var event = new ProjectileHitEvent.HitEntity(
                    wrapper.getOwner(), wrapper.getProjectile(),
                    wrapper.getTarget(), wrapper.getHitVec()
            );
            event.setCanceled(wrapper.isCanceled());
            return event;
        }

        @Override
        @NotNull
        public ProjectileHitEventWrapper.HitEntity toWrapper(ProjectileHitEvent.HitEntity event) {
            var wrapper = new ProjectileHitEventWrapper.HitEntity(
                    event.getOwner(), event.getProjectile(), event.getTarget(),
                    event.getHitVec(), event.isHeadshot(), event.isLegShot()
            );
            wrapper.setCanceled(event.isCanceled());
            return wrapper;
        }
    }

    public static class HitBlockConverter implements ForgeEventConverter<ProjectileHitEvent.HitBlock, ProjectileHitEventWrapper.HitBlock> {
        @Override
        @NotNull
        public ProjectileHitEvent.HitBlock toEvent(ProjectileHitEventWrapper.HitBlock wrapper) {
            var event = new ProjectileHitEvent.HitBlock(
                    wrapper.getPos(), wrapper.getState(), wrapper.getFace(),
                    wrapper.getOwner(), wrapper.getProjectile(), wrapper.getHitVec()
            );
            event.setCanceled(wrapper.isCanceled());
            return event;
        }

        @Override
        @NotNull
        public ProjectileHitEventWrapper.HitBlock toWrapper(ProjectileHitEvent.HitBlock event) {
            var wrapper = new ProjectileHitEventWrapper.HitBlock(
                    event.getPos(), event.getState(), event.getFace(),
                    event.getOwner(), event.getProjectile(), event.getHitVec()
            );
            wrapper.setCanceled(event.isCanceled());
            return wrapper;
        }
    }
}
