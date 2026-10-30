package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.player.Player;

public interface PlayerXpChangeCallback {
    net.fabricmc.fabric.api.event.Event<PlayerXpChangeCallback> EVENT = EventFactory.createArrayBacked(
            PlayerXpChangeCallback.class,
            callbacks -> event -> {
                for (PlayerXpChangeCallback callback : callbacks) {
                    if (event.isCanceled()) break;
                    callback.onXpChange(event);
                }
            }
    );

    void onXpChange(Event event);

    class Event {
        private final Player entity;
        private int amount;
        private boolean canceled;

        public Event(Player entity, int amount) {
            this.entity = entity;
            this.amount = amount;
        }

        public Player getEntity() {
            return this.entity;
        }

        public int getAmount() {
            return this.amount;
        }

        public void setAmount(int amount) {
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
