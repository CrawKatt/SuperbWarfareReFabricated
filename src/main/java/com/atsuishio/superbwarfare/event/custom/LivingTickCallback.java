package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.LivingEntity;

public interface LivingTickCallback {
    Event<LivingTickCallback> EVENT = EventFactory.createArrayBacked(
            LivingTickCallback.class,
            callbacks -> entity -> {
                for (LivingTickCallback callback : callbacks) {
                    callback.onLivingTick(entity);
                }
            }
    );

    void onLivingTick(LivingEntity entity);
}