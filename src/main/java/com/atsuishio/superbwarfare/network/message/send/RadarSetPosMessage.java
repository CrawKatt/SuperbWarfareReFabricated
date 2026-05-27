package com.atsuishio.superbwarfare.network.message.send;

import com.atsuishio.superbwarfare.menu.FuMO25Menu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class RadarSetPosMessage {

    private final BlockPos pos;

    public RadarSetPosMessage(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(RadarSetPosMessage message, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(message.pos);
    }

    public static RadarSetPosMessage decode(FriendlyByteBuf buffer) {
        return new RadarSetPosMessage(buffer.readBlockPos());
    }

    public static void handler(RadarSetPosMessage message, ServerPlayer player) {
        if (player == null) return;

        AbstractContainerMenu menu = player.containerMenu;
        if (menu instanceof FuMO25Menu fuMO25Menu) {
            if (!player.containerMenu.stillValid(player)) {
                return;
            }
            fuMO25Menu.setPos(message.pos.getX(), message.pos.getY(), message.pos.getZ());
        }
    }
}
