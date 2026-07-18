package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;

import java.util.Collection;

public interface LivingDropsCallback {
    net.fabricmc.fabric.api.event.Event<LivingDropsCallback> EVENT = EventFactory.createArrayBacked(
            LivingDropsCallback.class,
            callbacks -> event -> {
                for (var callback : callbacks) {
                    callback.onLivingDrops(event);
                    if (event.isCanceled()) {
                        return;
                    }
                }
            }
    );

    void onLivingDrops(Event event);

    class Event {
        private final LivingEntity entity;
        private final DamageSource source;
        private final Collection<ItemEntity> drops;
        private boolean canceled;

        public Event(LivingEntity entity, DamageSource source, Collection<ItemEntity> drops) {
            this.entity = entity;
            this.source = source;
            this.drops = drops;
        }

        public LivingEntity getEntity() { return entity; }
        public DamageSource getSource() { return source; }
        public Collection<ItemEntity> getDrops() { return drops; }
        public boolean isCanceled() { return canceled; }
        public void setCanceled(boolean canceled) { this.canceled = canceled; }
    }
}
