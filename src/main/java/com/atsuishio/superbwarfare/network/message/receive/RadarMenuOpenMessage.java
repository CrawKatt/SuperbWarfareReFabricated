package com.atsuishio.superbwarfare.network.message.receive;

import com.atsuishio.superbwarfare.network.ClientPacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public class RadarMenuOpenMessage {

    public BlockPos pos;

    public RadarMenuOpenMessage(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(RadarMenuOpenMessage message, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(message.pos);
    }

    public static RadarMenuOpenMessage decode(FriendlyByteBuf buffer) {
        return new RadarMenuOpenMessage(buffer.readBlockPos());
    }

    public static void handler(RadarMenuOpenMessage message) {
        ClientPacketHandler.handleRadarMenuOpen(message);
    }
}
