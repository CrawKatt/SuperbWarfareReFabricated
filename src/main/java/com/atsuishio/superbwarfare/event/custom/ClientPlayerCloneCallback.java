package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.Connection;

public interface ClientPlayerCloneCallback {
    Event<ClientPlayerCloneCallback> EVENT = EventFactory.createArrayBacked(
            ClientPlayerCloneCallback.class,
            callbacks -> (gameMode, oldPlayer, newPlayer, connection) -> {
                for (ClientPlayerCloneCallback callback : callbacks) {
                    callback.onClone(gameMode, oldPlayer, newPlayer, connection);
                }
            }
    );

    void onClone(MultiPlayerGameMode gameMode, LocalPlayer oldPlayer, LocalPlayer newPlayer, Connection connection);
}
