package com.atsuishio.superbwarfare.init

import net.minecraft.world.item.Rarity
import net.minecraft.network.chat.Style
import java.util.function.UnaryOperator

object ModRarities {
    @JvmField
    val LEGENDARY = ModEnumExtensions.legendary

    @JvmField
    val SUPERB: Rarity = ModEnumExtensions.superb

    @JvmField
    val BEAST: Rarity = ModEnumExtensions.beast

    @JvmField
    val VIRTUAL: Rarity = ModEnumExtensions.virtual
}

val Rarity.styleModifier: UnaryOperator<Style>
    get() = UnaryOperator { style ->
        when (this) {
            ModRarities.BEAST -> style.withColor(0xa56855)
            ModRarities.VIRTUAL -> style.withColor(0xFF9AAF)
            else -> style.withColor(color())
        }
    }
