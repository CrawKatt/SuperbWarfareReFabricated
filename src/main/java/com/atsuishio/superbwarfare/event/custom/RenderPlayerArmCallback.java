package com.atsuishio.superbwarfare.event.custom;

import com.atsuishio.superbwarfare.api.event.RenderPlayerArmEvent;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface RenderPlayerArmCallback {
    Event<RenderPlayerArmCallback> EVENT = EventFactory.createArrayBacked(
            RenderPlayerArmCallback.class,
            callbacks -> event -> {
                for (RenderPlayerArmCallback callback : callbacks) {
                    callback.render(event);

                    if (event.isCanceled()) {
                        return;
                    }
                }
            }
    );

    void render(RenderPlayerArmEvent event);

    static boolean post(RenderPlayerArmEvent event) {
        EVENT.invoker().render(event);
        return event.isCanceled();
    }
}