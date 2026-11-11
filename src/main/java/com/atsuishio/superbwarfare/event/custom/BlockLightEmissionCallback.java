package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public interface BlockLightEmissionCallback {
    Event<BlockLightEmissionCallback> EVENT = EventFactory.createArrayBacked(
            BlockLightEmissionCallback.class,
            callbacks -> (state, level, pos, emission) -> {
                for (BlockLightEmissionCallback callback : callbacks) {
                    emission = callback.getLightEmission(state, level, pos, emission);
                }
                return emission;
            }
    );

    int getLightEmission(BlockState state, BlockGetter level, BlockPos pos, int emission);
}
