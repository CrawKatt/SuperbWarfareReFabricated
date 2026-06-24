package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.entity.mixin.DamageAccess;
import com.atsuishio.superbwarfare.entity.mixin.DamageContainer;
import com.atsuishio.superbwarfare.entity.mixin.ICustomKnockback;
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.event.LivingEventHandler;
import com.atsuishio.superbwarfare.mobeffect.BurnMobEffect;
import com.atsuishio.superbwarfare.mobeffect.PhosphorusFireMobEffect;
import com.atsuishio.superbwarfare.mobeffect.ShockMobEffect;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Stack;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements ICustomKnockback, DamageAccess {

    @Unique
    protected final Stack<DamageContainer> damageContainers = new Stack<>();

    @Shadow
    @Nullable
    protected abstract SoundEvent getDeathSound();

    @Shadow
    protected abstract float getSoundVolume();

    @Shadow
    protected abstract void playHurtSound(DamageSource pSource);

    @Shadow
    protected abstract void actuallyHurt(DamageSource pDamageSource, float pDamageAmount);

    @Shadow
    public abstract void hurtHelmet(DamageSource pDamageSource, float pDamageAmount);

    @Shadow
    protected abstract boolean checkTotemDeathProtection(DamageSource pDamageSource);

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

    @Inject(method = "setSprinting(Z)V", at = @At("HEAD"), cancellable = true)
    public void setSprinting(boolean pSprinting, CallbackInfo ci) {
        if (((LivingEntity) (Object) this) instanceof Player player && player.level().isClientSide) {
            if (pSprinting && ClientEventHandler.zoom) {
                ci.cancel();
            }
        }
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
    public void superbWarfare$playHurtSound(DamageSource pSource) {
        this.playHurtSound(pSource);
    }

    @Override
    public void superbWarfare$actuallyHurt(DamageSource pDamageSource, float pDamageAmount) {
        this.actuallyHurt(pDamageSource, pDamageAmount);
    }

    @Override
    public void superbWarfare$hurtHelmet(DamageSource pDamageSource, float pDamageAmount) {
        this.hurtHelmet(pDamageSource, pDamageAmount);
    }

    @Override
    public boolean superbWarfare$checkTotemDeathProtection(DamageSource pDamageSource) {
        return this.checkTotemDeathProtection(pDamageSource);
    }

    @Inject(method = "dismountVehicle", at = @At("RETURN"))
    private void dismountVehicle(Entity pVehicle, CallbackInfo ci) {
        if (pVehicle instanceof VehicleEntity vehicle) {
            vehicle.removeSeatIndexTag(((LivingEntity) (Object) this));
        }
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$onHurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        Entity directEntity = source.getDirectEntity();

        if (directEntity instanceof LivingEntity attacker && ShockMobEffect.shouldCancelDamage(attacker)) {
            cir.setReturnValue(false);
            return;
        }

        if (LivingEventHandler.onEntityAttacked(self, source, amount)) {
            cir.setReturnValue(false);
        }
    }

    @ModifyVariable(method = "hurt", at = @At("HEAD"), argsOnly = true)
    private float superbwarfare$modifyHurtAmount(float amount, DamageSource source) {
        LivingEntity self = (LivingEntity) (Object) this;
        return LivingEventHandler.onEntityHurt(self, source, amount);
    }

    @Override
    public Stack<DamageContainer> superbwarfare$getDamageContainers() {
        return this.damageContainers;
    }

    @Inject(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", at = @At("TAIL"))
    private void superbwarfare$onAddEffect(
            MobEffectInstance effectInstance,
            @Nullable Entity source,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (!cir.getReturnValue()) return;

        BurnMobEffect.onBurnAdded(
                (LivingEntity) (Object) this,
                effectInstance,
                source
        );

        PhosphorusFireMobEffect.onPhosphorusFireAdded(
                (LivingEntity) (Object) this,
                effectInstance,
                source
        );

        ShockMobEffect.onShockAdded(
                (LivingEntity) (Object) this,
                effectInstance,
                source
        );
    }

    @Inject(method = "removeEffect", at = @At("HEAD"))
    private void superbwarfare$onRemoveEffect(
            Holder<MobEffect> effect,
            CallbackInfoReturnable<Boolean> cir
    ) {
        LivingEntity self = (LivingEntity) (Object) this;
        BurnMobEffect.onBurnRemoved(self, self.getEffect(effect));
        ShockMobEffect.onShockRemoved(self, self.getEffect(effect));
    }

    @Inject(method = "onEffectRemoved", at = @At("HEAD"))
    private void superbwarfare$onEffectRemoved(
            MobEffectInstance effectInstance,
            CallbackInfo ci
    ) {
        BurnMobEffect.onBurnRemoved(
                (LivingEntity) (Object) this,
                effectInstance
        );

        PhosphorusFireMobEffect.onPhosphorusFireRemoved(
                (LivingEntity) (Object) this,
                effectInstance
        );

        ShockMobEffect.onShockRemoved(
                (LivingEntity) (Object) this,
                effectInstance
        );
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void superbwarfare$mobEffectTick(CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;

        BurnMobEffect.onLivingTick(self);
        PhosphorusFireMobEffect.onLivingTick(self);
        ShockMobEffect.onLivingTick(self);
    }
}