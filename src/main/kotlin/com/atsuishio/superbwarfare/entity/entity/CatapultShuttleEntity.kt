package com.atsuishio.superbwarfare.entity.entity

import com.atsuishio.superbwarfare.init.ModEntities
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MoverType
import net.minecraft.world.level.Level
import net.minecraftforge.network.NetworkHooks

open class CatapultShuttleEntity(type: EntityType<out CatapultShuttleEntity>, world: Level) : Entity(type, world) {

    override fun getAddEntityPacket(): Packet<ClientGamePacketListener> {
        return NetworkHooks.getEntitySpawningPacket(this)
    }

    constructor(level: Level) : this(ModEntities.CATAPULT_SHUTTLE.get(), level)

    override fun isPickable(): Boolean {
        return !this.isRemoved
    }

    override fun defineSynchedData() {
    }

    override fun tick() {
        super.tick()
        val f = 0.8
        this.deltaMovement = this.deltaMovement.multiply(f, 0.0, f)
        this.move(MoverType.SELF, this.deltaMovement)
    }

    public override fun addAdditionalSaveData(compound: CompoundTag) {
    }

    public override fun readAdditionalSaveData(compound: CompoundTag) {
    }
}
