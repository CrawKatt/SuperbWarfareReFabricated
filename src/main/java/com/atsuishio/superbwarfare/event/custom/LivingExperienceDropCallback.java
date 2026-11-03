package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public interface LivingExperienceDropCallback {
    net.fabricmc.fabric.api.event.Event<LivingExperienceDropCallback> EVENT = EventFactory.createArrayBacked(
            LivingExperienceDropCallback.class,
            callbacks -> event -> {
                for (LivingExperienceDropCallback callback : callbacks) {
                    if (event.isCanceled()) break;
                    callback.onExperienceDrop(event);
                }
            }
    );

    void onExperienceDrop(Event event);

    class Event {
        private final LivingEntity entity;
        @Nullable private final Player attackingPlayer;
        private final int originalExperience;
        private int droppedExperience;
        private boolean canceled;

        public Event(LivingEntity entity, @Nullable Player attackingPlayer, int originalExperience) {
            this.entity = entity;
            this.attackingPlayer = attackingPlayer;
            this.originalExperience = this.droppedExperience = originalExperience;
        }

        public LivingEntity getEntity() { return entity; }
        @Nullable public Player getAttackingPlayer() { return attackingPlayer; }
        public int getOriginalExperience() { return originalExperience; }
        public int getDroppedExperience() { return droppedExperience; }
        public void setDroppedExperience(int droppedExperience) { this.droppedExperience = droppedExperience; }
        public boolean isCanceled() { return canceled; }
        public void setCanceled(boolean canceled) { this.canceled = canceled; }
    }
}
