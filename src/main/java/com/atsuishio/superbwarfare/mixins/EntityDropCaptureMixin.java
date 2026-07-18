package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.entity.mixin.LivingDropsCapture;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityDropCaptureMixin {

    @Inject(method = "spawnAtLocation(Lnet/minecraft/world/item/ItemStack;F)Lnet/minecraft/world/entity/item/ItemEntity;",
            at = @At("HEAD"), cancellable = true)
    private void superbwarfare$captureLivingDrop(ItemStack stack, float yOffset,
                                                  CallbackInfoReturnable<ItemEntity> cir) {
        if ((Object) this instanceof LivingDropsCapture capture && capture.superbwarfare$isCapturingDrops()) {
            cir.setReturnValue(capture.superbwarfare$captureDrop(stack, yOffset));
        }
    }
}
