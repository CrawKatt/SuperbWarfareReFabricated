package com.atsuishio.superbwarfare.resource.gun.pojo

import com.atsuishio.superbwarfare.serialization.kserializer.SerializedVector3f
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.joml.Vector3f

@Serializable
class ShootRecoilInfo {
    @SerialName("Offset")
    var offset: SerializedVector3f = Vector3f(1f, 1f, 1f)

    @SerialName("Rotation")
    var rotation: SerializedVector3f = Vector3f(1f, 1f, 1f)

    @SerialName("ZoomRate")
    var zoomRate: Float = 0.2f

    @SerialName("Speed")
    var speed: Float = 1f
}