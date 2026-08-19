package com.atsuishio.superbwarfare.compat.kubejs.event

import com.atsuishio.superbwarfare.api.event.LoadingDataEvent
import com.atsuishio.superbwarfare.api.event.LoadingJsonEvent
import com.atsuishio.superbwarfare.event.custom.LoadingDataCallback
import com.atsuishio.superbwarfare.event.custom.LoadingJsonCallback
import dev.latvian.mods.kubejs.event.EventGroup
import dev.latvian.mods.kubejs.event.EventHandler

object SbwKJSEventHandler {
    val GROUP: EventGroup = EventGroup.of("SuperbWarfareEvents")

    val LOADING_DATA_GUN: EventHandler = GROUP.server("loadingDataGun") { LoadingDataEventJS.Gun::class.java }
    val LOADING_DATA_VEHICLE: EventHandler =
        GROUP.server("loadingDataVehicle") { LoadingDataEventJS.Vehicle::class.java }
    val LOADING_JSON: EventHandler = GROUP.server("loadingJson") { LoadingJsonEventJS::class.java }

    fun init() {
        LoadingDataCallback.GUN.register { event ->
            if (onLoadingDataGun(event)) event.isCanceled = true
        }
        LoadingDataCallback.VEHICLE.register { event ->
            if (onLoadingDataVehicle(event)) event.isCanceled = true
        }
        LoadingJsonCallback.EVENT.register { event ->
            if (onLoadingJson(event)) event.isCanceled = true
        }
    }

    private fun onLoadingDataGun(event: LoadingDataEvent.Gun) =
        LOADING_DATA_GUN.hasListeners() && LOADING_DATA_GUN.post(LoadingDataEventJS.Gun(event)).interruptFalse()

    private fun onLoadingDataVehicle(event: LoadingDataEvent.Vehicle) =
        LOADING_DATA_VEHICLE.hasListeners() &&
            LOADING_DATA_VEHICLE.post(LoadingDataEventJS.Vehicle(event)).interruptFalse()

    private fun onLoadingJson(event: LoadingJsonEvent) =
        LOADING_JSON.hasListeners() && LOADING_JSON.post(LoadingJsonEventJS(event)).interruptFalse()
}
