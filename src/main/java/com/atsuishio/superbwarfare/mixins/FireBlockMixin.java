package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.block.HighEnergyExplosivesBlock;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FireBlock.class)
public class FireBlockMixin {

    @WrapOperation(
            method = "checkBurnOut",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/TntBlock;explode(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V")
    )
    private void superbwarfare$primeHighEnergyExplosives(Level level, BlockPos pos, Operation<Void> original, @Local BlockState state) {
        if (state.getBlock() instanceof HighEnergyExplosivesBlock block) {
            block.onCaughtFire(state, level, pos, null, null);
        } else {
            original.call(level, pos);
        }
    }
}
