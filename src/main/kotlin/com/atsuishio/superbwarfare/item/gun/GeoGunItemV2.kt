package com.atsuishio.superbwarfare.item.gun

import com.atsuishio.superbwarfare.client.PoseTool
import com.atsuishio.superbwarfare.client.renderer.gun.GeoGunRenderer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.model.HumanoidModel
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item.Properties
import net.minecraft.world.item.ItemStack

open class GeoGunItemV2(properties: Properties) : GunItem(properties) {

    @Environment(EnvType.CLIENT)
    fun getArmPose(
        entityLiving: LivingEntity,
        hand: InteractionHand,
        itemStack: ItemStack
    ): HumanoidModel.ArmPose {
        return armPose(entityLiving, hand, itemStack)
    }

    /**
     * 这个物品用哪个渲染器。需要特殊渲染的枪（比如修理工具的逐帧火焰）覆写它换成自己的子类，
     * 别的枪一律走 [GeoGunRenderer] 的常规路径，不需要知道这个方法存在。
     */
    @Environment(EnvType.CLIENT)
    open fun createRenderer(): GeoGunRenderer = GeoGunRenderer()

    open fun armPose(
        entityLiving: LivingEntity,
        hand: InteractionHand,
        itemStack: ItemStack
    ): HumanoidModel.ArmPose {
        return PoseTool.pose(entityLiving, hand, itemStack)
    }
}