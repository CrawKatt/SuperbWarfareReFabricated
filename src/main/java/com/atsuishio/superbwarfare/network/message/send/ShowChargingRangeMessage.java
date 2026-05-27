package com.atsuishio.superbwarfare.network.message.send;

import com.atsuishio.superbwarfare.menu.ChargingStationMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class ShowChargingRangeMessage {
    private final boolean operation;

    public ShowChargingRangeMessage(boolean operation) {
        this.operation = operation;
    }

    public static ShowChargingRangeMessage decode(FriendlyByteBuf buffer) {
        return new ShowChargingRangeMessage(buffer.readBoolean());
    }

    public static void encode(ShowChargingRangeMessage message, FriendlyByteBuf buffer) {
        buffer.writeBoolean(message.operation);
    }

    public static void handler(ShowChargingRangeMessage message, ServerPlayer player) {
        if (player == null) return;

        var menu = player.containerMenu;
        if (menu instanceof ChargingStationMenu chargingStationMenu) {
            if (!chargingStationMenu.stillValid(player)) return;

            chargingStationMenu.setShowRange(message.operation);
        }
    }
}
