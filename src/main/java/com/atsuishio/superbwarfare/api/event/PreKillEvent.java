package com.atsuishio.superbwarfare.api.event;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@ApiStatus.AvailableSince("0.8.0")
public class PreKillEvent {

    private final LivingEntity entity;
    private final DamageSource source;
    private final LivingEntity target;

    private boolean canceled;

    private PreKillEvent(LivingEntity entity, DamageSource source, LivingEntity target) {
        this.entity = entity;
        this.source = source;
        this.target = target;
    }

    public static class SendKillMessage extends PreKillEvent {

        public SendKillMessage(LivingEntity player, DamageSource source, LivingEntity target) {
            super(player, source, target);
        }
    }

    public static class Indicator extends PreKillEvent {

        public Indicator(LivingEntity player, DamageSource source, LivingEntity target) {
            super(player, source, target);
        }
    }

    public boolean isCanceled() {
        return this.canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
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
}
