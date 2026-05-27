package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.player.Player;

public interface RenderPlayerCallback {

    net.fabricmc.fabric.api.event.Event<RenderPlayerCallback> EVENT = EventFactory.createArrayBacked(
            RenderPlayerCallback.class,
            callbacks -> event -> {
                for (RenderPlayerCallback callback : callbacks) {
                    callback.onRenderPlayer(event);
                }
            }
    );

    void onRenderPlayer(Event event);

    class Event {

        private final Player entity;
        private boolean canceled;

        public Event(Player entity) {
            this.entity = entity;
        }

        public Player getEntity() {
            return this.entity;
        }

        public boolean isCanceled() {
            return this.canceled;
        }

        public void setCanceled(boolean canceled) {
            this.canceled = canceled;
        }
    }
}