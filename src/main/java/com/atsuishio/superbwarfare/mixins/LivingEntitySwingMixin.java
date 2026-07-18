package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.event.custom.LivingDeathCallback;
import com.atsuishio.superbwarfare.item.EntitySwingHook;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntitySwingMixin {
    @Inject(method = "swing(Lnet/minecraft/world/InteractionHand;Z)V", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$onSwing(InteractionHand hand, boolean updateSelf, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        ItemStack stack = entity.getItemInHand(hand);

        if (stack.getItem() instanceof EntitySwingHook hook && hook.onEntitySwing(stack, entity)) {
            ci.cancel();
        }
    }

    @Inject(method = "die", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$onLivingDeath(DamageSource source, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        LivingDeathCallback.Event event = new LivingDeathCallback.Event(entity, source);
        LivingDeathCallback.EVENT.invoker().onLivingDeath(event);

        if (event.isCanceled()) {
            ci.cancel();
        }
    }
}
