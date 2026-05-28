package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.item.ReequipAnimationHook;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {

    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;matches(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"
            )
    )
    private boolean superbwarfare$shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack) {
        if (newStack.getItem() instanceof ReequipAnimationHook hook) {
            return !hook.shouldCauseReequipAnimation(oldStack, newStack, false);
        }

        if (oldStack.getItem() instanceof ReequipAnimationHook hook) {
            return !hook.shouldCauseReequipAnimation(oldStack, newStack, false);
        }

        return ItemStack.matches(oldStack, newStack);
    }
}
