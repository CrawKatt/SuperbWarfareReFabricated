package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.Mod
import fuzs.extensibleenums.api.v2.BuiltInEnumFactories
import fuzs.extensibleenums.api.v2.core.EnumAppender
import net.minecraft.ChatFormatting
import net.minecraft.client.model.HumanoidModel
import net.minecraft.client.model.HumanoidModel.ArmPose
import net.minecraft.util.Mth
import net.minecraft.world.item.Rarity

object ModEnumExtensions {
    @JvmField
    val SUPERBWARFARE_LEGENDARY: Rarity = BuiltInEnumFactories.INSTANCE.createRarity(
        Mod.loc("superbwarfare_legendary"),
        ChatFormatting.GOLD
    )

    @JvmField
    val SUPERBWARFARE_SUPERB: Rarity = BuiltInEnumFactories.INSTANCE.createRarity(
        Mod.loc("superbwarfare_superb"),
        ChatFormatting.RED
    )

    @JvmField
    val SUPERBWARFARE_VIRTUAL: Rarity = BuiltInEnumFactories.INSTANCE.createRarity(
        Mod.loc("superbwarfare_virtual"),
        ChatFormatting.WHITE
    )

    @JvmStatic
    val legendary: Rarity get() = SUPERBWARFARE_LEGENDARY

    @JvmStatic
    val superb: Rarity get() = SUPERBWARFARE_SUPERB

    @JvmStatic
    val virtual: Rarity get() = SUPERBWARFARE_VIRTUAL

    object Client {
        @JvmField
        val SUPERBWARFARE_LUNGE_MINE_POSE: ArmPose = createArmPose("SUPERBWARFARE_LUNGE_MINE_POSE")

        @JvmField
        val SUPERBWARFARE_MINIGUN_POSE: ArmPose = createArmPose("SUPERBWARFARE_MINIGUN_POSE")

        @JvmField
        val SUPERBWARFARE_M2_POSE: ArmPose = createArmPose("SUPERBWARFARE_M2_POSE")

        @JvmField
        val SUPERBWARFARE_SUPER_STAR_SHOOTER_POSE: ArmPose = createArmPose("SUPERBWARFARE_SUPER_STAR_SHOOTER_POSE")

        private fun createArmPose(name: String): ArmPose {
            EnumAppender.create(ArmPose::class.java, Boolean::class.java)
                .addEnumConstant(name, false)
                .applyTo(HumanoidModel::class.java)
            return ArmPose.valueOf(name)
        }

        @JvmStatic
        val lungeMinePose: ArmPose get() = SUPERBWARFARE_LUNGE_MINE_POSE

        @JvmStatic
        val minigunPose: ArmPose get() = SUPERBWARFARE_MINIGUN_POSE

        @JvmStatic
        val m2Pose: ArmPose get() = SUPERBWARFARE_M2_POSE

        @JvmStatic
        val superStarShooterPose: ArmPose get() = SUPERBWARFARE_SUPER_STAR_SHOOTER_POSE

        @JvmStatic
        fun applyArmPose(model: HumanoidModel<*>) {
            val pose = model.rightArmPose

            when (pose) {
                SUPERBWARFARE_LUNGE_MINE_POSE -> {
                    model.rightArm.xRot = 20f * Mth.DEG_TO_RAD + model.head.xRot
                    model.rightArm.yRot = -12f * Mth.DEG_TO_RAD
                    model.leftArm.xRot = -45f * Mth.DEG_TO_RAD + model.head.xRot
                    model.leftArm.yRot = 40f * Mth.DEG_TO_RAD
                }
                SUPERBWARFARE_MINIGUN_POSE -> {
                    model.rightArm.xRot = 22.5f * Mth.DEG_TO_RAD + model.head.xRot
                    model.rightArm.yRot = model.head.yRot
                    model.leftArm.xRot = Mth.clamp(
                        -45f * Mth.DEG_TO_RAD + model.head.xRot,
                        -67.5f * Mth.DEG_TO_RAD,
                        0f
                    )
                    model.leftArm.yRot = Mth.clamp(
                        45f * Mth.DEG_TO_RAD + model.head.yRot,
                        45f * Mth.DEG_TO_RAD,
                        80f * Mth.DEG_TO_RAD
                    )
                }
                SUPERBWARFARE_M2_POSE -> {
                    model.rightArm.xRot = 45f * Mth.DEG_TO_RAD + model.head.xRot
                    model.rightArm.yRot = model.head.yRot
                    model.leftArm.xRot = Mth.clamp(
                        -45f * Mth.DEG_TO_RAD + model.head.xRot,
                        -67.5f * Mth.DEG_TO_RAD,
                        0f
                    )
                    model.leftArm.yRot = Mth.clamp(
                        45f * Mth.DEG_TO_RAD + model.head.yRot,
                        45f * Mth.DEG_TO_RAD,
                        80f * Mth.DEG_TO_RAD
                    )
                }
                SUPERBWARFARE_SUPER_STAR_SHOOTER_POSE -> {
                    model.rightArm.xRot = -70f * Mth.DEG_TO_RAD + model.head.xRot
                    model.rightArm.yRot = 0f
                    model.rightArm.zRot = 0f
                    model.leftArm.xRot = -70f * Mth.DEG_TO_RAD + model.head.xRot
                    model.leftArm.yRot = 0f
                    model.leftArm.zRot = 0f
                }

                else -> {}
            }
        }
    }
}
