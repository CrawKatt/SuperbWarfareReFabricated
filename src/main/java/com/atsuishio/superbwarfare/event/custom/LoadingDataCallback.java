package com.atsuishio.superbwarfare.event.custom;

import com.atsuishio.superbwarfare.api.event.LoadingDataEvent;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

@SuppressWarnings("unchecked")
public interface LoadingDataCallback<T extends LoadingDataEvent<?>> {
    void onLoading(T event);

    Event<LoadingDataCallback<LoadingDataEvent.Gun>> GUN = EventFactory.createArrayBacked(
            (Class<LoadingDataCallback<LoadingDataEvent.Gun>>) (Class<?>) LoadingDataCallback.class,
            callbacks -> event -> {
                for (var callback : callbacks) {
                    callback.onLoading(event);
                    if (event.isCanceled()) return;
                }
            }
    );

    Event<LoadingDataCallback<LoadingDataEvent.Vehicle>> VEHICLE = EventFactory.createArrayBacked(
            (Class<LoadingDataCallback<LoadingDataEvent.Vehicle>>) (Class<?>) LoadingDataCallback.class,
            callbacks -> event -> {
                for (var callback : callbacks) {
                    callback.onLoading(event);
                    if (event.isCanceled()) return;
                }
            }
    );
}
