package com.atsuishio.superbwarfare.event

import com.atsuishio.superbwarfare.api.event.PreKillEvent.Indicator
import com.atsuishio.superbwarfare.api.event.PreKillEvent.SendKillMessage
import com.atsuishio.superbwarfare.config.common.GameplayConfig
import com.atsuishio.superbwarfare.config.server.MiscConfig
import com.atsuishio.superbwarfare.config.server.VehicleConfig
import com.atsuishio.superbwarfare.data.gun.Ammo
import com.atsuishio.superbwarfare.data.gun.GunData
import com.atsuishio.superbwarfare.data.gun.GunProp
import com.atsuishio.superbwarfare.data.gun.SoundInfo
import com.atsuishio.superbwarfare.data.gun.value.ReloadState
import com.atsuishio.superbwarfare.entity.living.TargetEntity
import com.atsuishio.superbwarfare.entity.mixin.ICustomKnockback
import com.atsuishio.superbwarfare.entity.vehicle.base.AutoAimableEntity
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import com.atsuishio.superbwarfare.init.*
import com.atsuishio.superbwarfare.item.ammo.ammoBoxData
import com.atsuishio.superbwarfare.item.gun.GunItem
import com.atsuishio.superbwarfare.network.message.receive.ClientIndicatorMessage
import com.atsuishio.superbwarfare.network.message.receive.DrawClientMessage
import com.atsuishio.superbwarfare.network.message.receive.LivingGunKillMessage
import com.atsuishio.superbwarfare.perk.Perk
import com.atsuishio.superbwarfare.tools.*
import com.atsuishio.superbwarfare.tools.DamageTypeTool.isGunDamage
import com.atsuishio.superbwarfare.tools.DamageTypeTool.isHeadshotDamage
import com.atsuishio.superbwarfare.tools.DamageTypeTool.isModDamage
import com.atsuishio.superbwarfare.tools.FormatTool.format1D
import com.atsuishio.superbwarfare.tools.FormatTool.format2D
import com.atsuishio.superbwarfare.tools.postEvent
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundSource
import net.minecraft.util.Mth
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.OwnableEntity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.ItemStack
import java.util.*
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt

object LivingEventHandler {
    private val capturedDrops = ThreadLocal<MutableList<ItemEntity>?>()

    @JvmStatic
    fun onLivingChangeTargetEvent(entity: Mob, target: LivingEntity?) {
        val vehicle = entity.vehicle
        if (vehicle is VehicleEntity) {
            if (entity === vehicle.getNthEntity(vehicle.turretControllerIndex)) {
                if (target != null) {
                    vehicle.aiTurretTargetUUID = target.stringUUID
                } else {
                    vehicle.aiTurretTargetUUID = "undefined"
                }
            }

            if (entity === vehicle.getNthEntity(vehicle.passengerWeaponStationControllerIndex)) {
                if (target != null) {
                    vehicle.aiPassengerWeaponTargetUUID = target.stringUUID
                } else {
                    vehicle.aiPassengerWeaponTargetUUID = "undefined"
                }
            }
        }
    }

    @JvmStatic
    fun onEntityAttacked(entity: LivingEntity, source: DamageSource, amount: Float): Boolean {
        val vehicle = entity.vehicle
        if (!source.`is`(ModDamageTypes.VEHICLE_EXPLOSION) && !source.`is`(ModDamageTypes.AIR_CRASH)
            && vehicle is VehicleEntity
            && vehicle.isEnclosed(entity)
        ) {
            if (!source.`is`(ModTags.DamageTypes.VEHICLE_NOT_ABSORB)) {
                vehicle.hurt(source, amount)
            }
            return true
        }
        return false
    }

    @JvmStatic
    fun onEntityHurt(entity: LivingEntity, source: DamageSource, amount: Float): Float {
        var damage = handleVehicleHurt(entity, source, amount)
        if (damage == 0f) return 0f

        damage = handleGunPerksWhenHurt(entity, source, damage)
        renderDamageIndicator(entity, source, damage)
        damage = reduceDamage(entity, source, damage)
        giveExpToWeapon(entity, source, damage)
        handleGunLevels(entity, source, damage)
        return damage
    }

    @JvmStatic
    fun onEntityDeath(entity: LivingEntity, source: DamageSource) {
        killIndication(entity, source)
        handleGunPerksWhenDeath(entity, source)
        handlePlayerKillEntity(entity, source)
        giveKillExpToWeapon(entity, source)
    }

