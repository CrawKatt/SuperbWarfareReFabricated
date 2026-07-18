package com.atsuishio.superbwarfare.entity.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

public interface EntityPersistentDataAccess {

    static EntityPersistentDataAccess of(Entity entity) {
        return (EntityPersistentDataAccess) entity;
    }

    CompoundTag superbwarfare$getPersistentData();
}
