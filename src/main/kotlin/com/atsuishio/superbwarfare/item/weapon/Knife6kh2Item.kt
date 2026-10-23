package com.atsuishio.superbwarfare.item.weapon

import com.atsuishio.superbwarfare.init.RegistryName
import com.atsuishio.superbwarfare.item.CustomDamageProperty
import com.atsuishio.superbwarfare.tiers.ModItemTier
import net.minecraft.world.item.SwordItem

@RegistryName("knife_6kh2")
class Knife6kh2Item : SwordItem(
    ModItemTier.STEEL, CustomDamageProperty(1600).attributes(createAttributes(ModItemTier.STEEL, 4, -1.8F))
) {

}
