package com.atsuishio.superbwarfare.item.armor;

import com.atsuishio.superbwarfare.client.renderer.armor.UsChestIotvArmorRenderer;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import java.util.function.Supplier;

public class UsChestIotv extends BulletResistantArmor {

    public UsChestIotv() {
        super(Holder.direct(ModArmorMaterials.CEMENTED_CARBIDE),
                Type.CHESTPLATE,
                new Properties().durability(Type.CHESTPLATE.getDurability(50)),
                0.5f
        );
    }

    
    @Override
    public Supplier<GeoArmorRenderer<? extends Item>> getRenderer() {
        return UsChestIotvArmorRenderer::new;
    }
}
