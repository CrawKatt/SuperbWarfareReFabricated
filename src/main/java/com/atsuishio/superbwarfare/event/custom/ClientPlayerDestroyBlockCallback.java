package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public interface ClientPlayerDestroyBlockCallback {
    Event<ClientPlayerDestroyBlockCallback> EVENT = EventFactory.createArrayBacked(
            ClientPlayerDestroyBlockCallback.class,
            callbacks -> (state, level, pos, player, willHarvest, fluid) -> {
                for (ClientPlayerDestroyBlockCallback callback : callbacks) {
                    if (!callback.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid)) {
                        return false;
                    }
                }
                return true;
            }
    );

    boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player,
                                boolean willHarvest, FluidState fluid);
}
