package com.atsuishio.superbwarfare.item.armor;

import com.atsuishio.superbwarfare.client.renderer.armor.RuChest6b43ArmorRenderer;
import com.atsuishio.superbwarfare.tiers.ModArmorMaterials;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import java.util.function.Supplier;

public class RuChest6b43 extends BulletResistantArmor {

    public RuChest6b43() {
        super(Holder.direct(ModArmorMaterials.CEMENTED_CARBIDE),
                Type.CHESTPLATE,
                new Properties().durability(Type.CHESTPLATE.getDurability(50)),
                0.5f
        );
    }

    
    @Override
    public Supplier<GeoArmorRenderer<? extends Item>> getRenderer() {
        return RuChest6b43ArmorRenderer::new;
    }
}
