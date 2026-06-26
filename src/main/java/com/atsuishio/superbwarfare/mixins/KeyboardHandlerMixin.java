package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.event.ClickEventHandler;
import net.minecraft.client.KeyboardHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {

    @Inject(method = "keyPress(JIIII)V", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$onKeyPressed(long window, int keyCode, int scanCode, int action, int modifiers, CallbackInfo ci) {
        if (ClickEventHandler.onKeyPressed(keyCode, scanCode, action, modifiers)) {
            ci.cancel();
        }
    }
}