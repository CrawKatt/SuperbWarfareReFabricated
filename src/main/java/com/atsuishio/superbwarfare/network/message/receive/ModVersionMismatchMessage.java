package com.atsuishio.superbwarfare.network.message.receive;

import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.event.ModVersionEventHandler;
import net.minecraft.network.FriendlyByteBuf;

public record ModVersionMismatchMessage(String previousVersion, String currentVersion) {

    public static void encode(ModVersionMismatchMessage message, FriendlyByteBuf buffer) {
        buffer.writeUtf(message.previousVersion);
        buffer.writeUtf(message.currentVersion);
    }

    public static ModVersionMismatchMessage decode(FriendlyByteBuf buffer) {
        return new ModVersionMismatchMessage(buffer.readUtf(), buffer.readUtf());
    }

    public static void handler(ModVersionMismatchMessage message) {
        if (ModVersionEventHandler.updateClient(message.previousVersion(), message.currentVersion())) {
            ClientEventHandler.onPlayerLoggedIn();
        }
    }
}
