package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.event.custom.RenderPlayerCallback;
import com.atsuishio.superbwarfare.item.gun.GeoGunItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin {

    @Inject(method = "getArmPose", at = @At("RETURN"), cancellable = true)
    private static void superbwarfare$getArmPose(AbstractClientPlayer player, InteractionHand hand,
                                                  CallbackInfoReturnable<HumanoidModel.ArmPose> cir) {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.getItem() instanceof GeoGunItem geoGunItem) {
            cir.setReturnValue(geoGunItem.getArmPose(player, hand, stack));
        }
    }

    @Inject(method = "render(Lnet/minecraft/client/player/AbstractClientPlayer;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$onRenderPlayer(AbstractClientPlayer entity, float entityYaw, float partialTick,
                                               PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
                                               CallbackInfo ci) {
        RenderPlayerCallback.Event event = new RenderPlayerCallback.Event(entity);
        RenderPlayerCallback.EVENT.invoker().onRenderPlayer(event);
        if (event.isCanceled()) ci.cancel();
    }

}
