package com.atsuishio.superbwarfare.item;

import com.atsuishio.superbwarfare.client.tooltip.component.ChargingStationImageComponent;
import team.reborn.energy.api.EnergyStorage;
import com.atsuishio.superbwarfare.init.ModBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;

public class ChargingStationBlockItem extends BlockItem {

    public static final int MAX_ENERGY = 4000000;

    public ChargingStationBlockItem() {
        super(ModBlocks.CHARGING_STATION, new Properties().stacksTo(1));
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        long energy = stack.getOrDefault(EnergyStorage.ENERGY_COMPONENT, 0L);
        return energy != MAX_ENERGY && energy != 0;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        long energy = stack.getOrDefault(EnergyStorage.ENERGY_COMPONENT, 0L);
        return Math.round(energy * 13F / MAX_ENERGY);
    }

    @Override
    public int getBarColor(@NotNull ItemStack pStack) {
        return 0xFFFF00;
    }

    @Override
    public @NotNull Optional<TooltipComponent> getTooltipImage(@NotNull ItemStack pStack) {
        return Optional.of(new ChargingStationImageComponent(pStack));
    }

    @Override
    @ParametersAreNonnullByDefault
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("des.superbwarfare.charging_station").withStyle(ChatFormatting.GRAY));
    }
}
