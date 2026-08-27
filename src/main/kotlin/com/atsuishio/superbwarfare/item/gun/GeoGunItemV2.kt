package com.atsuishio.superbwarfare.item.gun

import com.atsuishio.superbwarfare.client.PoseTool
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.model.HumanoidModel
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item.Properties
import net.minecraft.world.item.ItemStack

// V2 items share the Fabric renderer registration in ClientRenderHandler.
open class GeoGunItemV2(properties: Properties) : GunItem(properties) {
    @Environment(EnvType.CLIENT)
    fun getArmPose(
        entityLiving: LivingEntity,
        hand: InteractionHand,
        itemStack: ItemStack
    ): HumanoidModel.ArmPose = PoseTool.pose(entityLiving, hand, itemStack)
}
