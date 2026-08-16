package com.atsuishio.superbwarfare.resource.vehicle

import com.atsuishio.superbwarfare.data.ModColor
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class SponsorInfo {
    @SerialName("Name")
    val name: String? = null

    @SerialName("Color")
    val color: ModColor = ModColor(0x7DEA79)
}