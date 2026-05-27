package com.atsuishio.superbwarfare.network.message.receive;

import com.atsuishio.superbwarfare.network.ClientPacketHandler;

public enum ResetCameraTypeMessage {
    INSTANCE;

    public static void handler() {
        ClientPacketHandler.handleResetCameraType();
    }
}
