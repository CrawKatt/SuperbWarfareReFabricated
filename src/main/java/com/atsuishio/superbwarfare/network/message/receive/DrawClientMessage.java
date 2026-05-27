package com.atsuishio.superbwarfare.network.message.receive;

import com.atsuishio.superbwarfare.event.ClientEventHandler;

public enum DrawClientMessage {
    INSTANCE;

    public static void handler() {
        ClientEventHandler.resetGunStatus();
    }
}
