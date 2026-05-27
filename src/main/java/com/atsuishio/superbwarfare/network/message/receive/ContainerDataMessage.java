package com.atsuishio.superbwarfare.network.message.receive;

import com.atsuishio.superbwarfare.network.ClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;

import java.util.List;

/**
 * Code based on @GoryMoon's Chargers
 */
public class ContainerDataMessage {

    private final int containerId;
    private final List<Pair> data;

    public ContainerDataMessage(int containerId, List<Pair> data) {
        this.containerId = containerId;
        this.data = data;
    }

    public static ContainerDataMessage decode(FriendlyByteBuf buf) {
        return new ContainerDataMessage(buf.readUnsignedByte(), buf.readList(byteBuf -> new Pair(byteBuf.readShort(), byteBuf.readLong())));
    }

    public static void encode(ContainerDataMessage message, FriendlyByteBuf buf) {
        buf.writeByte(message.containerId);
        buf.writeCollection(message.data, (byteBuf, p) -> p.write(byteBuf));
    }

    public static void handler(ContainerDataMessage message) {
        ClientPacketHandler.handleContainerDataMessage(message.containerId, message.data);
    }

    public static class Pair {

        public int id;
        public long data;

        public Pair(int id, long data) {
            this.id = id;
            this.data = data;
        }

        public void write(FriendlyByteBuf buf) {
            buf.writeShort(id);
            buf.writeLong(data);
        }
    }

}
