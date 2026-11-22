package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.event.custom.ServerBlockStateChangeCallback;
import com.atsuishio.superbwarfare.event.custom.ServerEntityTickCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {
    @Inject(method = "onBlockStateChange", at = @At("TAIL"))
    private void superbwarfare$onBlockStateChange(BlockPos pos, BlockState oldState, BlockState newState, CallbackInfo ci) {
        if (oldState != newState) {
            ServerBlockStateChangeCallback.EVENT.invoker().onBlockStateChange((ServerLevel) (Object) this, pos);
        }
    }

    @Inject(method = "tickNonPassenger", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;tick()V", shift = At.Shift.AFTER))
    private void superbwarfare$postEntityTick(Entity entity, CallbackInfo ci) {
        ServerEntityTickCallback.EVENT.invoker().onPostTick(new ServerEntityTickCallback.Event(entity));
    }

    @Inject(method = "tickPassenger", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;rideTick()V", shift = At.Shift.AFTER))
    private void superbwarfare$postPassengerTick(Entity vehicle, Entity passenger, CallbackInfo ci) {
        ServerEntityTickCallback.EVENT.invoker().onPostTick(new ServerEntityTickCallback.Event(passenger));
    }
}
