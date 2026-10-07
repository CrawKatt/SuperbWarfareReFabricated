package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public interface LivingIncomingDamageCallback {
    net.fabricmc.fabric.api.event.Event<LivingIncomingDamageCallback> EVENT = EventFactory.createArrayBacked(
            LivingIncomingDamageCallback.class,
            callbacks -> event -> {
                for (LivingIncomingDamageCallback callback : callbacks) {
                    if (event.isCanceled()) break;
                    callback.onIncomingDamage(event);
                }
            }
    );

    void onIncomingDamage(Event event);

    class Event {
        private final LivingEntity entity;
        private final DamageSource source;
        private float amount;
        private boolean canceled;

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

        public boolean isCanceled() {
            return this.canceled;
        }

        public void setCanceled(boolean canceled) {
            this.canceled = canceled;
        }
    }
}
