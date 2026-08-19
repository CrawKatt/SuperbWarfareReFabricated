package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.item.weapon.BeastItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerShieldMixin {

    @Inject(method = "blockUsingShield", at = @At("TAIL"))
    private void superbwarfare$disableShieldWithBeast(LivingEntity attacker, CallbackInfo ci) {
        if (attacker.getMainHandItem().getItem() instanceof BeastItem) {
            ((Player) (Object) this).disableShield(true);
        }
    }
}
