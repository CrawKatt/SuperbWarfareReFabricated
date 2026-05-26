package com.atsuishio.superbwarfare.api.event.wrapper;

import io.github.lounode.eventwrapper.eventbus.api.Cancelable;
import io.github.lounode.eventwrapper.eventbus.api.EventWrapper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

@Cancelable
public class PreKillEventWrapper extends EventWrapper {

    private final LivingEntity entity;
    private final DamageSource source;
    private final LivingEntity target;

    public PreKillEventWrapper(LivingEntity entity, DamageSource source, LivingEntity target) {
        this.entity = entity;
        this.source = source;
        this.target = target;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public DamageSource getSource() {
        return source;
    }

    public LivingEntity getTarget() {
        return target;
    }

    @Cancelable
    public static class SendKillMessage extends PreKillEventWrapper {
        public SendKillMessage(LivingEntity entity, DamageSource source, LivingEntity target) {
            super(entity, source, target);
        }
    }

    @Cancelable
    public static class Indicator extends PreKillEventWrapper {
        public Indicator(LivingEntity entity, DamageSource source, LivingEntity target) {
            super(entity, source, target);
        }
    }
}
