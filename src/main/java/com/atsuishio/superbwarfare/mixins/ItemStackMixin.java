package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.init.ModAttributes;
import com.atsuishio.superbwarfare.init.ModRarities;
import com.atsuishio.superbwarfare.item.ItemDamageExtension;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Unique
    private LivingEntity superbwarfare$damageEntity;

    @WrapMethod(method = "hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V")
    private void superbwarfare$withDamageEntity(int amount, LivingEntity entity, EquipmentSlot slot, Operation<Void> original) {
        LivingEntity previous = superbwarfare$damageEntity;
        superbwarfare$damageEntity = entity;
        try {
            original.call(amount, entity, slot);
        } finally {
            superbwarfare$damageEntity = previous;
        }
    }

    @ModifyVariable(method = "hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/server/level/ServerPlayer;Ljava/util/function/Consumer;)V",
            at = @At("HEAD"), argsOnly = true)
    private int superbwarfare$damageItem(int amount, int originalAmount, ServerLevel level, ServerPlayer player, Consumer<Item> onBroken) {
        ItemStack stack = (ItemStack) (Object) this;
        if (stack.isDamageableItem() && stack.getItem() instanceof ItemDamageExtension extension) {
            return extension.damageItem(stack, amount, superbwarfare$damageEntity != null ? superbwarfare$damageEntity : player, onBroken);
        }
        return amount;
    }

    @Inject(method = "getMaxDamage", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$getMaxDamage(CallbackInfoReturnable<Integer> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        if (stack.getItem() instanceof ItemDamageExtension extension) {
            cir.setReturnValue(extension.getMaxDamage(stack));
        }
    }

    @Inject(method = "isDamageableItem", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$isDamageable(CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        if (stack.getItem() instanceof ItemDamageExtension extension) {
            cir.setReturnValue(extension.isDamageable(stack));
        }
    }

    @Redirect(
            method = {
                    "forEachModifier(Lnet/minecraft/world/entity/EquipmentSlotGroup;Ljava/util/function/BiConsumer;)V",
                    "forEachModifier(Lnet/minecraft/world/entity/EquipmentSlot;Ljava/util/function/BiConsumer;)V"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/Item;getDefaultAttributeModifiers()Lnet/minecraft/world/item/component/ItemAttributeModifiers;"
            )
    )
    private ItemAttributeModifiers superbwarfare$durabilityScaledAttributes(Item item) {
        ItemAttributeModifiers modifiers = item.getDefaultAttributeModifiers();
        ItemStack stack = (ItemStack) (Object) this;
        if (stack.getDamageValue() <= 0) return modifiers;

        double scale = Math.max(0.0, 1.0 - (double) stack.getDamageValue() / stack.getMaxDamage());
        return new ItemAttributeModifiers(modifiers.modifiers().stream().map(entry -> {
            AttributeModifier modifier = entry.modifier();
            if (entry.attribute().value() != ModAttributes.BULLET_RESISTANCE || !modifier.is(Mod.ATTRIBUTE_MODIFIER)) {
                return entry;
            }
            return new ItemAttributeModifiers.Entry(
                    entry.attribute(),
                    new AttributeModifier(modifier.id(), modifier.amount() * scale, modifier.operation()),
                    entry.slot()
            );
        }).toList(), modifiers.showInTooltip());
    }

    @Inject(method = "getTooltipLines", at = @At("RETURN"))
    private void superbwarfare$virtualRarityColor(
            Item.TooltipContext context,
            Player player,
            TooltipFlag flag,
            CallbackInfoReturnable<List<Component>> cir
    ) {
        ItemStack stack = (ItemStack) (Object) this;
        List<Component> lines = cir.getReturnValue();
        if (stack.getRarity() == ModRarities.VIRTUAL && !lines.isEmpty()) {
            lines.set(0, lines.get(0).copy().withStyle(style -> style.withColor(0xFF9AAF)));
        }
    }
}
