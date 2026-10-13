package com.atsuishio.superbwarfare.item.gun.special

import com.atsuishio.superbwarfare.init.ModEnumExtensions
import com.atsuishio.superbwarfare.init.RegistryName
import com.atsuishio.superbwarfare.item.gun.GeoGunItem
import net.minecraft.client.model.HumanoidModel
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@RegistryName("lunge_mine")
class LungeMineItem : GeoGunItem(Properties()) {

    @OnlyIn(Dist.CLIENT)
    override fun armPose(
        entityLiving: LivingEntity,
        hand: InteractionHand,
        itemStack: ItemStack
    ): HumanoidModel.ArmPose {
        return if (!itemStack.isEmpty && entityLiving.usedItemHand == hand) ModEnumExtensions.Client.lungeMinePose
        else HumanoidModel.ArmPose.EMPTY
    }
}
