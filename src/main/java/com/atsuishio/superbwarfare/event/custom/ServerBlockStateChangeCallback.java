package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public interface ServerBlockStateChangeCallback {
    Event<ServerBlockStateChangeCallback> EVENT = EventFactory.createArrayBacked(
            ServerBlockStateChangeCallback.class,
            callbacks -> (level, pos) -> {
                for (var callback : callbacks) callback.onBlockStateChange(level, pos);
            }
    );

    void onBlockStateChange(ServerLevel level, BlockPos pos);
}
