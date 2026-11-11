package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.event.custom.BlockCaughtFireCallback;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Run before other fire hooks so a handled ignition cannot prime twice.
@Mixin(value = FireBlock.class, priority = 1100)
public class FireBlockMixin {
    @Inject(method = "checkBurnOut(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;ILnet/minecraft/util/RandomSource;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I", ordinal = 1))
    private void superbwarfare$onCaughtFire(Level level, BlockPos pos, int chance, RandomSource random, int age,
                                          CallbackInfo ci, @Local BlockState state,
                                          @Share("handledIgnition") LocalBooleanRef handled) {
        handled.set(BlockCaughtFireCallback.EVENT.invoker().onCaughtFire(state, level, pos, null, null));
    }

    @Inject(method = "checkBurnOut(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;ILnet/minecraft/util/RandomSource;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getBlock()Lnet/minecraft/world/level/block/Block;"),
            cancellable = true)
    private void superbwarfare$skipHandledIgnition(Level level, BlockPos pos, int chance, RandomSource random, int age,
                                                CallbackInfo ci, @Share("handledIgnition") LocalBooleanRef handled) {
        if (handled.get()) {
            ci.cancel();
        }
    }
}
