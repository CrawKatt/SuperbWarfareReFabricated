package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.event.custom.RenderPlayerCallback;
import com.atsuishio.superbwarfare.item.LungeMine;
import com.atsuishio.superbwarfare.item.gun.GunGeoItem;
import com.atsuishio.superbwarfare.item.gun.GeoGunItemV2;
import com.atsuishio.superbwarfare.client.PoseTool;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.item.gun.launcher.SuperStarShooterItem;
import com.atsuishio.superbwarfare.item.gun.machinegun.M2HBItem;
import com.atsuishio.superbwarfare.item.gun.machinegun.MinigunItem;
import com.atsuishio.superbwarfare.item.gun.special.BocekItem;
import com.atsuishio.superbwarfare.item.gun.special.RepairToolItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    protected PlayerRendererMixin(EntityRendererProvider.Context context, PlayerModel<AbstractClientPlayer> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Shadow
    protected abstract void setModelProperties(AbstractClientPlayer player);

    @Inject(method = "getArmPose", at = @At("RETURN"), cancellable = true)
    private static void superbwarfare$getArmPose(AbstractClientPlayer player, InteractionHand hand,
                                                  CallbackInfoReturnable<HumanoidModel.ArmPose> cir) {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.getItem() instanceof BocekItem) {
            cir.setReturnValue(HumanoidModel.ArmPose.BOW_AND_ARROW);
        } else if (superbwarfare$usesCustomArmPose(stack)) {
            cir.setReturnValue(HumanoidModel.ArmPose.EMPTY);
        } else if (stack.getItem() instanceof GeoGunItemV2) {
            cir.setReturnValue(PoseTool.pose(player, hand, stack));
        } else if (stack.getItem() instanceof GunGeoItem gun) {
            cir.setReturnValue(gun.getArmPose(player, hand, stack));
        }
    }

    @Unique
    private static boolean superbwarfare$usesCustomArmPose(ItemStack stack) {
        return stack.getItem() instanceof LungeMine
                || stack.getItem() instanceof M2HBItem
                || stack.getItem() instanceof MinigunItem
                || stack.getItem() instanceof SuperStarShooterItem
                || stack.getItem() instanceof RepairToolItem;
    }

    @Inject(method = "render(Lnet/minecraft/client/player/AbstractClientPlayer;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$onRenderPlayer(AbstractClientPlayer entity, float entityYaw, float partialTick,
                                               PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
                                               CallbackInfo ci) {
        RenderPlayerCallback.Event event = new RenderPlayerCallback.Event(entity);
        RenderPlayerCallback.EVENT.invoker().onRenderPlayer(event);
        if (event.isCanceled()) ci.cancel();
    }

    @Inject(
            method = "renderHand(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/player/AbstractClientPlayer;Lnet/minecraft/client/model/geom/ModelPart;Lnet/minecraft/client/model/geom/ModelPart;)V",
            at = @At("RETURN")
    )
    private void superbwarfare$renderThermalHand(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int combinedLight,
            AbstractClientPlayer player,
            ModelPart arm,
            ModelPart sleeve,
            CallbackInfo ci
    ) {
        if (!ClientEventHandler.activeThermalImaging) return;

        PlayerModel<AbstractClientPlayer> playerModel = this.getModel();
        this.setModelProperties(player);
        playerModel.attackTime = 0.0F;
        playerModel.crouching = false;
        playerModel.swimAmount = 0.0F;
        playerModel.setupAnim(player, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);

        arm.xRot = 0.0F;
        arm.render(poseStack, buffer.getBuffer(RenderType.entitySolid(player.getSkinTextureLocation())), LightTexture.FULL_BRIGHT, OverlayTexture.pack(15, 10));
        sleeve.xRot = 0.0F;
        sleeve.render(poseStack, buffer.getBuffer(RenderType.entityTranslucent(player.getSkinTextureLocation())), LightTexture.FULL_BRIGHT, OverlayTexture.pack(15, 10));
    }
}