    fun handleVehicleHurt(entity: LivingEntity, source: DamageSource, amount: Float): Float {
        val vehicle = entity.vehicle
        if (vehicle is VehicleEntity) {
            if (source.`is`(ModTags.DamageTypes.VEHICLE_IGNORE)) return amount

            if (vehicle.isEnclosed(entity)) {
                if (!source.`is`(ModDamageTypes.VEHICLE_EXPLOSION) && !source.`is`(ModDamageTypes.AIR_CRASH)) {
                    return 0f
                }
            } else {
                val rate = vehicle.getSeat(entity)?.damageAbsorbRate ?: 0.0f
                if (!source.`is`(ModTags.DamageTypes.VEHICLE_NOT_ABSORB)) {
                    vehicle.hurt(source, 0.7f * amount)
                }

                return amount * 0.3f
            }
        }
        return amount
    }

    /**
     * 计算伤害减免
     */
    private fun reduceDamage(entity: LivingEntity, source: DamageSource, amount: Float): Float {
        val sourceEntity = source.entity ?: return amount
        if (sourceEntity.level().isClientSide) return amount

        val originalAmount = amount.toDouble()
        var damage = originalAmount
        val bulletResistance = entity.getAttribute(ModAttributes.bulletResistanceHolder())?.value ?: 0.0

        val stack = if (sourceEntity is LivingEntity) sourceEntity.mainHandItem else ItemStack.EMPTY

        // 距离衰减
        if (isGunDamage(source) && stack.item is GunItem) {
            val data = GunData.from(stack)
            val distance = entity.position().distanceTo(sourceEntity.position())
            damage = reduceDamageByDistance(originalAmount, distance, data.damageReduceRate, data.damageReduceMinDistance)
        }

        // 计算防弹插板减伤
        val armor = entity.getItemBySlot(EquipmentSlot.CHEST)

        val tag = NBTTool.getTag(armor)
        if (armor != ItemStack.EMPTY && tag.contains("ArmorPlate")) {
            val armorValue = tag.getDouble("ArmorPlate")
            tag.putDouble("ArmorPlate", max(armorValue - damage, 0.0))
            NBTTool.saveTag(armor, tag)
            damage = max(damage - armorValue, 0.0)
        }

        // 计算防弹护具减伤
        if (source.`is`(ModTags.DamageTypes.PROJECTILE) || source.`is`(DamageTypes.MOB_PROJECTILE)) {
            damage *= 1 - 0.8 * Mth.clamp(bulletResistance, 0.0, 1.0)
        }

        if (source.`is`(ModTags.DamageTypes.PROJECTILE_ABSOLUTE)) {
            damage *= 1 - 0.2 * Mth.clamp(bulletResistance, 0.0, 1.0)
        }

        if (source.`is`(ModDamageTypes.PROJECTILE_EXPLOSION) || source.`is`(ModDamageTypes.MINE) || source.`is`(
                ModDamageTypes.PROJECTILE_HIT
            ) || source.`is`(ModDamageTypes.CUSTOM_EXPLOSION)
            || source.`is`(DamageTypes.EXPLOSION) || source.`is`(DamageTypes.PLAYER_EXPLOSION)
        ) {
            damage *= 1 - 0.3 * Mth.clamp(bulletResistance, 0.0, 1.0)
        }

        val result = damage.toFloat()

        if (entity is TargetEntity && sourceEntity is Player) {
            if (source.`is`(ModDamageTypes.BEAST)) {
                damage = Float.POSITIVE_INFINITY.toDouble()
            }

            sourceEntity.displayClientMessage(
                Component.translatable(
                    "tips.superbwarfare.target.damage",
                    format2D(damage),
                    format1D(entity.position().distanceTo(sourceEntity.position()), "m")
                ), false
            )
        }

        return result
    }

    private fun reduceDamageByDistance(amount: Double, distance: Double, rate: Double, minDistance: Double): Double {
        return amount / (1 + rate * max(0.0, distance - minDistance))
    }

