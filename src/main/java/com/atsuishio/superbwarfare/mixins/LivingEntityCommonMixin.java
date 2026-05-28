package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.event.custom.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;

@Mixin(LivingEntity.class)
public class LivingEntityCommonMixin {

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$onLivingAttack(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (!LivingAttackCallback.EVENT.invoker().allowAttack(entity, source, amount)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "hurt", at = @At("TAIL"))
    private void superbwarfare$onLivingHurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        LivingHurtCallback.Event event = new LivingHurtCallback.Event(entity, source, amount);
        LivingHurtCallback.EVENT.invoker().onLivingHurt(event);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void superbwarfare$onLivingTick(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        LivingTickCallback.EVENT.invoker().onLivingTick(entity);
    }

    @Inject(method = "addEffect*", at = @At("HEAD"))
    private void superbwarfare$onAddEffect(MobEffectInstance effectInstance, Entity entity, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        MobEffectAddedCallback.EVENT.invoker().onAdded(livingEntity, effectInstance, entity);
    }

    @Inject(method = "heal", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$onLivingHeal(float healAmount, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        LivingHealCallback.Event event = new LivingHealCallback.Event(entity, healAmount);
        LivingHealCallback.EVENT.invoker().onLivingHeal(event);
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Shadow
    protected Player lastHurtByPlayer;

    @Inject(method = "dropAllDeathLoot", at = @At("HEAD"))
    private void superbwarfare$onDropAllDeathLoot(DamageSource damageSource, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        LootingLevelCallback.Event lootingEvent = new LootingLevelCallback.Event(entity, damageSource, 0);
        LootingLevelCallback.EVENT.invoker().onLootingLevel(lootingEvent);
    }

    @Inject(method = "dropAllDeathLoot", at = @At("RETURN"))
    private void superbwarfare$onDropAllDeathLootReturn(DamageSource damageSource, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        LivingDropsCallback.Event dropEvent = new LivingDropsCallback.Event(entity, damageSource, new ArrayList<>());
        LivingDropsCallback.EVENT.invoker().onLivingDrops(dropEvent);
    }

    @Inject(method = "dropExperience", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$onLivingExperienceDrop(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        int experience = entity.getExperienceReward();
        if (this.lastHurtByPlayer != null) {
            Player player = this.lastHurtByPlayer;
            LivingExperienceDropCallback.Event event = new LivingExperienceDropCallback.Event(entity, player, experience);
            LivingExperienceDropCallback.EVENT.invoker().onLivingExperienceDrop(event);
            if (event.isCanceled()) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "onEffectRemoved", at = @At("HEAD"))
    private void superbwarfare$onMobEffectRemoved(MobEffectInstance effectInstance, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        MobEffectRemovedCallback.EVENT.invoker().onRemoved(entity, effectInstance);
    }
}
