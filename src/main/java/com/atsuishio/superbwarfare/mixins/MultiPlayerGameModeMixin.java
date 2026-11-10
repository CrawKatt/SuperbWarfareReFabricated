package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.atsuishio.superbwarfare.event.custom.ClientPlayerDestroyBlockCallback;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "destroyBlock(Lnet/minecraft/core/BlockPos;)Z", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Block;playerWillDestroy(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/entity/player/Player;)V"),
            cancellable = true)
    private void superbwarfare$beforeDestroyBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir,
                                                @Local Level level, @Local BlockState state) {
        if (!ClientPlayerDestroyBlockCallback.EVENT.invoker().onDestroyedByPlayer(
                state, level, pos, minecraft.player, false, level.getFluidState(pos))) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "isServerControlledInventory", at = @At("HEAD"), cancellable = true)
    public void isServerControlledInventory(CallbackInfoReturnable<Boolean> cir) {
        var player = this.minecraft.player;
        if (player == null) return;
        if (player.isPassenger() && player.getVehicle() instanceof VehicleEntity vehicle) {
            cir.setReturnValue(vehicle.hasMenu());
        }
    }
}
