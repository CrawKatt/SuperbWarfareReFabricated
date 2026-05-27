package com.atsuishio.superbwarfare.network.message.send;

import com.atsuishio.superbwarfare.menu.FuMO25Menu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class RadarChangeModeMessage {

    private final byte mode;

    public RadarChangeModeMessage(byte mode) {
        this.mode = mode;
    }

    public static void encode(RadarChangeModeMessage message, FriendlyByteBuf buffer) {
        buffer.writeByte(message.mode);
    }

    public static RadarChangeModeMessage decode(FriendlyByteBuf buffer) {
        return new RadarChangeModeMessage(buffer.readByte());
    }

    public static void handler(RadarChangeModeMessage message, ServerPlayer player) {
        byte mode = message.mode;
        if (mode < 1 || mode > 4) return;

        if (player == null) return;

        AbstractContainerMenu menu = player.containerMenu;
        if (menu instanceof FuMO25Menu fuMO25Menu) {
            if (!player.containerMenu.stillValid(player)) {
                return;
            }
            fuMO25Menu.setFuncTypeAndTime(mode);
        }
    }
}
