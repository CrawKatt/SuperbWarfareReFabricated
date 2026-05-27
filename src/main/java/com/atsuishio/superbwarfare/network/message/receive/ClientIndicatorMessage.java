package com.atsuishio.superbwarfare.network.message.receive;

import com.atsuishio.superbwarfare.network.ClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;

public class ClientIndicatorMessage {

    public final int type;
    public final int value;

    public ClientIndicatorMessage(int type, int value) {
        this.type = type;
        this.value = value;
    }

    public static void encode(ClientIndicatorMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
        buffer.writeInt(message.value);
    }

    public static ClientIndicatorMessage decode(FriendlyByteBuf buffer) {
        int type = buffer.readInt();
        int value = buffer.readInt();
        return new ClientIndicatorMessage(type, value);
    }

    public static void handler(ClientIndicatorMessage message) {
        ClientPacketHandler.handleClientIndicatorMessage(message);
    }
}
