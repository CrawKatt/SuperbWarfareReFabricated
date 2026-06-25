package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.item.weapon.HammerItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ResultSlot.class)
public abstract class ResultSlotMixin {

    @Shadow
    @Final
    private CraftingContainer craftSlots;

    @Inject(method = "onTake", at = @At("TAIL"))
    private void superbwarfare$onItemCrafted(Player player, ItemStack stack, CallbackInfo ci) {
        HammerItem.onItemCrafted(stack, this.craftSlots, player);
    }
}