package com.atsuishio.superbwarfare.network.message.receive;

import com.atsuishio.superbwarfare.event.ClientEventHandler;
import net.minecraft.network.FriendlyByteBuf;

public class ShootClientMessage {

    public double time;

    public ShootClientMessage(double time) {
        this.time = time;
    }

    public static void encode(ShootClientMessage message, FriendlyByteBuf buffer) {
        buffer.writeDouble(message.time);
    }

    public static ShootClientMessage decode(FriendlyByteBuf buffer) {
        return new ShootClientMessage(buffer.readDouble());
    }

    public static void handler() {
        ClientEventHandler.handleClientShoot();
    }
}
