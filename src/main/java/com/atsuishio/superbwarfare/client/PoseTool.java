package com.atsuishio.superbwarfare.client;

import com.atsuishio.superbwarfare.data.gun.GunData;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class PoseTool {

    public static HumanoidModel.ArmPose pose(LivingEntity entityLiving, InteractionHand hand, ItemStack stack) {
        if (GunData.from(stack).reload.empty()
                || GunData.from(stack).reload.normal()
                || GunData.from(stack).reloading()
                || GunData.from(stack).charging()) {
            return HumanoidModel.ArmPose.CROSSBOW_CHARGE;
        } else if (entityLiving.isSprinting() && entityLiving.onGround()) {
            return HumanoidModel.ArmPose.CROSSBOW_CHARGE;
        } else {
            return HumanoidModel.ArmPose.BOW_AND_ARROW;
        }
    }
}
