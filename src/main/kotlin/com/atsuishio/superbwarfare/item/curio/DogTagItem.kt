package com.atsuishio.superbwarfare.item.curio

import com.atsuishio.superbwarfare.client.TooltipTool
import com.atsuishio.superbwarfare.client.screens.DogTagEditorScreen
import com.atsuishio.superbwarfare.client.tooltip.component.DogTagImageComponent
import com.atsuishio.superbwarfare.item.ItemScreenProvider
import dev.emi.trinkets.api.SlotReference
import dev.emi.trinkets.api.Trinket
import dev.emi.trinkets.api.TrinketsApi
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.tooltip.TooltipComponent
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import java.util.*

class DogTagItem : Item(Properties().stacksTo(1)), Trinket, ItemScreenProvider {
    @Environment(EnvType.CLIENT)
    override fun appendHoverText(
        pStack: ItemStack,
        pLevel: Level?,
        pTooltipComponents: MutableList<Component>,
        pIsAdvanced: TooltipFlag
    ) {
        TooltipTool.addScreenProviderText(pTooltipComponents)
    }

    override fun canEquip(stack: ItemStack, slot: SlotReference, entity: LivingEntity): Boolean {
        return TrinketsApi.getTrinketComponent(entity)
            .map { !it.isEquipped(this) }
            .orElse(false)
    }

    override fun getTooltipImage(pStack: ItemStack): Optional<TooltipComponent> {
        return Optional.of(DogTagImageComponent(pStack))
    }

    @Environment(EnvType.CLIENT)
    override fun getItemScreen(stack: ItemStack, player: Player, hand: InteractionHand): Screen {
        return DogTagEditorScreen(stack, hand)
    }

    companion object {
        @JvmStatic
        fun getColors(stack: ItemStack): Array<ShortArray> {
            val colors: Array<ShortArray> = Array(16) { ShortArray(16) }
            for (el in colors) {
                Arrays.fill(el, (-1).toShort())
            }

            if (stack.tag == null) return colors
            val tag = stack.tag!!.getCompound("Colors")
            for (i in 0..15) {
                val color = tag.getIntArray("Color$i")
                for (j in color.indices) {
                    colors[i][j] = color[j].toShort()
                }
            }

            return colors
        }
    }
}
