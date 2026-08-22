package com.atsuishio.superbwarfare.api.event

import com.atsuishio.superbwarfare.data.gun.DefaultGunData
import com.atsuishio.superbwarfare.data.vehicle.DefaultVehicleData
import org.jetbrains.annotations.ApiStatus

@ApiStatus.AvailableSince("0.8.9")
open class LoadingDataEvent<T : Any> private constructor(
    val id: String,
    var data: T
) {
    var isCanceled: Boolean = false

    class Gun(id: String, data: DefaultGunData) : LoadingDataEvent<DefaultGunData>(id, data)

    class Vehicle(id: String, data: DefaultVehicleData) : LoadingDataEvent<DefaultVehicleData>(id, data)
}
