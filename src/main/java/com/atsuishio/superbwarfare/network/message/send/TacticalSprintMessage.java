package com.atsuishio.superbwarfare.network.message.send;

import com.atsuishio.superbwarfare.capability.player.PlayerVariable;
import com.atsuishio.superbwarfare.config.server.MiscConfig;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;


public record TacticalSprintMessage(boolean sprint) {

    public static void encode(TacticalSprintMessage message, FriendlyByteBuf buffer) {
        buffer.writeBoolean(message.sprint);
    }

    public static TacticalSprintMessage decode(FriendlyByteBuf buffer) {
        return new TacticalSprintMessage(buffer.readBoolean());
    }

    public static void handler(TacticalSprintMessage message, ServerPlayer player) {
        if (player == null) return;
        PlayerVariable.modify(player, capability -> capability.tacticalSprint = MiscConfig.ALLOW_TACTICAL_SPRINT.get() && message.sprint);
    }
}
