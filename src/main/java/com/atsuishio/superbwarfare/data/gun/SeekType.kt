package com.atsuishio.superbwarfare.data.gun

import com.google.gson.annotations.SerializedName

enum class SeekType {
    @SerializedName("None")
    NONE,

    @SerializedName("HoldFire")
    HOLD_FIRE,

    @SerializedName("HoldZoom")
    HOLD_ZOOM,
}
