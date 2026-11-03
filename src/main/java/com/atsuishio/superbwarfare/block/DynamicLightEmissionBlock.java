package com.atsuishio.superbwarfare.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public interface DynamicLightEmissionBlock {
    int getLightEmission(BlockState state, BlockGetter world, BlockPos pos);

    static int getEmission(BlockState state, BlockGetter world, BlockPos pos, int fallback) {
        return state.getBlock() instanceof DynamicLightEmissionBlock block
                ? block.getLightEmission(state, world, pos) : fallback;
    }
}
