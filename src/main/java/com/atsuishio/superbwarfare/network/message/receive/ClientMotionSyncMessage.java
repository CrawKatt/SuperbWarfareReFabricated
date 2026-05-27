package com.atsuishio.superbwarfare.network.message.receive;

import com.atsuishio.superbwarfare.network.ClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class ClientMotionSyncMessage {

    public final int id;
    public final float x;
    public final float y;
    public final float z;

    public ClientMotionSyncMessage(Entity entity) {
        this(entity.getId(), entity.getDeltaMovement());
    }

    public ClientMotionSyncMessage(int id, Vec3 motion) {
        this.id = id;
        this.x = (float) motion.x;
        this.y = (float) motion.y;
        this.z = (float) motion.z;
    }

    public static void encode(ClientMotionSyncMessage message, FriendlyByteBuf buffer) {
        buffer.writeVarInt(message.id);
        buffer.writeFloat(message.x);
        buffer.writeFloat(message.y);
        buffer.writeFloat(message.z);
    }

    public static ClientMotionSyncMessage decode(FriendlyByteBuf buffer) {
        int id = buffer.readVarInt();
        double x = buffer.readFloat();
        double y = buffer.readFloat();
        double z = buffer.readFloat();
        return new ClientMotionSyncMessage(id, new Vec3(x, y, z));
    }

    public static void handler(ClientMotionSyncMessage message) {
        ClientPacketHandler.handleClientSyncMotion(message);
    }
}
