package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.event.custom.BlockLightEmissionCallback;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockGetter.class)
public interface BlockGetterMixin {
    @WrapOperation(method = "getLightEmission", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;getLightEmission()I"))
    private int superbwarfare$lightEmission(BlockState state, Operation<Integer> original, BlockPos pos) {
        return BlockLightEmissionCallback.EVENT.invoker()
                .getLightEmission(state, (BlockGetter) this, pos, original.call(state));
    }
}
