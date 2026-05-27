package com.atsuishio.superbwarfare.network.message.send;

import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.tools.SoundTool;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;


public class ZoomMessage {

    private final int type;

    public ZoomMessage(int type) {
        this.type = type;
    }

    public static ZoomMessage decode(FriendlyByteBuf buffer) {
        return new ZoomMessage(buffer.readInt());
    }

    public static void encode(ZoomMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
    }

    public static void handler(ZoomMessage message, ServerPlayer player) {
        if (player == null) return;

        if (!(player.getVehicle() instanceof VehicleEntity vehicle)) return;

        if (message.type == 0) {
            if (vehicle.hasWeapon(vehicle.getSeatIndex(player)) && vehicle.banHand(player)) {
                SoundTool.playLocalSound(player, ModSounds.CANNON_ZOOM_IN.get(), 2, 1);
            }
        }

        if (message.type == 1) {
            if (vehicle.hasWeapon(vehicle.getSeatIndex(player)) && vehicle.banHand(player)) {
                SoundTool.playLocalSound(player, ModSounds.CANNON_ZOOM_OUT.get(), 2, 1);
            }
        }
    }

}
