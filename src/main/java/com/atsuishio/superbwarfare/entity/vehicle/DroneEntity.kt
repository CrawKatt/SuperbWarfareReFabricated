package com.atsuishio.superbwarfare.entity.vehicle

import com.atsuishio.superbwarfare.data.CustomData
import com.atsuishio.superbwarfare.data.drone_attachment.DroneAttachmentData
import com.atsuishio.superbwarfare.entity.getValue
import com.atsuishio.superbwarfare.entity.projectile.C4Entity
import com.atsuishio.superbwarfare.entity.setValue
import com.atsuishio.superbwarfare.entity.vehicle.base.GeoVehicleEntity
import com.atsuishio.superbwarfare.event.ClientMouseHandler
import com.atsuishio.superbwarfare.init.ModDamageTypes.causeCustomExplosionDamage
import com.atsuishio.superbwarfare.init.ModDamageTypes.causeDroneHitDamage
import com.atsuishio.superbwarfare.init.ModDamageTypes.causeVehicleStrikeDamage
import com.atsuishio.superbwarfare.init.ModItems
import com.atsuishio.superbwarfare.init.ModSerializers
import com.atsuishio.superbwarfare.init.ModSounds
import com.atsuishio.superbwarfare.init.ModTags
import com.atsuishio.superbwarfare.item.misc.MonitorItem
import com.atsuishio.superbwarfare.item.misc.MonitorItem.Companion.disLink
import com.atsuishio.superbwarfare.item.misc.MonitorItem.Companion.link
import com.atsuishio.superbwarfare.tools.DamageHandler.doDamage
import com.atsuishio.superbwarfare.tools.EntityFindUtil.findEntity
import com.atsuishio.superbwarfare.tools.EntityFindUtil.findPlayer
import com.atsuishio.superbwarfare.tools.TagDataParser
import com.atsuishio.superbwarfare.tools.getMaxZoom
import net.minecraft.ChatFormatting
import net.minecraft.core.registries.Registries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtUtils
import net.minecraft.nbt.StringTag
import net.minecraft.network.chat.Component
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.Mth
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.entity.AreaEffectCloud
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MoverType
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec2
import net.minecraft.world.phys.Vec3
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.items.ItemHandlerHelper
import net.minecraftforge.registries.ForgeRegistries
import org.joml.Math
import java.util.*
import kotlin.math.abs

open class DroneEntity(type: EntityType<out DroneEntity>, world: Level) : GeoVehicleEntity(type, world) {
    var fire: Boolean = false
    override var collisionCoolDown: Int = 0
    override var lastTickSpeed: Double = 0.0
    override var lastTickVerticalSpeed: Double = 0.0
    var currentItem: ItemStack = ItemStack.EMPTY

    var bodyPitch: Float = 0f
    var pitchO: Float = 0f

    var holdTickX: Int = 0
    var holdTickY: Int = 0
    var holdTickZ: Int = 0

    private var ammoCount by AMMO

    fun setBodyXRot(rot: Float) {
        this.bodyPitch = rot
    }

    fun getBodyPitch(tickDelta: Float): Float {
        return Mth.lerp(0.6f * tickDelta, pitchO, this.bodyPitch)
    }

    override fun defineSynchedData() {
        super.defineSynchedData()
        val data = DroneAttachmentData()

        this.entityData.define(DELTA_X_ROT, 0f)
        this.entityData.define(CONTROLLER, "undefined")
        this.entityData.define(LINKED, false)
        this.entityData.define(IS_KAMIKAZE, false)
        this.entityData.define(DISPLAY_ENTITY, "")
        this.entityData.define(
            DISPLAY_DATA, listOf(
                data.scale()[0], data.scale()[1], data.scale()[2],
                data.offset()[0], data.offset()[1], data.offset()[2],
                data.rotation()[0], data.rotation()[1], data.rotation()[2],
                data.xLength, data.zLength,
                data.tickCount.toFloat()
            )
        )
        this.entityData.define(DISPLAY_ENTITY_TAG, CompoundTag())
        this.entityData.define(AMMO, 0)
        this.entityData.define(MAX_AMMO, 1)
    }

