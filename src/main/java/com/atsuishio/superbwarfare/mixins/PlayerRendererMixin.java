package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.item.gun.GeoGunItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin {
    @Inject(method = "getArmPose", at = @At("RETURN"), cancellable = true)
    private static void superbwarfare$getArmPose(
            AbstractClientPlayer player,
            InteractionHand hand,
            CallbackInfoReturnable<HumanoidModel.ArmPose> cir
    ) {
        ItemStack stack = hand == InteractionHand.MAIN_HAND ? player.getMainHandItem() : player.getOffhandItem();

        if (stack.getItem() instanceof GeoGunItem geoGunItem) {
            cir.setReturnValue(geoGunItem.getArmPose(player, hand, stack));
        }
    }
}
