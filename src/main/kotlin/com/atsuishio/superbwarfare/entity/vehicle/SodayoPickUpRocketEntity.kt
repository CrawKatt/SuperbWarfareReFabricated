package com.atsuishio.superbwarfare.entity.vehicle

import com.atsuishio.superbwarfare.data.gun.GunProp
import com.atsuishio.superbwarfare.entity.projectile.MediumRocketEntity
import com.atsuishio.superbwarfare.entity.vehicle.base.ArtilleryEntity
import com.atsuishio.superbwarfare.init.ModDamageTypes.causeBurnDamage
import com.atsuishio.superbwarfare.init.ModEntities
import com.atsuishio.superbwarfare.init.ModSerializers
import com.atsuishio.superbwarfare.init.ModSounds
import com.atsuishio.superbwarfare.init.ModTags
import com.atsuishio.superbwarfare.item.projectile.MediumRocketItem
import com.atsuishio.superbwarfare.tools.OBB
import com.atsuishio.superbwarfare.tools.OBB.Companion.getLookingObb
import com.atsuishio.superbwarfare.tools.OBB.Companion.vector3dToVec3
import com.atsuishio.superbwarfare.tools.ParticleTool.spawnMediumCannonMuzzleParticles
import it.unimi.dsi.fastutil.ints.IntArrayList
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.level.entity.EntityTypeTest
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

