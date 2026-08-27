package com.atsuishio.superbwarfare.client;

import com.atsuishio.superbwarfare.data.gun.GunData;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class PoseTool {

    public static HumanoidModel.ArmPose pose(LivingEntity entityLiving, InteractionHand hand, ItemStack stack) {
        var data = GunData.from(stack);
        if ((entityLiving.isSprinting() && entityLiving.onGround())
                || data.reload.empty()
                || data.reload.normal()
                || data.reloading()
                || data.charging()) {
            return HumanoidModel.ArmPose.CROSSBOW_CHARGE;
        } else {
            return HumanoidModel.ArmPose.BOW_AND_ARROW;
        }
    }
}
