package com.atsuishio.superbwarfare.item.blockitem

import com.atsuishio.superbwarfare.init.ModBlocks
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.TooltipFlag
import team.reborn.energy.api.EnergyStorage
import team.reborn.energy.api.base.InfiniteEnergyStorage
import javax.annotation.ParametersAreNonnullByDefault

class CreativeChargingStationBlockItem :
    BlockItem(ModBlocks.CREATIVE_CHARGING_STATION, Properties().rarity(Rarity.EPIC).stacksTo(1)) {
    private val energy = InfiniteEnergyStorage.INSTANCE

    val energyStorage: EnergyStorage
        get() = energy

    @ParametersAreNonnullByDefault
    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltipComponents: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {
        tooltipComponents.add(
            Component.translatable("des.superbwarfare.creative_charging_station").withStyle(ChatFormatting.GRAY)
        )
    }
}
