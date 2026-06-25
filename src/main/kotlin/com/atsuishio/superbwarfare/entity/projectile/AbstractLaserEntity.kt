package com.atsuishio.superbwarfare.entity.projectile

import com.atsuishio.superbwarfare.client.animation.AnimationTicker
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.TraceableEntity
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.level.material.PushReaction
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import java.util.Optional
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/**
 * Code based on @BobMowzie's MowziesMobs, @EEEAB's EEEABsMobs and @Mercurows's DreamaticVoyage
 */
abstract class AbstractLaserEntity(type: EntityType<*>, level: Level, countDown: Int) : Entity(type, level), TraceableEntity {
    var caster: LivingEntity? = null
    var yaw: Float = 0f
    var pitch: Float = 0f
    var preYaw: Float = 0f
    var prePitch: Float = 0f
    var endPosX: Double = 0.0
    var endPosY: Double = 0.0
    var endPosZ: Double = 0.0
    var collidePosX: Double = 0.0
    var collidePosY: Double = 0.0
    var collidePosZ: Double = 0.0
    var prevCollidePosX: Double = 0.0
    var prevCollidePosY: Double = 0.0
    var prevCollidePosZ: Double = 0.0
    var blockSide: Direction? = null
    var on: Boolean = true
    var ticker: AnimationTicker = AnimationTicker(3)

    init {
        this.setCountDown(countDown)
        this.noCulling = true
    }

    override fun tick() {
        super.tick()
        this.prevCollidePosX = this.collidePosX
        this.prevCollidePosY = this.collidePosY
        this.prevCollidePosZ = this.collidePosZ
        this.preYaw = this.yaw
        this.prePitch = this.pitch
        this.yaw = this.getYaw()
        this.pitch = this.getPitch()
        this.xo = this.x
        this.yo = this.y
        this.zo = this.z
        if (this.tickCount == 1 && this.level().isClientSide) {
            this.caster = this.level().getEntity(getCasterId()) as LivingEntity
        }

        this.beamTick()

        if ((!this.on && this.ticker.isStopped()) || (this.caster != null && !this.caster!!.isAlive)) {
            this.discard()
        }
        this.ticker.changeTimer(this.on && this.isAccumulating())

        if (this.tickCount - this.getCountDown() > this.getDuration()) {
            this.on = false
        }
    }

    override fun readAdditionalSaveData(pCompound: CompoundTag) {
    }

    override fun addAdditionalSaveData(pCompound: CompoundTag) {
    }

    protected open fun beamTick() {
    }

    override fun getOwner(): LivingEntity? {
        return caster
    }

    override fun push(entityIn: Entity) {
    }

