package com.atsuishio.superbwarfare.data.gun

import com.google.gson.annotations.SerializedName

enum class ReloadType {
    @SerializedName("Magazine")
    MAGAZINE,

    @SerializedName("Clip")
    CLIP,

    @SerializedName("Iterative")
    ITERATIVE,
}
