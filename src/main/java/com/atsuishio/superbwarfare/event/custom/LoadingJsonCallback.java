package com.atsuishio.superbwarfare.event.custom;

import com.atsuishio.superbwarfare.api.event.LoadingJsonEvent;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface LoadingJsonCallback {
    Event<LoadingJsonCallback> EVENT = EventFactory.createArrayBacked(
            LoadingJsonCallback.class,
            callbacks -> event -> {
                for (var callback : callbacks) {
                    callback.onLoading(event);
                    if (event.isCanceled()) return;
                }
            }
    );

    void onLoading(LoadingJsonEvent event);

    static boolean post(LoadingJsonEvent event) {
        EVENT.invoker().onLoading(event);
        return event.isCanceled();
    }
}
