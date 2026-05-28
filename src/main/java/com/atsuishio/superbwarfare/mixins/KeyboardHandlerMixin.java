package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.event.custom.KeyInputCallback;
import net.minecraft.client.KeyboardHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {

    @Inject(method = "keyPress", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$onKeyInput(long window, int key, int scanCode, int action, int modifiers, CallbackInfo ci) {
        KeyInputCallback.Event event = new KeyInputCallback.Event(window, key, scanCode, action, modifiers);
        KeyInputCallback.EVENT.invoker().interact(event);
        if (event.isCanceled()) {
            ci.cancel();
        }
    }
}
