package com.atsuishio.superbwarfare.event

import com.atsuishio.superbwarfare.config.server.SpawnConfig
import com.atsuishio.superbwarfare.data.gun.GunData
import com.atsuishio.superbwarfare.data.mob_guns.GunDropData
import com.atsuishio.superbwarfare.data.mob_guns.MobGunData
import com.atsuishio.superbwarfare.data.mob_guns.MobGunState
import com.atsuishio.superbwarfare.data.mob_guns.effectiveChance
import com.atsuishio.superbwarfare.data.mob_guns.effectiveClearAmmo
import com.atsuishio.superbwarfare.data.mob_guns.effectivePlayerKillOnly
import com.atsuishio.superbwarfare.data.mob_guns.effectiveStripOverride
import com.atsuishio.superbwarfare.item.gun.GunItem
import com.atsuishio.superbwarfare.perk.functional.PowerfulAttraction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.GameRules

object EntityUseGunEventHandler {

    /** Reproduces EntityJoinLevelEvent, including its loaded-from-disk state. */
    @JvmStatic
    fun entityJoin(entity: Entity, loadedFromDisk: Boolean) {
        val mob = entity as? Mob ?: return
        if (mob.level().isClientSide) return

        if (MobGunState.selectionKey(mob) != null) {
            // The config switch controls new grants, not restoration of an existing gun.
            MobGunData.restore(mob)
            return
        }

        if (loadedFromDisk || !SpawnConfig.SPAWN_MOB_WITH_GUNS.get()) return

        MobGunData.grant(mob)
    }

    /**
     * Apply the gun-drop policy to captured death drops before vehicle collection processes them.
     */
    @JvmStatic
    fun onLivingDrops(entity: LivingEntity, source: DamageSource, drops: MutableCollection<ItemEntity>) {
        val mob = entity as? Mob ?: return
        if (mob.level().isClientSide || MobGunState.selectionKey(mob) == null) return

        fun isMobGun(stack: ItemStack) = stack.item is GunItem

        // Remove vanilla's equipment drop; this policy owns both its contents and chance.
        val vanillaDrops = drops.filter { isMobGun(it.item) }
        if (vanillaDrops.isNotEmpty()) drops.removeAll(vanillaDrops.toSet())

        val drop = MobGunData.from(mob)?.selection?.spawn?.drop
        if (drop.effectivePlayerKillOnly && mob.lastHurtByPlayerTime <= 0) return

        val level = mob.level() as? ServerLevel ?: return
        if (!level.gameRules.getBoolean(GameRules.RULE_DOMOBLOOT)) return

        val chance = drop.effectiveChance
        if (chance <= 0.0 || mob.level().random.nextDouble() >= chance) return

        val stack = vanillaDrops.firstOrNull()?.item
            ?: mob.mainHandItem.takeIf { isMobGun(it) }
            ?: return

        sanitizeDrop(stack, drop)

        // Powerful Attraction must see the stack only after its gun-drop policy is applied.
        if (!PowerfulAttraction.tryMoveDropToPlayer(source, stack)) {
            drops += ItemEntity(mob.level(), mob.x, mob.y + 0.5, mob.z, stack)
        }
    }

    /** Clear mob-only properties before the gun becomes a player drop. */
    private fun sanitizeDrop(stack: ItemStack, drop: GunDropData?) {
        val gunData = GunData.from(stack)

        if (drop.effectiveStripOverride) {
            gunData.propertyOverrideString.set("")
        }

        if (drop.effectiveClearAmmo) {
            gunData.ammo.set(0)
            gunData.virtualAmmo.set(0)
        }

        gunData.save()
    }
}
