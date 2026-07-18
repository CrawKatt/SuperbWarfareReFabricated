package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.item.Beast;
import com.atsuishio.superbwarfare.item.NetheriteHammer;
import com.atsuishio.superbwarfare.item.gun.GunItem;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RepairItemRecipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;

/**
 * Ports Forge's stack-aware repair recipe behavior for the items whose
 * durability/repairability cannot be represented by vanilla's Item fields.
 */
@Mixin(RepairItemRecipe.class)
public class RepairItemRecipeMixin {

    @Inject(method = "matches", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$matchStackAwareItems(
            CraftingContainer container, Level level,
            CallbackInfoReturnable<Boolean> cir) {
        List<ItemStack> inputs = superbwarfare$getInputs(container);
        if (!superbwarfare$containsStackAwareItem(inputs)) {
            return;
        }

        cir.setReturnValue(superbwarfare$canRepair(inputs));
    }

    @Inject(method = "assemble", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$assembleStackAwareItems(
            CraftingContainer container, RegistryAccess registryAccess,
            CallbackInfoReturnable<ItemStack> cir) {
        List<ItemStack> inputs = superbwarfare$getInputs(container);
        if (!superbwarfare$containsStackAwareItem(inputs)) {
            return;
        }

        if (!superbwarfare$canRepair(inputs)) {
            cir.setReturnValue(ItemStack.EMPTY);
            return;
        }

        ItemStack first = inputs.get(0);
        ItemStack second = inputs.get(1);
        int maxDamage = first.getMaxDamage();
        int firstRemaining = maxDamage - first.getDamageValue();
        int secondRemaining = maxDamage - second.getDamageValue();
        int repairedDamage = maxDamage
                - (firstRemaining + secondRemaining + maxDamage * 5 / 100);

        ItemStack result = new ItemStack(first.getItem());
        result.setDamageValue(Math.max(repairedDamage, 0));

        Map<Enchantment, Integer> curses = Maps.newHashMap();
        Map<Enchantment, Integer> firstEnchantments = EnchantmentHelper.getEnchantments(first);
        Map<Enchantment, Integer> secondEnchantments = EnchantmentHelper.getEnchantments(second);
        BuiltInRegistries.ENCHANTMENT.stream().filter(Enchantment::isCurse).forEach(enchantment -> {
            int level = Math.max(
                    firstEnchantments.getOrDefault(enchantment, 0),
                    secondEnchantments.getOrDefault(enchantment, 0));
            if (level > 0) {
                curses.put(enchantment, level);
            }
        });
        if (!curses.isEmpty()) {
            EnchantmentHelper.setEnchantments(curses, result);
        }

        cir.setReturnValue(result);
    }

    private static List<ItemStack> superbwarfare$getInputs(CraftingContainer container) {
        List<ItemStack> inputs = Lists.newArrayList();
        for (int slot = 0; slot < container.getContainerSize(); slot++) {
            ItemStack stack = container.getItem(slot);
            if (!stack.isEmpty()) {
                inputs.add(stack);
            }
        }
        return inputs;
    }

    private static boolean superbwarfare$containsStackAwareItem(List<ItemStack> inputs) {
        return inputs.stream().anyMatch(stack ->
                stack.getItem() instanceof GunItem
                        || stack.getItem() instanceof Beast
                        || stack.getItem() instanceof NetheriteHammer);
    }

    private static boolean superbwarfare$canRepair(List<ItemStack> inputs) {
        if (inputs.size() != 2) {
            return false;
        }

        ItemStack first = inputs.get(0);
        ItemStack second = inputs.get(1);
        return first.is(second.getItem())
                && first.getCount() == 1
                && second.getCount() == 1
                && superbwarfare$isRepairable(first);
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
}
