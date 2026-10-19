package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.HitResult;

public interface ProjectileImpactCallback {
    net.fabricmc.fabric.api.event.Event<ProjectileImpactCallback> EVENT = EventFactory.createArrayBacked(
            ProjectileImpactCallback.class,
            callbacks -> event -> {
                for (ProjectileImpactCallback callback : callbacks) {
                    callback.onProjectileImpact(event);
                    if (event.isCanceled()) {
                        return;
                    }
                }
            }
    );

    void onProjectileImpact(Event event);

    static boolean onProjectileImpact(Projectile projectile, HitResult ray) {
        var event = new Event(projectile, ray);
        EVENT.invoker().onProjectileImpact(event);
        return event.isCanceled();
    }

    class Event {
        private final Projectile projectile;
        private final HitResult ray;
        private boolean canceled;

        public Event(Projectile projectile, HitResult ray) {
            this.projectile = projectile;
            this.ray = ray;
        }

        public Projectile getEntity() {
            return this.projectile;
        }

        public Projectile getProjectile() {
            return this.projectile;
        }

        public HitResult getRayTraceResult() {
            return this.ray;
        }

        public boolean isCanceled() {
            return this.canceled;
        }

        public void setCanceled(boolean canceled) {
            this.canceled = canceled;
        }
    }
}
