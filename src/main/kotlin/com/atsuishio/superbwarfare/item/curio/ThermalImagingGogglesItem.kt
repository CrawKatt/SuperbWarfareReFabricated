package com.atsuishio.superbwarfare.item.curio

import com.atsuishio.superbwarfare.init.RegistryName
import dev.emi.trinkets.api.SlotReference
import dev.emi.trinkets.api.Trinket
import dev.emi.trinkets.api.TrinketsApi
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

@RegistryName("thermal_imaging_goggles")
open class ThermalImagingGogglesItem : Item(Properties().stacksTo(1)), Trinket {
    override fun canEquip(stack: ItemStack, slot: SlotReference, entity: LivingEntity): Boolean {
        return TrinketsApi.getTrinketComponent(entity)
            .map { !it.isEquipped(this) }
            .orElse(false)
    }
}
