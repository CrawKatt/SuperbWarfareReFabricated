package com.atsuishio.superbwarfare.data.vehicle.subdata

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class SeekInfo {
    @JvmField
    @SerialName("MaxSeekRange")
    var maxSeekRange: Double = 64.0

    @JvmField
    @SerialName("MinSeekRange")
    var minSeekRange: Double = 1.0

    @JvmField
    @SerialName("ChangeTargetTime")
    var changeTargetTime: Int = 60

    @JvmField
    @SerialName("SeekIterative")
    var seekIterative: Int = 20

    @JvmField
    @SerialName("MinTargetSize")
    var minTargetSize: Double = 0.25

    @JvmField
    @SerialName("SeekEnergyCost")
    var seekEnergyCost: Int = 1000
}