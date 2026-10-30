package com.atsuishio.superbwarfare.item.blockitem

import com.atsuishio.superbwarfare.init.ModBlocks
import com.atsuishio.superbwarfare.init.RegistryName
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

@RegistryName("resonance_base")
class ResonanceBaseBlockItem : BlockItem(ModBlocks.RESONANCE_BASE, Properties().rarity(Rarity.EPIC)) {
    override fun appendHoverText(
        stack: ItemStack,
        level: Level?,
        tooltip: MutableList<Component>,
        flag: TooltipFlag
    ) {
        tooltip.add(Component.translatable("des.superbwarfare.resonance_base").withStyle(ChatFormatting.GRAY))
    }
}
