package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.Entity;

public interface ServerEntityTickCallback {
    net.fabricmc.fabric.api.event.Event<ServerEntityTickCallback> EVENT = EventFactory.createArrayBacked(
            ServerEntityTickCallback.class,
            callbacks -> event -> {
                for (ServerEntityTickCallback callback : callbacks) {
                    callback.onPostTick(event);
                }
            }
    );

    void onPostTick(Event event);

    record Event(Entity entity) {
        public Entity getEntity() {
            return entity;
        }
    }
}
