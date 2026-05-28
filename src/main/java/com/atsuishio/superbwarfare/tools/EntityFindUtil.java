package com.atsuishio.superbwarfare.tools;

import com.atsuishio.superbwarfare.entity.vehicle.DroneEntity;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.AbortableIterationConsumer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.function.Consumer;

import java.util.UUID;

public class EntityFindUtil {

    /**
     * 获取世界里的所有实体，对ClientLevel和ServerLevel均有效
     *
     * @param level 目标世界
     * @return 所有实体
     */
    public static LevelEntityGetter<Entity> getEntities(Level level) {
        if (level instanceof ServerLevel serverLevel) {
            return new LevelEntityGetter<>() {
                @Override
                public Entity get(int id) {
                    return serverLevel.getEntity(id);
                }

                @Override
                public Entity get(UUID uuid) {
                    return serverLevel.getEntity(uuid);
                }

                @Override
                public Iterable<Entity> getAll() {
                    return serverLevel.getEntities((Entity) null,
                            new AABB(-3.0E7, -64.0, -3.0E7, 3.0E7, 320.0, 3.0E7),
                            e -> true);
                }

                @Override
                public <U extends Entity> void get(EntityTypeTest<Entity, U> p_156935_, AbortableIterationConsumer<U> p_261602_) {
                    for (Entity entity : getAll()) {
                        U cast = p_156935_.tryCast(entity);
                        if (cast != null) {
                            if (p_261602_.accept(cast).shouldAbort()) return;
                        }
                    }
                }

                @Override
                public void get(AABB p_156937_, Consumer<Entity> p_156938_) {
                    for (Entity entity : getAll()) {
                        if (p_156937_.contains(entity.getX(), entity.getY(), entity.getZ())) {
                            p_156938_.accept(entity);
                        }
                    }
                }

                @Override
                public <U extends Entity> void get(EntityTypeTest<Entity, U> p_156932_, AABB p_156933_, AbortableIterationConsumer<U> p_261542_) {
                    for (Entity entity : getAll()) {
                        if (p_156933_.contains(entity.getX(), entity.getY(), entity.getZ())) {
                            U cast = p_156932_.tryCast(entity);
                            if (cast != null) {
                                if (p_261542_.accept(cast).shouldAbort()) return;
                            }
                        }
                    }
                }
            };
        }
        return ((ClientLevel) level).getEntities();
    }

    /**
     * 查找当前已知实体，对ClientLevel和ServerLevel均有效
     *
     * @param level      实体所在世界
     * @param uuidString 目标实体UUID字符串
     * @return 目标实体或null
     */
    public static Entity findEntity(Level level, String uuidString) {
        try {
            var uuid = UUID.fromString(uuidString);
            Entity target;

            if (level instanceof ServerLevel serverLevel) {
                target = serverLevel.getEntity(uuid);
            } else {
                var clientLevel = (ClientLevel) level;
                target = clientLevel.getEntities().get(uuid);
            }
            return target;
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
