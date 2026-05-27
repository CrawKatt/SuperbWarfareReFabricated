package com.atsuishio.superbwarfare.network.message.send;

import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class ChangeVehicleSeatMessage {

    private final int index;

    public ChangeVehicleSeatMessage(int index) {
        this.index = index;
    }

    public static void encode(ChangeVehicleSeatMessage message, FriendlyByteBuf byteBuf) {
        byteBuf.writeInt(message.index);
    }

    public static ChangeVehicleSeatMessage decode(FriendlyByteBuf byteBuf) {
        return new ChangeVehicleSeatMessage(byteBuf.readInt());
    }

    public static void handler(ChangeVehicleSeatMessage message, ServerPlayer player) {
        if (player == null || !(player.getVehicle() instanceof VehicleEntity vehicle)) {
            return;
        }

        vehicle.changeSeat(player, message.index);
    }
}
