package com.atsuishio.superbwarfare.data.vehicle.subdata

import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import com.atsuishio.superbwarfare.entity.vehicle.utils.VehicleEngineUtils.aircraftEngine
import com.atsuishio.superbwarfare.entity.vehicle.utils.VehicleEngineUtils.helicopterEngine
import com.atsuishio.superbwarfare.entity.vehicle.utils.VehicleEngineUtils.shipEngine
import com.atsuishio.superbwarfare.entity.vehicle.utils.VehicleEngineUtils.tomEngine
import com.atsuishio.superbwarfare.entity.vehicle.utils.VehicleEngineUtils.trackEngine
import com.atsuishio.superbwarfare.entity.vehicle.utils.VehicleEngineUtils.wheelChairEngine
import com.atsuishio.superbwarfare.entity.vehicle.utils.VehicleEngineUtils.wheelEngine
import com.atsuishio.superbwarfare.serialization.kserializer.SerializedSoundEvent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.sounds.SoundEvents

@Serializable
abstract class EngineInfo {
    // 能量消耗比例
    @JvmField
    @SerialName("EnergyCostRate")
    var energyCostRate: Double = 1.0

    // 浮力，大于零时认为载具是两栖的
    @JvmField
    @SerialName("Buoyancy")
    var buoyancy: Double = 0.0

    // 前进加速度
    @JvmField
    @SerialName("Increment")
    var increment: Float = 0.001f

    // 后退加速度
    @JvmField
    @SerialName("Decrement")
    var decrement: Float = 0.001f

    @JvmField
    @SerialName("EngineSoundVolume")
    var engineSoundVolume: Float = 0.4f

    abstract fun work(vehicle: VehicleEntity)

    @Serializable
    open class Wheel : EngineInfo() {
        @JvmField
        @SerialName("WheelRotSpeed")
        var wheelRotSpeed: Double = 0.0

        @JvmField
        @SerialName("WheelDifferential")
        var wheelDifferential: Double = 0.0

        // 转向速度
        @JvmField
        @SerialName("SteeringSpeed")
        var steeringSpeed: Float = 0.1f

        // 最大前进速度系数
        @JvmField
        @SerialName("MaxForwardSpeedRate")
        var maxForwardSpeedRate: Float = 0.2f

        // 最大后退速度系数
        @JvmField
        @SerialName("MaxBackwardSpeedRate")
        var maxBackwardSpeedRate: Float = -0.1f

        override fun work(vehicle: VehicleEntity) {
            wheelEngine(vehicle, this)
        }
    }

    @Serializable
    class Track : Wheel() {
        @JvmField
        @SerialName("TrackRotSpeed")
        var trackRotSpeed: Double = 0.0

        @JvmField
        @SerialName("TrackDifferential")
        var trackDifferential: Double = 0.0

        override fun work(vehicle: VehicleEntity) {
            trackEngine(vehicle, this)
        }
    }

    @Serializable
    class WheelChair : Wheel() {
        @JvmField
        @SerialName("BodyRollRate")
        var bodyRollRate: Double = 1.0

        @JvmField
        @SerialName("CanJump")
        var canJump: Boolean = false

        @JvmField
        @SerialName("JumpEnergyCost")
        var jumpEnergyCost: Int = 400

        @JvmField
        @SerialName("JumpCoolDown")
        var jumpCoolDown: Int = 3

        @JvmField
        @SerialName("JumpForce")
        var jumpForce: Double = 0.6

        override fun work(vehicle: VehicleEntity) {
            wheelChairEngine(vehicle, this)
        }
    }

    @Serializable
    class Ship : EngineInfo() {
        @JvmField
        @SerialName("BodyPitchRate")
        var bodyPitchRate: Double = 1.0

        @JvmField
        @SerialName("BodyRollRate")
        var bodyRollRate: Double = 1.0

        // 转向速度
        @JvmField
        @SerialName("SteeringSpeed")
        var steeringSpeed: Float = 0.1f

        // 最大前进速度系数
        @JvmField
        @SerialName("MaxForwardSpeedRate")
        var maxForwardSpeedRate: Float = 0.2f

        // 最大后退速度系数
        @JvmField
        @SerialName("MaxBackwardSpeedRate")
        var maxBackwardSpeedRate: Float = -0.1f

        override fun work(vehicle: VehicleEntity) {
            shipEngine(vehicle, this)
        }
    }

    @Serializable
    open class Helicopter : EngineInfo() {
        @JvmField
        @SerialName("PitchSpeed")
        var pitchSpeed: Float = 1f

        @JvmField
        @SerialName("YawSpeed")
        var yawSpeed: Float = 1f

        @JvmField
        @SerialName("RollSpeed")
        var rollSpeed: Float = 1f

        @JvmField
        @SerialName("LiftSpeed")
        var liftSpeed: Float = 1f

        @JvmField
        @SerialName("Speed")
        var speed: Float = 1f

        // 引擎启动音效
        @JvmField
        @SerialName("EngineStartSound")
        var engineStartSound: SerializedSoundEvent = SoundEvents.EMPTY

        override fun work(vehicle: VehicleEntity) {
            helicopterEngine(vehicle, this)
        }
    }

    @Serializable
    open class Aircraft : Helicopter() {
        @JvmField
        @SerialName("SpeedRate")
        var speedRate: Float = 1f

        @JvmField
        @SerialName("GearRotateAngle")
        var gearRotateAngle: Float = 85f

        @JvmField
        @SerialName("HasGear")
        var hasGear: Boolean = true

        @JvmField
        @SerialName("HasStukaSound")
        var hasStukaSound: Boolean = false

        @JvmField
        @SerialName("Resistance")
        var resistance: Float = 1f

        override fun work(vehicle: VehicleEntity) {
            aircraftEngine(vehicle, this)
        }
    }

    @Serializable
    class Tom6 : Aircraft() {
        override fun work(vehicle: VehicleEntity) {
            tomEngine(vehicle, this)
        }
    }
}
