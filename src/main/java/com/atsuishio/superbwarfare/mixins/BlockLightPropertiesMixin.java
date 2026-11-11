package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.event.custom.BlockLightEmissionCallback;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LightEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LightEngine.class)
public abstract class BlockLightPropertiesMixin {
    @WrapOperation(method = "hasDifferentLightProperties", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;getLightEmission()I"), require = 2)
    private static int superbwarfare$lightEmission(BlockState state, Operation<Integer> original,
                                                 BlockGetter level, BlockPos pos, BlockState before, BlockState after) {
        return BlockLightEmissionCallback.EVENT.invoker()
                .getLightEmission(state, level, pos, original.call(state));
    }
}
