package com.atsuishio.superbwarfare.data.gun.ammo_consumer_strategy

import com.atsuishio.superbwarfare.data.gun.AmmoConsumer
import com.atsuishio.superbwarfare.data.gun.AmmoSource
import com.atsuishio.superbwarfare.data.gun.GunData
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import com.atsuishio.superbwarfare.capability.api.IItemHandler
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt

/**
 * exp
 * exp 10 (每1经验=10弹药)
 */
class ExpAmmoStrategy : AmmoConsumeStrategy() {

    override val defaultType = AmmoConsumer.AmmoConsumeType.ITEM

    var ammoPerXp: Float = 1F

    override fun create(): AmmoConsumeStrategy = ExpAmmoStrategy()

    override fun match(ammo: String) = ammo.lowercase().startsWith("exp", true)
            && ammo.lowercase() == "exp"
            || ammo.lowercase().substringAfter("exp").trimEnd().toFloatOrNull() != null

    override fun init(
        source: AmmoSource,
        count: Int,
        matchedString: String
    ) {
        super.init(source, count, matchedString)
        val extracted = matchedString.lowercase().substringAfter("exp").trimEnd().toFloatOrNull()?.coerceAtLeast(0F)
            ?: 1F
        ammoPerXp = if (extracted.isNaN() || extracted == 0F || extracted.isInfinite()) 1F else extracted
    }

    override fun consume(data: GunData, source: AmmoSource, shooter: Entity?, count: Int): Int {
        val player = (shooter as? Player) ?: return 0
        val consumed = count.coerceIn(0, count(data, source, player))
        if (consumed == 0) return 0
        val xpToConsume = ceil(consumed.toDouble() / ammoPerXp).toInt()
        player.giveExperiencePoints(-xpToConsume)
        return consumed
    }

    override fun consume(data: GunData, source: AmmoSource, handler: IItemHandler, count: Int) = 0

    override fun count(data: GunData, source: AmmoSource, entity: Entity?) =
        floor(((entity as? Player)?.let(::availableExperience) ?: 0).toDouble() * ammoPerXp).toInt()

    override fun count(data: GunData, source: AmmoSource, handler: IItemHandler?) = 0

    override fun withdraw(source: AmmoSource, ammoSupplier: Entity, count: Int): Int {
        val player = (ammoSupplier as? Player) ?: return 0
        val xpToRestore = floor(count.toDouble() / ammoPerXp).toInt()
        if (xpToRestore <= 0) return 0
        player.giveExperiencePoints(xpToRestore)
        return count
    }

    override fun withdraw(source: AmmoSource, handler: IItemHandler, count: Int) = 0

    // totalExperience is not updated by enchanting or /experience level changes.
    // Derive the spendable amount from the actual level and progress instead.
    private fun availableExperience(player: Player): Int {
        val level = player.experienceLevel.toDouble()
        val base = when {
            level <= 16 -> level * level + 6 * level
            level <= 31 -> (5 * level * level - 81 * level + 720) / 2
            else -> (9 * level * level - 325 * level + 4440) / 2
        }
        return (base + (player.experienceProgress * player.xpNeededForNextLevel).roundToInt())
            .coerceIn(0.0, Int.MAX_VALUE.toDouble()).toInt()
    }

    @Environment(EnvType.CLIENT)
    override fun getDisplayName(source: AmmoSource) = "Experience"
}
