package com.atsuishio.superbwarfare.item.trinket

import dev.emi.trinkets.api.SlotReference
import dev.emi.trinkets.api.TrinketItem
import dev.emi.trinkets.api.TrinketsApi
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack

class ThermalImagingGogglesItem : TrinketItem(Properties().stacksTo(1)) {
    override fun canEquip(stack: ItemStack, slot: SlotReference, entity: LivingEntity): Boolean {
        return TrinketsApi.getTrinketComponent(entity)
            .map { component -> !component.isEquipped(this) }
            .orElse(false)!!
    }

    override fun tick(stack: ItemStack, slot: SlotReference, entity: LivingEntity) {
        if (!entity.level().isClientSide) {
            entity.addEffect(MobEffectInstance(MobEffects.NIGHT_VISION, 3, 0, false, false))
        }
    }
}
