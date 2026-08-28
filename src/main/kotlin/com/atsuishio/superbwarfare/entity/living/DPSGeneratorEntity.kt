package com.atsuishio.superbwarfare.entity.living

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.capability.energy.SyncedEntityEnergyStorage
import com.atsuishio.superbwarfare.capability.energy.EnergyStorageHelper
import com.atsuishio.superbwarfare.client.animation.entity.DPSGeneratorAnimationInstance
import com.atsuishio.superbwarfare.entity.getValue
import com.atsuishio.superbwarfare.entity.setValue
import com.atsuishio.superbwarfare.entity.vehicle.damage.DamageModifier.Companion.createDefaultModifier
import com.atsuishio.superbwarfare.init.ModCapabilities
import com.atsuishio.superbwarfare.init.ModDamageTypes
import com.atsuishio.superbwarfare.init.ModItems
import com.atsuishio.superbwarfare.init.ModSounds
import com.atsuishio.superbwarfare.init.ModTags
import com.atsuishio.superbwarfare.resource.model.EntityModelReloadListener
import com.atsuishio.superbwarfare.tools.FormatTool
import com.atsuishio.superbwarfare.tools.playLocalSound
import net.minecraft.commands.arguments.EntityAnchorArgument
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.NonNullList
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.IntTag
import net.minecraft.network.chat.Component
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.entity.*
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.roundToInt

open class DPSGeneratorEntity(type: EntityType<DPSGeneratorEntity>, level: Level) : LivingEntity(type, level){
    val animationInstance: DPSGeneratorAnimationInstance? =
        if (this.level().isClientSide) DPSGeneratorAnimationInstance(this) else null
    open val modelInstance = EntityModelReloadListener.getModel(MODEL)?.createInstance()
    val energyStorage: SyncedEntityEnergyStorage =
        SyncedEntityEnergyStorage(5120, 0, 2560, this.entityData, ENERGY)

    private var damageDealt = 0f
    open var downTime by DOWN_TIME
    open var energy by ENERGY
    open var generatorLevel by LEVEL

    val maxEnergy: Int
        get() = when (this.generatorLevel) {
            1 -> 25600
            2 -> 102400
            3 -> 409600
            4 -> 1638400
            5 -> 6553600
            6 -> 26214400
            7 -> 104857600
            else -> 5120
        }

    val maxTransfer: Int
        get() = this.maxEnergy / 2

    init {
        this.noCulling = true
    }

    override fun defineSynchedData() {
        super.defineSynchedData()
        with(this.entityData) {
            define(DOWN_TIME, 0)
            define(ENERGY, 0)
            define(LEVEL, 0)
        }
    }

    override fun getArmorSlots(): Iterable<ItemStack?> {
        return NonNullList.withSize(1, ItemStack.EMPTY)
    }

    override fun getItemBySlot(pSlot: EquipmentSlot): ItemStack {
        return ItemStack.EMPTY
    }

    override fun setItemSlot(pSlot: EquipmentSlot, pStack: ItemStack) {
    }

    override fun getStandingEyeHeight(pPose: Pose, pSize: EntityDimensions): Float {
        return 1.57f
    }

    override fun causeFallDamage(l: Float, d: Float, source: DamageSource): Boolean {
        return false
    }

    override fun shouldRenderAtSqrDistance(pDistance: Double): Boolean {
        return true
    }

    override fun addAdditionalSaveData(tag: CompoundTag) {
        super.addAdditionalSaveData(tag)
        tag.put("Energy", energyStorage.serializeNBT())
        tag.putInt("Level", this.generatorLevel)
    }

    override fun readAdditionalSaveData(tag: CompoundTag) {
        super.readAdditionalSaveData(tag)
        val energyNBT = tag.get("Energy")
        if (energyNBT is IntTag) {
            energyStorage.deserializeNBT(energyNBT)
        }
        this.generatorLevel = tag.getInt("Level")

        energyStorage.setCapacity(this.maxEnergy)
        energyStorage.setMaxExtract(this.maxTransfer)
    }

