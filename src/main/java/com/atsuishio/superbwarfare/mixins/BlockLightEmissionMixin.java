package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.event.custom.BlockLightEmissionCallback;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LightChunk;
import net.minecraft.world.level.chunk.LightChunkGetter;
import net.minecraft.world.level.lighting.BlockLightEngine;
import net.minecraft.world.level.lighting.LayerLightSectionStorage;
import net.minecraft.world.level.lighting.LightEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockLightEngine.class)
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class BlockLightEmissionMixin extends LightEngine {
    protected BlockLightEmissionMixin(LightChunkGetter chunks, LayerLightSectionStorage storage) {
        super(chunks, storage);
    }

    @WrapOperation(method = "getEmission", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;getLightEmission()I"))
    private int superbwarfare$lightEmission(BlockState state, Operation<Integer> original,
                                          long packedPos, BlockState queriedState) {
        return superbwarfare$emissionAt(state, BlockPos.of(packedPos), original.call(state));
    }

    @WrapOperation(method = "method_51532", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;getLightEmission()I"))
    private int superbwarfare$initialLightEmission(BlockState state, Operation<Integer> original,
                                                 BlockPos pos, BlockState queriedState) {
        return superbwarfare$emissionAt(state, pos, original.call(state));
    }

    @Unique
    private int superbwarfare$emissionAt(BlockState state, BlockPos pos, int emission) {
        LightChunk chunk = getChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()));
        return chunk == null ? emission : BlockLightEmissionCallback.EVENT.invoker()
                .getLightEmission(state, chunk, pos, emission);
    }
}
