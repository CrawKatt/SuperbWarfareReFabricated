package com.atsuishio.superbwarfare.network.message.receive;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.menu.EnergyMenu;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Code based on @GoryMoon's Chargers
 */
public record ContainerDataMessage(int containerId, List<Pair> data) implements CustomPacketPayload {
    public static final Type<ContainerDataMessage> TYPE = new Type<>(Mod.loc("container_data"));

    public static final StreamCodec<ByteBuf, ContainerDataMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ContainerDataMessage::containerId,
            StreamCodec.composite(
                    ByteBufCodecs.INT, Pair::id,
                    ByteBufCodecs.VAR_LONG, Pair::data,
                    Pair::new
            ).apply(ByteBufCodecs.list()),
            ContainerDataMessage::data,
            ContainerDataMessage::new
    );


    public static void handler(ContainerDataMessage message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null
                && mc.player.containerMenu.containerId == message.containerId
                && mc.player.containerMenu instanceof EnergyMenu energyMenu) {
            message.data.forEach(p -> energyMenu.setData(p.id, p.data));
        }
    }

    @Override
    @NotNull
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public record Pair(int id, long data) {
    }

}
