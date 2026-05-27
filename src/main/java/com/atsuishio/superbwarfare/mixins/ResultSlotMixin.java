package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.item.Hammer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ResultSlot.class)
public class ResultSlotMixin {
    @Shadow
    @Final
    private Container craftSlots;

    @Inject(method = "onTake", at = @At("HEAD"))
    private void superbwarfare$beforeTake(Player player, ItemStack stack, CallbackInfo ci) {
        for (int i = 0; i < this.craftSlots.getContainerSize(); i++) {
            ItemStack input = this.craftSlots.getItem(i);

            if (input.getItem() instanceof Hammer hammer) {
                this.craftSlots.setItem(i, hammer.getCraftingRemainingItem(input));
            }
        }
    }

    @Inject(method = "onTake", at = @At("TAIL"))
    private void superbwarfare$afterTake(Player player, ItemStack stack, CallbackInfo ci) {
        Hammer.onItemCrafted(stack, this.craftSlots, player);
    }

    @Inject(method = "onTake", at = @At("TAIL"))
    private void superbwarfare$onItemCrafted(Player player, ItemStack stack, CallbackInfo ci) {
        Hammer.onItemCrafted(stack, this.craftSlots, player);
    }
}
