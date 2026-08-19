package com.atsuishio.superbwarfare.compat.coldsweat

/*
object ColdSweatCompatHandler {
    @JvmStatic
    fun onPlayerInVehicle(event: PlayerTickEvent) {
        val player = event.player ?: return
        val vehicle = player.vehicle
        if (vehicle is VehicleEntity
            && vehicle.hasEnergyStorage()
            && vehicle.isEnclosed(vehicle.getSeatIndex(player))
            && vehicle.energy > 0
        ) {
            Temperature.set(player, Temperature.Trait.CORE, 1.0)
        }
    }

    fun hasMod(): Boolean {
        return ModList.get().isLoaded(CompatHolder.COLD_SWEAT)
    }
}
*/
