package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public interface MobEffectRemovedCallback {
    Event<MobEffectRemovedCallback> EVENT = EventFactory.createArrayBacked(
            MobEffectRemovedCallback.class,
            callbacks -> (entity, instance) -> {
                for (MobEffectRemovedCallback callback : callbacks) {
                    callback.onRemoved(entity, instance);
                }
            }
    );

    void onRemoved(LivingEntity entity, @Nullable MobEffectInstance instance);
}