package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.event.custom.RedstoneConnectionCallback;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RedStoneWireBlock.class)
public abstract class RedStoneWireBlockMixin {
    @WrapOperation(
            method = "getConnectingSide(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Z)Lnet/minecraft/world/level/block/state/properties/RedstoneSide;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/RedStoneWireBlock;shouldConnectTo(Lnet/minecraft/world/level/block/state/BlockState;)Z", ordinal = 0)
    )
    private boolean superbwarfare$connectAbove(BlockState state, Operation<Boolean> original,
                                             BlockGetter level, BlockPos pos, Direction direction, boolean canGoUp) {
        Boolean result = RedstoneConnectionCallback.EVENT.invoker()
                .canConnectRedstone(state, level, pos.relative(direction).above(), null);
        return result != null ? result : original.call(state);
    }

    @WrapOperation(
            method = "getConnectingSide(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Z)Lnet/minecraft/world/level/block/state/properties/RedstoneSide;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/RedStoneWireBlock;shouldConnectTo(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z")
    )
    private boolean superbwarfare$connectBeside(BlockState state, Direction side, Operation<Boolean> original,
                                              BlockGetter level, BlockPos pos, Direction direction, boolean canGoUp) {
        Boolean result = RedstoneConnectionCallback.EVENT.invoker()
                .canConnectRedstone(state, level, pos.relative(direction), side);
        return result != null ? result : original.call(state, side);
    }

    @WrapOperation(
            method = "getConnectingSide(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Z)Lnet/minecraft/world/level/block/state/properties/RedstoneSide;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/RedStoneWireBlock;shouldConnectTo(Lnet/minecraft/world/level/block/state/BlockState;)Z", ordinal = 1)
    )
    private boolean superbwarfare$connectBelow(BlockState state, Operation<Boolean> original,
                                             BlockGetter level, BlockPos pos, Direction direction, boolean canGoUp) {
        Boolean result = RedstoneConnectionCallback.EVENT.invoker()
                .canConnectRedstone(state, level, pos.relative(direction).below(), null);
        return result != null ? result : original.call(state);
    }
}