    /**
     * 根据造成的伤害，提供武器经验
     */
    private fun giveExpToWeapon(entity: LivingEntity, source: DamageSource, amount: Float) {
        val sourceEntity = source.entity as? LivingEntity ?: return
        val stack = sourceEntity.mainHandItem
        if (stack.item !is GunItem) return
        if (entity.type.`is`(ModTags.EntityTypes.NO_EXPERIENCE)) return

        val data = GunData.from(stack)
        val expAmount = (0.5f * amount).coerceAtMost(entity.maxHealth)

        // 先处理发射器类武器或高爆弹的爆炸伤害
        if (source.`is`(ModDamageTypes.PROJECTILE_EXPLOSION)) {
            if (data.get(GunProp.EXPLOSION_DAMAGE) > 0 || GunData.from(stack).perk.getLevel(ModPerks.HE_BULLET) > 0) {
                data.exp.set(data.exp.get() + expAmount)
            }
        }

        // 再判断是不是枪械能造成的伤害
        if (!isGunDamage(source) && !source.`is`(DamageTypes.PLAYER_ATTACK)) return

        data.exp.set(data.exp.get() + expAmount)
        data.save()
    }

    private fun giveKillExpToWeapon(entity: LivingEntity, source: DamageSource) {
        val sourceEntity = source.entity as? LivingEntity ?: return
        val stack = sourceEntity.mainHandItem
        if (stack.item !is GunItem) return
        if (entity.type.`is`(ModTags.EntityTypes.NO_EXPERIENCE)) return

        val data = GunData.from(stack)
        val amount = (20 + 2 * entity.maxHealth).toDouble()

        // 先处理发射器类武器或高爆弹的爆炸伤害
        if (source.`is`(ModDamageTypes.PROJECTILE_EXPLOSION)) {
            if (data.get(GunProp.EXPLOSION_DAMAGE) > 0 || GunData.from(stack).perk.getLevel(ModPerks.HE_BULLET) > 0) {
                data.exp.add(amount)
            }
        }

        // 再判断是不是枪械能造成的伤害
        if (isGunDamage(source) || source.`is`(DamageTypes.PLAYER_ATTACK)) {
            data.exp.add(amount)
        }

        // 提升武器等级
        var level = data.level.get()
        var exp = data.exp.get()
        var upgradeExpNeeded = 20 * level.toDouble().pow(2.0) + 160 * level + 20

        while (exp >= upgradeExpNeeded) {
            exp -= upgradeExpNeeded
            level = data.level.get() + 1
            upgradeExpNeeded = 20 * level.toDouble().pow(2.0) + 160 * level + 20
            data.exp.set(exp)
            data.level.set(level)
        }
        data.save()
    }

    private fun handleGunLevels(entity: LivingEntity, source: DamageSource, amount: Float) {
        val sourceEntity = source.entity as? LivingEntity ?: return
        val stack = sourceEntity.mainHandItem
        if (stack.item !is GunItem) return
        if (entity.type.`is`(ModTags.EntityTypes.NO_EXPERIENCE)) return

        val data = GunData.from(stack)
        var level = data.level.get()
        var exp = data.exp.get()
        var upgradeExpNeeded = 20 * level.toDouble().pow(2.0) + 160 * level + 20

        while (exp >= upgradeExpNeeded) {
            exp -= upgradeExpNeeded
            level = data.level.get() + 1
            upgradeExpNeeded = 20 * level.toDouble().pow(2.0) + 160 * level + 20
            data.exp.set(exp)
            data.level.set(level)
        }
        data.save()
    }

    private fun killIndication(entity: LivingEntity, source: DamageSource) {
        if (!MiscConfig.SEND_KILL_FEEDBACK.get()) return

        val sourceEntity = source.entity ?: return

        // 如果配置不选择全局伤害提示，则只在伤害类型为mod添加的时显示指示器
        if (!GameplayConfig.GLOBAL_INDICATION.get() && !isModDamage(source)) {
            return
        }

        if (!sourceEntity.level().isClientSide() && sourceEntity is ServerPlayer) {
            if (postEvent(Indicator(sourceEntity, source, entity)).isCanceled) {
                return
            }

            SoundTool.playLocalSound(sourceEntity, ModSounds.TARGET_DOWN, 3f, 1f)
            sendPacketTo(sourceEntity, ClientIndicatorMessage(2, 8))
        }
    }

    private fun renderDamageIndicator(entity: LivingEntity, damagesource: DamageSource, amount: Float) {
        val sourceEntity = damagesource.entity ?: return

        if (sourceEntity is ServerPlayer && (damagesource.`is`(DamageTypes.EXPLOSION) || damagesource.`is`(DamageTypes.PLAYER_EXPLOSION)
                    || damagesource.`is`(ModDamageTypes.MINE) || damagesource.`is`(ModDamageTypes.PROJECTILE_EXPLOSION))
        ) {
            SoundTool.playLocalSound(sourceEntity, ModSounds.INDICATION, 1f, 1f)
            sendPacketTo(sourceEntity, ClientIndicatorMessage(0, 5))
        }
    }

