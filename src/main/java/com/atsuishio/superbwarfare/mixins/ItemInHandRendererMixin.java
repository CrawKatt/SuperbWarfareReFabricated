package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.item.ReequipAnimationHook;
import com.atsuishio.superbwarfare.item.gun.GunItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {

    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;matches(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"
            )
    )
    private boolean superbwarfare$shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack) {
        if (newStack.getItem() instanceof ReequipAnimationHook hook) {
            return !hook.shouldCauseReequipAnimation(oldStack, newStack, false);
        }

        if (oldStack.getItem() instanceof ReequipAnimationHook hook) {
            return !hook.shouldCauseReequipAnimation(oldStack, newStack, false);
        }

        return ItemStack.matches(oldStack, newStack);
    }

    @Inject(method = "renderArmWithItem", at = @At("HEAD"), cancellable = true)
    private void superbWarfare$renderArmWithItem(
            AbstractClientPlayer player, float partialTick, float pitch,
            InteractionHand hand, float swingProgress, ItemStack stack,
            float equipProgress, PoseStack poseStack,
            MultiBufferSource bufferSource, int packedLight,
            CallbackInfo ci
    ) {
        HumanoidArm mainArm = player.getMainArm();
        InteractionHand rightInteractionHand = mainArm == HumanoidArm.RIGHT ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        InteractionHand leftInteractionHand = mainArm == HumanoidArm.RIGHT ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        ItemStack rightHandItem = player.getItemInHand(rightInteractionHand);

        if (hand == leftInteractionHand && rightHandItem.getItem() instanceof GunItem) {
            ci.cancel();
            return;
        }

        if (hand == rightInteractionHand && rightHandItem.getItem() instanceof GunItem && ClientEventHandler.drawTime > 0.15) {
            ci.cancel();
            return;
        }

        if (!(stack.getItem() instanceof GunItem)) {
            return;
        }

        boolean mainHand = hand == InteractionHand.MAIN_HAND;
        HumanoidArm arm = mainHand ? mainArm : mainArm.getOpposite();
        boolean rightArm = arm == HumanoidArm.RIGHT;
        ItemDisplayContext displayContext = rightArm ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND;

        poseStack.pushPose();
        int side = rightArm ? 1 : -1;
        float stableEquipProgress = 0.0F;
        poseStack.translate(side * 0.56F, -0.52F + stableEquipProgress * -0.6F, -0.72F);

        ((ItemInHandRenderer) (Object) this).renderItem(player, stack, displayContext, !rightArm, poseStack, bufferSource, packedLight);
        poseStack.popPose();
        ci.cancel();
    }
}
