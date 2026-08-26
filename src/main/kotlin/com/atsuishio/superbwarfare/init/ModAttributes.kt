package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.Mod
import io.github.fabricators_of_create.porting_lib.attributes.PortingLibAttributes
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.RangedAttribute

object ModAttributes {
    @JvmField
    val BULLET_RESISTANCE: Attribute = Registration.attribute("bullet_resistance") {
        RangedAttribute("attribute.${Mod.MODID}.bullet_resistance", 0.0, 0.0, 1.0).setSyncable(true)
    }

    @JvmField
    val BLOCK_REACH: Attribute = PortingLibAttributes.BLOCK_REACH

    @JvmField
    val ENTITY_REACH: Attribute = PortingLibAttributes.ENTITY_REACH

    @JvmStatic
    fun init() = Unit
}
