package com.atsuishio.superbwarfare.event;

import com.atsuishio.superbwarfare.tools.HitboxHelper;
import net.minecraft.world.entity.player.Player;

public class HitboxHelperEventHandler {
    public static void onPlayerTick(Player player) {
        if (!player.level().isClientSide()) {
            HitboxHelper.onPlayerTick(player);
        }
    }

    public static void onPlayerLoggedOut(Player player) {
        HitboxHelper.onPlayerLoggedOut(player);
    }
}
