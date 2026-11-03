package com.atsuishio.superbwarfare.item.armor

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.config.server.MiscConfig
import com.atsuishio.superbwarfare.init.ModAttributes
import com.google.common.collect.HashMultimap
import com.google.common.collect.ImmutableMultimap
import com.google.common.collect.Multimap
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.ItemStack
import java.util.*
import java.util.function.Consumer

abstract class BulletproofArmorItem(
    material: ArmorMaterial,
    type: Type,
    private val bulletResistance: Double,
) : ArmorItem(material, type, FabricItemSettings().customDamage { stack, amount, entity, onBroken ->
    (stack.item as BulletproofArmorItem).damageItem(stack, amount, entity, onBroken)
}) {
    open fun <T : LivingEntity> damageItem(
        stack: ItemStack,
        amount: Int,
        entity: T,
        onBroken: Consumer<T>,
    ): Int {
        if (!MiscConfig.MOD_ARMOR_UNBREAKABLE.get()) return amount
        val maxDamage = stack.maxDamage - 1
        if (stack.damageValue > maxDamage) stack.damageValue = maxDamage
        return amount.coerceIn(0, maxDamage - stack.damageValue)
    }

    override fun getAttributeModifiers(stack: ItemStack, slot: EquipmentSlot): Multimap<Attribute, AttributeModifier> {
        if (MiscConfig.MOD_ARMOR_UNBREAKABLE.get() && stack.damageValue >= stack.maxDamage - 1) {
            return ImmutableMultimap.of()
        }

        val map = HashMultimap.create<Attribute, AttributeModifier>(super.getDefaultAttributeModifiers(slot))
        if (slot == type.slot) {
            map.put(
                ModAttributes.BULLET_RESISTANCE,
                AttributeModifier(
                    UUID(slot.toString().hashCode().toLong(), 0),
                    Mod.ATTRIBUTE_MODIFIER,
                    bulletResistance * (1 - stack.damageValue.toDouble() / stack.maxDamage),
                    AttributeModifier.Operation.ADDITION
                )
            )
        }
        return map
    }
}
