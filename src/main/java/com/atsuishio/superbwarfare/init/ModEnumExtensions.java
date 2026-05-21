package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.Mod;
import fuzs.extensibleenums.api.v2.BuiltInEnumFactories;
import fuzs.extensibleenums.api.v2.core.EnumAppender;
import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Rarity;

public class ModEnumExtensions {

    public static final Rarity SUPERBWARFARE_LEGENDARY = BuiltInEnumFactories.INSTANCE.createRarity(
            Mod.loc("superbwarfare_legendary"),
            ChatFormatting.GOLD
    );

    public static Rarity getLegendary() {
        return SUPERBWARFARE_LEGENDARY;
    }

    public static class Client {

        public static final HumanoidModel.ArmPose SUPERBWARFARE_LUNGE_MINE_POSE = createArmPose("SUPERBWARFARE_LUNGE_MINE_POSE");
        public static final HumanoidModel.ArmPose SUPERBWARFARE_AURELIA_SCEPTRE_POSE = createArmPose("SUPERBWARFARE_AURELIA_SCEPTRE_POSE");
        public static final HumanoidModel.ArmPose SUPERBWARFARE_MINIGUN_POSE = createArmPose("SUPERBWARFARE_MINIGUN_POSE");
        public static final HumanoidModel.ArmPose SUPERBWARFARE_M2_POSE = createArmPose("SUPERBWARFARE_M2_POSE");

        private static HumanoidModel.ArmPose createArmPose(String name) {
            EnumAppender.create(HumanoidModel.ArmPose.class, boolean.class)
                    .addEnumConstant(name, false)
                    .applyTo(HumanoidModel.class);

            return HumanoidModel.ArmPose.valueOf(name);
        }

        public static HumanoidModel.ArmPose getLungeMinePose() {
            return SUPERBWARFARE_LUNGE_MINE_POSE;
        }

        public static HumanoidModel.ArmPose getAureliaSceptrePose() {
            return SUPERBWARFARE_AURELIA_SCEPTRE_POSE;
        }

        public static HumanoidModel.ArmPose getMinigunPose() {
            return SUPERBWARFARE_MINIGUN_POSE;
        }

        public static HumanoidModel.ArmPose getM2Pose() {
            return SUPERBWARFARE_M2_POSE;
        }

        public static void applyArmPose(HumanoidModel<?> model) {
            var pose = model.rightArmPose;

            if (pose == SUPERBWARFARE_LUNGE_MINE_POSE) {
                model.rightArm.xRot = 20f * Mth.DEG_TO_RAD + model.head.xRot;
                model.rightArm.yRot = -12f * Mth.DEG_TO_RAD;
                model.leftArm.xRot = -45f * Mth.DEG_TO_RAD + model.head.xRot;
                model.leftArm.yRot = 40f * Mth.DEG_TO_RAD;
            } else if (pose == SUPERBWARFARE_AURELIA_SCEPTRE_POSE) {
                model.rightArm.xRot = -67.5f * Mth.DEG_TO_RAD + model.head.xRot + 0.05f * model.rightArm.xRot;
                model.rightArm.yRot = 5f * Mth.DEG_TO_RAD + model.head.yRot;
            } else if (pose == SUPERBWARFARE_MINIGUN_POSE) {
                model.rightArm.xRot = 22.5f * Mth.DEG_TO_RAD + model.head.xRot;
                model.rightArm.yRot = model.head.yRot;
                model.leftArm.xRot = Mth.clamp(-45f * Mth.DEG_TO_RAD + model.head.xRot, -67.5f * Mth.DEG_TO_RAD, 0f);
                model.leftArm.yRot = Mth.clamp(45f * Mth.DEG_TO_RAD + model.head.yRot, 45f * Mth.DEG_TO_RAD, 80f * Mth.DEG_TO_RAD);
            } else if (pose == SUPERBWARFARE_M2_POSE) {
                model.rightArm.xRot = 45f * Mth.DEG_TO_RAD + model.head.xRot;
                model.rightArm.yRot = model.head.yRot;
                model.leftArm.xRot = Mth.clamp(-45f * Mth.DEG_TO_RAD + model.head.xRot, -67.5f * Mth.DEG_TO_RAD, 0f);
                model.leftArm.yRot = Mth.clamp(45f * Mth.DEG_TO_RAD + model.head.yRot, 45f * Mth.DEG_TO_RAD, 80f * Mth.DEG_TO_RAD);
            }
        }

    }
}
