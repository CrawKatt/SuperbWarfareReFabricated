package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.Mod
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.RangedAttribute
import java.util.function.Supplier

object ModAttributes {
    @JvmField
    val BULLET_RESISTANCE: Attribute = Registration.attribute("bullet_resistance") {
        RangedAttribute("attribute.${Mod.MODID}.bullet_resistance", 0.0, 0.0, 1.0).setSyncable(true)
    }

    @JvmField
    val BLOCK_REACH: Attribute = Registration.attribute("block_reach") {
        RangedAttribute("attribute.${Mod.MODID}.block_reach", 4.5, 0.0, 1024.0).setSyncable(true)
    }

    @JvmField
    val ENTITY_REACH: Attribute = Registration.attribute("entity_reach") {
        RangedAttribute("attribute.${Mod.MODID}.entity_reach", 3.0, 0.0, 1024.0).setSyncable(true)
    }

    @JvmStatic
    fun init() = Unit
}
