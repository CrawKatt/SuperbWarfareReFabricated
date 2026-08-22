package com.atsuishio.superbwarfare.entity.vehicle.base

import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level

open class SpArtilleryEntity(type: EntityType<*>, world: Level) : ArtilleryEntity(type, world) {
    override fun canBind() = true
}
