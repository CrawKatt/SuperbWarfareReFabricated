package com.atsuishio.superbwarfare.data.vehicle.subdata

import com.atsuishio.superbwarfare.annotation.ServerOnly
import com.atsuishio.superbwarfare.data.ObjectToList
import com.atsuishio.superbwarfare.serialization.kserializer.SerializedVec3
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.world.phys.Vec3

@Serializable
class SeatInfo {
    @JvmField
    @SerialName("HidePassenger")
    var hidePassenger: Boolean = false

    @JvmField
    @SerialName("IsEnclosed")
    @ServerOnly
    var isEnclosed: Boolean? = null

    @JvmField
    @SerialName("Transform")
    var transform: String = "Default"

    @JvmField
    @SerialName("Pose")
    var pose: String = "Default"

    @JvmField
    @SerialName("Position")
    var position: SerializedVec3 = Vec3.ZERO

    @JvmField
    @SerialName("Orientation")
    var orientation: Float = 0f

    @JvmField
    @SerialName("CanRotateBody")
    var canRotateBody: Boolean = false

    @JvmField
    @SerialName("CanRotateHead")
    var canRotateHead: Boolean = true

    @JvmField
    @SerialName("HasThermalImaging")
    var hasThermalImaging: Boolean = false

    @JvmField
    @SerialName("MinPitch")
    var minPitch: Float = -90f

    @JvmField
    @SerialName("MaxPitch")
    var maxPitch: Float = 90f

    @JvmField
    @SerialName("MinYaw")
    var minYaw: Float = -514f

    @JvmField
    @SerialName("MaxYaw")
    var maxYaw: Float = 514f

    @SerialName("Weapons")
    private var weapons: ObjectToList<String>? = ObjectToList()

    fun weapons() = weapons?.list ?: mutableListOf()

    @JvmField
    @SerialName("CameraPos")
    var cameraPos: CameraPos? = null

    @JvmField
    @SerialName("BanHand")
    var banHand: Boolean = false

    @JvmField
    @SerialName("Sensitivity")
    var sensitivity: SerializedVec3 = Vec3(1.0, 1.0, 1.0)

    @JvmField
    @SerialName("DismountInfo")
    var dismountInfo: DismountInfo? = null
}