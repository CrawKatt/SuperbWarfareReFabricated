package com.atsuishio.superbwarfare.item.weapon

import com.atsuishio.superbwarfare.init.RegistryName
import com.atsuishio.superbwarfare.item.gun.special.HammerItem
import net.minecraft.world.item.ItemStack

@RegistryName("netherite_hammer")
class NetheriteHammerItem : HammerItem(Properties().fireResistant()) {
    override fun isDamageable(stack: ItemStack?) = false
}
