package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public interface LivingAttackCallback {
    Event<LivingAttackCallback> EVENT = EventFactory.createArrayBacked(
            LivingAttackCallback.class,
            callbacks -> (entity, source, amount) -> {
                for (LivingAttackCallback callback : callbacks) {
                    if (!callback.allowAttack(entity, source, amount)) {
                        return false;
                    }
                }
                return true;
            }
    );

    boolean allowAttack(LivingEntity entity, DamageSource source, float amount);
}