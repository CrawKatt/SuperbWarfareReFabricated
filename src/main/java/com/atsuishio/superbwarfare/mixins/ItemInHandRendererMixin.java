package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.client.VehicleClientRenderState;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.item.LungeMine;
import com.atsuishio.superbwarfare.item.gun.GunItem;
import com.atsuishio.superbwarfare.item.misc.MonitorItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {

    @Shadow
    private ItemStack mainHandItem;

    @Inject(method = "renderHandsWithItems", at = @At("HEAD"), cancellable = true)
    private void superbWarfare$renderHandsWithItems(
            float partialTick, PoseStack poseStack, MultiBufferSource.BufferSource bufferSource,
            LocalPlayer player, int packedLight, CallbackInfo ci
    ) {
        ClientEventHandler.handleWeaponTurn(partialTick);

        if (VehicleClientRenderState.shouldHideHandsAndHotbar(player)) {
            ci.cancel();
        }
    }

    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;matches(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"
            )
    )
    private boolean superbWarfare$monitorMatchesForEquipAnimation(ItemStack oldStack, ItemStack newStack) {
        if (!oldStack.isEmpty() && !newStack.isEmpty()
                && (oldStack.getItem() instanceof GunItem
                || oldStack.getItem() instanceof MonitorItem
                || oldStack.getItem() instanceof LungeMine)) {
            return true;
        }

        return ItemStack.matches(oldStack, newStack);
    }

    @ModifyVariable(method = "renderArmWithItem", at = @At("HEAD"), argsOnly = true, index = 5)
    private float superbwarfare$removeGunSwing(float value) {
        return mainHandItem.getItem() instanceof GunItem ? 0f : value;
    }

    @Inject(method = "renderArmWithItem", at = @At("HEAD"), cancellable = true)
    private void superbWarfare$renderArmWithItem(
            AbstractClientPlayer player, float partialTick, float pitch,
            InteractionHand hand, float swingProgress, ItemStack stack,
            float equipProgress, PoseStack poseStack,
            MultiBufferSource bufferSource, int packedLight,
            CallbackInfo ci
    ) {
        if (ClientEventHandler.shouldCancelHandRender(hand)) {
            ci.cancel();
        }
    }
}
