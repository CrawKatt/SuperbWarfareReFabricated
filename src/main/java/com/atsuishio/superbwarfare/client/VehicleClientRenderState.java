package com.atsuishio.superbwarfare.client;

import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import net.minecraft.world.entity.player.Player;

public final class VehicleClientRenderState {
    public static boolean shouldHideHandsAndHotbar(Player player) {
        return player != null
                && !player.isSpectator()
                && player.getVehicle() instanceof VehicleEntity vehicle
                && vehicle.banHand(player);
    }

    public static boolean shouldHideVehiclePassenger(Player player) {
        return player != null
                && !player.isSpectator()
                && player.getVehicle() instanceof VehicleEntity vehicle
                && vehicle.hidePassenger(player);
    }
}
