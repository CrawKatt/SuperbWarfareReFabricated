package com.atsuishio.superbwarfare.event;

import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class EntityEventHandler {

    public static Vec3 cancelExplosionKnockback(Entity entity, Vec3 knockbackVelocity) {
        if (entity instanceof VehicleEntity) {
            return Vec3.ZERO;
        }
        return knockbackVelocity;
    }
}
