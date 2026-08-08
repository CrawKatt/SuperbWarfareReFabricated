package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.init.ModMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.MilkBucketItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(MilkBucketItem.class)
public class MilkBucketItemMixin {

    @Redirect(
            method = "finishUsingItem",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;removeAllEffects()Z")
    )
    private boolean superbwarfare$preservePhosphorusFire(LivingEntity entity) {
        boolean removed = false;
        for (MobEffectInstance effect : List.copyOf(entity.getActiveEffects())) {
            if (effect.getEffect() != ModMobEffects.PHOSPHORUS_FIRE) {
                removed |= entity.removeEffect(effect.getEffect());
            }
        }
        return removed;
    }
}
