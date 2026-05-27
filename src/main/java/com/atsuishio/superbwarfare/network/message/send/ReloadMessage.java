package com.atsuishio.superbwarfare.network.message.send;

import com.atsuishio.superbwarfare.data.gun.GunData;
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.atsuishio.superbwarfare.event.GunEventHandler;
import com.atsuishio.superbwarfare.item.gun.GunItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public enum ReloadMessage {
    INSTANCE;

    public static void handler(ServerPlayer player) {
        if (player != null) {
            pressAction(player);
        }
    }

    public static void pressAction(Player player) {
        if (player.getVehicle() instanceof VehicleEntity vehicle && vehicle.hasWeapon(vehicle.getSeatIndex(player))) {
            vehicle.modifyGunData(vehicle.getSeatIndex(player), data -> GunEventHandler.tryStartReload(vehicle.getAmmoSupplier(), data));
            return;
        }

        var stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof GunItem)) return;
        GunEventHandler.tryStartReload(player, GunData.from(stack));
    }
}
