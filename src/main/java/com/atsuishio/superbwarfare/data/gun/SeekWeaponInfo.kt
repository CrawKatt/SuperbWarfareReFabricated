package com.atsuishio.superbwarfare.data.gun

import com.atsuishio.superbwarfare.data.StringOrVec3
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class SeekWeaponInfo {
    @JvmField
    @SerializedName("SeekDirection")
    @SerialName("SeekDirection")
    var seekDirection: StringOrVec3 = StringOrVec3("Default")

    @JvmField
    @SerializedName("SeekRange")
    @SerialName("SeekRange")
    var seekRange = 384.0

    @JvmField
    @SerializedName("SeekAngle")
    @SerialName("SeekAngle")
    var seekAngle = 20.0

    @JvmField
    @SerializedName("MinTargetHeight")
    @SerialName("MinTargetHeight")
    var minTargetHeight = 0.0

    @JvmField
    @SerializedName("MaxTargetHeight")
    @SerialName("MaxTargetHeight")
    var maxTargetHeight = 114514.0

    @JvmField
    @SerializedName("SeekTime")
    @SerialName("SeekTime")
    var seekTime = 10

    @JvmField
    @SerializedName("MinTargetSize")
    @SerialName("MinTargetSize")
    var minTargetSize = 0.0

    @JvmField
    @SerializedName("CalculateTrajectory")
    @SerialName("CalculateTrajectory")
    var calculateTrajectory = false

    @JvmField
    @SerializedName("OnlyLockBlock")
    @SerialName("OnlyLockBlock")
    var onlyLockBlock = false

    @JvmField
    @SerializedName("OnlyLockEntity")
    @SerialName("OnlyLockEntity")
    var onlyLockEntity = false

    @JvmField
    @SerializedName("MaxGuidedRange")
    @SerialName("MaxGuidedRange")
    var maxGuidedRange = 2048.0

    @JvmField
    @SerializedName("CanGuidedByRadar")
    @SerialName("CanGuidedByRadar")
    var canGuidedByRadar = true

    @JvmField
    @SerializedName("AffectedByStealthTarget")
    @SerialName("AffectedByStealthTarget")
    var affectedByStealthTarget = true
}