    /**
     * 换弹时切换枪械，取消换弹音效播放
     */
    @JvmStatic
    fun handleChangeSlot(entity: LivingEntity, slot: EquipmentSlot, oldStack: ItemStack, newStack: ItemStack) {
        if (entity is Player && slot == EquipmentSlot.MAINHAND) {
            if (entity.level().isClientSide) return

            if (entity is ServerPlayer) {
                if (newStack.item is GunItem) {
                    checkCopyGuns(newStack, entity)
                }

                if (newStack.item !== oldStack.item || (newStack.item is GunItem && !GunData.from(newStack)
                        .initialized())
                    || (oldStack.item is GunItem && !GunData.from(oldStack).initialized())
                    || (newStack.item is GunItem && oldStack.item is GunItem && (GunsTool.getGunUUID(
                        NBTTool.getTag(
                            newStack
                        )
                    ) != GunsTool.getGunUUID(NBTTool.getTag(oldStack))))
                ) {
                    sendPacketTo(entity, DrawClientMessage)

                    val oldGun = oldStack.item
                    if (oldGun is GunItem) {
                        val oldData = GunData.from(oldStack)

                        stopGunReloadSound(entity, oldData)

                        if (oldData.get(GunProp.BOLT_ACTION_TIME) > 0) {
                            oldData.bolt.actionTimer.reset()
                        }

                        oldData.reload.setTime(0)

                        oldData.reload.setState(ReloadState.NOT_RELOADING)

                        if (oldData.get(GunProp.ITERATIVE_TIME) != 0) {
                            oldData.stopped.set(false)
                            oldData.forceStop.set(false)
                            oldData.reload.setStage(0)
                            oldData.reload.prepareTimer.reset()
                            oldData.reload.prepareLoadTimer.reset()
                            oldData.reload.iterativeLoadTimer.reset()
                            oldData.reload.finishTimer.reset()
                        }

                        if (oldStack.`is`(ModItems.SENTINEL)) {
                            oldData.charge.timer.reset()
                        }

                        // TODO 如何保存修改后的数据
                        oldGun.onChangeSlot(oldData, entity)
                        oldData.save()
                    }

                    if (newStack.item is GunItem) {
                        val newData = GunData.from(newStack)

                        if (newData.get(GunProp.BOLT_ACTION_TIME) > 0) {
                            newData.bolt.actionTimer.reset()
                        }

                        newData.reload.setState(ReloadState.NOT_RELOADING)
                        newData.reload.reloadTimer.reset()

                        if (newData.get(GunProp.ITERATIVE_TIME) != 0) {
                            newData.forceStop.set(false)
                            newData.stopped.set(false)
                            newData.reload.setStage(0)
                            newData.reload.prepareTimer.reset()
                            newData.reload.prepareLoadTimer.reset()
                            newData.reload.iterativeLoadTimer.reset()
                            newData.reload.finishTimer.reset()
                        }

                        if (newStack.`is`(ModItems.SENTINEL)) {
                            newData.charge.timer.reset()
                        }

                        for (type in Perk.Type.entries) {
                            val instance = newData.perk.getInstances(type)
                            instance.forEach { perk ->
                                perk.perk.onChangeSlot(
                                    newData,
                                    perk,
                                    entity
                                )
                            }
                        }

                        newData.save()
                    }
                }
            }
        }
    }

    private fun checkCopyGuns(stack: ItemStack, player: Player) {
        val data = GunData.from(stack)
        if (!data.initialized()) return
        val uuid = data.gunDataTag.getUUID("UUID")

        for (item in player.getInventory().items) {
            if (item == stack) continue
            if (item.item is GunItem) {
                val itemData = GunData.from(item)
                val dataTag = itemData.gunDataTag
                if (!dataTag.hasUUID("UUID")) continue
                if (dataTag.getUUID("UUID") == uuid) {
                    data.gunDataTag.putUUID("UUID", UUID.randomUUID())
                    return
                }
            }
        }
    }

    @JvmStatic
    fun stopGunReloadSound(player: ServerPlayer, data: GunData) {
        val soundInfo: SoundInfo = data.get(GunProp.SOUND_INFO)
        soundInfo.cancellableSounds.list
            .forEach { str ->
                val location = ResourceLocation.tryParse(str)
                if (location != null) {
                    player.connection.send(ClientboundStopSoundPacket(location, SoundSource.PLAYERS))
                }
            }
    }

