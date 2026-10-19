package com.atsuishio.superbwarfare.entity.projectile

import com.atsuishio.superbwarfare.client.animation.entity.BasicProjectileAnimationInstance
import com.atsuishio.superbwarfare.entity.getValue
import com.atsuishio.superbwarfare.entity.mixin.ICustomKnockback
import com.atsuishio.superbwarfare.entity.setValue
import com.atsuishio.superbwarfare.event.custom.ProjectileImpactCallback
import com.atsuishio.superbwarfare.init.ModDamageTypes
import com.atsuishio.superbwarfare.tools.SeekTool
import com.atsuishio.superbwarfare.tools.forceHurt
import com.atsuishio.superbwarfare.tools.localPlayer
import net.minecraft.core.particles.DustParticleOptions
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.entity.projectile.ProjectileUtil
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import org.joml.Vector3f

open class ReforgingGravityBallEntity(type: EntityType<out ReforgingGravityBallEntity>, level: Level) :
    Projectile(type, level), IBulletProperties, BasicGeoProjectileEntity {

    private val animation = if (level.isClientSide) BasicProjectileAnimationInstance(this, true) else null
    private var damageValue = 12f
    private var explosionDamageValue = 80f
    private var flightLife = 200
    private var age = 0
    private var gravityValue = 0.04f
    private var activationDelay = 10

    var anchored by ANCHORED
        private set
    var anchorTime by ANCHOR_TIME
        private set
    var activationTime by ACTIVATION_TIME
        private set
    var expansionDuration by EXPANSION_DURATION
        private set
    var fieldDuration by FIELD_DURATION
        private set
    private var damageInterval by DAMAGE_INTERVAL
    private var ownerId by OWNER_ID
    private var syncedAttackRadius by ATTACK_RADIUS
    private var syncedCoreRadius by CORE_RADIUS
    private var syncedPullStrength by PULL_STRENGTH

    val attackRadius: Double get() = syncedAttackRadius.toDouble()
    val coreRadius: Double get() = syncedCoreRadius.toDouble()
    val pullStrength: Double get() = syncedPullStrength.toDouble()
    private val collapseDuration: Int get() = minOf(COLLAPSE_DURATION, fieldDuration)
    private val collapseStartTime: Long get() = activationTime + fieldDuration - collapseDuration
    val collapsing: Boolean get() = anchored && level().gameTime >= collapseStartTime
    val fieldActive: Boolean
        get() = anchored && level().gameTime >= activationTime && level().gameTime < collapseStartTime

    private val damageTick: Boolean
        get() = (level().gameTime - activationTime) % damageInterval == (damageInterval / 2).toLong()

    private val attractionTick: Boolean get() = fieldActive && !damageTick

    fun expansionProgress(partialTicks: Float = 0f): Float =
        if (!anchored) 0f else {
            val elapsed = level().gameTime - anchorTime + partialTicks.toDouble()
            expansionFraction(elapsed, expansionDuration) * (1f - collapseProgress(partialTicks))
        }

    private fun collapseProgress(partialTicks: Float = 0f): Float =
        if (!anchored) 0f else expansionFraction(
            level().gameTime - collapseStartTime + partialTicks.toDouble(),
            // Keep the restored singularity visible briefly before the final damage pulse.
            (collapseDuration - 2).coerceAtLeast(1)
        )

    init {
        noPhysics = true
        noCulling = true
    }

    override fun defineSynchedData() {
        entityData.define(ANCHORED, false)
        entityData.define(ANCHOR_TIME, 0L)
        entityData.define(ACTIVATION_TIME, 0L)
        entityData.define(EXPANSION_DURATION, 20)
        entityData.define(FIELD_DURATION, 100)
        entityData.define(DAMAGE_INTERVAL, 10)
        entityData.define(ATTACK_RADIUS, 8f)
        entityData.define(CORE_RADIUS, 0.75f)
        entityData.define(PULL_STRENGTH, 0.6f)
        entityData.define(OWNER_ID, -1)
    }

    override fun getAnimationInstance() = animation
    override fun getAddEntityPacket(): Packet<ClientGamePacketListener> = ClientboundAddEntityPacket(this)

    override fun recreateFromPacket(packet: ClientboundAddEntityPacket) {
        super.recreateFromPacket(packet)
        lerpMotion(packet.xa, packet.ya, packet.za)
    }

    override fun getDamage() = damageValue
    override fun setDamage(value: Float) {
        damageValue = value.coerceAtLeast(0f)
    }

    override fun getExplosionDamage() = explosionDamageValue
    override fun setExplosionDamage(value: Float) {
        explosionDamageValue = value.coerceAtLeast(0f)
    }

    override fun getLife() = flightLife
    override fun setLife(value: Int) {
        flightLife = value.coerceAtLeast(1)
    }

    override fun getCustomGravity() = gravityValue
    override fun setCustomGravity(gravity: Float) {
        gravityValue = gravity.coerceAtLeast(0f)
    }

    @Deprecated("Deprecated in Java")
    override fun isPushedByFluid() = false

    override fun tick() {
        super.tick()
        age++
        if (!level().isClientSide) {
            ownerId = owner?.id ?: -1
            if (anchored && level().gameTime >= activationTime + fieldDuration) {
                collapseCore()
                discard()
                return
            }
            if (!anchored && age >= flightLife) {
                discard()
                return
            }
            if (collapsing && level().gameTime == collapseStartTime) {
                level().playSound(null, x, y, z, SoundEvents.BEACON_DEACTIVATE, SoundSource.PLAYERS, 0.8f, 1.8f)
            }
        }

        if (anchored) {
            deltaMovement = Vec3.ZERO
        } else {
            fly()
        }

        if (level().isClientSide) {
            tickClient()
        } else if (fieldActive) {
            if (level().gameTime == activationTime) {
                level().playSound(null, x, y, z, SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 0.7f, 1.4f)
            }
            affectTargets()
        }
    }

    @Environment(EnvType.CLIENT)
    private fun tickClient() {
        if (attractionTick) {
            val player = localPlayer
            if (player != null && player.level() === level() && canAffect(player)) {
                player.deltaMovement = attractedVelocity(player)
            }
        }
        if (tickCount % 2 != 0) return
        val progress = expansionProgress()
        val spread = if (fieldActive) 1.8 else 0.08 + progress * 1.4
        level().addParticle(
            DustParticleOptions(Vector3f(1f, 0.78f, 0.1f), 0.25f + progress * 0.4f),
            x + (random.nextDouble() - 0.5) * spread,
            y + (random.nextDouble() - 0.5) * spread,
            z + (random.nextDouble() - 0.5) * spread,
            0.0, 0.0, 0.0
        )
    }

    private fun fly() {
        val nextPosition = position().add(deltaMovement)
        if (!level().isClientSide) {
            val blockHit = level().clip(
                ClipContext(
                    position(),
                    nextPosition,
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    this
                )
            )
            // Clip the entity sweep at the nearest block so targets behind walls cannot anchor the ball.
            val end = if (blockHit.type == HitResult.Type.BLOCK) blockHit.location else nextPosition
            val entityHit = ProjectileUtil.getEntityHitResult(
                this, position(), end, boundingBox.expandTowards(deltaMovement).inflate(1.0),
                { canHitEntity(it) }, position().distanceToSqr(end)
            )
            val hit = entityHit ?: blockHit
            if (hit.type != HitResult.Type.MISS && !ProjectileImpactCallback.onProjectileImpact(this, hit)) {
                onHit(hit)
                return
            }
        }
        setPos(nextPosition)
        deltaMovement = deltaMovement.add(0.0, -gravityValue.toDouble(), 0.0)
    }

    override fun onHitBlock(hit: BlockHitResult) {
        if (level().isClientSide || anchored) return
        super.onHitBlock(hit)
        val normal = Vec3.atLowerCornerOf(hit.direction.normal)
        anchorAt(hit.location.add(normal.scale(0.65)))
    }

    override fun onHitEntity(hit: EntityHitResult) {
        if (level().isClientSide || anchored) return
        super.onHitEntity(hit)
        anchorAt(hit.location)
    }

    private fun anchorAt(location: Vec3) {
        setPos(location)
        deltaMovement = Vec3.ZERO
        isNoGravity = true
        anchorTime = level().gameTime
        // ActivationDelay is a minimum wait; the field must also finish unfolding.
        activationTime = anchorTime + maxOf(activationDelay, expansionDuration)
        anchored = true
        hasImpulse = true
        level().playSound(null, x, y, z, SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.PLAYERS, 0.65f, 0.8f)
    }

    /** The same eligibility check is used for server damage and client-side player attraction. */
    fun canAffect(target: LivingEntity): Boolean {
        val shooter = owner ?: level().getEntity(ownerId)
        return target.isAlive && !target.isSpectator && target !== shooter
                && !(target is Player && target.isCreative)
                && !SeekTool.IS_FRIENDLY.test(shooter, target)
                && (shooter == null || !shooter.isPassengerOfSameVehicle(target))
                && target.boundingBox.center.distanceToSqr(position()) <= attackRadius * attackRadius
    }

    private fun attractedVelocity(target: LivingEntity): Vec3 {
        val toCenter = position().subtract(target.boundingBox.center)
        val distance = toCenter.length()
        val current = target.deltaMovement

        if (attackRadius <= 0.0 || distance > attackRadius || pullStrength <= 0.0) {
            return current
        }

        val core = coreRadius.coerceIn(0.0, attackRadius)

        val motion = if (distance <= core) {
            current.scale(0.15).add(toCenter.scale(0.5))
        } else {
            val proximity = (attackRadius - distance) / (attackRadius - core)
            val force = pullStrength * (0.35 + 0.65 * proximity)
            current.add(toCenter.scale(force / distance))
        }

        val speed = motion.length()
        return if (speed > MAX_PULL_SPEED) {
            motion.scale(MAX_PULL_SPEED / speed)
        } else {
            motion
        }
    }

    private fun affectTargets() {
        val targets =
            level().getEntitiesOfClass(LivingEntity::class.java, boundingBox.inflate(attackRadius), ::canAffect)
        // A damage pulse never writes attraction velocity in the same tick.
        if (damageTick) damageTargets(targets) else attractTargets(targets)
    }

    private fun damageTargets(targets: List<LivingEntity>, finalPulse: Boolean = false) {
        val source = ModDamageTypes.causeGravityDamage(level().registryAccess(), this, owner)
        for (target in targets) {
            val distance = target.boundingBox.center.distanceTo(position())
            val damage = if (finalPulse) getExplosionDamage()
            else damageValue * damageMultiplier(distance, attackRadius, coreRadius)
            if (damage <= 0f) continue
            val knockback = ICustomKnockback.getInstance(target)
            val previousStrength = knockback.`superbWarfare$getKnockbackStrength`()
            knockback.`superbWarfare$setKnockbackStrength`(0.0)
            try {
                if (finalPulse) target.invulnerableTime = 0
                target.forceHurt(source, damage)
            } finally {
                knockback.`superbWarfare$setKnockbackStrength`(previousStrength)
            }
        }
    }

    private fun collapseCore() {
        val targets = level().getEntitiesOfClass(LivingEntity::class.java, boundingBox.inflate(coreRadius)) {
            canAffect(it) && it.boundingBox.center.distanceToSqr(position()) <= coreRadius * coreRadius
        }
        damageTargets(targets, finalPulse = true)
        level().playSound(null, x, y, z, SoundEvents.RESPAWN_ANCHOR_DEPLETE.value(), SoundSource.PLAYERS, 0.8f, 1.7f)
    }

    private fun attractTargets(targets: List<LivingEntity>) {
        for (target in targets) {
            // Player movement is owned by that player's client. Never send server pull velocities to players.
            if (target !is Player) {
                target.deltaMovement = attractedVelocity(target)
                target.hasImpulse = true
                target.hurtMarked = true
            }
        }
    }

    override fun readAdditionalSaveData(tag: CompoundTag) {
        super.readAdditionalSaveData(tag)
        if (tag.contains("Damage")) setDamage(tag.getFloat("Damage"))
        if (tag.contains("ExplosionDamage")) setExplosionDamage(tag.getFloat("ExplosionDamage"))
        if (tag.contains("Life")) setLife(tag.getInt("Life"))
        if (tag.contains("Age")) age = tag.getInt("Age").coerceAtLeast(0)
        if (tag.contains("Gravity")) setCustomGravity(tag.getFloat("Gravity"))
        if (tag.contains("ActivationDelay")) activationDelay = tag.getInt("ActivationDelay").coerceAtLeast(0)
        if (tag.contains("ExpansionDuration")) expansionDuration = tag.getInt("ExpansionDuration").coerceAtLeast(1)
        if (tag.contains("DamageInterval")) damageInterval = tag.getInt("DamageInterval").coerceAtLeast(2)
        if (tag.contains("FieldDuration")) fieldDuration = tag.getInt("FieldDuration").coerceAtLeast(1)
        if (tag.contains("AttackRadius")) syncedAttackRadius = tag.getFloat("AttackRadius").coerceIn(0.5f, 32f)
        if (tag.contains("CoreRadius")) syncedCoreRadius = tag.getFloat("CoreRadius").coerceIn(0.1f, attackRadius.toFloat())
        if (tag.contains("PullStrength")) syncedPullStrength = tag.getFloat("PullStrength").coerceIn(0f, 1f)
        if (tag.contains("Anchored")) anchored = tag.getBoolean("Anchored")
        if (tag.contains("ActivationTime")) activationTime = tag.getLong("ActivationTime")
        if (tag.contains("AnchorTime")) anchorTime = tag.getLong("AnchorTime")
        else if (anchored) anchorTime = activationTime - maxOf(activationDelay, expansionDuration)
        isNoGravity = anchored
    }

    override fun addAdditionalSaveData(tag: CompoundTag) {
        super.addAdditionalSaveData(tag)
        tag.putFloat("Damage", damageValue)
        tag.putFloat("ExplosionDamage", getExplosionDamage())
        tag.putInt("Life", flightLife)
        tag.putInt("Age", age)
        tag.putFloat("Gravity", gravityValue)
        tag.putInt("ActivationDelay", activationDelay)
        tag.putInt("ExpansionDuration", expansionDuration)
        tag.putInt("DamageInterval", damageInterval)
        tag.putInt("FieldDuration", fieldDuration)
        tag.putFloat("AttackRadius", attackRadius.toFloat())
        tag.putFloat("CoreRadius", coreRadius.toFloat())
        tag.putFloat("PullStrength", pullStrength.toFloat())
        tag.putBoolean("Anchored", anchored)
        tag.putLong("AnchorTime", anchorTime)
        tag.putLong("ActivationTime", activationTime)
    }

    companion object {
        private fun expansionFraction(elapsed: Double, duration: Int): Float {
            val progress = (elapsed / duration.coerceAtLeast(1)).coerceIn(0.0, 1.0)
            return (progress * progress * (3.0 - 2.0 * progress)).toFloat()
        }

        private fun damageMultiplier(distance: Double, radius: Double, coreRadius: Double): Float {
            if (radius <= 0 || distance > radius) return 0f
            val core = coreRadius.coerceIn(0.0, radius)
            if (distance <= core) return 1f
            val falloff = ((distance - core) / (radius - core)).coerceIn(0.0, 1.0)
            return (1.0 - 0.8 * falloff).toFloat()
        }

        const val MAX_PULL_SPEED = 10f
        private const val COLLAPSE_DURATION = 10

        @JvmField
        val ANCHORED: EntityDataAccessor<Boolean> =
            SynchedEntityData.defineId(ReforgingGravityBallEntity::class.java, EntityDataSerializers.BOOLEAN)

        @JvmField
        val ANCHOR_TIME: EntityDataAccessor<Long> =
            SynchedEntityData.defineId(ReforgingGravityBallEntity::class.java, EntityDataSerializers.LONG)

        @JvmField
        val EXPANSION_DURATION: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(ReforgingGravityBallEntity::class.java, EntityDataSerializers.INT)

        @JvmField
        val DAMAGE_INTERVAL: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(ReforgingGravityBallEntity::class.java, EntityDataSerializers.INT)

        @JvmField
        val ACTIVATION_TIME: EntityDataAccessor<Long> =
            SynchedEntityData.defineId(ReforgingGravityBallEntity::class.java, EntityDataSerializers.LONG)

        @JvmField
        val FIELD_DURATION: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(ReforgingGravityBallEntity::class.java, EntityDataSerializers.INT)

        @JvmField
        val ATTACK_RADIUS: EntityDataAccessor<Float> =
            SynchedEntityData.defineId(ReforgingGravityBallEntity::class.java, EntityDataSerializers.FLOAT)

        @JvmField
        val CORE_RADIUS: EntityDataAccessor<Float> =
            SynchedEntityData.defineId(ReforgingGravityBallEntity::class.java, EntityDataSerializers.FLOAT)

        @JvmField
        val PULL_STRENGTH: EntityDataAccessor<Float> =
            SynchedEntityData.defineId(ReforgingGravityBallEntity::class.java, EntityDataSerializers.FLOAT)

        @JvmField
        val OWNER_ID: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(ReforgingGravityBallEntity::class.java, EntityDataSerializers.INT)
    }
}
