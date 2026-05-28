package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.event.custom.LivingAttackCallback;
import com.atsuishio.superbwarfare.event.custom.LivingHurtCallback;
import com.atsuishio.superbwarfare.event.custom.LivingTickCallback;
import com.atsuishio.superbwarfare.event.custom.MobEffectAddedCallback;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
}
