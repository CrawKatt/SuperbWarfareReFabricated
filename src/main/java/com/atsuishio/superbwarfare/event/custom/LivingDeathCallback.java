package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public interface LivingDeathCallback {
    net.fabricmc.fabric.api.event.Event<LivingDeathCallback> EVENT = EventFactory.createArrayBacked(
            LivingDeathCallback.class,
            callbacks -> event -> {
                for (LivingDeathCallback callback : callbacks) {
                    if (event.isCanceled()) break;
                    callback.onDeath(event);
                }
            }
    );

    void onDeath(Event event);

    class Event {
        private final LivingEntity entity;
        private final DamageSource source;
        private boolean canceled;

        public Event(LivingEntity entity, DamageSource source) {
            this.entity = entity;
            this.source = source;
        }

        public LivingEntity getEntity() { return entity; }
        public DamageSource getSource() { return source; }
        public boolean isCanceled() { return canceled; }
        public void setCanceled(boolean canceled) { this.canceled = canceled; }
    }
}
