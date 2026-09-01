package com.atsuishio.superbwarfare.item.gun.handgun;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import com.atsuishio.superbwarfare.client.GunRendererBuilder;
import com.atsuishio.superbwarfare.client.model.item.Mp443ItemModel;
import com.atsuishio.superbwarfare.item.gun.GunGeoItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import java.util.function.Supplier;

public class Mp443Item extends GunGeoItem {

    public Mp443Item() {
        super(new Properties().rarity(Rarity.COMMON));
    }

    @Override
    @Environment(EnvType.CLIENT)
    public Supplier<? extends GeoItemRenderer<? extends Item>> getRenderer() {
        return GunRendererBuilder.simple(Mp443ItemModel::new);
    }

}
