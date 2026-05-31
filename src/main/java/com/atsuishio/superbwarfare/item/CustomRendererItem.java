package com.atsuishio.superbwarfare.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.world.item.Item;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import java.util.function.Supplier;

public interface CustomRendererItem {
    @Environment(EnvType.CLIENT)
    Supplier<? extends GeoItemRenderer<? extends Item>> getRenderer();
}
