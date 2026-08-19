package com.atsuishio.superbwarfare.event.custom;

import com.atsuishio.superbwarfare.api.event.RegisterContainersEvent;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface RegisterContainersCallback {
    Event<RegisterContainersCallback> EVENT = EventFactory.createArrayBacked(
            RegisterContainersCallback.class,
            callbacks -> event -> {
                for (var callback : callbacks) callback.register(event);
            }
    );

    void register(RegisterContainersEvent event);
}
