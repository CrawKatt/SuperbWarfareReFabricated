package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.Mod
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.RangedAttribute

object ModAttributes {
    @JvmField
    val BULLET_RESISTANCE: Attribute = Registry.register(
        BuiltInRegistries.ATTRIBUTE,
        Mod.loc("bullet_resistance"),
        RangedAttribute(
            "attribute.${Mod.MODID}.bullet_resistance",
            0.0,
            0.0,
            1.0
        ).setSyncable(true)
    )

    @JvmStatic
    fun bulletResistanceHolder(): Holder<Attribute> {
        return BuiltInRegistries.ATTRIBUTE
            .getHolder(
                BuiltInRegistries.ATTRIBUTE
                    .getResourceKey(BULLET_RESISTANCE)
                    .orElseThrow()
            )
            .orElseThrow()
    }

    @JvmStatic
    fun init() {
    }
}