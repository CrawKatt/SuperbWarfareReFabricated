package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.item.Beast;
import com.atsuishio.superbwarfare.item.NetheriteHammer;
import com.atsuishio.superbwarfare.item.gun.GunItem;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.stream.Collectors;

@Mixin(GrindstoneMenu.class)
public class GrindstoneMenuMixin {

    @Shadow
    @Final
    private Container resultSlots;

    @Shadow
    @Final
    private Container repairSlots;

    @Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$createStackAwareResult(CallbackInfo ci) {
        ItemStack first = this.repairSlots.getItem(0);
        ItemStack second = this.repairSlots.getItem(1);

        // A single stack follows vanilla exactly. Stack-aware durability only
        // changes Forge's two-stack repair path.
        if (first.isEmpty() || second.isEmpty()
                || !superbwarfare$isStackAware(first)
                && !superbwarfare$isStackAware(second)) {
            return;
        }

        ItemStack result = ItemStack.EMPTY;
        if (first.getCount() == 1
                && second.getCount() == 1
                && first.is(second.getItem())) {
            int maxDamage = first.getMaxDamage();
            int firstRemaining = maxDamage - first.getDamageValue();
            int secondRemaining = maxDamage - second.getDamageValue();
            int repairedDamage = Math.max(maxDamage
                    - (firstRemaining + secondRemaining + maxDamage * 5 / 100), 0);

            ItemStack merged = superbwarfare$mergeEnchants(first, second);
            boolean repairable = superbwarfare$isRepairable(merged);
            int resultCount = 1;
            if (!repairable) {
                repairedDamage = first.getDamageValue();
            }

            if (!merged.isDamageableItem() || !repairable) {
                if (ItemStack.matches(first, second)) {
                    resultCount = 2;
                } else {
                    superbwarfare$setResult(ItemStack.EMPTY);
                    ci.cancel();
                    return;
                }
            }

            if (resultCount <= merged.getMaxStackSize()) {
                result = superbwarfare$removeNonCurses(merged, repairedDamage, resultCount);
            }
        }

        superbwarfare$setResult(result);
        ci.cancel();
    }

    private void superbwarfare$setResult(ItemStack result) {
        this.resultSlots.setItem(0, result);
        ((GrindstoneMenu) (Object) this).broadcastChanges();
    }

    private static boolean superbwarfare$isStackAware(ItemStack stack) {
        return stack.getItem() instanceof GunItem
                || stack.getItem() instanceof Beast
                || stack.getItem() instanceof NetheriteHammer;
    }

    private static boolean superbwarfare$isRepairable(ItemStack stack) {
        if (stack.getItem() instanceof Beast || stack.getItem() instanceof NetheriteHammer) {
            return false;
        }
        if (stack.getItem() instanceof GunItem gun) {
            return gun.getMaxDamage(stack) > 0;
        }
        return stack.isDamageableItem();
    }

    private static ItemStack superbwarfare$mergeEnchants(ItemStack copyTo, ItemStack copyFrom) {
        ItemStack result = copyTo.copy();
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(copyFrom);
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            Enchantment enchantment = entry.getKey();
            if (!enchantment.isCurse()
                    || EnchantmentHelper.getItemEnchantmentLevel(enchantment, result) == 0) {
                result.enchant(enchantment, entry.getValue());
            }
        }
        return result;
    }

    private static ItemStack superbwarfare$removeNonCurses(ItemStack stack, int damage, int count) {
        ItemStack result = stack.copyWithCount(count);
        result.removeTagKey("Enchantments");
        result.removeTagKey("StoredEnchantments");
        if (damage > 0) {
            result.setDamageValue(damage);
        } else {
            result.removeTagKey("Damage");
        }

        Map<Enchantment, Integer> curses = EnchantmentHelper.getEnchantments(stack)
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().isCurse())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        EnchantmentHelper.setEnchantments(curses, result);
        result.setRepairCost(0);
        if (result.is(Items.ENCHANTED_BOOK) && curses.isEmpty()) {
            result = new ItemStack(Items.BOOK);
            if (stack.hasCustomHoverName()) {
                result.setHoverName(stack.getHoverName());
            }
        }

        for (int i = 0; i < curses.size(); i++) {
            result.setRepairCost(AnvilMenu.calculateIncreasedRepairCost(result.getBaseRepairCost()));
        }
        return result;
    }
}
