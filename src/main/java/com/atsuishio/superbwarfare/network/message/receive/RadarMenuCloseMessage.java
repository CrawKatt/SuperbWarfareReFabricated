package com.atsuishio.superbwarfare.network.message.receive;

import com.atsuishio.superbwarfare.network.ClientPacketHandler;

public enum RadarMenuCloseMessage {
    INSTANCE;

    public static void handler() {
        ClientPacketHandler.handleRadarMenuClose();
    }
}
