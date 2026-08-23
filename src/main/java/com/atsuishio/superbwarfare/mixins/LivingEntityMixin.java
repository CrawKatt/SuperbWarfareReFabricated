package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.entity.mixin.DamageAccess;
import com.atsuishio.superbwarfare.entity.mixin.DamageContainer;
import com.atsuishio.superbwarfare.entity.mixin.ICustomKnockback;
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.event.LivingEventHandler;
import com.atsuishio.superbwarfare.init.ModTags;
import com.atsuishio.superbwarfare.mobeffect.BurnMobEffect;
import com.atsuishio.superbwarfare.mobeffect.PhosphorusFireMobEffect;
import com.atsuishio.superbwarfare.mobeffect.ShockMobEffect;
import com.atsuishio.superbwarfare.mobeffect.TraumaMobEffect;
import com.atsuishio.superbwarfare.perk.functional.PowerfulAttraction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
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
    private DamageSource superbwarfare$dropSource;

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
        return TraumaMobEffect.modifyIncomingDamage(self, LivingEventHandler.onEntityHurt(self, source, amount));
    }

    @ModifyVariable(method = "heal(F)V", at = @At("HEAD"), argsOnly = true)
    private float superbwarfare$modifyHealAmount(float healAmount) {
        return TraumaMobEffect.modifyHeal((LivingEntity) (Object) this, healAmount);
    }

    @ModifyVariable(method = "knockback(DDD)V", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private double superbwarfare$customKnockback(double strength) {
        float customStrength = LivingEventHandler.onKnockback((LivingEntity) (Object) this);
        return customStrength >= 0 ? customStrength : strength;
    }

    @Inject(method = "causeFallDamage", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$vehicleFallProtection(
            float fallDistance,
            float damageMultiplier,
            DamageSource source,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (LivingEventHandler.onEntityFall((LivingEntity) (Object) this, fallDistance, damageMultiplier)) {
            cir.setReturnValue(false);
        }
    }

    @Override
    public Stack<DamageContainer> superbwarfare$getDamageContainers() {
        return this.damageContainers;
    }

    @Inject(method = "canBeAffected", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$vehicleEffectImmunity(
            MobEffectInstance effectInstance,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (LivingEventHandler.onEffectApply((LivingEntity) (Object) this, effectInstance)) {
            cir.setReturnValue(false);
        }
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

    @Inject(
            method = "dropAllDeathLoot(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;)V",
            at = @At("HEAD")
    )
    private void superbwarfare$beginPowerfulAttractionDrops(ServerLevel level, DamageSource source, CallbackInfo ci) {
        PowerfulAttraction.beginDropCapture(source);
        LivingEventHandler.beginLivingDrops();
    }

    @Inject(
            method = "dropAllDeathLoot(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;)V",
            at = @At("RETURN")
    )
    private void superbwarfare$endPowerfulAttractionDrops(ServerLevel level, DamageSource source, CallbackInfo ci) {
        PowerfulAttraction.endDropCapture();
        LivingEventHandler.finishLivingDrops((LivingEntity) (Object) this, source, level);
    }

    @Inject(
            method = "getExperienceReward(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Entity;)I",
            at = @At("RETURN"),
            cancellable = true
    )
    private void superbwarfare$powerfulAttractionExperience(
            ServerLevel level,
            Entity attackingEntity,
            CallbackInfoReturnable<Integer> cir
    ) {
        LivingEntity self = (LivingEntity) (Object) this;
        Player player = attackingEntity instanceof Player attacker ? attacker : null;
        int result = PowerfulAttraction.handleExperienceDrop(
                player,
                self.getLastDamageSource(),
                cir.getReturnValue()
        );

        if (LivingEventHandler.onLivingExperienceDrop(self, player, result)) {
            result = 0;
        }

        cir.setReturnValue(result);
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
