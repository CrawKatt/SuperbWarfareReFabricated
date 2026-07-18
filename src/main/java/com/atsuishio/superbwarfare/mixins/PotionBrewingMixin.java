package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.recipe.ModPotionRecipes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionBrewing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotionBrewing.class)
public class PotionBrewingMixin {

    @Inject(method = "isIngredient", at = @At("HEAD"), cancellable = true)
    private static void superbwarfare$isCustomIngredient(
            ItemStack input, CallbackInfoReturnable<Boolean> cir) {
        if (ModPotionRecipes.isIngredient(input)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "hasMix", at = @At("HEAD"), cancellable = true)
    private static void superbwarfare$hasCustomMix(
            ItemStack input, ItemStack reagent,
            CallbackInfoReturnable<Boolean> cir) {
        if (ModPotionRecipes.hasMix(input, reagent)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "mix", at = @At("HEAD"), cancellable = true)
    private static void superbwarfare$mixCustomPotion(
            ItemStack reagent, ItemStack potion,
            CallbackInfoReturnable<ItemStack> cir) {
        ItemStack result = ModPotionRecipes.mix(potion, reagent);
        if (!result.isEmpty()) {
            cir.setReturnValue(result);
        }
    }
}
