package com.atsuishio.superbwarfare.client

import com.atsuishio.superbwarfare.capability.player.PlayerChargeState
import com.atsuishio.superbwarfare.client.gun.GunChargeHandler
import com.atsuishio.superbwarfare.data.gun.GunArmPose
import com.atsuishio.superbwarfare.data.gun.GunData
import com.atsuishio.superbwarfare.data.gun.GunProp
import com.atsuishio.superbwarfare.event.DeployedWeaponHandler
import com.atsuishio.superbwarfare.tools.localPlayer
import net.minecraft.client.model.HumanoidModel
import net.minecraft.client.model.HumanoidModel.ArmPose
import net.minecraft.util.Mth
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.HumanoidArm
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment

@Environment(EnvType.CLIENT)
object PoseTool {
    private val armPoseFactory = HumanoidModel.ArmPose.EMPTY as Any as ArmPoseExtensible

    // 不要删这个，纯为了加载本类
    fun init() {
    }

    @JvmStatic
    fun pose(entityLiving: LivingEntity, hand: InteractionHand?, stack: ItemStack): HumanoidModel.ArmPose {
        val data = GunData.from(stack)
        val held = when (data.get(GunProp.ARM_POSE)) {
            GunArmPose.DEFAULT -> null
            GunArmPose.MINI_GUN -> MINI_GUN_POSE
            GunArmPose.SUPER_STAR_SHOOTER -> SUPER_STAR_SHOOTER_POSE
            GunArmPose.SINGLE_HAND -> SINGLE_HAND_POSE
            GunArmPose.LUNGE_MINE -> LUNGE_MINE_POSE
        }
        if (held != null) {
            if (entityLiving is Player && DeployedWeaponHandler.isDeployed(entityLiving)) return ArmPose.EMPTY
            return if (!stack.isEmpty && entityLiving.usedItemHand == hand) held else ArmPose.EMPTY
        }

        // 冲锋窗口内一律端枪（哪怕正在疾跑），疾跑那个扛枪姿势不参与
        if (isBayonetCharging(entityLiving)) return HumanoidModel.ArmPose.BOW_AND_ARROW

        return if ((entityLiving.isSprinting && entityLiving.onGround())
            || data.reload.empty()
            || data.reload.normal()
            || data.reloading()
        ) {
            HumanoidModel.ArmPose.CROSSBOW_CHARGE
        } else {
            HumanoidModel.ArmPose.BOW_AND_ARROW
        }
    }

    // 本人的窗口在本地（要即时），别人只能看服务端同步过来的那份
    private fun isBayonetCharging(entityLiving: LivingEntity): Boolean =
        if (entityLiving === localPlayer) {
            GunChargeHandler.isCharging()
        } else {
            PlayerChargeState.isCharging(entityLiving)
        }

    private val SUPER_STAR_SHOOTER_POSE: ArmPose = armPoseFactory.create(
        "SuperStarShooterItem",
        false,
        ArmPoseExtensible.Transformer { model: HumanoidModel<*>, entity: LivingEntity?, arm: HumanoidArm ->
        if (arm != HumanoidArm.LEFT) {
            model.rightArm.xRot = -70f * Mth.DEG_TO_RAD + model.head.xRot
            model.rightArm.yRot = 0f
            model.rightArm.zRot = 0f
            model.leftArm.xRot = -70f * Mth.DEG_TO_RAD + model.head.xRot
            model.leftArm.yRot = 0f
            model.leftArm.zRot = 0f
        }
    })

    private val MINI_GUN_POSE: ArmPose = armPoseFactory.create(
        "MiniGunItem",
        false,
        ArmPoseExtensible.Transformer { model: HumanoidModel<*>, entity: LivingEntity?, arm: HumanoidArm ->
        if (arm != HumanoidArm.LEFT) {
            model.rightArm.xRot = 40f * Mth.DEG_TO_RAD + model.head.xRot
            model.rightArm.yRot = model.head.yRot
            model.leftArm.xRot =
                Mth.clamp(-45f * Mth.DEG_TO_RAD + model.head.xRot, -67.5f * Mth.DEG_TO_RAD, 0f * Mth.DEG_TO_RAD)
            model.leftArm.yRot =
                Mth.clamp(45f * Mth.DEG_TO_RAD + model.head.yRot, 45f * Mth.DEG_TO_RAD, 80f * Mth.DEG_TO_RAD)
        }
    })

    private val SINGLE_HAND_POSE: ArmPose = armPoseFactory.create(
        "RepairTool",
        false,
        ArmPoseExtensible.Transformer { model: HumanoidModel<*>, entity: LivingEntity?, arm: HumanoidArm ->
        if (arm != HumanoidArm.LEFT) {
            model.rightArm.xRot = -67.5f * Mth.DEG_TO_RAD + model.head.xRot + 0.05f * model.rightArm.xRot
            model.rightArm.yRot = 5f * Mth.DEG_TO_RAD + model.head.yRot
        }
    })

    private val LUNGE_MINE_POSE: ArmPose = armPoseFactory.create(
        "LungeMineItem",
        false,
        ArmPoseExtensible.Transformer { model: HumanoidModel<*>, entity: LivingEntity?, arm: HumanoidArm ->
        if (arm != HumanoidArm.LEFT) {
            model.rightArm.xRot = 20f * Mth.DEG_TO_RAD + model.head.xRot
            model.rightArm.yRot = -12f * Mth.DEG_TO_RAD
            model.leftArm.xRot = -45f * Mth.DEG_TO_RAD + model.head.xRot
            model.leftArm.yRot = 40f * Mth.DEG_TO_RAD
        }
    })
}
