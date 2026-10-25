package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.player.LocalPlayer;

public interface ClientPlayerCloneCallback {
    Event<ClientPlayerCloneCallback> EVENT = EventFactory.createArrayBacked(
            ClientPlayerCloneCallback.class,
            callbacks -> (oldPlayer, newPlayer) -> {
                for (ClientPlayerCloneCallback callback : callbacks) {
                    callback.onClone(oldPlayer, newPlayer);
                }
            }
    );

    void onClone(LocalPlayer oldPlayer, LocalPlayer newPlayer);
}
