package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public interface EntityPairingCallback {
    Event<EntityPairingCallback> EVENT = EventFactory.createArrayBacked(EntityPairingCallback.class,
            callbacks -> (entity, player) -> {
                for (EntityPairingCallback callback : callbacks) {
                    callback.onPairing(entity, player);
                }
            });

    void onPairing(Entity entity, ServerPlayer player);
}
