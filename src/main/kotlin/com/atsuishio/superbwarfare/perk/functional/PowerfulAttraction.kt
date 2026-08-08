package com.atsuishio.superbwarfare.perk.functional

import com.atsuishio.superbwarfare.data.gun.GunData
import com.atsuishio.superbwarfare.item.gun.GunItem
import com.atsuishio.superbwarfare.perk.Perk
import com.atsuishio.superbwarfare.tools.DamageTypeTool
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

object PowerfulAttraction : Perk("powerful_attraction", Type.FUNCTIONAL) {
    private val CURRENT_DROP_SOURCE = ThreadLocal<DamageSource?>()

    @JvmStatic
    fun beginDropCapture(source: DamageSource?) {
        CURRENT_DROP_SOURCE.set(source)
    }

    @JvmStatic
    fun endDropCapture() {
        CURRENT_DROP_SOURCE.remove()
    }

    @JvmStatic
    fun tryMoveCurrentDropToPlayer(drop: ItemStack): Boolean {
        return tryMoveDropToPlayer(CURRENT_DROP_SOURCE.get(), drop)
    }

    @JvmStatic
    fun tryMoveDropToPlayer(source: DamageSource?, drop: ItemStack): Boolean {
        if (source == null || drop.isEmpty) return false

        val player = source.entity as? Player ?: return false
        val stack = player.mainHandItem

        if (stack.item !is GunItem) return false

        val level = GunData.from(stack).perk.getLevel(this)

        if (level <= 0) return false
        if (!DamageTypeTool.isGunDamage(source) && !source.`is`(DamageTypes.PLAYER_ATTACK)) return false

        val copy = drop.copy()

        if (!player.addItem(copy)) {
            CURRENT_DROP_SOURCE.remove()
            try {
                player.drop(copy, false)
            } finally {
                CURRENT_DROP_SOURCE.set(source)
            }
        }

        return true
    }

    @JvmStatic
    fun handleExperienceDrop(player: Player?, source: DamageSource?, originalXp: Int): Int {
        if (player == null || source == null) return originalXp

        val stack = player.mainHandItem

        if (stack.item !is GunItem) return originalXp

        val level = GunData.from(stack).perk.getLevel(this)

        if (level <= 0) return originalXp
        if (!DamageTypeTool.isGunDamage(source) && !source.`is`(DamageTypes.PLAYER_ATTACK)) return originalXp

        player.giveExperiencePoints((originalXp * (0.8f + 0.2f * level)).toInt())

        return 0
    }
}
