package com.atsuishio.superbwarfare.item.armor;

import com.atsuishio.superbwarfare.client.renderer.armor.UsHelmetPasgtArmorRenderer;
import com.atsuishio.superbwarfare.tiers.ModArmorMaterials;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import java.util.function.Supplier;

public class UsHelmetPasgt extends BulletResistantArmor {

    public UsHelmetPasgt() {
        super(Holder.direct(ModArmorMaterials.CEMENTED_CARBIDE),
                Type.HELMET,
                new Properties().durability(Type.HELMET.getDurability(50)),
                0.2f
        );
    }

    
    @Override
    public Supplier<GeoArmorRenderer<? extends Item>> getRenderer() {
        return UsHelmetPasgtArmorRenderer::new;
    }
}
