package com.atsuishio.superbwarfare.item.gun.machinegun

import com.atsuishio.superbwarfare.client.PoseTool
import com.atsuishio.superbwarfare.event.DeployedWeaponHandler
import com.atsuishio.superbwarfare.init.ModRarities
import com.atsuishio.superbwarfare.init.RegistryName
import com.atsuishio.superbwarfare.item.gun.GeoGunItem
import net.minecraft.client.model.HumanoidModel
import net.minecraft.client.model.HumanoidModel.ArmPose
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment

@RegistryName("minigun")
class MinigunItem : GeoGunItem(Properties().rarity(ModRarities.LEGENDARY)) {

    @Environment(EnvType.CLIENT)
    override fun armPose(
        entityLiving: LivingEntity,
        hand: InteractionHand,
        itemStack: ItemStack
    ): ArmPose {
        if (entityLiving is Player && DeployedWeaponHandler.isDeployed(entityLiving)) return HumanoidModel.ArmPose.EMPTY
        return if (!itemStack.isEmpty && entityLiving.usedItemHand == hand) PoseTool.MINI_GUN_POSE else ArmPose.EMPTY
    }
}
