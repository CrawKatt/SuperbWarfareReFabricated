package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.entity.mixin.DamageAccess;
import com.atsuishio.superbwarfare.entity.mixin.ICustomKnockback;
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.atsuishio.superbwarfare.init.ModTags;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements ICustomKnockback, DamageAccess {

    @Shadow
    @Nullable
    protected abstract SoundEvent getDeathSound();

    @Shadow
    protected abstract float getSoundVolume();

    @Shadow
    protected abstract void playHurtSound(DamageSource source);

    @Shadow
    protected abstract void actuallyHurt(DamageSource source, float amount);

    @Shadow
    protected abstract void hurtHelmet(DamageSource source, float amount);

    @Shadow
    protected abstract boolean checkTotemDeathProtection(DamageSource source);

    @Unique
    private double superbwarfare$knockbackStrength = -1;

    @Override
    public void superbWarfare$setKnockbackStrength(double strength) {
        this.superbwarfare$knockbackStrength = strength;
    }

    @Override
    public void superbWarfare$resetKnockbackStrength() {
        this.superbwarfare$knockbackStrength = -1;
    }

    @Override
    public double superbWarfare$getKnockbackStrength() {
        return this.superbwarfare$knockbackStrength;
    }

    @Override
    public SoundEvent superbWarfare$getDeathSound() {
        return this.getDeathSound();
    }

    @Override
    public float superbWarfare$getSoundVolume() {
        return this.getSoundVolume();
    }

    @Override
    public void superbWarfare$playHurtSound(DamageSource source) {
        this.playHurtSound(source);
    }

    @Override
    public void superbWarfare$actuallyHurt(DamageSource source, float amount) {
        this.actuallyHurt(source, amount);
    }

    @Override
    public void superbWarfare$hurtHelmet(DamageSource source, float amount) {
        this.hurtHelmet(source, amount);
    }

    @Override
    public boolean superbWarfare$checkTotemDeathProtection(DamageSource source) {
        return this.checkTotemDeathProtection(source);
    }

    @Inject(method = "dismountVehicle", at = @At("RETURN"))
    private void superbwarfare$dismountVehicle(Entity vehicle, CallbackInfo ci) {
        if (vehicle instanceof VehicleEntity vehicleEntity) {
            vehicleEntity.removeSeatIndexTag((LivingEntity) (Object) this);
        }
    }

    @Shadow
    @Nullable
    public DamageSource lastDamageSource;

    @Shadow
    public long lastDamageStamp;

    @Inject(method = "playHurtSound", at = @At("HEAD"), cancellable = true)
    protected void playHurtSound(DamageSource pSource, CallbackInfo ci) {
        if (pSource.is(ModTags.DamageTypes.NO_HURT_EFFECT)) {
            ci.cancel();
        }
    }

    @Inject(method = "handleDamageEvent", at = @At("HEAD"), cancellable = true)
    public void handleDamageEvent(DamageSource pSource, CallbackInfo ci) {
        if (pSource.is(ModTags.DamageTypes.NO_HURT_EFFECT)) {
            ci.cancel();

            LivingEntity living = (LivingEntity) (Object) this;
            living.invulnerableTime = 0;
            living.hurtTime = 0;
            living.hurtDuration = 0;
            this.lastDamageSource = pSource;
            this.lastDamageStamp = living.level().getGameTime();
        }
    }
}
