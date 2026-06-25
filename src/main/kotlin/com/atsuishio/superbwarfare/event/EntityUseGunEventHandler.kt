package com.atsuishio.superbwarfare.event

import com.atsuishio.superbwarfare.config.server.SpawnConfig
import com.atsuishio.superbwarfare.data.mob_guns.MobGunData
import com.atsuishio.superbwarfare.entity.goal.GunShootGoal
import com.atsuishio.superbwarfare.mixins.MobAccessor
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.Mob

object EntityUseGunEventHandler {
    @JvmStatic
    fun entityJoin(entity: Entity) {
        if (!SpawnConfig.SPAWN_MOB_WITH_GUNS.get()) return
        if (entity !is Mob) return

        val data = MobGunData.from(entity) ?: return
        if (data.probability() <= 0 || data.probability() < entity.level().random.nextDouble()) {
            return
        }

        val gunData = data.getGunData() ?: return

        // TODO 正确处理权重
        (entity as MobAccessor).goalSelector.addGoal(data.goalWeight(), GunShootGoal(entity, data))

        if (data.backupAmmoCount() > 0) {
            gunData.virtualAmmo.set(data.backupAmmoCount())
        }

        if (data.spawnWithLoadedAmmo()) {
            gunData.reloadAmmo(entity)
        }

        gunData.save()

        entity.setItemInHand(InteractionHand.MAIN_HAND, gunData.stack)
    }
}
