package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.mixins.AttributeSupplierAccessor
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeInstance
import net.minecraft.world.entity.ai.attributes.DefaultAttributes
import net.minecraft.world.entity.ai.attributes.RangedAttribute
import java.util.HashMap

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
        val attribute = bulletResistanceHolder()
        BuiltInRegistries.ENTITY_TYPE
            .filter(DefaultAttributes::hasSupplier)
            .forEach { rawType ->
                @Suppress("UNCHECKED_CAST")
                val type = rawType as EntityType<out LivingEntity>
                val supplier = DefaultAttributes.getSupplier(type)
                val instances = HashMap((supplier as AttributeSupplierAccessor).`superbWarfare$getInstances`())
                instances.putIfAbsent(attribute, AttributeInstance(attribute) {})
                FabricDefaultAttributeRegistry.register(type, AttributeSupplierAccessor.`superbWarfare$create`(instances))
            }
    }
}
