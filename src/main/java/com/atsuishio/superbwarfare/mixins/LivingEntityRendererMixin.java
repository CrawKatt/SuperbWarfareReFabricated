package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.client.VehicleClientRenderState;
import com.atsuishio.superbwarfare.client.renderer.special.PhosphorusFireRenderer;
import com.atsuishio.superbwarfare.data.vehicle.VehicleData;
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

    @Inject(method = "setupRotations", at = @At("TAIL"))
    public void setupRotations(T entity, PoseStack poseStack, float bob, float yBodyRot, float partialTick, float scale, CallbackInfo ci) {
        if (entity.getRootVehicle() != entity && entity.getRootVehicle() instanceof VehicleEntity vehicle) {
            float a = Mth.wrapDegrees(Mth.lerp(partialTick, entity.yBodyRotO, entity.yBodyRot) - Mth.lerp(partialTick, vehicle.yRotO, vehicle.getYRot()));

            var seats = VehicleData.compute(vehicle).seats();
            int index = vehicle.getSeatIndex(entity);
            if (index < 0 || index >= seats.size()) return;

            var seat = seats.get(index);
            if (seat.transform.equals("VehicleFlat")) return;

            if (entity.yBodyRot == vehicle.getYRot()) {
                a = 0;
            }

            float r = (Mth.abs(a) - 90f) / 90f;
            float r2;
            if (Mth.abs(a) <= 90f) {
                r2 = a / 90f;
            } else {
                if (a < 0) {
                    r2 = -(180f + a) / 90f;
                } else {
                    r2 = (180f - a) / 90f;
                }
            }

            poseStack.mulPose(Axis.XP.rotationDegrees(r * vehicle.getViewXRot(partialTick) - r2 * vehicle.getRoll(partialTick)));
            poseStack.mulPose(Axis.ZP.rotationDegrees(r * vehicle.getRoll(partialTick) + r2 * vehicle.getViewXRot(partialTick)));
        }
    }
}
