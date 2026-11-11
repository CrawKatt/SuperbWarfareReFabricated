package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.event.custom.BlockCaughtFireCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TntBlock.class)
public class TntBlockMixin {
    @Inject(method = "explode(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/LivingEntity;)V",
            at = @At("HEAD"), cancellable = true)
    private static void superbwarfare$onCaughtFire(Level level, BlockPos pos, LivingEntity igniter, CallbackInfo ci) {
        BlockState state = level.getBlockState(pos);
        if (state.getBlock() instanceof TntBlock &&
                BlockCaughtFireCallback.EVENT.invoker().onCaughtFire(state, level, pos, null, igniter)) {
            ci.cancel();
        }
    }
}
