package com.atsuishio.superbwarfare.item;

import com.atsuishio.superbwarfare.capability.energy.ModEnergyApi;
import com.atsuishio.superbwarfare.client.tooltip.component.CellImageComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

public class BatteryItem extends Item {

    private final Supplier<Integer> energyCapacity;
    public int maxEnergy;

    public BatteryItem(int maxEnergy, Properties properties) {
        super(properties.stacksTo(1));
        this.maxEnergy = maxEnergy;
        this.energyCapacity = () -> maxEnergy;
    }

    @Override
    public boolean isBarVisible(ItemStack pStack) {
        return ModEnergyApi.getEnergyStored(pStack) != maxEnergy;
    }

    @Override
    public int getBarWidth(ItemStack pStack) {
        var energy = ModEnergyApi.getEnergyStored(pStack);

        return Math.round(energy * 13F / maxEnergy);
    }

    @Override
    public int getBarColor(@NotNull ItemStack pStack) {
        return 0xFFFF00;
    }

    @Override
    public @NotNull Optional<TooltipComponent> getTooltipImage(@NotNull ItemStack pStack) {
        return Optional.of(new CellImageComponent(pStack));
    }

    public ItemStack makeFullEnergyStack() {
        ItemStack stack = new ItemStack(this);
        ModEnergyApi.receiveEnergy(stack, maxEnergy, false);
        return stack;
    }
}
