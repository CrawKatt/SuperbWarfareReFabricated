package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public interface LivingHurtCallback {
    net.fabricmc.fabric.api.event.Event<LivingHurtCallback> EVENT = EventFactory.createArrayBacked(
            LivingHurtCallback.class,
            callbacks -> event -> {
                for (LivingHurtCallback callback : callbacks) {
                    callback.onLivingHurt(event);
                }
            }
    );

    void onLivingHurt(Event event);

    class Event {
        private final LivingEntity entity;
        private final DamageSource source;
        private float amount;

        public Event(LivingEntity entity, DamageSource source, float amount) {
            this.entity = entity;
            this.source = source;
            this.amount = amount;
        }

        public LivingEntity getEntity() {
            return this.entity;
        }

        public DamageSource getSource() {
            return this.source;
        }

        public float getAmount() {
            return this.amount;
        }

        public void setAmount(float amount) {
            this.amount = amount;
        }
    }
}