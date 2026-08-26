package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;

public interface PlayerMenuOpenedCallback {
    Event<PlayerMenuOpenedCallback> EVENT = EventFactory.createArrayBacked(PlayerMenuOpenedCallback.class,
            callbacks -> (player, menu) -> {
                for (PlayerMenuOpenedCallback callback : callbacks) {
                    callback.onOpened(player, menu);
                }
            });

    void onOpened(ServerPlayer player, AbstractContainerMenu menu);
}
