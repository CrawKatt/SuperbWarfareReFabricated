package com.atsuishio.superbwarfare.item.weapon

import com.atsuishio.superbwarfare.init.RegistryName
import com.atsuishio.superbwarfare.tiers.ModItemTier
import net.minecraft.world.item.SwordItem

@RegistryName("knife_seitengewehr_84")
class KnifeSeitengewehr84Item : SwordItem(
    ModItemTier.STEEL, 4, -1.8F, Properties().durability(1600)
)
