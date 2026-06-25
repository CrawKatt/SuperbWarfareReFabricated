package com.atsuishio.superbwarfare.compat.thermoo

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.compat.CompatHolder
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.server.level.ServerPlayer

object ThermooCompatHandler {
    private const val VEHICLE_CORE_TEMPERATURE = 1

    @JvmStatic
    fun init() {
        if (hasMod()) {
            Mod.LOGGER.info("Thermoo detected, enabling temperature compatibility for enclosed powered vehicles")
        }
    }

    @JvmStatic
    fun onPlayerInVehicle(player: ServerPlayer) {
        if (!hasMod() || player.vehicle !is VehicleEntity) {
            return
        }

        val vehicle = player.vehicle as VehicleEntity

        if (vehicle.hasEnergyStorage()
            && vehicle.isEnclosed(vehicle.getSeatIndex(player))
            && vehicle.energy > 0
        ) {
            ThermooCompatApi.setTemperature(player, VEHICLE_CORE_TEMPERATURE)
        }
    }

    @JvmStatic
    fun hasMod(): Boolean {
        return FabricLoader.getInstance().isModLoaded(CompatHolder.THERMOO)
    }
}