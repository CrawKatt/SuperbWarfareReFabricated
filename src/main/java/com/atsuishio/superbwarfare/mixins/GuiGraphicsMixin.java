package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.client.decorator.ContainerItemDecorator;
import com.atsuishio.superbwarfare.client.decorator.LuckyContainerItemDecorator;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphics.class)
public class GuiGraphicsMixin {

    @Inject(method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At("TAIL"))
    private void superbwarfare$renderContainerItemDecorator(Font font, ItemStack stack, int x, int y, String text, CallbackInfo ci) {
        renderSuperbWarfareDecorators(font, stack, x, y);
    }

    @Unique
    private void renderSuperbWarfareDecorators(Font font, ItemStack stack, int x, int y) {
        GuiGraphics guiGraphics = (GuiGraphics) (Object) this;

        if (new ContainerItemDecorator().render(guiGraphics, font, stack, x, y)) {
            return;
        }

        new LuckyContainerItemDecorator().render(guiGraphics, font, stack, x, y);
    }
}
