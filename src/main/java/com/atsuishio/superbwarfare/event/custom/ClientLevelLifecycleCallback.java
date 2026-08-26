package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;

public interface ClientLevelLifecycleCallback {
    Event<ClientLevelLifecycleCallback> LOAD = createEvent();
    Event<ClientLevelLifecycleCallback> UNLOAD = createEvent();

    private static Event<ClientLevelLifecycleCallback> createEvent() {
        return EventFactory.createArrayBacked(ClientLevelLifecycleCallback.class,
                callbacks -> (client, level) -> {
                    for (ClientLevelLifecycleCallback callback : callbacks) {
                        callback.onLevel(client, level);
                    }
                });
    }

    void onLevel(Minecraft client, ClientLevel level);
}
