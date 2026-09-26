package com.atsuishio.superbwarfare.item.weapon

import com.atsuishio.superbwarfare.init.RegistryName
import com.atsuishio.superbwarfare.item.CustomDamageProperty
import com.atsuishio.superbwarfare.tiers.ModItemTier
import net.minecraft.world.item.SwordItem

@RegistryName("knife")
open class KnifeItem : SwordItem(
    ModItemTier.STEEL,
    CustomDamageProperty(1600).attributes(createAttributes(ModItemTier.STEEL, 4, -1.8f))
) {

}
