package com.atsuishio.superbwarfare.tools;

import com.atsuishio.superbwarfare.entity.vehicle.DroneEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.UUID;

public class EntityFindUtil {

    /**
     * 获取世界里的所有实体，对ServerLevel有效
     *
     * @param level 目标世界
     * @return 所有实体
     */
    public static Iterable<Entity> getEntities(Level level) {
        if (level instanceof ServerLevel serverLevel) {
            var bounds = AABB.ofSize(Vec3.ZERO, 3.0E7, 3.0E7, 3.0E7);
            return serverLevel.getEntitiesOfClass(Entity.class, bounds, e -> true);
        }
        return List.of();
    }

    /**
     * 查找当前已知实体
     *
     * @param level      实体所在世界
     * @param uuidString 目标实体UUID字符串
     * @return 目标实体或null
     */
    public static Entity findEntity(Level level, String uuidString) {
        try {
            var uuid = UUID.fromString(uuidString);
            if (level instanceof ServerLevel serverLevel) {
                return serverLevel.getEntity(uuid);
            }
            return null;
        } catch (Exception ignored) {
        }

        return null;
    }

    public static Player findPlayer(Level level, String uuidString) {
        var target = findEntity(level, uuidString);
        if (target instanceof Player player) {
            return player;
        }

        return null;
    }

    public static DroneEntity findDrone(Level level, String uuidString) {
        var target = findEntity(level, uuidString);
        if (target instanceof DroneEntity drone) {
            return drone;
        }

        return null;
    }

}
