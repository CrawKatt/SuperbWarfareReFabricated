package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public interface LivingExperienceDropCallback {
    net.fabricmc.fabric.api.event.Event<LivingExperienceDropCallback> EVENT = EventFactory.createArrayBacked(
            LivingExperienceDropCallback.class,
            callbacks -> event -> {
                for (var callback : callbacks) {
                    callback.onLivingExperienceDrop(event);
                    if (event.isCanceled()) {
                        return;
                    }
                }
            }
    );

    void onLivingExperienceDrop(Event event);

    class Event {
        private final LivingEntity entity;
        private final Player attackingPlayer;
        private int droppedExperience;
        private boolean canceled;

        public Event(LivingEntity entity, Player attackingPlayer, int droppedExperience) {
            this.entity = entity;
            this.attackingPlayer = attackingPlayer;
            this.droppedExperience = droppedExperience;
        }

        public LivingEntity getEntity() { return entity; }
        public Player getAttackingPlayer() { return attackingPlayer; }
        public int getDroppedExperience() { return droppedExperience; }
        public void setDroppedExperience(int droppedExperience) { this.droppedExperience = droppedExperience; }
        public boolean isCanceled() { return canceled; }
        public void setCanceled(boolean canceled) { this.canceled = canceled; }
    }
}