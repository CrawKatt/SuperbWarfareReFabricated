package com.atsuishio.superbwarfare.event.custom;

import com.atsuishio.superbwarfare.Mod;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public interface LivingDeathCallback {
    ResourceLocation LOWEST_PHASE = new ResourceLocation(Mod.MODID, "living_death_lowest");
    net.fabricmc.fabric.api.event.Event<LivingDeathCallback> EVENT = EventFactory.createWithPhases(
            LivingDeathCallback.class,
            callbacks -> event -> {
                for (LivingDeathCallback callback : callbacks) {
                    callback.onLivingDeath(event);
                    if (event.isCanceled()) {
                        return;
                    }
                }
            },
            net.fabricmc.fabric.api.event.Event.DEFAULT_PHASE, LOWEST_PHASE
    );

    void onLivingDeath(Event event);

    class Event {
        private final LivingEntity entity;
        private final DamageSource source;
        private boolean canceled;

        public Event(LivingEntity entity, DamageSource source) {
            this.entity = entity;
            this.source = source;
        }

        public LivingEntity getEntity() {
            return this.entity;
        }

        public DamageSource getSource() {
            return this.source;
        }

        public boolean isCanceled() {
            return this.canceled;
        }

        public void setCanceled(boolean canceled) {
            this.canceled = canceled;
        }
    }
}