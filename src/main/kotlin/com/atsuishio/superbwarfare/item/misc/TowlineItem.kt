package com.atsuishio.superbwarfare.item.misc

import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import com.atsuishio.superbwarfare.item.IVehicleInteract
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

open class TowlineItem : Item(Properties()), IVehicleInteract {
    override fun onInteractVehicle(
        vehicle: VehicleEntity,
        stack: ItemStack,
        player: Player,
        hand: InteractionHand
    ): InteractionResult? {
        return null
    }
}