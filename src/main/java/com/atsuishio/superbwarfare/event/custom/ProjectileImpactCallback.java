package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.HitResult;

import java.util.Objects;

public interface ProjectileImpactCallback {
    net.fabricmc.fabric.api.event.Event<ProjectileImpactCallback> EVENT = EventFactory.createArrayBacked(
            ProjectileImpactCallback.class,
            callbacks -> event -> {
                for (var callback : callbacks) {
                    callback.onImpact(event);
                    if (event.isCanceled()) {
                        return;
                    }
                }
            }
    );

    void onImpact(Event event);

    static boolean onProjectileImpact(Projectile projectile, HitResult rayTraceResult) {
        Event event = new Event(projectile, rayTraceResult);
        EVENT.invoker().onImpact(event);
        return event.isCanceled() || event.getImpactResult() != ImpactResult.DEFAULT;
    }

    enum ImpactResult {
        DEFAULT,
        SKIP_ENTITY,
        STOP_AT_CURRENT,
        STOP_AT_CURRENT_NO_DAMAGE
    }

    class Event {
        private final Projectile projectile;
        private final HitResult rayTraceResult;
        private boolean canceled;
        private ImpactResult impactResult = ImpactResult.DEFAULT;

        public Event(Projectile projectile, HitResult rayTraceResult) {
            this.projectile = projectile;
            this.rayTraceResult = rayTraceResult;
        }

        public Projectile getEntity() {
            return this.projectile;
        }

        public Projectile getProjectile() {
            return this.projectile;
        }

        public HitResult getRayTraceResult() {
            return this.rayTraceResult;
        }

        public boolean isCanceled() {
            return this.canceled;
        }

        public void setCanceled(boolean canceled) {
            this.canceled = canceled;
        }

        public ImpactResult getImpactResult() {
            return this.impactResult;
        }

        public void setImpactResult(ImpactResult impactResult) {
            this.impactResult = Objects.requireNonNull(impactResult, "ImpactResult cannot be null");
        }
    }
}
