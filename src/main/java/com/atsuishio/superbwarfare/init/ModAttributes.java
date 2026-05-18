package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.Mod;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class ModAttributes {
    public static final Attribute BULLET_RESISTANCE = Registry.register(
            BuiltInRegistries.ATTRIBUTE,
            Mod.loc("bullet_resistance"),
            new RangedAttribute("attribute." + Mod.MODID + ".bullet_resistance", 0, 0, 1).setSyncable(true)
    );

    public static Holder<Attribute> bulletResistanceHolder() {
        return BuiltInRegistries.ATTRIBUTE.getHolder(BuiltInRegistries.ATTRIBUTE.getResourceKey(BULLET_RESISTANCE).orElseThrow()).orElseThrow();
    }

    public static void init() {
    }
}
