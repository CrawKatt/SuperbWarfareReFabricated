package com.atsuishio.superbwarfare.event;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.config.server.SpawnConfig;
import com.atsuishio.superbwarfare.data.mob_guns.MobGunData;
import com.atsuishio.superbwarfare.entity.goal.GunShootGoal;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Mob;

public class EntityUseGunEventHandler {

    // TODO: Register in Mod.java using Fabric event API
    public static void entityJoin(Mob mob) {
        if (!SpawnConfig.SPAWN_MOB_WITH_GUNS.get()) return;

        var data = MobGunData.from(mob);

        if (data == null || data.probability() <= 0 || data.probability() < mob.level().random.nextDouble()) {
            return;
        }

        var gunData = data.getGunData();
        if (gunData == null) {
            return;
        }

        mob.goalSelector.addGoal(data.goalWeight(), new GunShootGoal<>(mob, data));

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
