package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.Mod;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

import java.util.function.Supplier;

public class ModAttributes {
    public static final Supplier<Attribute> BULLET_RESISTANCE = Registration.attribute("bullet_resistance",
            () -> new RangedAttribute("attribute." + Mod.MODID + ".bullet_resistance", 0, 0, 1).setSyncable(true));
}