    /**
     * 发送击杀消息
     */
    private fun handlePlayerKillEntity(entity: LivingEntity, source: DamageSource) {
        val damageTypeResourceKey = if (source.typeHolder().unwrapKey().isPresent) source.typeHolder().unwrapKey()
            .get() else DamageTypes.GENERIC

        var attacker: LivingEntity? = null
        val sourceEntity = source.entity
        val directEntity = source.directEntity

        if (sourceEntity is LivingEntity) {
            attacker = sourceEntity
        }

        if (directEntity is Projectile && directEntity.owner is LivingEntity) {
            val owner = directEntity.owner as LivingEntity
            if (owner is ServerPlayer) {
                attacker = owner
            } else if (owner is OwnableEntity && owner.owner is ServerPlayer) {
                attacker = owner
            }
        }

        if (attacker == null) return

        if (postEvent(SendKillMessage(attacker, source, entity)).isCanceled) {
            return
        }

        if (MiscConfig.SEND_KILL_FEEDBACK.get()) {
            if (isHeadshotDamage(source)) {
                sendPacketToAll(
                    LivingGunKillMessage(
                        attacker.id,
                        entity.id,
                        true,
                        damageTypeResourceKey
                    )
                )
            } else {
                sendPacketToAll(
                    LivingGunKillMessage(
                        attacker.id,
                        entity.id,
                        false,
                        damageTypeResourceKey
                    )
                )
            }
        }
    }

    private fun handleGunPerksWhenHurt(entity: LivingEntity, source: DamageSource, amount: Float): Float {
        if (!isGunDamage(source) && !source.`is`(DamageTypes.PLAYER_ATTACK)) return amount

        var attacker: LivingEntity? = null
        val sourceEntity = source.entity
        val directEntity = source.directEntity

        if (sourceEntity is LivingEntity) {
            attacker = sourceEntity
        }

        if (directEntity is Projectile && directEntity.owner is LivingEntity) {
            val owner = directEntity.owner as LivingEntity
            if (owner is ServerPlayer) {
                attacker = owner
            } else if (owner is OwnableEntity && owner.owner is ServerPlayer) {
                attacker = owner
            }
        }

        if (attacker == null) return amount

        val stack = attacker.mainHandItem
        if (stack.item !is GunItem) return amount

        var damage = amount

        val data = GunData.from(stack)
        for (type in Perk.Type.entries) {
            val instance = data.perk.getInstances(type)

            instance.forEach {
                if (isGunDamage(source)) {
                    damage = it.perk.getModifiedDamage(damage, data, it, entity, source)
                    it.perk.onHurtEntity(damage, data, it, entity, source)
                } else if (source.`is`(DamageTypes.PLAYER_ATTACK)) {
                    it.perk.onMeleeAttack(data, it, entity, source)
                }
            }
        }

        return damage
    }

    private fun handleGunPerksWhenDeath(entity: LivingEntity, source: DamageSource) {
        if (!isGunDamage(source)) return

        var attacker: LivingEntity? = null
        val sourceEntity = source.entity
        val directEntity = source.directEntity

        if (sourceEntity is LivingEntity) {
            attacker = sourceEntity
        }

        if (directEntity is Projectile && directEntity.owner is LivingEntity) {
            val owner = directEntity.owner as LivingEntity
            if (owner is ServerPlayer) {
                attacker = owner
            } else if (owner is OwnableEntity && owner.owner is ServerPlayer) {
                attacker = owner
            }
        }

        if (attacker == null) return

        val stack = attacker.mainHandItem
        if (stack.item !is GunItem) return

        val data = GunData.from(stack)
        for (type in Perk.Type.entries) {
            val instance = data.perk.getInstances(type)
            instance.forEach { it.perk.onKill(data, it, entity, source) }
        }
    }

    @JvmStatic
    fun onPickup(pickUp: ItemEntity, entity: Player): Boolean {
        if (!VehicleConfig.VEHICLE_ITEM_PICKUP.get()) return true
        val vehicle = entity.vehicle as? VehicleEntity ?: return true
        if (!vehicle.level().isClientSide) {
            val stack = pickUp.item.copy()
            val oldCount = stack.count

            val count = InventoryTool.insertItem(vehicle.inventory.getItems(), stack)

            pickUp.discard()

            if (oldCount > count) {
                val item = ItemStack(stack.item, oldCount - count)
                if (!entity.addItem(item)) {
                    entity.drop(item, false)
                }
            }
        }
        return false
    }

