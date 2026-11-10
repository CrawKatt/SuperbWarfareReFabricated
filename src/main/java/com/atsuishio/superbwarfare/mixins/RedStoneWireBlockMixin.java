package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.block.BoobyTrapBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RedStoneWireBlock.class)
public class RedStoneWireBlockMixin {

    @Inject(
            method = "shouldConnectTo(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void superbwarfare$connectToBoobyTrap(BlockState state, @Nullable Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (state.getBlock() instanceof BoobyTrapBlock && direction != null) {
            cir.setReturnValue(true);
        }
    }
}
