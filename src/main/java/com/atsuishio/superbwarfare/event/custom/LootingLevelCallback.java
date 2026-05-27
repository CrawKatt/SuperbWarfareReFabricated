package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public interface LootingLevelCallback {
    net.fabricmc.fabric.api.event.Event<LootingLevelCallback> EVENT = EventFactory.createArrayBacked(
            LootingLevelCallback.class,
            callbacks -> event -> {
                for (var callback : callbacks) callback.onLootingLevel(event);
            }
    );

    void onLootingLevel(Event event);

    class Event {
        private final LivingEntity entity;
        private final DamageSource damageSource;
        private int lootingLevel;

        public Event(LivingEntity entity, DamageSource damageSource, int lootingLevel) {
            this.entity = entity;
            this.damageSource = damageSource;
            this.lootingLevel = lootingLevel;
        }

        public LivingEntity getEntity() { return entity; }
        public DamageSource getDamageSource() { return damageSource; }
        public int getLootingLevel() { return lootingLevel; }
        public void setLootingLevel(int lootingLevel) { this.lootingLevel = lootingLevel; }
    }
}
