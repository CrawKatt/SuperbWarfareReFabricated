package com.atsuishio.superbwarfare.item.curio

import com.atsuishio.superbwarfare.client.TooltipTool
import com.atsuishio.superbwarfare.config.server.MapConfig
import com.atsuishio.superbwarfare.init.ModItems
import com.atsuishio.superbwarfare.init.ModKeyMappings
import com.atsuishio.superbwarfare.network.message.receive.OpenTacticalMapScreenMessage
import com.atsuishio.superbwarfare.tools.sendPacket
import dev.emi.trinkets.api.SlotReference
import dev.emi.trinkets.api.Trinket
import dev.emi.trinkets.api.TrinketsApi
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

open class TacticalTerminalItem : Item(Properties().stacksTo(1).rarity(Rarity.UNCOMMON)), Trinket {
    override fun canEquip(stack: ItemStack, slot: SlotReference, entity: LivingEntity): Boolean {
        return TrinketsApi.getTrinketComponent(entity)
            .map { !it.isEquipped(this) }
            .orElse(false)
    }

    override fun appendHoverText(
        stack: ItemStack,
        level: Level?,
        tooltip: MutableList<Component>,
        flag: TooltipFlag
    ) {
        TooltipTool.addDevelopingText(tooltip)
        if (!MapConfig.ENABLE_TACTICAL_MAP.get()) {
            tooltip.add(
                Component.translatable("des.superbwarfare.tactical_terminal.disabled").withStyle(ChatFormatting.RED)
            )
        }
        tooltip.add(
            Component.translatable(
                "des.superbwarfare.tactical_terminal",
                Component.literal("[${ModKeyMappings.TOGGLE_TACTICAL_MAP.getTranslatedKeyMessage().string}]")
                    .withStyle(ChatFormatting.AQUA)
            ).withStyle(ChatFormatting.GRAY)
        )
    }

    override fun use(
        pLevel: Level,
        pPlayer: Player,
        pUsedHand: InteractionHand
    ): InteractionResultHolder<ItemStack> {
        val stack = pPlayer.getItemInHand(pUsedHand)
        if (!MapConfig.ENABLE_TACTICAL_MAP.get()) {
            return InteractionResultHolder.fail(stack)
        }

        val level = pPlayer.level()
        if (!level.isClientSide) {
            pPlayer.sendPacket(OpenTacticalMapScreenMessage)
        }
        return InteractionResultHolder.consume(stack)
    }

    companion object {
        @JvmStatic
        fun isTerminalEquipped(entity: LivingEntity?): Boolean {
            if (entity == null) return false
            return TrinketsApi.getTrinketComponent(entity)
                .map { it.isEquipped(ModItems.TACTICAL_TERMINAL) }
                .orElse(false)
        }
    }
}