open class SodayoPickUpRocketEntity(type: EntityType<SodayoPickUpRocketEntity>, level: Level) :
    ArtilleryEntity(type, level) {

    val barrelObbs: List<OBB>
        get() = getOBBs().filter { it.part == OBB.Part.INTERACTIVE }

    var cooldown: Int = 0

    override fun defineSynchedData() {
        super.defineSynchedData()
        val list = IntArrayList()
        repeat(this.getContainerSize()) {
            list.add(-1)
        }
        this.entityData.define(LOADED_AMMO, list)
    }

    public override fun readAdditionalSaveData(compound: CompoundTag) {
        super.readAdditionalSaveData(compound)
        setChanged()
    }

    override fun interact(player: Player, hand: InteractionHand): InteractionResult {
        val stack = player.mainHandItem
        val lookingObb = getLookingObb(player, player.getEntityReach())
        val level = this.level()

        if (stack.isEmpty) {
            // 取出炮弹
            player.swing(InteractionHand.MAIN_HAND)
            if (level is ServerLevel && cooldown == 0) {
                for (i in barrelObbs.indices) {
                    if (lookingObb === barrelObbs[i]) {
                        if (getItems()[i].isEmpty) {
                            return super.interact(player, hand)
                        } else {
                            player.addItem(getItems()[i].copyWithCount(1))
                            val vec3 = vector3dToVec3(barrelObbs[i].center)
                            level.playSound(
                                null,
                                vec3.x,
                                vec3.y,
                                vec3.z,
                                ModSounds.TYPE_63_RELOAD,
                                SoundSource.PLAYERS,
                                1f,
                                random.nextFloat() * 0.1f + 0.9f
                            )
                            cooldown = 5
                            getItems()[i] = ItemStack.EMPTY
                            setChanged()
                            return InteractionResult.SUCCESS
                        }
                    }
                }
            }
        }

        if (stack.item is MediumRocketItem) {
            for (i in barrelObbs.indices) {
                if (lookingObb === barrelObbs[i] && getItems()[i].isEmpty && level is ServerLevel && cooldown == 0) {
                    this.setItem(i, stack.copyWithCount(1))
                    if (!player.isCreative) {
                        stack.shrink(1)
                    }
                    val vec3 = vector3dToVec3(barrelObbs[i].center)
                    level.playSound(
                        null,
                        vec3.x,
                        vec3.y,
                        vec3.z,
                        ModSounds.TYPE_63_RELOAD,
                        SoundSource.PLAYERS,
                        1f,
                        random.nextFloat() * 0.1f + 0.9f
                    )
                    cooldown = 5
                    setChanged()
                }
                player.swing(InteractionHand.MAIN_HAND)
            }
            return InteractionResult.SUCCESS
        }

        if (cooldown == 0 && !player.isShiftKeyDown && (stack.`is`(ModTags.Items.TOOLS_CROWBAR) || stack.`is`(Items.FLINT_AND_STEEL))) {
            // 发射
            if (lookingAtBarrel(player)) {
                // 精准发射
                for (i in barrelObbs.indices) {
                    if (lookingObb === barrelObbs[i] && getItems()[i].item is MediumRocketItem) {
                        cooldown = 10
                        shoot(player, i)
                        getItems()[i] = ItemStack.EMPTY
                        setChanged()
                    }
                }
                player.swing(InteractionHand.MAIN_HAND)
                return InteractionResult.SUCCESS
            } else {
                // 顺序发射
                for (i in 0..11) {
                    if (getItems()[i].item is MediumRocketItem) {
                        cooldown = 10
                        shoot(player, i)
                        getItems()[i] = ItemStack.EMPTY
                        setChanged()

                        player.swing(InteractionHand.MAIN_HAND)
                        return InteractionResult.SUCCESS
                    }
                }
            }
        }

        return super.interact(player, hand)
    }

    fun lookingAtBarrel(player: Player): Boolean {
        val lookingObb = getLookingObb(player, player.getEntityReach())

        for (i in 0..11) {
            if (lookingObb === barrelObbs[i]) {
                return true
            }
        }

        return false
    }

    override fun canBind(): Boolean {
        return true
    }

    override fun vehicleShoot(living: LivingEntity?, weaponName: String, targetPos: Vec3?) {
        if (this.isWreck) return
        // 顺序发射
        for (i in 0..11) {
            if (getItems()[i].item is MediumRocketItem && living is Player && cooldown == 0) {
                shoot(living, i)
                cooldown = 3
                getItems()[i] = ItemStack.EMPTY
                setChanged()
            }
        }
    }

    fun shoot(player: Player?, i: Int) {
        val stack = getItems()[i]
        val item = stack.item
        val level = this.level()

        if (item !is MediumRocketItem) {
            return
        }

        val gunData = getGunData(item.type.toString()) ?: return

        val shootVelocity = getProjectileVelocity(gunData)
        val shootSpread = getProjectileSpread(gunData)
        val shootGravity = getProjectileGravity(gunData)

        val obb = barrelObbs[i]
        val shootPos = vector3dToVec3(obb.center)

        val entityToSpawn = MediumRocketEntity(
            ModEntities.MEDIUM_ROCKET,
            shootPos.x,
            shootPos.y,
            shootPos.z,
            level(),
            gunData.get(GunProp.DAMAGE).toFloat(),
            gunData.get(GunProp.EXPLOSION_RADIUS).toFloat(),
            gunData.get(GunProp.EXPLOSION_DAMAGE).toFloat(),
            0f,
            0,
            item.type,
            gunData.get(GunProp.SPREAD_AMOUNT),
            gunData.get(GunProp.SPREAD_ANGLE)
        )
        entityToSpawn.durability(gunData.get(GunProp.AP_DURABILITY))
        entityToSpawn.setCustomGravity(shootGravity)
        entityToSpawn.owner = player

        val barrelVector = getBarrelVector(1f)
        entityToSpawn.shoot(barrelVector.x, barrelVector.y, barrelVector.z, shootVelocity, shootSpread)
        level().addFreshEntity(entityToSpawn)

        val sound = gunData.get(GunProp.SOUND_INFO).fire3P
        if (sound != null) {
            level().playSound(
                null,
                shootPos.x,
                shootPos.y,
                shootPos.z,
                sound,
                SoundSource.PLAYERS,
                gunData.get(GunProp.SOUND_RADIUS).toFloat(),
                random.nextFloat() * 0.1f + 0.95f
            )
        }

        val ab = AABB(boundingBox.center, boundingBox.center).inflate(0.75)
            .move(barrelVector.scale(-2.0)).expandTowards(barrelVector.scale(-5.0))

        // 尾焰
        for (entity in level.getEntities(
            EntityTypeTest.forClass(Entity::class.java),
            ab
        ) { it !== this }) {
            entity.hurt(causeBurnDamage(entity.level().registryAccess(), player), 30 - 2 * entity.distanceTo(this))
            val force = 4 - 0.7 * entity.distanceTo(this)
            entity.push(-force * barrelVector.x, -force * barrelVector.y, -force * barrelVector.z)
        }

        if (level is ServerLevel) {
            spawnMediumCannonMuzzleParticles(
                barrelVector.scale(-1.0),
                shootPos.add(barrelVector.scale(-0.5)),
                level,
                this
            )
            spawnMediumCannonMuzzleParticles(
                barrelVector.scale(-1.0),
                shootPos.add(barrelVector.scale(-1.5)),
                level,
                this
            )
            spawnMediumCannonMuzzleParticles(barrelVector, shootPos.add(barrelVector.scale(1.5)), level, this)
        }

        gunData.shakePlayers(this)
    }

    override fun baseTick() {
        super.baseTick()

        if (decoyInputDown) {
            horn()
        }

        if (cooldown > 0) {
            cooldown--
        }

        this.refreshDimensions()
    }

    override var maxStackSize: Int = 1

    override fun canPlaceItem(slot: Int, stack: ItemStack): Boolean {
        return false
    }

    override fun setChanged() {
        super.setChanged()
        val list = arrayListOf<Int>()
        for (item in this.getItems()) {
            val i = item.item
            if (i is MediumRocketItem) {
                list.add(i.type.ordinal)
            } else {
                list.add(-1)
            }
        }
        this.entityData.set(LOADED_AMMO, list)
    }

    companion object {
        @JvmField
        val LOADED_AMMO: EntityDataAccessor<List<Int>> = SynchedEntityData.defineId(
            SodayoPickUpRocketEntity::class.java,
            ModSerializers.INT_LIST_SERIALIZER
        )
    }
}
