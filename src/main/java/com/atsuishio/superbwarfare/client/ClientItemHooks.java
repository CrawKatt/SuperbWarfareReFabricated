package com.atsuishio.superbwarfare.client;

import com.atsuishio.superbwarfare.event.ClientEventHandler;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;

public final class ClientItemHooks {
    public static void enterMonitorCamera() {
        ClientEventHandler.lastCameraType = Minecraft.getInstance().options.getCameraType();
        Minecraft.getInstance().options.setCameraType(CameraType.THIRD_PERSON_BACK);
    }

    public static void restoreMonitorCamera() {
        if (ClientEventHandler.lastCameraType != null) {
            Minecraft.getInstance().options.setCameraType(ClientEventHandler.lastCameraType);
        }
    }

    public static void startLungeMineSprint() {
        ClientEventHandler.lungeSprint = 180;
    }
}
