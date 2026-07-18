package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.client.PoseTool;
import com.atsuishio.superbwarfare.event.custom.RenderPlayerCallback;
import com.atsuishio.superbwarfare.item.LungeMine;
import com.atsuishio.superbwarfare.item.gun.GunGeoItem;
import com.atsuishio.superbwarfare.item.gun.handgun.AureliaSceptreItem;
import com.atsuishio.superbwarfare.item.gun.machinegun.M2HBItem;
import com.atsuishio.superbwarfare.item.gun.machinegun.MinigunItem;
import com.atsuishio.superbwarfare.item.gun.special.BocekItem;
import com.atsuishio.superbwarfare.item.gun.special.RepairToolItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {
    @Inject(method = "getArmPose", at = @At("RETURN"), cancellable = true)
    private static void superbwarfare$getArmPose(AbstractClientPlayer player, InteractionHand hand, CallbackInfoReturnable<HumanoidModel.ArmPose> cir) {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.getItem() instanceof BocekItem) {
            cir.setReturnValue(HumanoidModel.ArmPose.BOW_AND_ARROW);
            return;
        }

        if (superbwarfare$usesCustomArmPose(stack)) {
            cir.setReturnValue(HumanoidModel.ArmPose.EMPTY);
            return;
        }

        if (stack.getItem() instanceof GunGeoItem) {
            cir.setReturnValue(PoseTool.pose(player, hand, stack));
        }
    }

    @Unique
    private static boolean superbwarfare$usesCustomArmPose(ItemStack stack) {
        return stack.getItem() instanceof LungeMine
                || stack.getItem() instanceof AureliaSceptreItem
                || stack.getItem() instanceof M2HBItem
                || stack.getItem() instanceof MinigunItem
                || stack.getItem() instanceof RepairToolItem;
    }

    @Inject(method = "render(Lnet/minecraft/client/player/AbstractClientPlayer;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$onRenderPlayer(AbstractClientPlayer entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, CallbackInfo ci) {
        RenderPlayerCallback.Event event = new RenderPlayerCallback.Event(entity);
        RenderPlayerCallback.EVENT.invoker().onRenderPlayer(event);
        if (event.isCanceled()) {
            ci.cancel();
        }
    }
}
