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
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.GameRules

object EntityUseGunEventHandler {

    /**
     * 生物加入世界时决定「发枪 / 还原」。
     *
     * 已登记过配置键的生物（读档、换维度、区块重载）按该键恢复 goal；
     * 旧存档里的普通生物不补发枪；新生成的生物则按数据包配置抽取。
     */
    @JvmStatic
    fun entityJoin(entity: Entity, loadedFromDisk: Boolean) {
        val mob = entity as? Mob ?: return
        if (mob.level().isClientSide) return

        if (MobGunState.selectionKey(mob) != null) {
            MobGunData.restore(mob)
            return
        }

        if (loadedFromDisk || !SpawnConfig.SPAWN_MOB_WITH_GUNS.get()) return
        MobGunData.grant(mob)
    }

    /**
     * 持枪生物的掉落由数据包逐条策略配置（[GunDropData]），服务器配置只作为兜底默认值。
     * Este callback se registra antes que los consumidores de drops, como PowerfulAttraction.
     */
    @JvmStatic
    fun onLivingDrops(entity: LivingEntity, drops: MutableCollection<ItemEntity>) {
        val mob = entity as? Mob ?: return
        if (mob.level().isClientSide || MobGunState.selectionKey(mob) == null) return

        // La configuración de mob gun controla las armas del mob, no las que lleve un jugador.
        fun isMobGun(stack: ItemStack) = stack.item is GunItem

        // Sustituye el drop vanilla de equipo: el contenido y la probabilidad vienen de la estrategia.
        val vanillaDrops = drops.filter { isMobGun(it.item) }
        if (vanillaDrops.isNotEmpty()) drops.removeAll(vanillaDrops.toSet())

        val drop = MobGunData.from(mob)?.selection?.spawn?.drop
        if (drop.effectivePlayerKillOnly && mob.lastHurtByPlayerTime <= 0) return

        val level = mob.level() as? ServerLevel ?: return
        if (!level.gameRules.getBoolean(GameRules.RULE_DOMOBLOOT)) return

        val chance = drop.effectiveChance
        if (chance <= 0.0 || level.random.nextDouble() >= chance) return

        val stack = vanillaDrops.firstOrNull()?.item
            ?: mob.mainHandItem.takeIf(::isMobGun)
            ?: return

        sanitizeDrop(stack, drop)
        drops += ItemEntity(level, mob.x, mob.y + 0.5, mob.z, stack)
    }

    private fun sanitizeDrop(stack: ItemStack, drop: GunDropData?) {
        val gunData = GunData.from(stack)

        if (drop.effectiveStripOverride) gunData.propertyOverrideString.set("")
        if (drop.effectiveClearAmmo) {
            gunData.ammo.set(0)
            gunData.virtualAmmo.set(0)
        }

        gunData.save()
    }
}
