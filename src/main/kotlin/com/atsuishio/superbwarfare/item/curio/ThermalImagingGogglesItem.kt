package com.atsuishio.superbwarfare.item.curio

import dev.emi.trinkets.api.SlotReference
import dev.emi.trinkets.api.Trinket
import dev.emi.trinkets.api.TrinketsApi
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class ThermalImagingGogglesItem : Item(Properties().stacksTo(1)), Trinket {
    override fun canEquip(stack: ItemStack, slot: SlotReference, entity: LivingEntity): Boolean {
        return TrinketsApi.getTrinketComponent(entity)
            .map { !it.isEquipped(this) }
            .orElse(false)
    }

    override fun tick(stack: ItemStack, slot: SlotReference, entity: LivingEntity) {
        if (!entity.level().isClientSide) {
            entity.addEffect(MobEffectInstance(MobEffects.NIGHT_VISION, 3, 0, false, false))
        }
    }
}
