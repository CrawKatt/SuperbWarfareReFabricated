package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.init.ModRarities;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemStack.class)
public class ItemStackRarityMixin {

    @Redirect(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/MutableComponent;withStyle(Lnet/minecraft/ChatFormatting;)Lnet/minecraft/network/chat/MutableComponent;", ordinal = 0))
    private MutableComponent superbwarfare$styleTooltipRarity(MutableComponent component, ChatFormatting formatting) {
        return superbwarfare$styleRarity(component, formatting);
    }

    @Redirect(method = "getDisplayName", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/MutableComponent;withStyle(Lnet/minecraft/ChatFormatting;)Lnet/minecraft/network/chat/MutableComponent;", ordinal = 1))
    private MutableComponent superbwarfare$styleDisplayRarity(MutableComponent component, ChatFormatting formatting) {
        return superbwarfare$styleRarity(component, formatting);
    }

    @Unique
    private MutableComponent superbwarfare$styleRarity(MutableComponent component, ChatFormatting formatting) {
        if (((ItemStack) (Object) this).getRarity() == ModRarities.VIRTUAL) {
            return component.withStyle(style -> style.withColor(0xFF9AAF));
        }
        return component.withStyle(formatting);
    }
}