    override fun hurt(source: DamageSource, amount: Float): Boolean {
        // 不处理/kill伤害
        var amount = DAMAGE_MODIFIER.compute(this, source, amount)
        if (source.`is`(DamageTypes.GENERIC_KILL)) {
            this.remove(RemovalReason.KILLED)
            return super.hurt(source, amount)
        }

        damageDealt += amount

        if (this.health < 0.01f) {
            amount = 0f
        }

        if (!this.level().isClientSide()) {
            this.level().playSound(
                null,
                BlockPos.containing(this.x, this.y, this.z),
                ModSounds.HIT,
                SoundSource.BLOCKS,
                1f,
                1f
            )
        } else {
            this.level().playLocalSound(
                this.x,
                this.y,
                this.z,
                ModSounds.HIT,
                SoundSource.BLOCKS,
                1f,
                1f,
                false
            )
        }
        return super.hurt(source, (amount / 2.0.pow(this.generatorLevel.toDouble())).toFloat())
    }

    override fun isPickable(): Boolean {
        return this.downTime == 0
    }

    override fun interact(player: Player, hand: InteractionHand): InteractionResult {
        if (!player.mainHandItem.isEmpty && !player.mainHandItem.`is`(ModTags.Items.TOOLS_CROWBAR)) {
            return InteractionResult.PASS
        }

        if (player.isShiftKeyDown) {
            if (!this.level().isClientSide()) {
                this.discard()
            }

            if (!player.abilities.instabuild) {
                player.addItem(ItemStack(ModItems.DPS_GENERATOR_DEPLOYER))
            }
        } else {
            this.lookAt(EntityAnchorArgument.Anchor.EYES, Vec3(player.x, this.y, player.z))
            this.xRot = 0f
            this.xRotO = this.xRot
            this.downTime = 0
        }

        return InteractionResult.sidedSuccess(this.level().isClientSide())
    }

    override fun tick() {
        super.tick()
        if (this.downTime > 0) {
            this.downTime -= 1
        }

        // 每秒恢复生命并充能下方方块
        if (this.tickCount % 20 == 0) {
            val damage = this.maxHealth - this.health
            val entityCap = this.energyStorage

            if (damage > 0) {
                // DPS显示
                val lastSource = this.getLastDamageSource()
                if (lastSource != null) {
                    val attacker = lastSource.entity
                    if (attacker is Player && !this.level().isClientSide) {
                        val displayDamage =
                            if (lastSource.`is`(ModDamageTypes.BEAST)) Float.POSITIVE_INFINITY else damageDealt
                        attacker.displayClientMessage(
                            Component.translatable(
                                "tips.superbwarfare.dps_generator.dps",
                                FormatTool.format1DZ(displayDamage.toDouble())
                            ), true
                        )
                    }
                }

                // 发电
                entityCap.setMaxReceive(entityCap.capacity.toInt())
                EnergyStorageHelper.insert(
                    entityCap,
                    (128.0 * max(this.generatorLevel, 1) * 2.0.pow(
                        this.generatorLevel.toDouble()
                    ) * damage).roundToInt().toLong()
                )
                entityCap.setMaxReceive(0)
            }

            // 充能底部方块
            this.chargeBlockBelow()

            if (this.health < 0.01f) {
                this.generatorLevel = (this.generatorLevel + 1).coerceAtMost(7)
                entityCap.setCapacity(this.maxEnergy)
                entityCap.setMaxExtract(this.maxTransfer)

                if (!this.level().isClientSide()) {
                    this.level().playSound(
                        null,
                        BlockPos.containing(this.x, this.y, this.z),
                        ModSounds.DPS_GENERATOR_EVOLVE,
                        SoundSource.BLOCKS,
                        0.5f,
                        1f
                    )
                } else {
                    this.level().playLocalSound(
                        this.x,
                        this.y,
                        this.z,
                        ModSounds.DPS_GENERATOR_EVOLVE,
                        SoundSource.BLOCKS,
                        0.5f,
                        1f,
                        false
                    )
                }
            }
            this.health = this.maxHealth
            damageDealt = 0f
        }
    }

