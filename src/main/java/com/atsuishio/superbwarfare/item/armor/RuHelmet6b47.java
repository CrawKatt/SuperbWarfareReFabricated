package com.atsuishio.superbwarfare.item.armor;

import com.atsuishio.superbwarfare.client.renderer.armor.RuHelmet6b47ArmorRenderer;
import com.atsuishio.superbwarfare.init.ModArmorMaterials;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import java.util.function.Supplier;

public class RuHelmet6b47 extends BulletResistantArmor {

    public RuHelmet6b47() {
        super(Holder.direct(ModArmorMaterials.CEMENTED_CARBIDE),
                Type.HELMET,
                new Properties().durability(Type.HELMET.getDurability(50)),
                0.2f
        );
    }

    
    @Override
    public Supplier<GeoArmorRenderer<? extends Item>> getRenderer() {
        return RuHelmet6b47ArmorRenderer::new;
    }
}
