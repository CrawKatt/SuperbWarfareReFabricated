package com.atsuishio.superbwarfare.item

import com.atsuishio.superbwarfare.init.ModEnumExtensions
import com.atsuishio.superbwarfare.init.RegistryName
import com.atsuishio.superbwarfare.item.gun.GeoGunItem
import net.minecraft.client.model.HumanoidModel
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.inventory.tooltip.TooltipComponent
import net.minecraft.world.item.ItemStack
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import java.util.*

@Deprecated("reserved for compatibility, DO NOT USE")
sealed interface LungeMine

@RegistryName("lunge_mine")
open class LungeMineItem : GeoGunItem(Properties()), @Suppress("DEPRECATION") LungeMine {

    @Environment(EnvType.CLIENT)
    override fun armPose(
        entityLiving: LivingEntity,
        hand: InteractionHand,
        itemStack: ItemStack
    ): HumanoidModel.ArmPose {
        return if (!itemStack.isEmpty && entityLiving.usedItemHand == hand) ModEnumExtensions.Client.lungeMinePose
        else HumanoidModel.ArmPose.EMPTY
    }

    override fun getTooltipImage(pStack: ItemStack): Optional<TooltipComponent> {
        return Optional.empty()
    }
}