    override fun getDeltaMovement(): Vec3 {
        return Vec3(0.0, 0.0, 0.0)
    }

    override fun isPushable(): Boolean {
        return false
    }

    override fun getMainArm(): HumanoidArm {
        return HumanoidArm.RIGHT
    }

    override fun doPush(entityIn: Entity) {
    }

    override fun pushEntities() {
    }

    override fun setNoGravity(ignored: Boolean) {
        super.setNoGravity(true)
    }

    override fun aiStep() {
        super.aiStep()
        this.updateSwingTime()
        this.isNoGravity = true
    }

    override fun tickDeath() {
        ++this.deathTime
        if (this.deathTime >= 100) {
            this.spawnAtLocation(ItemStack(ModItems.DPS_GENERATOR_DEPLOYER))
            this.remove(RemovalReason.KILLED)
        }
    }

    protected fun chargeBlockBelow() {
        val entityCap = this.energyStorage

        if (!entityCap.supportsExtraction() || entityCap.amount <= 0) return
        val blockPos = this.blockPosition().below()
        val cap = ModCapabilities.ENERGY_BLOCK.find(this.level(), blockPos, Direction.UP)
        if (cap == null || !cap.supportsInsertion()) return

        val extract = EnergyStorageHelper.simulateExtract(entityCap, entityCap.amount)
        val extracted = EnergyStorageHelper.insert(cap, extract)
        if (extracted <= 0) return

        this.level().blockEntityChanged(blockPos)
        EnergyStorageHelper.extract(entityCap, extracted)
    }

    fun beastCharge() {
        if (this.generatorLevel < 7) {
            this.generatorLevel = 7
            this.energyStorage.setCapacity(this.maxEnergy)
            this.energyStorage.setMaxExtract(this.maxTransfer)
            this.energyStorage.setEnergy(this.maxEnergy)
        }
    }

    override fun getPickResult(): ItemStack? {
        return ItemStack(ModItems.DPS_GENERATOR_DEPLOYER)
    }

    companion object {
        @JvmField
        val DOWN_TIME: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(DPSGeneratorEntity::class.java, EntityDataSerializers.INT)

        @JvmField
        val ENERGY: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(DPSGeneratorEntity::class.java, EntityDataSerializers.INT)

        @JvmField
        val LEVEL: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(DPSGeneratorEntity::class.java, EntityDataSerializers.INT)

        val MODEL = loc("models/bedrock/entity/dps_generator.geo.json")

        @JvmStatic
        fun onDPSGeneratorDown(entity: LivingEntity, source: DamageSource, amount: Float): Boolean {
            val dpsGenerator = entity as? DPSGeneratorEntity ?: return true
            // 不处理/kill伤害
            if (source.`is`(DamageTypes.GENERIC_KILL)) return true
            val sourceEntity = source.entity

            dpsGenerator.health = 0.00001f

            if (sourceEntity is Player) {
                sourceEntity.playLocalSound(ModSounds.TARGET_DOWN, 1f, 1f)
                dpsGenerator.downTime = 40
            }
            return false
        }

        fun createAttributes(): AttributeSupplier.Builder {
            return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.0)
                .add(Attributes.MAX_HEALTH, 40.0)
                .add(Attributes.ARMOR, 0.0)
                .add(Attributes.ATTACK_DAMAGE, 0.0)
                .add(Attributes.FOLLOW_RANGE, 16.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 10.0)
                .add(Attributes.FLYING_SPEED, 0.0)
        }

        private val DAMAGE_MODIFIER = createDefaultModifier()
            .immuneTo(DamageTypes.IN_WALL)
            .immuneTo(DamageTypes.DROWN)
            .immuneTo(DamageTypes.LAVA)
            .immuneTo(DamageTypes.CACTUS)
            .immuneTo(DamageTypes.FALL)
            .immuneTo(DamageTypes.SWEET_BERRY_BUSH)
            .immuneTo(DamageTypes.BAD_RESPAWN_POINT)
    }
}
