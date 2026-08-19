package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.item.weapon.MilitaryShovelItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {

    @Shadow
    public EnchantmentCategory category;

    @Inject(method = "canEnchant", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$canEnchantMilitaryShovel(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.getItem() instanceof MilitaryShovelItem) {
            cir.setReturnValue(category == EnchantmentCategory.BREAKABLE
                    || category == EnchantmentCategory.VANISHABLE
                    || category == EnchantmentCategory.DIGGER
                    || category == EnchantmentCategory.WEAPON);
        }
    }
}
