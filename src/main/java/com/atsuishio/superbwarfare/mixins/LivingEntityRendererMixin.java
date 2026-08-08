package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.client.VehicleClientRenderState;
import com.atsuishio.superbwarfare.client.renderer.special.PhosphorusFireRenderer;
import com.atsuishio.superbwarfare.data.vehicle.VehicleData;
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.atsuishio.superbwarfare.entity.vehicle.utils.VehicleVecUtils;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.joml.Quaterniond;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// From Immersive_Aircraft
@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity> {

    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At("HEAD"), cancellable = true)
    public void render(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, CallbackInfo ci) {
        PhosphorusFireRenderer.render(entity, poseStack, bufferSource);

        if (!(entity instanceof Player player)) return;

        if (VehicleClientRenderState.shouldHideVehiclePassenger(player)) {
            ci.cancel();
        } else if (ClientEventHandler.zoomVehicle && player == Minecraft.getInstance().player) {
            ci.cancel();
        }
    }

    @Inject(method = "setupRotations", at = @At("HEAD"), cancellable = true)
    public void setupRotations(T entity, PoseStack poseStack, float bob, float yBodyRot, float partialTick, float scale, CallbackInfo ci) {
        if (entity.getRootVehicle() != entity && entity.getRootVehicle() instanceof VehicleEntity vehicle) {
            var seats = VehicleData.compute(vehicle).seats();
            int index = vehicle.getSeatIndex(entity);
            if (index < 0 || index >= seats.size()) return;

            ci.cancel();
            var seat = seats.get(index);

            float transformYaw = (float) VehicleVecUtils.getYRotFromVector(vehicle.getTransformDirectionNoOrientation(partialTick, entity));
            var passengerWeaponStationYawRot = Axis.YP.rotationDegrees(-transformYaw);

            Quaterniond quaterniond = vehicle.getRotationFromString(seat.transform, partialTick).mul(new Quaterniond(passengerWeaponStationYawRot));
            Quaternionf quaternionf = new Quaternionf(quaterniond.x, quaterniond.y, quaterniond.z, quaterniond.w);

            poseStack.mulPose(quaternionf);
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - yBodyRot));

            float renderScale = vehicle.getPassengerRenderScale();

            if (Minecraft.getInstance().player != null && ClientEventHandler.zoomVehicle && entity.getRootVehicle() == Minecraft.getInstance().player.getRootVehicle()) {
                renderScale = 0;
            }

            poseStack.scale(renderScale, renderScale, renderScale);
        }
    }

    @Inject(method = "isBodyVisible(Lnet/minecraft/world/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true)
    private void superbWarfare$isBodyVisible(T livingEntity, CallbackInfoReturnable<Boolean> cir) {
        if (ClientEventHandler.activeThermalImaging) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "shouldShowName(Lnet/minecraft/world/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$shouldShowName(T entity, CallbackInfoReturnable<Boolean> cir) {
        if (!ClientEventHandler.shouldRenderNameTag(entity)) {
            cir.setReturnValue(false);
        }
    }
}
