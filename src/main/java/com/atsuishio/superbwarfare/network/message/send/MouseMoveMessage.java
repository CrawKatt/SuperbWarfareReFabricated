package com.atsuishio.superbwarfare.network.message.send;

import com.atsuishio.superbwarfare.entity.vehicle.DroneEntity;
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.tools.EntityFindUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class MouseMoveMessage {

    private final double speedX;
    private final double speedY;

    public MouseMoveMessage(double speedX, double speedY) {
        this.speedX = speedX;
        this.speedY = speedY;
    }


    public static void encode(MouseMoveMessage message, FriendlyByteBuf byteBuf) {
        byteBuf.writeDouble(message.speedX);
        byteBuf.writeDouble(message.speedY);
    }

    public static MouseMoveMessage decode(FriendlyByteBuf byteBuf) {
        return new MouseMoveMessage(byteBuf.readDouble(), byteBuf.readDouble());
    }

    public static void handler(MouseMoveMessage message, ServerPlayer player) {
        if (player == null) {
            return;
        }

        var entity = player.getVehicle();

        if (entity instanceof VehicleEntity vehicle) {
            vehicle.mouseInput(message.speedX, message.speedY);
        }

        ItemStack stack = player.getMainHandItem();

        if (stack.is(ModItems.MONITOR.get()) && stack.getOrCreateTag().getBoolean("Using") && stack.getOrCreateTag().getBoolean("Linked")) {
            DroneEntity drone = EntityFindUtil.findDrone(player.level(), stack.getOrCreateTag().getString("LinkedDrone"));
            if (drone != null) {
                drone.mouseInput(message.speedX, message.speedY);
            }
        }
    }
}
