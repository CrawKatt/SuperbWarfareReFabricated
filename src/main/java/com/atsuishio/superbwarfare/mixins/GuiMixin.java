package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.client.ClientRenderHandler;
import com.atsuishio.superbwarfare.event.custom.RenderGuiOverlayCallback;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;enableBlend()V",
                    ordinal = 0,
                    shift = At.Shift.AFTER
            )
    )
    private void superbwarfare$renderOverlaysBelowVanilla(GuiGraphics guiGraphics, float partialTick, CallbackInfo ci) {
        ClientRenderHandler.renderOverlays(guiGraphics, partialTick);
        RenderSystem.enableBlend();
    }

    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$onRenderCrosshair(GuiGraphics guiGraphics, CallbackInfo ci) {
        RenderGuiOverlayCallback.Event event = new RenderGuiOverlayCallback.Event(RenderGuiOverlayCallback.Overlay.CROSSHAIR);
        RenderGuiOverlayCallback.EVENT.invoker().onRenderOverlay(event);
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$onRenderHotbar(float partialTick, GuiGraphics guiGraphics, CallbackInfo ci) {
        RenderGuiOverlayCallback.Event event = new RenderGuiOverlayCallback.Event(RenderGuiOverlayCallback.Overlay.HOTBAR);
        RenderGuiOverlayCallback.EVENT.invoker().onRenderOverlay(event);
        if (event.isCanceled()) {
            ci.cancel();
        }
    }
}
