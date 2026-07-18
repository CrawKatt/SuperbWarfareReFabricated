package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.item.Beast;
import com.atsuishio.superbwarfare.item.NetheriteHammer;
import com.atsuishio.superbwarfare.item.gun.GunItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackLegacyIdMixin {
    @Inject(method = "getMaxDamage", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$getStackSensitiveMaxDamage(CallbackInfoReturnable<Integer> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        if (stack.getItem() instanceof GunItem gun) {
            cir.setReturnValue(gun.getMaxDamage(stack));
        }
    }

    @Inject(method = "isDamageableItem", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$isStackSensitiveDamageable(CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        if (stack.getItem() instanceof Beast || stack.getItem() instanceof NetheriteHammer) {
            cir.setReturnValue(false);
            return;
        }

        if (stack.getItem() instanceof GunItem gun) {
            CompoundTag tag = stack.getTag();
            cir.setReturnValue(gun.getMaxDamage(stack) > 0
                    && (tag == null || !tag.getBoolean("Unbreakable")));
        }
    }

    @ModifyArg(
            method = "of",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;<init>(Lnet/minecraft/nbt/CompoundTag;)V"
            ),
            index = 0
    )
    private static CompoundTag superbwarfare$remapLegacyItemId(CompoundTag original) {
        String replacement = switch (original.getString("id")) {
            case "superbwarfare:abekiri" -> "superbwarfare:homemade_shotgun";
            case "superbwarfare:m2hb_blueprint" -> "superbwarfare:m_2_hb_blueprint";
            case "superbwarfare:rocket_70" -> "superbwarfare:small_rocket";
            case "superbwarfare:us_helmet_pastg" -> "superbwarfare:us_helmet_pasgt";
            case "superbwarfare:agm" -> "superbwarfare:large_anti_ground_missile";
            case "superbwarfare:wire_guide_missile" -> "superbwarfare:medium_anti_ground_missile";
            default -> null;
        };

        if (replacement == null) {
            return original;
        }

        CompoundTag remapped = original.copy();
        remapped.putString("id", replacement);
        return remapped;
    }
}
