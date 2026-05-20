package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.client.VehicleClientRenderState;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {

    @Inject(method = "renderItemHotbar", at = @At("HEAD"), cancellable = true)
    private void superbWarfare$renderItemHotbar(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (VehicleClientRenderState.shouldHideHandsAndHotbar(Minecraft.getInstance().player)) {
            ci.cancel();
        }
    }

    @Inject(method = "renderSelectedItemName", at = @At("HEAD"), cancellable = true)
    private void superbWarfare$renderSelectedItemName(GuiGraphics guiGraphics, CallbackInfo ci) {
        if (VehicleClientRenderState.shouldHideHandsAndHotbar(Minecraft.getInstance().player)) {
            ci.cancel();
        }
    }
}
