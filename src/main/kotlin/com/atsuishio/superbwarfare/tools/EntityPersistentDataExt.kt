package com.atsuishio.superbwarfare.tools

import com.atsuishio.superbwarfare.capability.PersistentDataAccessor
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.Entity

/**
 * Fabric replacement for Forge's `Entity#getPersistentData()`.
 * Backed by [EntityPersistentDataMixin] via [PersistentDataAccessor].
 */
val Entity.persistentData: CompoundTag
    get() = (this as PersistentDataAccessor).`superbwarfare$getPersistentData`()
