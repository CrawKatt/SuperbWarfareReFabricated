package com.atsuishio.superbwarfare.item.gun.machinegun

import com.atsuishio.superbwarfare.init.ModEnumExtensions
import com.atsuishio.superbwarfare.init.RegistryName
import com.atsuishio.superbwarfare.item.gun.GeoGunItemV2
import net.minecraft.client.model.HumanoidModel
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment

@RegistryName("m_2_hb")
object M2HBItem : GeoGunItemV2(Properties().rarity(Rarity.RARE)) {

    @Environment(EnvType.CLIENT)
    override fun armPose(
        entityLiving: LivingEntity,
        hand: InteractionHand,
        itemStack: ItemStack
    ): HumanoidModel.ArmPose {
        return if (!itemStack.isEmpty && entityLiving.usedItemHand == hand) ModEnumExtensions.Client.m2Pose else HumanoidModel.ArmPose.EMPTY
    }
}