    @JvmStatic
    fun onLivingDrops(entity: LivingEntity, source: DamageSource, drops: MutableCollection<ItemEntity>) {
        playerDropAmmoBox(entity, source, drops)
        vehicleCollectDrops(entity, source, drops)
    }

    @JvmStatic
    fun beginLivingDrops() {
        capturedDrops.set(arrayListOf())
    }

    @JvmStatic
    fun captureLivingDrop(drop: ItemEntity) {
        capturedDrops.get()?.add(drop)
    }

    @JvmStatic
    fun finishLivingDrops(entity: LivingEntity, source: DamageSource, level: ServerLevel) {
        val drops = capturedDrops.get() ?: return
        capturedDrops.remove()

        val originalDrops = drops.toSet()
        onLivingDrops(entity, source, drops)
        (originalDrops - drops.toSet()).forEach(ItemEntity::discard)
        (drops - originalDrops).forEach(level::addFreshEntity)
    }

    /**
     * 开启死亡掉落 & 保留武器弹药时，玩家死亡会掉落一个弹药盒
     */
    private fun playerDropAmmoBox(entity: LivingEntity, source: DamageSource, drops: MutableCollection<ItemEntity>) {
        val player = entity as? Player ?: return
        if (!MiscConfig.DROP_AMMO_BOX.get()) return

        val cap = ModComponents.PLAYER_VARIABLE.get(player)

        val drop = Ammo.entries.sumOf { it.get(cap) } > 0
        if (!drop) return

        val stack = ItemStack(ModItems.AMMO_BOX)

        for (type in Ammo.entries) {
            type.set(stack, type.get(cap))
            type.set(cap, 0)
        }

        stack.ammoBoxData = stack.ammoBoxData.asDrop()

        ModComponents.PLAYER_VARIABLE.sync(player)

        drops += ItemEntity(player.level(), player.x, player.y + 1, player.z, stack)
    }

    /**
     * 载具撞死生物时自动收集掉落物
     */
    private fun vehicleCollectDrops(entity: LivingEntity, source: DamageSource, drops: MutableCollection<ItemEntity>) {
        if (!VehicleConfig.COLLECT_DROPS_BY_CRASHING.get()) return

        if (!source.`is`(ModDamageTypes.VEHICLE_STRIKE)) return

        val player = source.entity as? Player ?: return
        val vehicle = player.vehicle as? VehicleEntity ?: return

        val removed = arrayListOf<ItemEntity>()

        drops.forEach {
            val stack = it.item
            InventoryTool.insertItem(vehicle.inventory.getItems(), stack)

            if (stack.count <= 0) {
                player.drop(stack, false)
                removed.add(it)
            }
        }

        drops -= removed.toSet()
    }

    @JvmStatic
    fun onLivingExperienceDrop(entity: LivingEntity, player: Player?, originalXp: Int): Boolean {
        player ?: return false

        if (player.vehicle is VehicleEntity) {
            player.giveExperiencePoints(originalXp)
            return true
        }
        return false
    }

    @JvmStatic
    fun onKnockback(entity: LivingEntity): Float {
        val knockback = ICustomKnockback.getInstance(entity)
        if (knockback.`superbWarfare$getKnockbackStrength`() >= 0) {
            return knockback.`superbWarfare$getKnockbackStrength`().toFloat()
        }
        return -1f
    }

    @JvmStatic
    fun onEntityFall(entity: LivingEntity, fallDistance: Float, damageMultiplier: Float): Boolean {
        return entity.vehicle is VehicleEntity
    }

    @JvmStatic
    fun onPreSendKillMessage(event: SendKillMessage) {
        if (event.source.directEntity is AutoAimableEntity && event.target !is Player) {
            event.isCanceled = true
        }
    }

    @JvmStatic
    fun onPreIndicator(event: Indicator) {
        if (event.source.directEntity is AutoAimableEntity && event.target !is Player) {
            event.isCanceled = true
        }
    }

    @JvmStatic
    fun onEffectApply(entity: LivingEntity, effectInstance: MobEffectInstance): Boolean {
        val vehicle = entity.vehicle
        if (effectInstance.effect.value().category == MobEffectCategory.HARMFUL
            && vehicle is VehicleEntity
            && vehicle.isEnclosed(vehicle.getSeatIndex(entity))
        ) {
            return true
        }
        return false
    }
}
