package com.atsuishio.superbwarfare.event.custom;

import com.atsuishio.superbwarfare.api.event.ShootEvent;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

@SuppressWarnings("unchecked")
public interface ShootCallback<T extends ShootEvent> {
    void onShoot(T event);

    Event<ShootCallback<ShootEvent.Pre>> PRE = EventFactory.createArrayBacked(
            (Class<ShootCallback<ShootEvent.Pre>>) (Class<?>) ShootCallback.class,
            callbacks -> event -> {
                for (var callback : callbacks) {
                    callback.onShoot(event);
                }
            }
    );

    Event<ShootCallback<ShootEvent.Post>> POST = EventFactory.createArrayBacked(
            (Class<ShootCallback<ShootEvent.Post>>) (Class<?>) ShootCallback.class,
            callbacks -> event -> {
                for (var callback : callbacks) {
                    callback.onShoot(event);
                }
            }
    );
}