package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.entity.mixin.LivingDropsCapture;
import com.atsuishio.superbwarfare.entity.mixin.DamageAccess;
import com.atsuishio.superbwarfare.event.LivingEventHandler;
import com.atsuishio.superbwarfare.event.custom.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;

@Mixin(LivingEntity.class)
public class LivingEntityCommonMixin implements LivingDropsCapture {

    @Unique
    private boolean superbwarfare$capturingDrops;

    @Unique
    private final ArrayList<ItemEntity> superbwarfare$capturedDrops = new ArrayList<>();

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$onLivingAttack(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (!LivingAttackCallback.EVENT.invoker().allowAttack(entity, source, amount)) {
            cir.setReturnValue(false);
        }
    }

    @Redirect(method = "hurt",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;actuallyHurt(Lnet/minecraft/world/damagesource/DamageSource;F)V"))
    private void superbwarfare$onLivingHurt(LivingEntity entity, DamageSource source, float amount) {
        LivingHurtCallback.Event event = new LivingHurtCallback.Event(entity, source, amount);
        LivingHurtCallback.EVENT.invoker().onLivingHurt(event);

        if (event.getAmount() > 0.0F) {
            ((DamageAccess) entity).superbWarfare$actuallyHurt(source, event.getAmount());
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void superbwarfare$onLivingTick(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        LivingTickCallback.EVENT.invoker().onLivingTick(entity);
    }

    @Inject(method = "canBeAffected", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$allowEffect(MobEffectInstance effectInstance, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if (LivingEventHandler.onEffectApply(livingEntity, effectInstance)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", at = @At("TAIL"))
    private void superbwarfare$onAddEffect(MobEffectInstance effectInstance, Entity source, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            MobEffectAddedCallback.EVENT.invoker().onAdded((LivingEntity) (Object) this, effectInstance, source);
        }
    }

    @Inject(method = "heal", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$onLivingHeal(float healAmount, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        LivingHealCallback.Event event = new LivingHealCallback.Event(entity, healAmount);
        LivingHealCallback.EVENT.invoker().onLivingHeal(event);
        ci.cancel();
        if (!event.isCanceled() && event.getAmount() > 0.0F && entity.getHealth() > 0.0F) {
            entity.setHealth(entity.getHealth() + event.getAmount());
        }
    }

    @ModifyVariable(method = "knockback", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private double superbwarfare$modifyKnockbackStrength(double strength) {
        float customStrength = LivingEventHandler.onKnockback((LivingEntity) (Object) this);
        return customStrength >= 0 ? customStrength : strength;
    }

    @Inject(method = "causeFallDamage", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$onCauseFallDamage(float fallDistance, float damageMultiplier,
                                                  DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        if (LivingEventHandler.onEntityFall((LivingEntity) (Object) this, fallDistance, damageMultiplier)) {
            cir.setReturnValue(false);
        }
    }

    @Shadow
    protected Player lastHurtByPlayer;

    @ModifyVariable(method = "dropAllDeathLoot", at = @At(value = "STORE"), ordinal = 0)
    private int superbwarfare$modifyLootingLevel(int lootingLevel, DamageSource damageSource) {
        LivingEntity entity = (LivingEntity) (Object) this;
        LootingLevelCallback.Event lootingEvent = new LootingLevelCallback.Event(entity, damageSource, lootingLevel);
        LootingLevelCallback.EVENT.invoker().onLootingLevel(lootingEvent);
        return lootingEvent.getLootingLevel();
    }

    @Inject(method = "dropAllDeathLoot", at = @At("HEAD"))
    private void superbwarfare$beginCapturingDrops(DamageSource damageSource, CallbackInfo ci) {
        this.superbwarfare$capturedDrops.clear();
        this.superbwarfare$capturingDrops = true;
    }

    @Inject(method = "dropAllDeathLoot", at = @At("RETURN"))
    private void superbwarfare$onDropAllDeathLootReturn(DamageSource damageSource, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        this.superbwarfare$capturingDrops = false;
        LivingDropsCallback.Event dropEvent = new LivingDropsCallback.Event(entity, damageSource, this.superbwarfare$capturedDrops);
        LivingDropsCallback.EVENT.invoker().onLivingDrops(dropEvent);
        if (!dropEvent.isCanceled()) {
            this.superbwarfare$capturedDrops.forEach(entity.level()::addFreshEntity);
        }
        this.superbwarfare$capturedDrops.clear();
    }

    @Redirect(method = "dropExperience",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ExperienceOrb;award(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/Vec3;I)V"))
    private void superbwarfare$onLivingExperienceDrop(ServerLevel level, Vec3 position, int experience) {
        LivingEntity entity = (LivingEntity) (Object) this;
        LivingExperienceDropCallback.Event event = new LivingExperienceDropCallback.Event(entity, this.lastHurtByPlayer, experience);
        LivingExperienceDropCallback.EVENT.invoker().onLivingExperienceDrop(event);
        if (!event.isCanceled()) {
            ExperienceOrb.award(level, position, event.getDroppedExperience());
        }
    }

    @Override
    public boolean superbwarfare$isCapturingDrops() {
        return this.superbwarfare$capturingDrops;
    }

    @Override
    public ItemEntity superbwarfare$captureDrop(ItemStack stack, float yOffset) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (stack.isEmpty() || entity.level().isClientSide) {
            return null;
        }

        ItemEntity itemEntity = new ItemEntity(entity.level(), entity.getX(), entity.getY() + yOffset,
                entity.getZ(), stack);
        itemEntity.setDefaultPickUpDelay();
        this.superbwarfare$capturedDrops.add(itemEntity);
        return itemEntity;
    }

    @Inject(method = "onEffectRemoved", at = @At("HEAD"))
    private void superbwarfare$onMobEffectRemoved(MobEffectInstance effectInstance, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        MobEffectRemovedCallback.EVENT.invoker().onRemoved(entity, effectInstance);
    }
}
