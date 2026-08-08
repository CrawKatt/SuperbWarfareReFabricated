package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.atsuishio.superbwarfare.init.ModEnumExtensions;
import com.atsuishio.superbwarfare.item.gun.GunItem;
import com.atsuishio.superbwarfare.item.trinket.ParachuteItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin<T extends LivingEntity> {

    @Shadow
    @Final
    public ModelPart leftArm;

    @Shadow
    @Final
    public ModelPart rightArm;

    @Shadow
    @Final
    public ModelPart leftLeg;

    @Shadow
    @Final
    public ModelPart rightLeg;

    @Shadow
    @Final
    public ModelPart body;

    @Shadow
    @Final
    public ModelPart head;

    @Shadow
    @Final
    public ModelPart hat;

    @Inject(method = "poseRightArm(Lnet/minecraft/world/entity/LivingEntity;)V", at = @At("TAIL"))
    private void superbwarfare$applyCustomArmPose(T entity, CallbackInfo ci) {
        ModEnumExtensions.Client.applyArmPose((HumanoidModel<?>) (Object) this);
    }

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    private void superbwarfare$setupAnim(T livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks,
                                         float netHeadYaw, float headPitch, CallbackInfo ci) {
        if (ParachuteItem.isParachuteOpen(livingEntity)) {
            this.leftArm.xRot = -180 * Mth.DEG_TO_RAD;
            this.rightArm.xRot = -180 * Mth.DEG_TO_RAD;

            this.leftArm.yRot = -15 * Mth.DEG_TO_RAD;
            this.rightArm.yRot = 15 * Mth.DEG_TO_RAD;

            this.leftLeg.xRot = 0;
            this.rightLeg.xRot = 0;
            this.leftLeg.yRot = 0;
            this.rightLeg.yRot = 0;

            this.body.xRot = 0;
            this.body.yRot = 0;
            this.body.zRot = 0;
        }

        if (livingEntity.getVehicle() instanceof VehicleEntity vehicle) {
            var index = vehicle.getSeatIndex(livingEntity);
            var seats = vehicle.computed().seats();
            if (index >= seats.size() || index < 0) return;
            var seat = seats.get(index);

            if (seat.pose.equals("Pilot")) {
                this.head.xRot = 0;
                this.head.yRot = 0;
                this.head.zRot = 0;
                this.hat.xRot = 0;
                this.hat.yRot = 0;
                this.hat.zRot = 0;

                this.rightArm.xRot = -55 * Mth.DEG_TO_RAD;
                this.rightArm.yRot = -15f * Mth.DEG_TO_RAD;
                this.rightArm.zRot = -30f * Mth.DEG_TO_RAD;
            }

            if (seat.pose.equals("Tow")) {
                this.head.xRot = 0;
                this.hat.xRot = 0;

                this.leftArm.yRot = 45 * Mth.DEG_TO_RAD;
                this.leftArm.xRot = -115 * Mth.DEG_TO_RAD;

                this.rightArm.yRot = 25 * Mth.DEG_TO_RAD;
                this.rightArm.xRot = -115 * Mth.DEG_TO_RAD;
            }

            if (seat.pose.equals("Climb")) {
                this.leftArm.xRot = -112.5f * Mth.DEG_TO_RAD;
                this.rightArm.xRot = -112.5f * Mth.DEG_TO_RAD;
            }

            if (seat.pose.equals("Stand")) {
                this.leftLeg.xRot = 0;
                this.leftLeg.yRot = 0;
                this.leftLeg.zRot = 0;

                this.rightLeg.xRot = 0;
                this.rightLeg.yRot = 0;
                this.rightLeg.zRot = 0;
            }

            if (seat.pose.equals("MachineGunStand")) {
                this.leftArm.xRot = -90 * Mth.DEG_TO_RAD;
                this.leftArm.yRot = 0;
                this.leftArm.zRot = 0;

                this.rightArm.xRot = -90 * Mth.DEG_TO_RAD;
                this.rightArm.yRot = 0;
                this.rightArm.zRot = 0;

                this.leftLeg.xRot = 0;
                this.leftLeg.yRot = 0;
                this.leftLeg.zRot = 0;

                this.rightLeg.xRot = 0;
                this.rightLeg.yRot = 0;
                this.rightLeg.zRot = 0;
            }
        }

        if (livingEntity.getMainHandItem().getItem() instanceof GunItem
                && livingEntity.getPose() == Pose.SWIMMING && !livingEntity.isSwimming()) {
            this.hat.xRot = (livingEntity.getViewXRot(1) - 90) * Mth.DEG_TO_RAD;
            this.head.xRot = (livingEntity.getViewXRot(1) - 90) * Mth.DEG_TO_RAD;
            this.hat.yRot = 0;
            this.head.yRot = 0;

            this.leftArm.xRot = (-180 + livingEntity.getViewXRot(1)) * Mth.DEG_TO_RAD;
            this.rightArm.xRot = (-180 + livingEntity.getViewXRot(1)) * Mth.DEG_TO_RAD;

            this.leftArm.yRot = 0;
            this.rightArm.yRot = 0;

            this.leftArm.zRot = -30 * Mth.DEG_TO_RAD;
            this.rightArm.zRot = 0;

            this.rightArm.x = -3f;
            this.leftArm.x = 3f;
        }
    }
}
