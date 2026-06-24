package com.atsuishio.superbwarfare.item.armor;

import com.atsuishio.superbwarfare.client.renderer.armor.GeHelmetM35ArmorRenderer;
import com.atsuishio.superbwarfare.tiers.ModArmorMaterials;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import java.util.function.Supplier;

public class GeHelmetM35 extends BulletResistantArmor {

    public GeHelmetM35() {
        super(Holder.direct(ModArmorMaterials.STEEL),
                Type.HELMET,
                new Properties().durability(Type.HELMET.getDurability(35))
        );
    }

    
    @Override
    public Supplier<GeoArmorRenderer<? extends Item>> getRenderer() {
        return GeHelmetM35ArmorRenderer::new;
    }
}
