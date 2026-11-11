package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.event.custom.BlockLightEmissionCallback;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

@Mixin(ChunkAccess.class)
public abstract class ChunkLightSourcesMixin {
    @WrapOperation(method = "findBlockLightSources", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/chunk/ChunkAccess;findBlocks(Ljava/util/function/Predicate;Ljava/util/function/BiConsumer;)V"))
    private void superbwarfare$lightSources(ChunkAccess chunk, Predicate<BlockState> predicate,
                                          BiConsumer<BlockPos, BlockState> consumer, Operation<Void> original) {
        original.call(chunk, predicate.or(BlockState::hasBlockEntity), (BiConsumer<BlockPos, BlockState>) (pos, state) -> {
            int emission = BlockLightEmissionCallback.EVENT.invoker()
                    .getLightEmission(state, chunk, pos, state.getLightEmission());
            if (emission > 0) consumer.accept(pos, state);
        });
    }
}
