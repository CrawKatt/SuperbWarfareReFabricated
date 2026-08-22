package com.atsuishio.superbwarfare.item.trinket

import com.atsuishio.superbwarfare.client.TooltipTool
import com.atsuishio.superbwarfare.client.screens.DogTagEditorScreen
import com.atsuishio.superbwarfare.client.tooltip.component.DogTagImageComponent
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import com.atsuishio.superbwarfare.init.ModDataComponents
import com.atsuishio.superbwarfare.item.IVehicleInteract
import com.atsuishio.superbwarfare.item.ItemScreenProvider
import dev.emi.trinkets.api.SlotReference
import dev.emi.trinkets.api.TrinketItem
import dev.emi.trinkets.api.TrinketsApi
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.tooltip.TooltipComponent
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import java.util.Arrays
import java.util.Optional

class DogTagItem : TrinketItem(Properties().stacksTo(1)), ItemScreenProvider, IVehicleInteract {
    @Environment(EnvType.CLIENT)
    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltipComponents: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {
        TooltipTool.addScreenProviderText(tooltipComponents)
    }

    override fun canEquip(stack: ItemStack, slot: SlotReference, entity: LivingEntity): Boolean {
        return TrinketsApi.getTrinketComponent(entity)
            .map { component -> !component.isEquipped(this) }
            .orElse(false)!!
    }

    override fun getTooltipImage(stack: ItemStack): Optional<TooltipComponent> {
        return Optional.of(DogTagImageComponent(stack))
    }

    @Environment(EnvType.CLIENT)
    override fun getItemScreen(stack: ItemStack, player: Player, hand: InteractionHand): Screen {
        return DogTagEditorScreen(stack, hand)
    }

    override fun onInteractVehicle(
        vehicle: VehicleEntity,
        stack: ItemStack,
        player: Player,
        hand: InteractionHand
    ): InteractionResult? {
        if (!player.isShiftKeyDown) return null
        vehicle.dogTagIcon = getColors(stack).map { it.toList() }.toList()
        return InteractionResult.SUCCESS
    }

    companion object {
        @JvmStatic
        fun getColors(stack: ItemStack): Array<ShortArray> {
            val colors = Array(16) { ShortArray(16) }

            for (row in colors) {
                Arrays.fill(row, (-1).toShort())
            }

            val data = stack.get(ModDataComponents.DOG_TAG_IMAGE)
                .takeIf { !it.isNullOrEmpty() }
                ?: return colors

            for (i in colors.indices) {
                val color = data.getOrNull(i)
                    ?.takeIf { it.isNotEmpty() }
                    ?: continue

                for (j in colors[i].indices) {
                    val value = color.getOrNull(j) ?: continue
                    colors[i][j] = value
                }
            }

            return colors
        }
    }
}