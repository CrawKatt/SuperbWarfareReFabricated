package com.atsuishio.superbwarfare.network.message.receive;

import com.atsuishio.superbwarfare.network.ClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public record ClientSetMotionMessage(Vec3 motion, Vec3 position) {

    public static void encode(ClientSetMotionMessage message, FriendlyByteBuf buffer) {
        buffer.writeVector3f(message.motion.toVector3f());
        buffer.writeVector3f(message.position.toVector3f());
    }

    public static ClientSetMotionMessage decode(FriendlyByteBuf buffer) {
        Vector3f v = buffer.readVector3f();
        Vector3f p = buffer.readVector3f();
        return new ClientSetMotionMessage(new Vec3(v), new Vec3(p));
    }

    public static void handler(ClientSetMotionMessage message) {
        ClientPacketHandler.handleClientSetMotion(message);
    }
}
