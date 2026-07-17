package com.atsuishio.superbwarfare.entity.misc

import com.atsuishio.superbwarfare.block.AircraftCatapultBlock
import com.atsuishio.superbwarfare.init.ModEntities
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import net.minecraftforge.network.NetworkHooks
import kotlin.math.atan2

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

        if (!this.level().isClientSide) {
            this.moveOnCatapult()
        }
    }

    /** 上一 tick 所在的弹射器方块位置，用于检测转弯 */
    private var lastBelowPos: BlockPos? = null

    /** 上一 tick 的有效运动方向，用于检测转弯 */
    private var lastEffectiveFacing: Direction? = null

    /**
     * 检测下方方块是否为弹射器方块，根据弹射器方块的属性控制滑块运动。
     * 参考原版矿车与轨道方块的交互逻辑：直道逐渐加速，转弯或尽头急刹车。
     * 移动方式参考 FastThrowableProjectile.projectileMove，使用 setPos 直接设位
     * 避免 move(MoverType.SELF, ...) 的碰撞检测造成客户端卡顿。
     */
    private fun moveOnCatapult() {
        val belowPos = BlockPos.containing(this.x, this.boundingBox.minY - 0.01, this.z)
        val belowState = this.level().getBlockState(belowPos)

        if (belowState.block !is AircraftCatapultBlock) {
            this.lastBelowPos = null
            this.lastEffectiveFacing = null
            return
        }

        val power = belowState.getValue(AircraftCatapultBlock.LAUNCH_POWER)
        val facing = belowState.getValue(AircraftCatapultBlock.FACING)
        val reversed = belowState.getValue(AircraftCatapultBlock.REVERSED)

        // 对齐实体朝向到轨道方向（平滑旋转）
        val targetYRot = getWorldYRot(facing)
        val diffY = Mth.wrapDegrees(targetYRot - this.yRot)
        this.yRot += 0.9f * diffY
        this.yRotO += 0.9f * diffY

        val effectiveFacing = if (reversed) facing.opposite else facing
        val moveDir = Vec3(effectiveFacing.stepX.toDouble(), 0.0, effectiveFacing.stepZ.toDouble())

        // 检测转弯：进入了新的方块，且运动方向发生变化
        val isTurn = lastBelowPos != null &&
                lastBelowPos != belowPos &&
                lastEffectiveFacing != null &&
                lastEffectiveFacing != effectiveFacing

        if (power == 0 || isTurn) {
            // 无动力或转弯时，急刹车到当前方块中心
            this.setPos(belowPos.x + 0.5, this.y, belowPos.z + 0.5)
            this.deltaMovement = Vec3.ZERO
            this.lastBelowPos = belowPos
            this.lastEffectiveFacing = effectiveFacing
            return
        }

        // 检查运动方向的下一个方块是否为弹射器方块（检测"轨道"是否延续）
        val nextBelowPos = belowPos.relative(effectiveFacing)
        val nextBelowState = this.level().getBlockState(nextBelowPos)

        if (nextBelowState.block !is AircraftCatapultBlock) {
            // 前方没有轨道，急停在当前弹射器方块中心
            this.setPos(belowPos.x + 0.5, this.y, belowPos.z + 0.5)
            this.deltaMovement = Vec3.ZERO
            this.lastBelowPos = null
            this.lastEffectiveFacing = null
            return
        }

        // 每 tick 加速：速度逐渐增加，模拟弹射器持续推动
        val acceleration = power / 75.0
        this.deltaMovement = this.deltaMovement.add(moveDir.scale(acceleration))

        // 摩擦力衰减
        this.deltaMovement = this.deltaMovement.multiply(0.98, 0.0, 0.98)

        // 将速度投影到轨道方向，滤除垂直于轨道的漂移分量
        val dot = this.deltaMovement.dot(moveDir)
        if (dot > 0) {
            this.deltaMovement = moveDir.scale(dot)
        } else {
            this.deltaMovement = Vec3.ZERO
        }

        // 参考 FastThrowableProjectile.projectileMove：直接 setPos 而非 move(MoverType.SELF, ...)
        this.setPos(this.x + this.deltaMovement.x, this.y, this.z + this.deltaMovement.z)

        // 在垂直于运动方向对齐到方块中心，确保转弯时准确定位在轨道上
        if (effectiveFacing.axis == Direction.Axis.X) {
            this.setPos(this.x, this.y, belowPos.z + 0.5)
        } else {
            this.setPos(belowPos.x + 0.5, this.y, this.z)
        }

        this.lastBelowPos = belowPos
        this.lastEffectiveFacing = effectiveFacing
    }

    /**
     * 根据弹射器方块的朝向计算世界 yaw 角度。
     */
    private fun getWorldYRot(facing: Direction): Float {
        return Math.toDegrees(atan2(facing.stepX.toDouble(), facing.stepZ.toDouble())).toFloat()
    }

    public override fun addAdditionalSaveData(compound: CompoundTag) {
    }

    public override fun readAdditionalSaveData(compound: CompoundTag) {
    }
}
