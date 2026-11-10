package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface RedstoneConnectionCallback {
    Event<RedstoneConnectionCallback> EVENT = EventFactory.createArrayBacked(
            RedstoneConnectionCallback.class,
            callbacks -> (state, level, pos, side) -> {
                for (RedstoneConnectionCallback callback : callbacks) {
                    Boolean result = callback.canConnectRedstone(state, level, pos, side);
                    if (result != null) {
                        return result;
                    }
                }
                return null;
            }
    );

    @Nullable
    Boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction side);
}
