package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.block.DynamicLightEmissionBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

@Mixin(ChunkAccess.class)
public abstract class ChunkLightSourcesMixin {
    @ModifyArg(method = "findBlockLightSources", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/chunk/ChunkAccess;findBlocks(Ljava/util/function/Predicate;Ljava/util/function/BiConsumer;)V"), index = 0)
    private Predicate<BlockState> superbwarfare$dynamicLightSources(Predicate<BlockState> original) {
        return state -> original.test(state) || state.getBlock() instanceof DynamicLightEmissionBlock;
    }

    @ModifyArg(method = "findBlockLightSources", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/chunk/ChunkAccess;findBlocks(Ljava/util/function/Predicate;Ljava/util/function/BiConsumer;)V"), index = 1)
    private BiConsumer<BlockPos, BlockState> superbwarfare$emittingLightSources(BiConsumer<BlockPos, BlockState> original) {
        return (pos, state) -> {
            if (!(state.getBlock() instanceof DynamicLightEmissionBlock)
                    || DynamicLightEmissionBlock.getEmission(state, (ChunkAccess) (Object) this, pos, 0) > 0) {
                original.accept(pos, state);
            }
        };
    }
}
