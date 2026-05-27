package com.atsuishio.superbwarfare.network.message.receive;

import com.atsuishio.superbwarfare.network.ClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;

public class ClientTacticalSprintSyncMessage {

    public boolean flag;

    public ClientTacticalSprintSyncMessage(boolean flag) {
        this.flag = flag;
    }

    public static void encode(ClientTacticalSprintSyncMessage message, FriendlyByteBuf buffer) {
        buffer.writeBoolean(message.flag);
    }

    public static ClientTacticalSprintSyncMessage decode(FriendlyByteBuf buffer) {
        return new ClientTacticalSprintSyncMessage(buffer.readBoolean());
    }

    public static void handler(ClientTacticalSprintSyncMessage message) {
        ClientPacketHandler.handleClientTacticalSprintSync(message.flag);
    }
}
