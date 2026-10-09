package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.LivingEntity;

public interface LivingTickPreCallback {
    net.fabricmc.fabric.api.event.Event<LivingTickPreCallback> EVENT = EventFactory.createArrayBacked(
            LivingTickPreCallback.class,
            callbacks -> event -> {
                for (LivingTickPreCallback callback : callbacks) {
                    callback.onLivingTick(event);
                    if (event.isCanceled()) {
                        return;
                    }
                }
            }
    );

    void onLivingTick(Event event);

    class Event {
        private final LivingEntity entity;
        private boolean canceled;

        public Event(LivingEntity entity) {
            this.entity = entity;
        }

        public LivingEntity getEntity() {
            return this.entity;
        }

        public boolean isCanceled() {
            return this.canceled;
        }

        public void setCanceled(boolean canceled) {
            this.canceled = canceled;
        }
    }
}
