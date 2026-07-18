package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.Mod;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

import java.util.function.Supplier;

public class ModAttributes {
    public static final Supplier<Attribute> BULLET_RESISTANCE = Registration.attribute("bullet_resistance",
            () -> new RangedAttribute("attribute." + Mod.MODID + ".bullet_resistance", 0, 0, 1).setSyncable(true));
    public static final Supplier<Attribute> BLOCK_REACH = Registration.attribute("block_reach",
            () -> new RangedAttribute("attribute." + Mod.MODID + ".block_reach", 4.5, 0, 1024).setSyncable(true));
    public static final Supplier<Attribute> ENTITY_REACH = Registration.attribute("entity_reach",
            () -> new RangedAttribute("attribute." + Mod.MODID + ".entity_reach", 3, 0, 1024).setSyncable(true));

    public static void register() {
        BULLET_RESISTANCE.get();
        BLOCK_REACH.get();
        ENTITY_REACH.get();
    }
}
