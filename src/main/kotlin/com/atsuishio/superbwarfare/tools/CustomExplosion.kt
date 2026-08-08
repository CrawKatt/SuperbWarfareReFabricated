package com.atsuishio.superbwarfare.tools

import com.atsuishio.superbwarfare.config.server.ExplosionConfig
import com.atsuishio.superbwarfare.init.ModDamageTypes
import com.atsuishio.superbwarfare.init.ModSounds
import com.atsuishio.superbwarfare.network.message.receive.ClientIndicatorMessage
import com.atsuishio.superbwarfare.network.message.receive.ShakeClientMessage.Companion.sendToNearbyPlayers
import com.atsuishio.superbwarfare.tools.DamageHandler.doDamage
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.Mth
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.item.PrimedTnt
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.level.Explosion
import net.minecraft.world.level.ExplosionDamageCalculator
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import java.util.function.Supplier
import kotlin.math.floor
import kotlin.math.sqrt

class CustomExplosion @JvmOverloads constructor(
    private val level: Level,
    private val sourceEntity: Entity?,
    damageSource: DamageSource?,
    pDamageCalculator: ExplosionDamageCalculator?,
    private val damage: Float,
    private val x: Double,
    private val y: Double,
    private val z: Double,
    private val radius: Float,
    pBlockInteraction: BlockInteraction,
    smallParticle: ParticleOptions = ParticleTypes.EXPLOSION,
    bigParticle: ParticleOptions = ParticleTypes.EXPLOSION_EMITTER,
    sound: Holder<SoundEvent> = SoundEvents.GENERIC_EXPLODE
) : Explosion(
    level,
    sourceEntity, damageSource, null,
    x, y, z, radius,
    false, pBlockInteraction, smallParticle, bigParticle, sound
) {
    private val damageSource: DamageSource = damageSource ?: level.damageSources().explosion(this)
    private val damageCalculator: ExplosionDamageCalculator = pDamageCalculator ?: ExplosionDamageCalculator()

    private var fireTime = 0
    private var damageMultiplier = 1f

    constructor(
        pLevel: Level,
        pSource: Entity?,
        damage: Float,
        pToBlowX: Double,
        pToBlowY: Double,
        pToBlowZ: Double,
        pRadius: Float,
        pBlockInteraction: BlockInteraction
    ) : this(pLevel, pSource, null, null, damage, pToBlowX, pToBlowY, pToBlowZ, pRadius, pBlockInteraction)

    constructor(
        pLevel: Level,
        pSource: Entity?,
        source: DamageSource?,
        damage: Float,
        pToBlowX: Double,
        pToBlowY: Double,
        pToBlowZ: Double,
        pRadius: Float,
        pBlockInteraction: BlockInteraction
    ) : this(pLevel, pSource, source, null, damage, pToBlowX, pToBlowY, pToBlowZ, pRadius, pBlockInteraction) {
        sendToNearbyPlayers(
            level,
            pToBlowX,
            pToBlowY,
            pToBlowZ,
            (4 * radius).toDouble(),
            20 + 0.2 * radius,
            50 + 0.5 * radius
        )
    }

    constructor(
        pLevel: Level,
        pSource: Entity?,
        source: DamageSource?,
        damage: Float,
        pToBlowX: Double,
        pToBlowY: Double,
        pToBlowZ: Double,
        pRadius: Float
    ) : this(pLevel, pSource, source, null, damage, pToBlowX, pToBlowY, pToBlowZ, pRadius, BlockInteraction.KEEP) {
        sendToNearbyPlayers(level, pToBlowX, pToBlowY, pToBlowZ, radius.toDouble(), 5 + 0.2 * radius, 2 + 0.02 * radius)
    }

    fun setFireTime(fireTime: Int): CustomExplosion {
        this.fireTime = fireTime
        return this
    }

    fun setDamageMultiplier(damageMultiplier: Float): CustomExplosion {
        this.damageMultiplier = damageMultiplier
        return this
    }

    @Suppress("DEPRECATION")
    override fun explode() {
        if (ExplosionConfig.EXPLOSION_DESTROY.get()) {
            this.level.gameEvent(this.sourceEntity, GameEvent.EXPLODE, Vec3(this.x, this.y, this.z))

            val set: MutableSet<BlockPos> = mutableSetOf()
            val center = Vec3(this.x, this.y, this.z)
            val random = level.random

            val aabb = AABB(
                x - 0.5 * radius,
                y - 0.5 * radius,
                z - 0.5 * radius,
                x + 0.5 * radius,
                y + 0.5 * radius,
                z + 0.5 * radius
            )

            val minPos = BlockPos(
                floor(aabb.minX).toInt(),
                floor(aabb.minY).toInt(),
                floor(aabb.minZ).toInt()
            )

            val maxPos = BlockPos(
                floor(aabb.maxX).toInt(),
                floor(aabb.maxY).toInt(),
                floor(aabb.maxZ).toInt()
            )

            BlockPos.betweenClosedStream(minPos, maxPos).forEach { blockpos ->
                var effectiveRadius = 0.4 * radius
                val distanceSqr = blockpos.center.distanceToSqr(center).toFloat()
                var force = this.radius * (0.25f + random.nextFloat() * 0.15f) * 0.02f * damage

                if (distanceSqr > radius * radius * 0.15) {
                    effectiveRadius += (random.nextDouble() - 0.5) * radius * 0.2
                }

                if (
                    level.isInWorldBounds(blockpos) &&
                    blockpos.center.distanceToSqr(center) <= effectiveRadius * effectiveRadius
                ) {
                    val blockState = this.level.getBlockState(blockpos)
                    var resistance = blockState.block.defaultDestroyTime()

                    if (
                        blockState.soundType === SoundType.METAL ||
                        blockState.soundType === SoundType.COPPER ||
                        blockState.soundType === SoundType.NETHERITE_BLOCK
                    ) {
                        resistance *= 3f
                    }

                    force *= (1 - distanceSqr / (effectiveRadius * effectiveRadius)).toFloat()

                    if (
                        resistance != -1f &&
                        force > resistance &&
                        this.damageCalculator.shouldBlockExplode(
                            this,
                            this.level,
                            blockpos,
                            blockState,
                            force
                        )
                    ) {
                        if (level is ServerLevel) {
                            level.destroyBlock(blockpos, true)
                        }

                        set.add(blockpos)
                    }
                }
            }

            this.toBlow.addAll(set)
        }

        val diameter = this.radius * 2f

        val x0 = Mth.floor(this.x - diameter.toDouble() - 1)
        val x1 = Mth.floor(this.x + diameter.toDouble() + 1)
        val y0 = Mth.floor(this.y - diameter.toDouble() - 1)
        val y1 = Mth.floor(this.y + diameter.toDouble() + 1)
        val z0 = Mth.floor(this.z - diameter.toDouble() - 1)
        val z1 = Mth.floor(this.z + diameter.toDouble() + 1)

        val list = this.level.getEntities(
            this.sourceEntity,
            AABB(
                x0.toDouble(),
                y0.toDouble(),
                z0.toDouble(),
                x1.toDouble(),
                y1.toDouble(),
                z1.toDouble()
            )
        )

        val position = Vec3(this.x, this.y, this.z)
        var hit = false

        for (entity in list) {
            if (!entity.ignoreExplosion(this)) {
                val distanceRate = sqrt(entity.distanceToSqr(position)) / diameter.toDouble()

                if (distanceRate <= 1) {
                    val xDistance = entity.x - this.x
                    val yDistance = (if (entity is PrimedTnt) entity.y else entity.eyeY) - this.y
                    val zDistance = entity.z - this.z
                    val distance = sqrt(xDistance * xDistance + yDistance * yDistance + zDistance * zDistance)

                    if (distance != 0.0) {
                        val seenPercent = Mth.clamp(
                            getSeenPercent(position, entity).toDouble(),
                            0.01 * ExplosionConfig.EXPLOSION_PENETRATION_RATIO.get(),
                            Double.POSITIVE_INFINITY
                        )

                        val damagePercent = (1 - distanceRate) * seenPercent
                        val damageFinal = (damagePercent * damagePercent + damagePercent) / 2 * damage

                        if (entity is Monster) {
                            doDamage(
                                entity,
                                this.damageSource,
                                damageFinal.toFloat() * (1 + 0.2f * this.damageMultiplier)
                            )
                        } else {
                            doDamage(
                                entity,
                                this.damageSource,
                                damageFinal.toFloat()
                            )
                        }

                        if (entity is LivingEntity) {
                            var force = damageFinal * 0.015

                            val blockpos = BlockPos.containing(position.x, position.y, position.z)
                            val blockstate = this.level.getBlockState(blockpos)
                            val fluidstate = this.level.getFluidState(blockpos)

                            val optional = this.damageCalculator.getBlockExplosionResistance(
                                this,
                                this.level,
                                blockpos,
                                blockstate,
                                fluidstate
                            )

                            if (optional.isPresent) {
                                force -= ((optional.get() + 0.3f) * 0.3f).toDouble()
                            }

                            val knockback = position.vectorTo(entity.boundingBox.center).normalize()
                            entity.deltaMovement = entity.deltaMovement.add(knockback.scale(force))

                            hit = true
                            entity.invulnerableTime = 1

                            if (fireTime > 0) {
                                entity.remainingFireTicks = fireTime
                            }
                        }
                    }
                }
            }

            if (hit) {
                val player = this.damageSource.entity

                if (player is ServerPlayer) {
                    SoundTool.playLocalSound(player, ModSounds.INDICATION)
                    ServerPlayNetworking.send(player, ClientIndicatorMessage(0, 5))
                }
            }
        }
    }

    class Builder(private var directSource: Entity) {
        private val level: Level = directSource.level()

        private var sourceEntity: Entity? = directSource
        private var attackerEntity: Entity? = directSource

        private var damage = 0f
        private var radius = 0f

        private var particleType: ParticleTool.ParticleType? = ParticleTool.ParticleType.MINI

        private var destroyBlock: Supplier<BlockInteraction> =
            Supplier {
                if (ExplosionConfig.EXPLOSION_DESTROY.get()) {
                    BlockInteraction.DESTROY
                } else {
                    BlockInteraction.KEEP
                }
            }

        private var fireTime = 0
        private var damageMultiplier = 1f
        private var damageSource: DamageSource? = null
        private var particlePosition: Vec3? = null

        var position: Vec3 = Vec3(directSource.x, directSource.eyeY, directSource.z)

        fun directSource(directSource: Entity): Builder {
            this.directSource = directSource
            return this
        }

        fun source(source: Entity?): Builder {
            this.sourceEntity = source
            return this
        }

        fun attacker(attacker: Entity?): Builder {
            this.attackerEntity = attacker
            return this
        }

        fun damage(damage: Float): Builder {
            this.damage = damage
            return this
        }

        fun radius(radius: Float): Builder {
            this.radius = radius
            return this
        }

        fun withParticleType(particleType: ParticleTool.ParticleType?): Builder {
            this.particleType = particleType
            return this
        }

        fun destroyBlock(destroyBlock: Supplier<BlockInteraction>): Builder {
            this.destroyBlock = destroyBlock
            return this
        }

        fun keepBlock(): Builder {
            this.destroyBlock = Supplier { BlockInteraction.KEEP }
            return this
        }

        fun fireTime(fireTime: Int): Builder {
            this.fireTime = fireTime
            return this
        }

        fun damageMultiplier(damageMultiplier: Float): Builder {
            this.damageMultiplier = damageMultiplier
            return this
        }

        fun damageSource(damageSource: DamageSource?): Builder {
            this.damageSource = damageSource
            return this
        }

        fun position(position: Vec3): Builder {
            this.position = position
            return this
        }

        fun particlePosition(particlePosition: Vec3?): Builder {
            this.particlePosition = particlePosition
            return this
        }

        fun explode() {
            if (level.isClientSide) return

            val source = this.damageSource ?: ModDamageTypes.causeCustomExplosionDamage(
                level.registryAccess(),
                sourceEntity,
                attackerEntity
            )

            val customExplosion = CustomExplosion(
                level, directSource,
                source, damage,
                position.x, position.y, position.z, radius, destroyBlock.get()
            )
                .setFireTime(fireTime)
                .setDamageMultiplier(damageMultiplier)
            customExplosion.explode()
            customExplosion.finalizeExplosion(false)

            ParticleTool.spawnExplosionParticles(
                particleType,
                directSource.level(),
                particlePosition ?: position
            )
        }
    }
}
