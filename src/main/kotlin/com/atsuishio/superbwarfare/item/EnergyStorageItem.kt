package com.atsuishio.superbwarfare.item

import net.minecraft.world.item.ItemStack

interface EnergyStorageItem {
    fun getMaxEnergy(stack: ItemStack): Int

    fun getMaxReceiveEnergy(stack: ItemStack) = getMaxEnergy(stack)

    fun getMaxExtractEnergy(stack: ItemStack) = getMaxEnergy(stack)
}
