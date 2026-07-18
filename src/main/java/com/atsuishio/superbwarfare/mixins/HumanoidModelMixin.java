package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.item.LungeMine;
import com.atsuishio.superbwarfare.item.gun.handgun.AureliaSceptreItem;
import com.atsuishio.superbwarfare.item.gun.machinegun.M2HBItem;
import com.atsuishio.superbwarfare.item.gun.machinegun.MinigunItem;
import com.atsuishio.superbwarfare.item.gun.special.RepairToolItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin<T extends LivingEntity> {

    @Inject(method = "poseRightArm", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$applyCustomRightArmPose(T entity, CallbackInfo ci) {
        if (!(entity instanceof Player)) return;

        InteractionHand hand = entity.getUsedItemHand();
        HumanoidArm arm = hand == InteractionHand.MAIN_HAND ? entity.getMainArm() : entity.getMainArm().getOpposite();
        if (arm != HumanoidArm.RIGHT) return;

        var item = entity.getItemInHand(hand).getItem();
        var model = (HumanoidModel<?>) (Object) this;

        if (item instanceof LungeMine) {
            model.rightArm.xRot = 20f * Mth.DEG_TO_RAD + model.head.xRot;
            model.rightArm.yRot = -12f * Mth.DEG_TO_RAD;
            model.leftArm.xRot = -45f * Mth.DEG_TO_RAD + model.head.xRot;
            model.leftArm.yRot = 40f * Mth.DEG_TO_RAD;
        } else if (item instanceof M2HBItem) {
            model.rightArm.xRot = 45f * Mth.DEG_TO_RAD + model.head.xRot;
            model.rightArm.yRot = model.head.yRot;
            model.leftArm.xRot = Mth.clamp(-45f * Mth.DEG_TO_RAD + model.head.xRot, -67.5f * Mth.DEG_TO_RAD, 0f);
            model.leftArm.yRot = Mth.clamp(45f * Mth.DEG_TO_RAD + model.head.yRot, 45f * Mth.DEG_TO_RAD, 80f * Mth.DEG_TO_RAD);
        } else if (item instanceof MinigunItem) {
            model.rightArm.xRot = 22.5f * Mth.DEG_TO_RAD + model.head.xRot;
            model.rightArm.yRot = model.head.yRot;
            model.leftArm.xRot = Mth.clamp(-45f * Mth.DEG_TO_RAD + model.head.xRot, -67.5f * Mth.DEG_TO_RAD, 0f);
            model.leftArm.yRot = Mth.clamp(45f * Mth.DEG_TO_RAD + model.head.yRot, 45f * Mth.DEG_TO_RAD, 80f * Mth.DEG_TO_RAD);
        } else if (item instanceof AureliaSceptreItem || item instanceof RepairToolItem) {
            model.rightArm.xRot = -67.5f * Mth.DEG_TO_RAD + model.head.xRot + 0.05f * model.rightArm.xRot;
            model.rightArm.yRot = 5f * Mth.DEG_TO_RAD + model.head.yRot;
        } else {
            return;
        }

        ci.cancel();
    }
}
