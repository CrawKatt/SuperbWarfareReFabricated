package com.atsuishio.superbwarfare.client

import com.atsuishio.superbwarfare.capability.player.PlayerChargeState
import com.atsuishio.superbwarfare.client.gun.GunChargeHandler
import com.atsuishio.superbwarfare.data.gun.GunData
import com.atsuishio.superbwarfare.tools.localPlayer
import net.minecraft.client.model.HumanoidModel
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
object PoseTool {
    @JvmStatic
    fun pose(entityLiving: LivingEntity, hand: InteractionHand?, stack: ItemStack): HumanoidModel.ArmPose {
        // 冲锋窗口内一律端枪（哪怕正在疾跑），疾跑那个扛枪姿势不参与
        if (isBayonetCharging(entityLiving)) return HumanoidModel.ArmPose.BOW_AND_ARROW

        val data = GunData.from(stack)
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
}