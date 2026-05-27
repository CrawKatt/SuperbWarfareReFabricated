package com.atsuishio.superbwarfare.network.message.receive;

import com.atsuishio.superbwarfare.network.ClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;

public record FinishAssemblingVehicleMessage(int containerId) {

    public static void encode(FinishAssemblingVehicleMessage message, FriendlyByteBuf byteBuf) {
        byteBuf.writeVarInt(message.containerId);
    }

    public static FinishAssemblingVehicleMessage decode(FriendlyByteBuf byteBuf) {
        return new FinishAssemblingVehicleMessage(byteBuf.readVarInt());
    }

    public static void handler(FinishAssemblingVehicleMessage message) {
        ClientPacketHandler.handleFinishAssemblingVehicleMessage(message);
    }
}
