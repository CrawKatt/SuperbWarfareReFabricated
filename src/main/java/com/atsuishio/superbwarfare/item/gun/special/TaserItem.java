package com.atsuishio.superbwarfare.item.gun.special;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import com.atsuishio.superbwarfare.client.renderer.gun.TaserItemRenderer;
import com.atsuishio.superbwarfare.data.gun.GunData;
import com.atsuishio.superbwarfare.data.gun.ShootParameters;
import com.atsuishio.superbwarfare.init.ModPerks;
import com.atsuishio.superbwarfare.capability.energy.ModEnergyApi;
import com.atsuishio.superbwarfare.item.BatteryItem;
import com.atsuishio.superbwarfare.item.gun.GunGeoItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

public class TaserItem extends GunGeoItem {

    public TaserItem() {
        super(new Item.Properties());
    }

    @Override
    @Environment(EnvType.CLIENT)
    public Supplier<? extends GeoItemRenderer<? extends Item>> getRenderer() {
        return TaserItemRenderer::new;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (entity instanceof Player player) {
            for (var cell : player.getInventory().items) {
                if (cell.getItem() instanceof BatteryItem) {
                    assert ModEnergyApi.get(stack) != null;
                    int stackMaxEnergy = ModEnergyApi.getMaxEnergyStored(stack);
                    int stackEnergy = ModEnergyApi.getEnergyStored(stack);

                    assert ModEnergyApi.get(cell) != null;
                    int cellEnergy = ModEnergyApi.getEnergyStored(cell);

                    int stackEnergyNeed = Math.min(cellEnergy, stackMaxEnergy - stackEnergy);

                    if (cellEnergy > 0) {
                        ModEnergyApi.receiveEnergy(stack, stackEnergyNeed, false);
                    }
                    ModEnergyApi.extractEnergy(cell, stackEnergyNeed, false);
                }
            }
        }
    }

    @Override
    public void afterShoot(@NotNull ShootParameters parameters) {
        super.afterShoot(parameters);

        var data = parameters.data();

        var stack = data.stack;
        int perkLevel = data.perk.getLevel(ModPerks.VOLT_OVERLOAD);
        ModEnergyApi.extractEnergy(stack, 400 + 100 * perkLevel, false);
    }

    @Override
    public boolean canShoot(GunData data, @Nullable Entity shooter) {
        var stack = data.stack;

        int perkLevel = data.perk.getLevel(ModPerks.VOLT_OVERLOAD);
        var hasEnoughEnergy = ModEnergyApi.getEnergyStored(stack) >= 400 + 100 * perkLevel;

        if (!hasEnoughEnergy) return false;

        return super.canShoot(data, shooter);
    }
}
