package com.atsuishio.superbwarfare.resource.gun.pojo

import com.atsuishio.superbwarfare.serialization.kserializer.SerializedResourceLocation
import com.atsuishio.superbwarfare.serialization.kserializer.SerializedVector3f
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.joml.Vector3f

@Serializable
class ShellEjectInfo {
    @SerialName("BoneName")
    var boneName: String = "shell"

    @SerialName("ShellModel")
    var shellModel: SerializedResourceLocation? = null

    @SerialName("ShellTexture")
    var shellTexture: SerializedResourceLocation? = null

    @SerialName("Size")
    var size: Float = 1f

    @SerialName("InitialVelocity")
    var initialVelocity: SerializedVector3f = Vector3f(1.6f, 0.9f, 0.25f)

    @SerialName("RandomVelocity")
    var randomVelocity: SerializedVector3f = Vector3f(0.4f, 0.35f, 0.15f)

    @SerialName("Acceleration")
    var acceleration: SerializedVector3f = Vector3f(0f, -18f, 0f)

    @SerialName("AngularVelocity")
    var angularVelocity: SerializedVector3f = Vector3f(-1800f, -2000f, 240f)

    @SerialName("RandomAngle")
    var randomAngle: SerializedVector3f = Vector3f(0f, 0f, 0f)

    @SerialName("LivingTime")
    var livingTime: Float = 0.9f

    @SerialName("MaxActive")
    var maxActive: Int = 32
}