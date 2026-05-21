package com.atsuishio.superbwarfare.network.message.send;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.capability.player.PlayerVariable;
import com.atsuishio.superbwarfare.config.server.MiscConfig;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.jetbrains.annotations.NotNull;

public record TacticalSprintMessage(boolean sprint) implements CustomPacketPayload {
    public static final Type<TacticalSprintMessage> TYPE = new Type<>(Mod.loc("tactical_sprint"));

    public static final StreamCodec<ByteBuf, TacticalSprintMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            TacticalSprintMessage::sprint,
            TacticalSprintMessage::new
    );

    public static void handler(TacticalSprintMessage message, final ServerPlayNetworking.Context context) {
        var player = context.player();

        var cap = PlayerVariable.getOrDefault(player).watch();
        cap.tacticalSprint = MiscConfig.ALLOW_TACTICAL_SPRINT.get() && message.sprint;
        cap.sync(player);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
