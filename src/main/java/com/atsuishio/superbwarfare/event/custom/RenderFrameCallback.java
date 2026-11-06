package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.DeltaTracker;

@Environment(EnvType.CLIENT)
@FunctionalInterface
public interface RenderFrameCallback {
    Event<RenderFrameCallback> EVENT = EventFactory.createArrayBacked(RenderFrameCallback.class, callbacks -> deltaTracker -> {
        for (RenderFrameCallback callback : callbacks) {
            callback.onRenderFrame(deltaTracker);
        }
    });

    void onRenderFrame(DeltaTracker deltaTracker);
}
