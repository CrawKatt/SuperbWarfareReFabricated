package com.atsuishio.superbwarfare.init;

import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.item.Rarity;

public class ModEnumExtensions {

    public static final Rarity SUPERBWARFARE_LEGENDARY = Rarity.EPIC;

    public static Rarity getLegendary() {
        return SUPERBWARFARE_LEGENDARY;
    }

    public static class Client {

        // These will need proper ArmPose registration via mixin later
        // For now use existing poses as placeholders
        public static final HumanoidModel.ArmPose LUNGE_MINE_POSE = HumanoidModel.ArmPose.CROSSBOW_CHARGE;
        public static final HumanoidModel.ArmPose AURELIA_SCEPTRE_POSE = HumanoidModel.ArmPose.BOW_AND_ARROW;
        public static final HumanoidModel.ArmPose MINIGUN_POSE = HumanoidModel.ArmPose.BLOCK;
        public static final HumanoidModel.ArmPose M2_POSE = HumanoidModel.ArmPose.THROW_SPEAR;

        public static HumanoidModel.ArmPose getLungeMinePose() { return LUNGE_MINE_POSE; }
        public static HumanoidModel.ArmPose getAureliaSceptrePose() { return AURELIA_SCEPTRE_POSE; }
        public static HumanoidModel.ArmPose getMinigunPose() { return MINIGUN_POSE; }
        public static HumanoidModel.ArmPose getM2Pose() { return M2_POSE; }
    }
}
