package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.Entity;

public interface RenderNameTagCallback {

    net.fabricmc.fabric.api.event.Event<RenderNameTagCallback> EVENT = EventFactory.createArrayBacked(
            RenderNameTagCallback.class,
            callbacks -> event -> {
                for (RenderNameTagCallback callback : callbacks) {
                    callback.onRenderNameTag(event);
                }
            }
    );

    void onRenderNameTag(Event event);

    class Event {

        private final Entity entity;
        private boolean canceled;

        public Event(Entity entity) {
            this.entity = entity;
        }

        public Entity getEntity() {
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