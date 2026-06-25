package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModTags;
import com.atsuishio.superbwarfare.item.material.VehicleResetKitItem;
import com.atsuishio.superbwarfare.item.weapon.HammerItem;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin {

    @Inject(method = "getRemainingItemsFor", at = @At("RETURN"), cancellable = true)
    private <I extends RecipeInput, T extends Recipe<I>> void superbwarfare$recipeRemainders(
            RecipeType<T> recipeType,
            I recipeInput,
            Level level,
            CallbackInfoReturnable<NonNullList<ItemStack>> cir
    ) {
        if (!RecipeType.CRAFTING.equals(recipeType)) return;

        var remainingItems = cir.getReturnValue();
        boolean changed = false;

        for (int i = 0; i < recipeInput.size() && i < remainingItems.size(); i++) {
            var itemStack = recipeInput.getItem(i);

            if (itemStack.is(ModTags.Items.HAMMER) && remainingItems.get(i).isEmpty()) {
                remainingItems.set(i, HammerItem.getCraftingRemainingStack(itemStack));
                changed = true;
            }

            if (itemStack.is(ModItems.VEHICLE_RESET_KIT) && remainingItems.get(i).isEmpty()) {
                remainingItems.set(i, VehicleResetKitItem.getCraftingRemainingItem(itemStack));
                changed = true;
            }
        }

        if (changed) {
            cir.setReturnValue(remainingItems);
        }
    }
}