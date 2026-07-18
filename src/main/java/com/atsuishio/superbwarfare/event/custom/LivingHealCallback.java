package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.LivingEntity;

public interface LivingHealCallback {
    net.fabricmc.fabric.api.event.Event<LivingHealCallback> EVENT = EventFactory.createArrayBacked(
            LivingHealCallback.class,
            callbacks -> event -> {
                for (LivingHealCallback callback : callbacks) {
                    callback.onLivingHeal(event);
                    if (event.isCanceled()) {
                        return;
                    }
                }
            }
    );

    void onLivingHeal(Event event);

    class Event {
        private final LivingEntity entity;
        private float amount;
        private boolean canceled;

        public Event(LivingEntity entity, float amount) {
            this.entity = entity;
            this.amount = amount;
        }

        public LivingEntity getEntity() {
            return this.entity;
        }

        public float getAmount() {
            return this.amount;
        }

        public void setAmount(float amount) {
            this.amount = amount;
        }

        public boolean isCanceled() {
            return this.canceled;
        }

        public void setCanceled(boolean canceled) {
            this.canceled = canceled;
        }
    }
}