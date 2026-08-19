package com.atsuishio.superbwarfare.init

import fuzs.extensibleenums.api.extensibleenums.v1.BuiltInEnumFactories
import net.minecraft.ChatFormatting
import net.minecraft.world.item.Rarity

object ModRarities {
    @JvmField
    val LEGENDARY: Rarity = BuiltInEnumFactories.createRarity("superbwarfare_legendary", ChatFormatting.GOLD)

    @JvmField
    val SUPERB: Rarity = BuiltInEnumFactories.createRarity("superbwarfare_superb", ChatFormatting.RED)

    @JvmField
    val VIRTUAL: Rarity = BuiltInEnumFactories.createRarity("superbwarfare_virtual", ChatFormatting.WHITE)
}