    override fun causeFallDamage(l: Float, d: Float, source: DamageSource): Boolean {
        return false
    }

    override fun shouldSendHitSounds(): Boolean {
        return false
    }

    open fun setAmmo(count: Int) {
        this.ammoCount = count
    }

    open fun getAmmo() = this.ammoCount

    override fun addAdditionalSaveData(compound: CompoundTag) {
        super.addAdditionalSaveData(compound)
        compound.putBoolean("Linked", this.entityData.get(LINKED))
        compound.putString("Controller", this.entityData.get(CONTROLLER))
        compound.putInt("Ammo", this.ammoCount)
        compound.putBoolean("KamikazeMode", this.entityData.get(IS_KAMIKAZE))
        compound.putInt("MaxAmmo", this.entityData.get(MAX_AMMO))
        compound.putString("DisplayEntity", this.entityData.get(DISPLAY_ENTITY))
        compound.putString("DisplayEntityTag", this.entityData.get(DISPLAY_ENTITY_TAG).toString())
        compound.putString("DisplayData", this.entityData.get(DISPLAY_DATA).joinToString(","))

        val item = CompoundTag()
        this.currentItem.save(item)
        compound.put("Item", item)
    }

    public override fun readAdditionalSaveData(compound: CompoundTag) {
        super.readAdditionalSaveData(compound)
        if (compound.contains("Linked")) this.entityData.set(LINKED, compound.getBoolean("Linked"))
        if (compound.contains("Controller")) this.entityData.set(CONTROLLER, compound.getString("Controller"))
        if (compound.contains("Ammo")) this.ammoCount = compound.getInt("Ammo")
        if (compound.contains("KamikazeMode")) this.entityData.set(IS_KAMIKAZE, compound.getBoolean("KamikazeMode"))
        if (compound.contains("Item")) this.currentItem = ItemStack.of(compound.getCompound("Item"))
        if (compound.contains("MaxAmmo")) this.entityData.set(MAX_AMMO, compound.getInt("MaxAmmo"))
        if (compound.contains("DisplayEntity")) this.entityData.set(
            DISPLAY_ENTITY,
            compound.getString("DisplayEntity")
        )
        if (compound.contains("DisplayEntityTag")) this.entityData.set(
            DISPLAY_ENTITY_TAG,
            compound.getCompound("DisplayEntityTag")
        )
        if (compound.contains("DisplayData")) this.entityData.set(
            DISPLAY_DATA,
            compound.getString("DisplayData").split(",").map { it.toFloat() }
        )
    }

    override fun maxRepairCoolDown(): Int {
        return -1
    }

    override fun baseTick() {
        pitchO = this.bodyPitch
        setBodyXRot(this.bodyPitch * 0.9f)

        super.baseTick()

        setZRot(roll * 0.9f)

        lastTickSpeed = this.deltaMovement.length()
        lastTickVerticalSpeed = this.deltaMovement.y

        if (collisionCoolDown > 0) {
            collisionCoolDown--
        }

        val controller = findPlayer(this.level(), this.entityData.get(CONTROLLER))

        if (!this.onGround()) {
            if (controller != null) {
                val stack = controller.mainHandItem
                if (!stack.`is`(ModItems.MONITOR.get()) || !stack.getOrCreateTag().getBoolean("Using")) {
                    leftInputDown = false
                    rightInputDown = false
                    forwardInputDown = false
                    backInputDown = false
                    upInputDown = false
                    downInputDown = false
                }

                if (tickCount % 5 == 0) {
                    controller.inventory.items
                        .filter { it.item === ModItems.MONITOR.get() }
                        .forEach {
                            if (it.getOrCreateTag().getString(MonitorItem.LINKED_DRONE) == this.getStringUUID()) {
                                MonitorItem.getDronePos(it, this.position())
                            }
                        }
                }
            }
        }

        if (this.isInWater) {
            this.hurt(
                DamageSource(
                    level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE)
                        .getHolderOrThrow(DamageTypes.EXPLOSION), controller
                ), 0.25f + (2 * lastTickSpeed).toFloat()
            )
        }

