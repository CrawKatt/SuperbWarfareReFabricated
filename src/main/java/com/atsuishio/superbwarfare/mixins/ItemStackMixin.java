package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.init.ModAttributes;
import com.atsuishio.superbwarfare.init.ModRarities;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public class ItemStackMixin {

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
