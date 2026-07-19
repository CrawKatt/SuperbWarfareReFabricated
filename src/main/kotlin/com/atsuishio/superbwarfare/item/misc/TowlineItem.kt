package com.atsuishio.superbwarfare.item.misc

import com.atsuishio.superbwarfare.config.server.VehicleConfig
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import com.atsuishio.superbwarfare.item.IVehicleInteract
import com.atsuishio.superbwarfare.tools.EntityFindUtil
import com.atsuishio.superbwarfare.tools.getOrCreateTag
import com.atsuishio.superbwarfare.tools.tag
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

open class TowlineItem : Item(Properties().stacksTo(1)), IVehicleInteract {

    companion object {
        private const val TAG_TOW_TARGET = "TowTarget"
    }

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltipComponents: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {
        val tag = stack.tag
        val target = tag?.getString(TAG_TOW_TARGET)
        if (!target.isNullOrBlank()) {
            tooltipComponents.add(
                Component.translatable("des.superbwarfare.towline.target_selected")
                    .withStyle(ChatFormatting.GOLD)
            )
        } else {
            tooltipComponents.add(
                Component.translatable("des.superbwarfare.towline.hint")
                    .withStyle(ChatFormatting.GRAY)
            )
        }
    }

    override fun onInteractVehicle(
        vehicle: VehicleEntity,
        stack: ItemStack,
        player: Player,
        hand: InteractionHand
    ): InteractionResult? {
        if (player.level().isClientSide) return InteractionResult.SUCCESS

        val tag = stack.getOrCreateTag()
        val existingTarget = tag.getString(TAG_TOW_TARGET)

        // Shift+right-click: clear towing / clear stored target
        if (player.isShiftKeyDown) {
            val towedBy = vehicle.towedByUUID
            val towing = vehicle.towingUUID

            if (towedBy.isNotBlank() || towing.isNotBlank()) {
                vehicle.clearTowingInfo()

                player.displayClientMessage(
                    Component.translatable("tips.superbwarfare.towline.unlinked")
                        .withStyle(ChatFormatting.YELLOW),
                    true
                )
                player.playSound(SoundEvents.CHAIN_BREAK, 1.0f, 1.0f)
            }

            // Also clear stored target if present
            if (existingTarget.isNotBlank()) {
                tag.remove(TAG_TOW_TARGET)
                player.displayClientMessage(
                    Component.translatable("tips.superbwarfare.towline.selection_cleared")
                        .withStyle(ChatFormatting.GRAY),
                    true
                )
            }

            return InteractionResult.SUCCESS
        }

        if (existingTarget.isBlank()) {
            // First click: select the towing vehicle
            tag.putString(TAG_TOW_TARGET, vehicle.stringUUID)
            player.displayClientMessage(
                Component.translatable(
                    "tips.superbwarfare.towline.select_towing",
                    vehicle.displayName
                ).withStyle(ChatFormatting.GREEN),
                true
            )
            player.playSound(SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, 1.0f, 1.0f)
            return InteractionResult.SUCCESS
        }

        // Second click: select the towed vehicle
        val firstVehicle = EntityFindUtil.findEntity(vehicle.level(), existingTarget) as? VehicleEntity

        if (firstVehicle == null) {
            tag.remove(TAG_TOW_TARGET)
            player.displayClientMessage(
                Component.translatable("tips.superbwarfare.towline.target_lost")
                    .withStyle(ChatFormatting.RED),
                true
            )
            return InteractionResult.FAIL
        }

        if (firstVehicle === vehicle) {
            tag.remove(TAG_TOW_TARGET)
            player.displayClientMessage(
                Component.translatable("tips.superbwarfare.towline.same_vehicle")
                    .withStyle(ChatFormatting.RED),
                true
            )
            return InteractionResult.FAIL
        }

        // Check if either vehicle is already in a towing relationship
        if (firstVehicle.towingUUID.isNotBlank() || firstVehicle.towedByUUID.isNotBlank()) {
            player.displayClientMessage(
                Component.translatable("tips.superbwarfare.towline.already_linked")
                    .withStyle(ChatFormatting.RED),
                true
            )
            tag.remove(TAG_TOW_TARGET)
            return InteractionResult.FAIL
        }
        if (vehicle.towingUUID.isNotBlank() || vehicle.towedByUUID.isNotBlank()) {
            player.displayClientMessage(
                Component.translatable("tips.superbwarfare.towline.already_linked")
                    .withStyle(ChatFormatting.RED),
                true
            )
            tag.remove(TAG_TOW_TARGET)
            return InteractionResult.FAIL
        }

        // Distance check
        val dist = vehicle.distanceTo(firstVehicle)
        val maxDist = VehicleConfig.TOW_MAX_DISTANCE.get().toDouble()
        if (dist > maxDist) {
            player.displayClientMessage(
                Component.translatable(
                    "tips.superbwarfare.towline.too_far",
                    String.format("%.1f", dist),
                    maxDist.toInt()
                ).withStyle(ChatFormatting.RED),
                true
            )
            tag.remove(TAG_TOW_TARGET)
            return InteractionResult.FAIL
        }

        // Link the two vehicles
        firstVehicle.towingUUID = vehicle.stringUUID
        vehicle.towedByUUID = firstVehicle.stringUUID
        tag.remove(TAG_TOW_TARGET)

        player.displayClientMessage(
            Component.translatable(
                "tips.superbwarfare.towline.linked",
                firstVehicle.displayName,
                vehicle.displayName
            ).withStyle(ChatFormatting.GREEN),
            true
        )
        player.playSound(SoundEvents.CHAIN_PLACE, 1.0f, 1.0f)

        return InteractionResult.sidedSuccess(player.level().isClientSide)
    }
}
