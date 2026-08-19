package com.atsuishio.superbwarfare.perk.functional

import com.atsuishio.superbwarfare.data.gun.GunData
import com.atsuishio.superbwarfare.item.gun.GunItem
import com.atsuishio.superbwarfare.perk.Perk
import com.atsuishio.superbwarfare.event.custom.LivingDropsCallback
import com.atsuishio.superbwarfare.event.custom.LootingLevelCallback
import com.atsuishio.superbwarfare.tools.DamageTypeTool
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player

object PowerfulAttraction : Perk("powerful_attraction", Type.FUNCTIONAL) {
    fun onLivingDrops(event: LivingDropsCallback.Event) {
        val source = event.source
        val player = source.entity as? Player ?: return
        val stack = player.mainHandItem

        if (stack.item !is GunItem) return

        val level = GunData.from(stack).perk.getLevel(this)

        if (level <= 0) return
        if (!DamageTypeTool.isGunDamage(source) && !source.`is`(DamageTypes.PLAYER_ATTACK)) return

        event.drops.forEach {
            val item = it.item
            if (!player.addItem(item.copy())) {
                player.drop(item, false)
            }
        }
        event.isCanceled = true
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

    fun onLootingLevel(event: LootingLevelCallback.Event) {
        val source = event.damageSource
        val sourceEntity = source.entity
        if (sourceEntity !is LivingEntity) return

        val stack = sourceEntity.mainHandItem
        if (stack.item !is GunItem) return

        val level = GunData.from(stack).perk.getLevel(this)
        if (level > 0 && (DamageTypeTool.isGunDamage(source) || source.`is`(DamageTypes.PLAYER_ATTACK))) {
            event.lootingLevel = level / 4
        }
    }
}
