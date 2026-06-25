package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.item.weapon.BeastItem;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

    @Shadow
    public abstract ItemStack getItem();

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$beastCannotBeHurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (this.getItem().getItem() instanceof BeastItem) {
            cir.setReturnValue(false);
        }
    }
}