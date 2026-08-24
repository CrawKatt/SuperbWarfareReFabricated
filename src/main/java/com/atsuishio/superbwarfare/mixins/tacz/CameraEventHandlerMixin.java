package com.atsuishio.superbwarfare.mixins.tacz;

import cn.sh1rocu.simplebedrockmodel.api.event.ViewportEvent;
import com.github.mcmodderanchor.simplebedrockmodel.v1.client.handler.CameraEventHandler;
import com.github.mcmodderanchor.simplebedrockmodel.v1.client.handler.FirstPersonRenderHandler;
import com.tacz.guns.api.item.IGun;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CameraEventHandler.class, remap = false)
public abstract class CameraEventHandlerMixin {

    @Inject(method = "applyLevelCameraAnimation", at = @At("HEAD"), cancellable = true)
    private static void superbWarfare$letTaczHandleItsCamera(ViewportEvent.ComputeCameraAngles event,
                                                             CallbackInfo ci) {
        var animation = FirstPersonRenderHandler.getActiveAnimationInstance();
        if (animation != null && animation.currentItem().getItem() instanceof IGun) {
            ci.cancel();
        }
    }
}
