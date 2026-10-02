package com.atsuishio.superbwarfare.event.custom;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

public class MobEffectAddedEvent {
    private final LivingEntity entity;
    @Nullable
    private final MobEffectInstance oldEffectInstance;
    private final MobEffectInstance effectInstance;
    @Nullable
    private final Entity source;

    public MobEffectAddedEvent(
            LivingEntity entity,
            @Nullable MobEffectInstance oldEffectInstance,
            MobEffectInstance effectInstance,
            @Nullable Entity source
    ) {
        this.entity = entity;
        this.oldEffectInstance = oldEffectInstance;
        this.effectInstance = effectInstance;
        this.source = source;
    }

    public LivingEntity getEntity() {
        return this.entity;
    }

    @Nullable
    public MobEffectInstance getOldEffectInstance() {
        return this.oldEffectInstance;
    }

    public MobEffectInstance getEffectInstance() {
        return this.effectInstance;
    }

    @Nullable
    public Entity getEffectSource() {
        return this.source;
    }
}
