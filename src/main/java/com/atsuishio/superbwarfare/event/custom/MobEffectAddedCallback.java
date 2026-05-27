package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public interface MobEffectAddedCallback {
    Event<MobEffectAddedCallback> EVENT = EventFactory.createArrayBacked(
            MobEffectAddedCallback.class,
            callbacks -> (entity, instance, source) -> {
                for (MobEffectAddedCallback callback : callbacks) {
                    callback.onAdded(entity, instance, source);
                }
            }
    );

    void onAdded(LivingEntity entity, MobEffectInstance instance, @Nullable Entity source);
}