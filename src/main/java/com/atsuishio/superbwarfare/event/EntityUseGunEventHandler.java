package com.atsuishio.superbwarfare.event;

import com.atsuishio.superbwarfare.config.server.SpawnConfig;
import com.atsuishio.superbwarfare.data.mob_guns.MobGunData;
import com.atsuishio.superbwarfare.entity.goal.GunShootGoal;
import com.atsuishio.superbwarfare.mixins.MobAccessor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;

public class EntityUseGunEventHandler {

    public static void entityJoin(Entity entity) {
        if (!SpawnConfig.SPAWN_MOB_WITH_GUNS.get()) return;

        if (!(entity instanceof Mob mob)) return;

        var data = MobGunData.from(mob);

        if (data == null || data.probability() <= 0 || data.probability() < entity.level().random.nextDouble()) {
            return;
        }

        var gunData = data.getGunData();
        if (gunData == null) {
            return;
        }

        // TODO 正确处理权重
        ((MobAccessor) mob).getGoalSelector().addGoal(data.goalWeight(), new GunShootGoal<>(mob, data));

        if (data.backupAmmoCount() > 0) {
            gunData.virtualAmmo.set(data.backupAmmoCount());
        }

        if (data.spawnWithLoadedAmmo()) {
            gunData.reloadAmmo(mob);
        }

        gunData.save();

        mob.setItemInHand(InteractionHand.MAIN_HAND, gunData.stack);
    }
}