        if (this.fire && this.ammoCount > 0) {
            if (!this.entityData.get(IS_KAMIKAZE)) {
                this.ammoCount -= 1
                if (controller != null && this.level() is ServerLevel) {
                    droneDrop(controller)
                }
            } else {
                if (controller != null) {
                    if (controller.mainHandItem.`is`(ModItems.MONITOR.get())) {
                        disLink(controller.mainHandItem, controller)
                    }
                    this.hurt(
                        DamageSource(
                            level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE)
                                .getHolderOrThrow(DamageTypes.EXPLOSION), controller
                        ), 10000f
                    )
                }
            }
            this.fire = false
        }

        this.refreshDimensions()
    }

    private fun droneDrop(player: Player?) {
        val data = CustomData.DRONE_ATTACHMENT[getItemId(this.currentItem)] ?: return
        val dropEntity = EntityType.byString(data.dropEntity())
            .map { it.create(this.level()) }
            .orElse(null) ?: return

        if (player != null && dropEntity is Projectile) {
            dropEntity.owner = player
        }

        val tag: CompoundTag = TagDataParser.parseObject(data.dropData()) {
            if (player == null) return@parseObject StringTag.valueOf(it)
            val uuid = player.getUUID()
            when (it) {
                "@sbw:owner" -> NbtUtils.createUUID(uuid)
                "@sbw:owner_string_lower" -> StringTag.valueOf(uuid.toString().replace("-", "").lowercase(Locale.ROOT))
                "@sbw:owner_string_upper" -> StringTag.valueOf(uuid.toString().replace("-", "").uppercase(Locale.ROOT))
                else -> StringTag.valueOf(it)
            }
        }
        dropEntity.load(tag)

        val dropPos = data.dropPosition()
        dropEntity.setPos(this.x + dropPos[0], this.y + dropPos[1], this.z + dropPos[2])

        val vec3 = Vec3(0.2 * this.deltaMovement.x, 0.2 * this.deltaMovement.y, 0.2 * this.deltaMovement.z)
        dropEntity.deltaMovement = vec3
        val d0 = vec3.horizontalDistance()
        dropEntity.yRot = (Mth.atan2(vec3.x, vec3.z) * (180f / Math.PI.toFloat()).toDouble()).toFloat()
        dropEntity.xRot = (Mth.atan2(vec3.y, d0) * (180f / Math.PI.toFloat()).toDouble()).toFloat()
        dropEntity.yRotO = dropEntity.yRot
        dropEntity.xRotO = dropEntity.xRot

        this.level().addFreshEntity(dropEntity)
    }

    override fun interact(player: Player, hand: InteractionHand): InteractionResult {
        val stack = player.mainHandItem
        if (stack.item === ModItems.MONITOR.get()) {
            if (!player.isShiftKeyDown) {
                if (!this.entityData.get(LINKED)) {
                    if (stack.getOrCreateTag().getBoolean("Linked")) {
                        player.displayClientMessage(
                            Component.translatable("tips.superbwarfare.monitor.already_linked")
                                .withStyle(ChatFormatting.RED), true
                        )
                        return InteractionResult.sidedSuccess(this.level().isClientSide())
                    }

                    this.entityData.set(LINKED, true)
                    this.entityData.set(CONTROLLER, player.getStringUUID())

                    link(stack, this.getStringUUID())
                    player.displayClientMessage(
                        Component.translatable("tips.superbwarfare.monitor.linked").withStyle(ChatFormatting.GREEN),
                        true
                    )

                    if (player is ServerPlayer) {
                        player.level().playSound(
                            null,
                            player.onPos,
                            SoundEvents.ARROW_HIT_PLAYER,
                            SoundSource.PLAYERS,
                            0.5f,
                            1f
                        )
                    }
                } else {
                    player.displayClientMessage(
                        Component.translatable("tips.superbwarfare.drone.already_linked").withStyle(ChatFormatting.RED),
                        true
                    )
                }
            } else {
                if (this.entityData.get(LINKED)) {
                    if (!stack.getOrCreateTag().getBoolean("Linked")) {
                        player.displayClientMessage(
                            Component.translatable("tips.superbwarfare.drone.already_linked")
                                .withStyle(ChatFormatting.RED), true
                        )
                        return InteractionResult.sidedSuccess(this.level().isClientSide())
                    }

                    this.entityData.set(CONTROLLER, "none")
                    this.entityData.set(LINKED, false)

                    disLink(stack, player)
                    player.displayClientMessage(
                        Component.translatable("tips.superbwarfare.monitor.unlinked").withStyle(ChatFormatting.RED),
                        true
                    )

                    if (player is ServerPlayer) {
                        player.level().playSound(
                            null,
                            player.onPos,
                            SoundEvents.ARROW_HIT_PLAYER,
                            SoundSource.PLAYERS,
                            0.5f,
                            1f
                        )
                    }
                }
            }
        } else if (player.isShiftKeyDown) {
            if (stack.isEmpty || stack.`is`(ModTags.Items.TOOLS_CROWBAR)) {
                // 无人机拆除
                ItemHandlerHelper.giveItemToPlayer(player, ItemStack(ModItems.DRONE.get()))

                // 返还弹药
                repeat(this.ammoCount) {
                    ItemHandlerHelper.giveItemToPlayer(player, this.currentItem.copy())
                }

                player.inventory.items
                    .filter { it.item === ModItems.MONITOR.get() }
                    .forEach {
                        if (it.getOrCreateTag().getString(MonitorItem.LINKED_DRONE) == this.getStringUUID()) {
                            disLink(it, player)
                        }
                    }

                if (!this.level().isClientSide()) {
                    this.discard()
                }
            }
        } else {
            if (stack.isEmpty) {
                // 返还单个弹药
                val ammo = this.ammoCount
                if (ammo > 0) {
                    ItemHandlerHelper.giveItemToPlayer(player, this.currentItem.copy())
                    this.ammoCount = ammo - 1
                    if (ammo == 1) {
                        this.entityData.set(DISPLAY_ENTITY, "")
                        this.entityData.set(MAX_AMMO, 1)
                        this.entityData.set(IS_KAMIKAZE, false)
                        this.currentItem = ItemStack.EMPTY
                    }
                }
            } else {
                // 自定义挂载
                val itemID: String = getItemId(stack)
                val attachmentData = CustomData.DRONE_ATTACHMENT[itemID]

                // 是否能挂载该物品
                if (attachmentData != null && this.ammoCount < attachmentData.count()) {
                    if (this.entityData.get(DISPLAY_ENTITY) == attachmentData.displayEntity()
                        && ItemStack.matches(this.currentItem, stack.copyWithCount(1))
                    ) {
                        // 同种物品挂载
                        this.ammoCount += 1

                        if (!player.isCreative) {
                            stack.shrink(1)
                        }
                        if (player is ServerPlayer) {
                            player.level().playSound(
                                null,
                                player.onPos,
                                ModSounds.BULLET_SUPPLY.get(),
                                SoundSource.PLAYERS,
                                0.5f,
                                1f
                            )
                        }
                    } else if (this.ammoCount == 0) {
                        // 不同种物品挂载
                        this.currentItem = stack.copyWithCount(1)
                        this.entityData.set(DISPLAY_ENTITY, attachmentData.displayEntity())
                        this.ammoCount += 1
                        this.entityData.set(IS_KAMIKAZE, attachmentData.isKamikaze)
                        this.entityData.set(MAX_AMMO, attachmentData.count())

                        if (!player.isCreative) {
                            stack.shrink(1)
                        }
                        if (player is ServerPlayer) {
                            player.level().playSound(
                                null,
                                player.onPos,
                                ModSounds.BULLET_SUPPLY.get(),
                                SoundSource.PLAYERS,
                                0.5f,
                                1f
                            )
                        }

                        val scale = attachmentData.scale()
                        val offset = attachmentData.offset()
                        val rotation = attachmentData.rotation()

                        if (attachmentData.displayData() != null) {
                            this.entityData.set(
                                DISPLAY_ENTITY_TAG,
                                TagDataParser.parseObject(attachmentData.displayData()) {
                                    val uuid = player.getUUID()
                                    when (it) {
                                        "@sbw:owner" -> NbtUtils.createUUID(uuid)
                                        "@sbw:owner_string_lower" -> StringTag.valueOf(
                                            uuid.toString().replace("-", "").lowercase(Locale.ROOT)
                                        )

                                        "@sbw:owner_string_upper" -> StringTag.valueOf(
                                            uuid.toString().replace("-", "").uppercase(Locale.ROOT)
                                        )

                                        else -> StringTag.valueOf(it)
                                    }
                                }
                            )
                        }

                        this.entityData.set(
                            DISPLAY_DATA, listOf(
                                scale[0], scale[1], scale[2],
                                offset[0], offset[1], offset[2],
                                rotation[0], rotation[1], rotation[2],
                                attachmentData.xLength, attachmentData.zLength,
                                attachmentData.tickCount.toFloat()
                            )
                        )
                    }
                }
            }
        }

        return InteractionResult.sidedSuccess(this.level().isClientSide())
    }

    override fun travel() {
        if (!this.onGround()) {
            // left and right
            if (rightInputDown) {
                holdTickX++
                deltaRot -= 0.3f * Math.min(holdTickX, 5)
            } else if (this.leftInputDown) {
                holdTickX++
                deltaRot += 0.3f * Math.min(holdTickX, 5)
            } else {
                holdTickX = 0
            }

            // forward and backward
            if (forwardInputDown) {
                holdTickZ++
                this.entityData.set(
                    DELTA_X_ROT,
                    this.entityData.get(DELTA_X_ROT) - 0.3f * Math.min(holdTickZ, 5)
                )
            } else if (backInputDown) {
                holdTickZ++
                this.entityData.set(
                    DELTA_X_ROT,
                    this.entityData.get(DELTA_X_ROT) + 0.3f * Math.min(holdTickZ, 5)
                )
            } else {
                holdTickZ = 0
            }

            this.setDeltaMovement(this.deltaMovement.multiply(0.965, 0.7, 0.965))
        } else {
            this.setDeltaMovement(this.deltaMovement.multiply(0.8, 1.0, 0.8))
            this.setZRot(this.roll * 0.7f)
            this.xRot *= 0.7f
            this.setBodyXRot(this.bodyPitch * 0.7f)
        }

        if (this.isInWater && this.tickCount % 4 == 0) {
            this.setDeltaMovement(this.deltaMovement.multiply(0.6, 0.6, 0.6))
            this.hurt(
                causeVehicleStrikeDamage(
                    this.level().registryAccess(),
                    this,
                    if (this.getFirstPassenger() == null) this else this.getFirstPassenger()
                ), 26 + (60 * ((lastTickSpeed - 0.4) * (lastTickSpeed - 0.4))).toFloat()
            )
        }

        val up = this.upInputDown
        val down = this.downInputDown

        if (up) {
            holdTickY++
            power = Math.min(power + 0.01f * Math.min(holdTickY, 5), 0.2f)
            setDeltaMovement(Vec3(deltaMovement.x, 0.05 * holdTickY, deltaMovement.z))
        } else if (down) {
            holdTickY++
            power = Math.max(power - 0.02f * Math.min(holdTickY, 5), if (this.onGround()) 0f else 0.06f)
            setDeltaMovement(Vec3(deltaMovement.x, -0.05 * holdTickY, deltaMovement.z))
        } else {
            holdTickY = 0
        }

        if (!(up || down)) {
            power = if (this.deltaMovement.y() < 0) {
                Math.min(power + 0.005f, 0.2f)
            } else {
                Math.max(power - (if (this.onGround()) 0.0005f else 0.005f), 0.02f)
            }
        }

        deltaRot *= 0.7f
        this.entityData.set(DELTA_X_ROT, this.entityData.get(DELTA_X_ROT) * 0.7f)

        this.setZRot(Mth.clamp(this.roll - deltaRot, -30f, 30f))
        this.setBodyXRot(Mth.clamp(this.bodyPitch - this.entityData.get(DELTA_X_ROT), -30f, 30f))

        setDeltaMovement(deltaMovement.add(0.0, power * 0.6, 0.0))

        val direction = getRightDirection().mul(deltaRot)
        setDeltaMovement(
            deltaMovement.add(
                Vec3(
                    direction.x.toDouble(),
                    direction.y.toDouble(),
                    direction.z.toDouble()
                ).scale(0.017)
            )
        )

        val directionZ = getForwardDirection().mul(-this.entityData.get(DELTA_X_ROT))
        setDeltaMovement(
            deltaMovement.add(
                Vec3(
                    directionZ.x.toDouble(),
                    directionZ.y.toDouble(),
                    directionZ.z.toDouble()
                ).scale(0.017)
            )
        )

        val controller = findPlayer(this.level(), this.entityData.get(CONTROLLER))
        if (controller != null) {
            val stack = controller.mainHandItem
            if (stack.`is`(ModItems.MONITOR.get()) && stack.getOrCreateTag().getBoolean("Using")) {
                this.yRot += 0.5f * mouseMoveSpeedX
                this.xRot = (this.xRot + 0.5f * mouseMoveSpeedY).coerceIn(-10f, 90f)
            }
        }

        val f = 0.7f
        val aabb = AABB.ofSize(this.eyePosition, f.toDouble(), 0.3, f.toDouble())
        val level = this.level()
        for (target in level.getEntitiesOfClass(Entity::class.java, aabb) { true }) {
            if (this !== target && target != null &&
                !(target is ItemEntity || target is Projectile
                        || target.type.`is`(ModTags.EntityTypes.DECOY)
                        || target is AreaEffectCloud || target is C4Entity)
            ) {
                hitEntityCrash(controller, target)
            }
        }
    }

    open fun hitEntityCrash(player: Player?, target: Entity) {
        if (lastTickSpeed > 0.05) {
            val attachedEntity = this.entityData.get<String>(DISPLAY_ENTITY)
            if (!attachedEntity.isEmpty() && 50 * lastTickSpeed > this.health) {
                val data = CustomData.DRONE_ATTACHMENT[getItemId(this.currentItem)]
                if (data != null) {
                    if (data.isKamikaze) {
                        EntityType.byString(attachedEntity).ifPresent {
                            val bomb: Entity? = it.create(this.level())
                            doDamage(
                                target,
                                causeCustomExplosionDamage(this.level().registryAccess(), bomb, player),
                                data.hitDamage
                            )
                            target.invulnerableTime = 0
                        }
                    } else {
                        doDamage(
                            target,
                            causeDroneHitDamage(this.level().registryAccess(), this, player),
                            (5 * lastTickSpeed).toFloat()
                        )
                    }
                }

                if (player != null && player.mainHandItem.`is`(ModItems.MONITOR.get())) {
                    disLink(player.mainHandItem, player)
                }
            }
            this.hurt(
                DamageSource(
                    level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE)
                        .getHolderOrThrow(DamageTypes.EXPLOSION), player ?: this
                ), ((if (!this.entityData.get(DISPLAY_ENTITY).isEmpty()) 50 else 4) * lastTickSpeed).toFloat()
            )
        }
    }

    override fun engineRunning(): Boolean {
        return abs(power) > 0.05
    }

    override fun getEngineSoundVolume(): Float {
        if (abs(power) <= 0.05) {
            return 0f
        }

        val player = findPlayer(this.level(), this.entityData.get(CONTROLLER)) ?: return power

        val stack = player.mainHandItem
        if (stack.`is`(ModItems.MONITOR.get())
            && stack.getOrCreateTag().getBoolean("Using")
            && stack.getOrCreateTag().getBoolean("Linked")
        ) {
            return power * 0.5f
        }
        return power * 2f
    }

    override fun move(movementType: MoverType, movement: Vec3) {
        super.move(movementType, movement)
        val controller = findPlayer(this.level(), this.entityData.get(CONTROLLER))

        if (lastTickSpeed < 0.2 || collisionCoolDown > 0) return

        if ((verticalCollision) && abs(lastTickVerticalSpeed.toFloat()) > 1) {
            this.hurt(
                causeCustomExplosionDamage(
                    this.level().registryAccess(),
                    this,
                    controller ?: this
                ),
                (20 * ((abs(lastTickVerticalSpeed.toFloat()) - 1) * (lastTickSpeed - 0.2) * (lastTickSpeed - 0.2))).toFloat()
            )
            collisionCoolDown = 4
        }

        if (this.horizontalCollision) {
            this.hurt(
                causeCustomExplosionDamage(
                    this.level().registryAccess(),
                    this,
                    controller ?: this
                ), (10 * ((lastTickSpeed - 0.2) * (lastTickSpeed - 0.2))).toFloat()
            )
            collisionCoolDown = 4
        }
    }

    override fun getPickResult(): ItemStack? {
        return ItemStack(ModItems.DRONE.get())
    }

    override fun destroy() {
        val controller = findPlayer(this.level(), this.entityData.get(CONTROLLER))
        if (controller != null) {
            if (controller.mainHandItem.`is`(ModItems.MONITOR.get())) {
                disLink(controller.mainHandItem, controller)
            }
        }

        // 无人机爆炸
        if (level() is ServerLevel) {
            level().explode(null, this.x, this.y, this.z, 0f, Level.ExplosionInteraction.NONE)
        }

        val data = CustomData.DRONE_ATTACHMENT[getItemId(this.currentItem)]
        if (data != null) {
            if (data.isKamikaze) {
                kamikazeExplosion()
            } else {
                if (this.level() is ServerLevel) {
                    val count = this.ammoCount
                    repeat(count) {
                        droneDrop(controller)
                    }
                }
            }
        }

        val id = this.entityData.get(CONTROLLER)
        val uuid: UUID?
        try {
            uuid = UUID.fromString(id)
        } catch (_: IllegalArgumentException) {
            this.discard()
            return
        }

        val player = this.level().getPlayerByUUID(uuid)
        player?.inventory?.items?.filter { it.item === ModItems.MONITOR.get() }?.forEach {
            if (it.getOrCreateTag().getString(MonitorItem.LINKED_DRONE) == this.getStringUUID()) {
                disLink(it, player)
            }
        }
        super.destroy()
        discard()
    }

    private fun kamikazeExplosion() {
        val attacker = findEntity(this.level(), lastAttackerUUID)
        if (findPlayer(this.level(), this.entityData.get(CONTROLLER)) == null) return

        // 挂载实体的数据
        val attachedEntity = this.entityData.get<String>(DISPLAY_ENTITY)
        if (attachedEntity.isEmpty()) return

        val data = CustomData.DRONE_ATTACHMENT[getItemId(this.currentItem)] ?: return

        val bomb = EntityType.byString(attachedEntity)
            .map { it.create(this.level()) }
            .orElse(null) ?: return

        val radius = data.explosionRadius

        createCustomExplosion()
            .source(bomb)
            .attacker(attacker)
            .damage(data.explosionDamage)
            .radius(radius)
            .explode()

        // TODO 药水迫击炮炮弹
//        if (mode == 1) {
//            ParticleTool.spawnMediumExplosionParticles(this.level(), this.position());
//
//            if (this.currentItem.getItem() instanceof MortarShell) {
//                this.createAreaCloud(this.currentItem.get(DataComponents.POTION_CONTENTS), this.level(), ExplosionConfig.DRONE_KAMIKAZE_EXPLOSION_DAMAGE.get(), ExplosionConfig.DRONE_KAMIKAZE_EXPLOSION_RADIUS.get());
//            }
//        }
    }

    private fun createAreaCloud(potion: Potion, level: Level, duration: Int, radius: Float) {
        if (potion === Potions.EMPTY) return

        val cloud = AreaEffectCloud(
            level,
            this.x + 0.75 * deltaMovement.x,
            this.y + 0.5 * bbHeight + 0.75 * deltaMovement.y,
            this.z + 0.75 * deltaMovement.z
        )
        cloud.setPotion(potion)
        cloud.duration = duration
        cloud.radius = radius

        val controller = findPlayer(this.level(), this.entityData.get(CONTROLLER))
        if (controller != null) {
            cloud.setOwner(controller)
        }
        level.addFreshEntity(cloud)
    }

    override fun canCrushEntities(): Boolean {
        return false
    }

    @OnlyIn(Dist.CLIENT)
    override fun getCameraRotation(
        partialTicks: Float,
        player: Player,
        zoom: Boolean,
        isFirstPerson: Boolean
    ): Vec2? {
        return Vec2(
            (getYaw(partialTicks) - ClientMouseHandler.freeCameraYaw).toFloat(),
            (getPitch(partialTicks) + ClientMouseHandler.freeCameraPitch).toFloat()
        )
    }

    @OnlyIn(Dist.CLIENT)
    override fun getCameraPosition(
        partialTicks: Float,
        player: Player,
        zoom: Boolean,
        isFirstPerson: Boolean
    ): Vec3? {
        val transform = getClientVehicleTransform(partialTicks)
        val maxCameraPosition =
            transformPosition(transform, 0.0, 0.75, -2 - 0.2 * ClientMouseHandler.custom3pDistanceLerp)
        return getMaxZoom(transform, maxCameraPosition)
    }

    open fun getController() = findPlayer(this.level(), this.entityData.get(CONTROLLER))

    companion object {
        @JvmField
        val LINKED: EntityDataAccessor<Boolean> =
            SynchedEntityData.defineId(DroneEntity::class.java, EntityDataSerializers.BOOLEAN)

        @JvmField
        val CONTROLLER: EntityDataAccessor<String> =
            SynchedEntityData.defineId(DroneEntity::class.java, EntityDataSerializers.STRING)

        @JvmField
        val IS_KAMIKAZE: EntityDataAccessor<Boolean> =
            SynchedEntityData.defineId(DroneEntity::class.java, EntityDataSerializers.BOOLEAN)

        @JvmField
        val DELTA_X_ROT: EntityDataAccessor<Float> =
            SynchedEntityData.defineId(DroneEntity::class.java, EntityDataSerializers.FLOAT)

        @JvmField
        val DISPLAY_ENTITY: EntityDataAccessor<String> =
            SynchedEntityData.defineId(DroneEntity::class.java, EntityDataSerializers.STRING)

        @JvmField
        val DISPLAY_ENTITY_TAG: EntityDataAccessor<CompoundTag> =
            SynchedEntityData.defineId(DroneEntity::class.java, EntityDataSerializers.COMPOUND_TAG)

        // scale[3], offset[3], rotation[3], xLength, zLength, tickCount
        @JvmField
        val DISPLAY_DATA: EntityDataAccessor<List<Float>> = SynchedEntityData.defineId(
            DroneEntity::class.java,
            ModSerializers.FLOAT_LIST_SERIALIZER.get()
        )

        @JvmField
        val AMMO: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(DroneEntity::class.java, EntityDataSerializers.INT)

        @JvmField
        val MAX_AMMO: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(DroneEntity::class.java, EntityDataSerializers.INT)

        @JvmStatic
        fun getItemId(stack: ItemStack): String {
            val key = ForgeRegistries.ITEMS.getKey(stack.item) ?: return ""
            return key.toString()
        }
    }
}