    override fun getPistonPushReaction(): PushReaction {
        return PushReaction.IGNORE
    }

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        builder
            .define(DATA_CASTER_ID, -1)
            .define(DATA_YAW, 0f)
            .define(DATA_PITCH, 0f)
            .define(DATA_DURATION, 0)
            .define(DATA_COUNT_DOWN, 0)
    }

    fun setCasterId(id: Int) {
        this.entityData.set(DATA_CASTER_ID, id)
    }

    fun getCasterId(): Int {
        return this.entityData.get(DATA_CASTER_ID)
    }

    fun isAccumulating(): Boolean {
        return this.tickCount > this.getCountDown()
    }

    fun getYaw(): Float {
        return this.entityData.get(DATA_YAW)
    }

    fun setYaw(rotAngle: Float) {
        this.entityData.set(DATA_YAW, rotAngle)
    }

    fun getPitch(): Float {
        return this.entityData.get(DATA_PITCH)
    }

    fun setPitch(rotAngle: Float) {
        this.entityData.set(DATA_PITCH, rotAngle)
    }

    fun getDuration(): Int {
        return this.entityData.get(DATA_DURATION)
    }

    fun setDuration(duration: Int) {
        this.entityData.set(DATA_DURATION, duration)
    }

    fun getCountDown(): Int {
        return this.entityData.get(DATA_COUNT_DOWN)
    }

    fun setCountDown(countDown: Int) {
        this.entityData.set(DATA_COUNT_DOWN, countDown)
    }

    protected fun calculateEndPos(radius: Double) {
        if (level().isClientSide) {
            endPosX = x + radius * cos(yaw.toDouble()) * cos(pitch.toDouble())
            endPosZ = z + radius * sin(yaw.toDouble()) * cos(pitch.toDouble())
            endPosY = y + radius * sin(pitch.toDouble())
        } else {
            endPosX = x + radius * cos(getYaw().toDouble()) * cos(getPitch().toDouble())
            endPosZ = z + radius * sin(getYaw().toDouble()) * cos(getPitch().toDouble())
            endPosY = y + radius * sin(getPitch().toDouble())
        }
    }

    fun raytraceEntities(world: Level, from: Vec3, to: Vec3): CustomHitResult {
        val result = CustomHitResult()
        result.setBlockHit(world.clip(ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)))
        if (result.getBlockHit() != null) {
            val hitVec = result.getBlockHit()!!.location
            collidePosX = hitVec.x
            collidePosY = hitVec.y
            collidePosZ = hitVec.z
            blockSide = result.getBlockHit()!!.direction
        } else {
            collidePosX = endPosX
            collidePosY = endPosY
            collidePosZ = endPosZ
            blockSide = null
        }
        val entities = world.getEntitiesOfClass(
            LivingEntity::class.java,
            AABB(
                min(x, collidePosX),
                min(y, collidePosY),
                min(z, collidePosZ),
                max(x, collidePosX),
                max(y, collidePosY),
                max(z, collidePosZ)
            ).inflate(1.0, 1.0, 1.0)
        )
        for (entity in entities) {
            if (entity == this.caster) {
                continue
            }
            val pad = entity.pickRadius + getBaseScale()
            val aabb = entity.boundingBox.inflate(pad.toDouble(), pad.toDouble(), pad.toDouble())
            val hit: Optional<Vec3> = aabb.clip(from, to)
            if (aabb.contains(from)) {
                result.addEntityHit(entity)
            } else if (hit.isPresent) {
                result.addEntityHit(entity)
            }
        }
        return result
    }

    override fun isAttackable(): Boolean {
        return false
    }

    override fun displayFireAnimation(): Boolean {
        return false
    }

    protected fun onHit(hitResult: HitResult) {
        val type = hitResult.type
        if (type == HitResult.Type.ENTITY) {
            this.onHitEntity(hitResult as EntityHitResult)
            this.level().gameEvent(GameEvent.PROJECTILE_LAND, hitResult.location, GameEvent.Context.of(this, null))
        } else if (type == HitResult.Type.BLOCK) {
            val blockhitresult = hitResult as BlockHitResult
            this.onHitBlock(blockhitresult)
            val blockpos: BlockPos = blockhitresult.blockPos
            this.level().gameEvent(
                GameEvent.PROJECTILE_LAND,
                blockpos,
                GameEvent.Context.of(this, this.level().getBlockState(blockpos))
            )
        }
    }

    protected open fun onHitEntity(result: EntityHitResult) {
    }

    protected open fun onHitBlock(result: BlockHitResult) {
    }

    protected open fun getBaseScale(): Float {
        return 0.5f
    }

    class CustomHitResult {
        private var blockHit: BlockHitResult? = null
        private val entities: MutableList<LivingEntity> = ArrayList()

        fun getBlockHit(): BlockHitResult? {
            return blockHit
        }

        fun getEntities(): List<LivingEntity> {
            return entities
        }

        fun setBlockHit(rayTraceResult: HitResult) {
            if (rayTraceResult.type == HitResult.Type.BLOCK) {
                this.blockHit = rayTraceResult as BlockHitResult
            }
        }

        fun addEntityHit(entity: LivingEntity) {
            entities.add(entity)
        }
    }

    companion object {
        private val DATA_CASTER_ID: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(AbstractLaserEntity::class.java, EntityDataSerializers.INT)
        private val DATA_YAW: EntityDataAccessor<Float> =
            SynchedEntityData.defineId(AbstractLaserEntity::class.java, EntityDataSerializers.FLOAT)
        private val DATA_PITCH: EntityDataAccessor<Float> =
            SynchedEntityData.defineId(AbstractLaserEntity::class.java, EntityDataSerializers.FLOAT)
        private val DATA_DURATION: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(AbstractLaserEntity::class.java, EntityDataSerializers.INT)
        private val DATA_COUNT_DOWN: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(AbstractLaserEntity::class.java, EntityDataSerializers.INT)
    }
}