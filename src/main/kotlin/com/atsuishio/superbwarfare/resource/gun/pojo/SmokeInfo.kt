package com.atsuishio.superbwarfare.resource.gun.pojo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class SmokeInfo {
    // 初始大小
    @SerialName("Size")
    var size: Float = 0.3f

    // 增长量
    @SerialName("Growth")
    var growth: Float = 0.3f

    // 持续时间
    @SerialName("Lifetime")
    var lifetime: Float = 0.3f

    // 扩散速度
    @SerialName("Speed")
    var speed: Float = 0.6f

    // 粒子数量
    @SerialName("Count")
    var count: Float = 4f

    // 透明度
    @SerialName("Opacity")
    var opacity: Float = 1f

    // 速度衰减
    @SerialName("Drag")
    var drag: Float = 2f

    @SerialName("RandomSize")
    var randomSize: Float = 0.4f

    @SerialName("RandomGrowth")
    var randomGrowth: Float = 0.5f

    @SerialName("RandomLifetime")
    var randomLifetime: Float = 0.3f

    @SerialName("RandomSpeed")
    var randomSpeed: Float = 0.6f

    @SerialName("RandomCount")
    var randomCount: Float = 0.3f

    @SerialName("RandomOpacity")
    var randomOpacity: Float = 0.3f
}