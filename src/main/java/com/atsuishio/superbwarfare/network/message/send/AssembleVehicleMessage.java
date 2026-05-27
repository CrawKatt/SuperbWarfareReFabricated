package com.atsuishio.superbwarfare.network.message.send;

import com.atsuishio.superbwarfare.menu.VehicleAssemblingMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class AssembleVehicleMessage {

    private final ResourceLocation id;
    private final int containerId;

    public AssembleVehicleMessage(ResourceLocation id, int containerId) {
        this.id = id;
        this.containerId = containerId;
    }

    public static void encode(AssembleVehicleMessage message, FriendlyByteBuf byteBuf) {
        byteBuf.writeResourceLocation(message.id);
        byteBuf.writeVarInt(message.containerId);
    }

    public static AssembleVehicleMessage decode(FriendlyByteBuf byteBuf) {
        return new AssembleVehicleMessage(byteBuf.readResourceLocation(), byteBuf.readVarInt());
    }

    public static void handler(AssembleVehicleMessage message, ServerPlayer player) {
        if (player == null) return;
        if (player.containerMenu.containerId != message.containerId) return;
        if (player.containerMenu instanceof VehicleAssemblingMenu menu) {
            menu.assembleVehicle(message.id, player);
        }
    }
}
