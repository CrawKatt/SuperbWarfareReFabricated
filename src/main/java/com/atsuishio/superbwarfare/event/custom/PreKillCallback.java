package com.atsuishio.superbwarfare.event.custom;

import com.atsuishio.superbwarfare.api.event.PreKillEvent;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface PreKillCallback {
    Event<PreKillCallback> EVENT = EventFactory.createArrayBacked(
            PreKillCallback.class,
            callbacks -> event -> {
                for (PreKillCallback callback : callbacks) {
                    callback.preKill(event);

                    if (event.isCanceled()) {
                        return;
                    }
                }
            }
    );

    void preKill(PreKillEvent event);

    static boolean post(PreKillEvent event) {
        EVENT.invoker().preKill(event);
        return event.isCanceled();
    }
}