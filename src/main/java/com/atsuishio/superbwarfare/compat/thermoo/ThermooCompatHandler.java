package com.atsuishio.superbwarfare.compat.thermoo;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.compat.CompatHolder;
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.level.ServerPlayer;

public class ThermooCompatHandler {
    private static final int VEHICLE_CORE_TEMPERATURE = 1;

    public static void init() {
        if (hasMod()) {
            Mod.LOGGER.info("Thermoo detected, enabling temperature compatibility for enclosed powered vehicles");
        }
    }

    public static void onPlayerInVehicle(ServerPlayer player) {
        if (!hasMod() || !(player.getVehicle() instanceof VehicleEntity vehicle)) {
            return;
        }

        if (vehicle.hasEnergyStorage()
                && vehicle.isEnclosed(vehicle.getSeatIndex(player))
                && vehicle.getEnergy() > 0
        ) {
            ThermooCompatApi.setTemperature(player, VEHICLE_CORE_TEMPERATURE);
        }
    }

    public static boolean hasMod() {
        return FabricLoader.getInstance().isModLoaded(CompatHolder.THERMOO);
    }
}
