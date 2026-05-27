package com.atsuishio.superbwarfare.data.vehicle;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.network.NetworkRegistry;
import com.atsuishio.superbwarfare.network.message.receive.VehiclesDataMessage;
import net.minecraft.server.level.ServerPlayer;

public class VehicleDataTool {

    // TODO: Register in Mod.java using Fabric event API
    public static void onPlayerLogin(ServerPlayer player) {
        var server = player.getServer();
        if (server != null && server.isSingleplayerOwner(player.getGameProfile())) {
            return;
        }

        NetworkRegistry.sendToPlayer(player, VehiclesDataMessage.create());
    }

    // TODO: Register in Mod.java using Fabric event API
    public static void onDataPackSync(net.minecraft.server.players.PlayerList players) {
        var server = players.getServer();

        var message = VehiclesDataMessage.create();
        for (var player : players.getPlayers()) {
            if (server.isSingleplayerOwner(player.getGameProfile())) {
                continue;
            }

            NetworkRegistry.sendToPlayer(player, message);
        }
    }
}
