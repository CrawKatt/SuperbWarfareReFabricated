package com.atsuishio.superbwarfare.item.gun.machinegun

import com.atsuishio.superbwarfare.client.PoseTool
import com.atsuishio.superbwarfare.init.RegistryName
import com.atsuishio.superbwarfare.item.gun.GeoGunItemV2
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.model.HumanoidModel
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity

@RegistryName("m_2_hb")
object M2HBItem : GeoGunItemV2(Properties().rarity(Rarity.RARE)) {

    @Environment(EnvType.CLIENT)
    override fun armPose(
        entityLiving: LivingEntity,
        hand: InteractionHand,
        itemStack: ItemStack
    ): HumanoidModel.ArmPose {
        return if (!itemStack.isEmpty && entityLiving.usedItemHand == hand) PoseTool.MINI_GUN_POSE else HumanoidModel.ArmPose.EMPTY
    }
}
