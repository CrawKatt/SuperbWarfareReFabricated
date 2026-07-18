package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.event.LivingEventHandler;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {

    @Inject(method = "playerTouch", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$onPlayerPickup(Player player, CallbackInfo ci) {
        ItemEntity item = (ItemEntity) (Object) this;
        if (item.level().isClientSide || item.hasPickUpDelay()) {
            return;
        }

        if (LivingEventHandler.onPickup(player, item)) {
            ci.cancel();
        }
    }
}
