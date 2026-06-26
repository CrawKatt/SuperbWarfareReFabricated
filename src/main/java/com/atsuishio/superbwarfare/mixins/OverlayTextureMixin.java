package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.event.ClientEventHandler;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OverlayTexture.class)
public class OverlayTextureMixin {

    @Inject(method = "u(F)I", at = @At("HEAD"), cancellable = true)
    private static void superbWarfare$u(float u, CallbackInfoReturnable<Integer> cir) {
        if (ClientEventHandler.activeThermalImaging) {
            cir.setReturnValue(15);
        }
    }

    @Inject(method = "v(Z)I", at = @At("HEAD"), cancellable = true)
    private static void superbWarfare$v(boolean hurt, CallbackInfoReturnable<Integer> cir) {
        if (ClientEventHandler.activeThermalImaging) {
            cir.setReturnValue(10);
        }
    }
}
