package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;

public interface LivingDropsCallback {
    net.fabricmc.fabric.api.event.Event<LivingDropsCallback> EVENT = EventFactory.createArrayBacked(
            LivingDropsCallback.class,
            callbacks -> event -> {
                for (LivingDropsCallback callback : callbacks) {
                    if (event.isCanceled()) break;
                    callback.onDrops(event);
                }
            }
    );
    ThreadLocal<ArrayDeque<Event>> CAPTURES = new ThreadLocal<>();

    void onDrops(Event event);

    static void beginCapture(Event event) {
        var captures = CAPTURES.get();
        if (captures == null) {
            captures = new ArrayDeque<>();
            CAPTURES.set(captures);
        }
        captures.push(event);
    }

    static void endCapture() {
        var captures = CAPTURES.get();
        captures.pop();
        if (captures.isEmpty()) CAPTURES.remove();
    }

    static boolean captureDrop(Entity owner, ItemEntity drop) {
        var captures = CAPTURES.get();
        if (captures == null) return false;
        for (Event event : captures) {
            if (event.getEntity() == owner) {
                event.getDrops().add(drop);
                return true;
            }
        }
        return false;
    }

    class Event {
        private final LivingEntity entity;
        private final DamageSource source;
        private final Collection<ItemEntity> drops = new ArrayList<>();
        private final boolean recentlyHit;
        private boolean canceled;

        public Event(LivingEntity entity, DamageSource source, boolean recentlyHit) {
            this.entity = entity;
            this.source = source;
            this.recentlyHit = recentlyHit;
        }

        public LivingEntity getEntity() { return entity; }
        public DamageSource getSource() { return source; }
        public Collection<ItemEntity> getDrops() { return drops; }
        public boolean isRecentlyHit() { return recentlyHit; }
        public boolean isCanceled() { return canceled; }
        public void setCanceled(boolean canceled) { this.canceled = canceled; }
    }
}
