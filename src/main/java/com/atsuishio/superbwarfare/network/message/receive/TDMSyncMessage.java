package com.atsuishio.superbwarfare.network.message.receive;

import com.atsuishio.superbwarfare.network.ClientPacketHandler;
import com.atsuishio.superbwarfare.world.TDMSavedData;
import net.minecraft.network.FriendlyByteBuf;

public record TDMSyncMessage(TDMSavedData data) {

    public static void encode(TDMSyncMessage message, FriendlyByteBuf buf) {
        buf.writeCollection(message.data.getEntities(), FriendlyByteBuf::writeUtf);
    }

    public static TDMSyncMessage decode(FriendlyByteBuf buf) {
        return new TDMSyncMessage(new TDMSavedData(buf.readList(FriendlyByteBuf::readUtf)));
    }

    public static void handler(TDMSyncMessage message) {
        ClientPacketHandler.handleTDMSyncMessage(message);
    }
}
