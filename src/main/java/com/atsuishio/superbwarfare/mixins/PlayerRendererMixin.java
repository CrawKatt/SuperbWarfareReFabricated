package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.init.ModEnumExtensions;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.item.gun.GunGeoItem;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
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
    private static void superbwarfare$getArmPose(AbstractClientPlayer player, InteractionHand hand, CallbackInfoReturnable<HumanoidModel.ArmPose> cir) {
        ItemStack stack = hand == InteractionHand.MAIN_HAND ? player.getMainHandItem() : player.getOffhandItem();

        if (stack.getItem() instanceof GunGeoItem gunGeoItem) {
            cir.setReturnValue(gunGeoItem.getArmPose(player, hand, stack));
        } else if (stack.is(ModItems.LUNGE_MINE) && player.getUsedItemHand() == hand) {
            cir.setReturnValue(ModEnumExtensions.Client.getLungeMinePose());
        }
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
        arm.render(poseStack, buffer.getBuffer(RenderType.entitySolid(player.getSkin().texture())), LightTexture.FULL_BRIGHT, OverlayTexture.pack(15, 10));
        sleeve.xRot = 0.0F;
        sleeve.render(poseStack, buffer.getBuffer(RenderType.entityTranslucent(player.getSkin().texture())), LightTexture.FULL_BRIGHT, OverlayTexture.pack(15, 10));
    }
}
