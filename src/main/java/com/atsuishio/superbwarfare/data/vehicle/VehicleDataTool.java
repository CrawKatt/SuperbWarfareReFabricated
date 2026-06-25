package com.atsuishio.superbwarfare.data.vehicle;

import com.atsuishio.superbwarfare.network.message.receive.VehiclesDataMessage;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class VehicleDataTool {
    public static void register() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            var player = handler.getPlayer();
            if (server.isSingleplayerOwner(player.getGameProfile())) {
                return;
            }

            ServerPlayNetworking.send(player, VehiclesDataMessage.Companion.create());
        });

        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, serverResourceManager, success) -> {
            var message = VehiclesDataMessage.Companion.create();
            for (var player : server.getPlayerList().getPlayers()) {
                if (server.isSingleplayerOwner(player.getGameProfile())) {
                    continue;
                }

                ServerPlayNetworking.send(player, message);
            }
        });
    }
}