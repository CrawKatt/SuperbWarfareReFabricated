package com.atsuishio.superbwarfare.network.message.send;

import com.atsuishio.superbwarfare.menu.ReforgingTableMenu;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;

public enum GunReforgeMessage {
    INSTANCE;

    public static void handler(ServerPlayer player) {
        if (player == null) {
            return;
        }

        AbstractContainerMenu abstractcontainermenu = player.containerMenu;
        if (abstractcontainermenu instanceof ReforgingTableMenu menu) {
            if (!menu.stillValid(player)) {
                return;
            }
            menu.generateResult();
        }
    }
}
