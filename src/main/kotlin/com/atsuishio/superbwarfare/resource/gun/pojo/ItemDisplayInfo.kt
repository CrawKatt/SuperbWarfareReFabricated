package com.atsuishio.superbwarfare.resource.gun.pojo

import com.atsuishio.superbwarfare.serialization.kserializer.SerializedVector3f
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.joml.Vector3f

@Serializable
class ItemDisplayInfo {
    @SerialName("translation")
    var translation: SerializedVector3f = Vector3f(0f, 0f, 0f)

    @SerialName("rotation")
    var rotation: SerializedVector3f = Vector3f(0f, 0f, 0f)

    @SerialName("scale")
    var scale: SerializedVector3f = Vector3f(0f, 0f, 0f)
}