package com.atsuishio.superbwarfare.item.gun.machinegun;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import com.atsuishio.superbwarfare.client.renderer.gun.DevotionItemRenderer;
import com.atsuishio.superbwarfare.item.gun.GunGeoItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import java.util.function.Supplier;

public class DevotionItem extends GunGeoItem {

    public DevotionItem() {
        super(new Item.Properties().rarity(Rarity.EPIC));
    }

    @Override
    @Environment(EnvType.CLIENT)
    public Supplier<? extends GeoItemRenderer<? extends Item>> getRenderer() {
        return DevotionItemRenderer::new;
    }

}
