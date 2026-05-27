package com.atsuishio.superbwarfare.event;

import com.atsuishio.superbwarfare.tools.HitboxHelper;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class HitboxHelperEventHandler {
    public static void registerEvents() {
        ServerTickEvents.END_SERVER_TICK.register(server -> server.getPlayerList().getPlayers().forEach(HitboxHelper::onPlayerTick));

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            HitboxHelper.onPlayerLoggedOut(handler.player);
        });
    }
}