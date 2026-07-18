package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.event.ClientEventHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityClientMixin {

    @Inject(method = "setSprinting(Z)V", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$preventSprintingWhileZoomed(boolean sprinting, CallbackInfo ci) {
        if (sprinting && (Object) this instanceof Player && ClientEventHandler.zoom) {
            ci.cancel();
        }
    }
}
