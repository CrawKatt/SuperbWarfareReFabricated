package com.atsuishio.superbwarfare.event.custom;

import com.atsuishio.superbwarfare.api.event.ReloadEvent;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

@SuppressWarnings("unchecked")
public interface ReloadCallback<T extends ReloadEvent> {
    void onReload(T event);

    Event<ReloadCallback<ReloadEvent.Pre>> PRE = EventFactory.createArrayBacked(
            (Class<ReloadCallback<ReloadEvent.Pre>>) (Class<?>) ReloadCallback.class,
            callbacks -> event -> {
                for (var callback : callbacks) {
                    callback.onReload(event);
                }
            }
    );

    Event<ReloadCallback<ReloadEvent.Post>> POST = EventFactory.createArrayBacked(
            (Class<ReloadCallback<ReloadEvent.Post>>) (Class<?>) ReloadCallback.class,
            callbacks -> event -> {
                for (var callback : callbacks) {
                    callback.onReload(event);
                }
            }
    );
}
