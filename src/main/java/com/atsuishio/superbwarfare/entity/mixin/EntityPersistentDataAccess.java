package com.atsuishio.superbwarfare.entity.mixin;

import com.atsuishio.superbwarfare.capability.PersistentDataAccessor;
import net.minecraft.world.entity.Entity;

public interface EntityPersistentDataAccess extends PersistentDataAccessor {

    static EntityPersistentDataAccess of(Entity entity) {
        return (EntityPersistentDataAccess) entity;
    }

}
