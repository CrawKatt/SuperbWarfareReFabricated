package com.atsuishio.superbwarfare.item.gun.special;

import com.atsuishio.superbwarfare.client.renderer.gun.TaserItemRenderer;
import com.atsuishio.superbwarfare.data.gun.GunData;
import com.atsuishio.superbwarfare.data.gun.ShootParameters;
import com.atsuishio.superbwarfare.capability.energy.EnergyStorageHelper;
import team.reborn.energy.api.EnergyStorage;
import com.atsuishio.superbwarfare.init.ModPerks;
import com.atsuishio.superbwarfare.item.gun.GunGeoItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import java.util.function.Supplier;

public class TaserItem extends GunGeoItem {

    public TaserItem() {
        super(new Properties());
    }

    @Override
    public Supplier<? extends GeoItemRenderer<? extends Item>> getRenderer() {
        return TaserItemRenderer::new;
    }

    @Override
    public void afterShoot(@NotNull ShootParameters parameters) {
        super.afterShoot(parameters);

        var data = parameters.data;

        var stack = data.stack;
        int perkLevel = data.perk.getLevel(ModPerks.INSTANCE.getVOLT_OVERLOAD());

        var energyStorage = EnergyStorage.ITEM.find(stack, null);
        if (energyStorage != null) {
            EnergyStorageHelper.extract(energyStorage, 400 + 100L * perkLevel);
        }
    }

    @Override
    public boolean canShoot(GunData data, @Nullable Entity shooter) {
        int perkLevel = data.perk.getLevel(ModPerks.INSTANCE.getVOLT_OVERLOAD());

        var energyStorage = EnergyStorage.ITEM.find(data.stack, null);
        var hasEnoughEnergy = energyStorage != null && energyStorage.getAmount() >= 400 + 100 * perkLevel;

        if (!hasEnoughEnergy) return false;

        return super.canShoot(data, shooter);
    }
}
