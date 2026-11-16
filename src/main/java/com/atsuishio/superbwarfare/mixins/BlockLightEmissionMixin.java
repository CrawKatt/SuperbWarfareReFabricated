package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.block.DynamicLightEmissionBlock;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LightChunk;
import net.minecraft.world.level.lighting.BlockLightEngine;
import net.minecraft.world.level.lighting.LightEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockLightEngine.class)
public abstract class BlockLightEmissionMixin {
    @ModifyExpressionValue(method = "getEmission", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;getLightEmission()I"))
    private int superbwarfare$dynamicEmission(int original, long packedPos, BlockState state) {
        return superbwarfare$emission(original, BlockPos.of(packedPos), state);
    }

    @ModifyExpressionValue(method = "method_51532", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;getLightEmission()I"))
    private int superbwarfare$initialEmission(int original, BlockPos pos, BlockState state) {
        return superbwarfare$emission(original, pos, state);
    }

    private int superbwarfare$emission(int original, BlockPos pos, BlockState state) {
        if (!(state.getBlock() instanceof DynamicLightEmissionBlock)) return original;
        // Read the loaded chunk directly: lighting can run outside the world's main thread.
        LightChunk chunk = ((LightEngine<?, ?>) (Object) this).chunkSource.getChunkForLighting(pos.getX() >> 4, pos.getZ() >> 4);
        return chunk == null ? original : DynamicLightEmissionBlock.getEmission(state, chunk, pos, original);
    }
}
