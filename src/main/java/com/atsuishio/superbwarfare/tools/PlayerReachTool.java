package com.atsuishio.superbwarfare.tools;

import com.atsuishio.superbwarfare.init.ModAttributes;
import net.minecraft.world.entity.player.Player;

public final class PlayerReachTool {
    public static double getBlockReach(Player player) {
        double reach = 4.5D + player.getAttributeValue(ModAttributes.BLOCK_REACH);
        return reach == 0 ? 0 : reach + (player.isCreative() ? 0.5D : 0);
    }

    public static double getEntityReach(Player player) {
        double reach = 3.0D + player.getAttributeValue(ModAttributes.ENTITY_REACH);
        return reach == 0 ? 0 : reach + (player.isCreative() ? 3.0D : 0);
    }
}
