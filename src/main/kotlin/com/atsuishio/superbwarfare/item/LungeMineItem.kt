package com.atsuishio.superbwarfare.item

import com.atsuishio.superbwarfare.client.PoseTool
import com.atsuishio.superbwarfare.init.RegistryName
import com.atsuishio.superbwarfare.item.gun.GeoGunItem
import net.minecraft.client.model.HumanoidModel
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

@Deprecated("reserved for compatibility, DO NOT USE")
sealed interface LungeMine

@RegistryName("lunge_mine")
open class LungeMineItem : GeoGunItem(Properties()), @Suppress("DEPRECATION") LungeMine {

    @OnlyIn(Dist.CLIENT)
    override fun armPose(
        entityLiving: LivingEntity,
        hand: InteractionHand,
        itemStack: ItemStack
    ): HumanoidModel.ArmPose {
        return if (!itemStack.isEmpty && entityLiving.usedItemHand == hand) PoseTool.LUNGE_MINE_POSE
        else HumanoidModel.ArmPose.EMPTY
    }
}