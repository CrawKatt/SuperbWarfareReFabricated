package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.event.LivingEventHandler;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.item.gun.special.BeastGunTestItem;
import com.atsuishio.superbwarfare.item.weapon.BeastItem;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

    @Shadow
    public abstract ItemStack getItem();

    @Inject(method = "playerTouch", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$vehiclePickup(Player player, CallbackInfo ci) {
        if (!LivingEventHandler.onPickup((ItemEntity) (Object) this, player)) {
            ci.cancel();
        }
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$beastCannotBeHurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (this.getItem().getItem() instanceof BeastItem || this.getItem().getItem() instanceof BeastGunTestItem) {
            cir.setReturnValue(false);
        } else if (this.getItem().is(ModItems.CONTAINER)
                && (source.is(DamageTypeTags.IS_EXPLOSION) || source.is(DamageTypes.CACTUS))) {
            cir.setReturnValue(false);
        }
    }
}
