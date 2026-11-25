package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.entity.vehicle.remote.RemoteControl;
import com.atsuishio.superbwarfare.event.custom.PlayerMenuOpenedCallback;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.RelativeMovement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.OptionalInt;
import java.util.Set;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
    @Inject(method = "changeDimension", at = @At("HEAD"), cancellable = true)
    private void sbw$checkRemoteTravel(ServerLevel destination, CallbackInfoReturnable<Entity> cir) {
        if (!RemoteControl.canTravel((ServerPlayer) (Object) this)) cir.setReturnValue(null);
    }

    @Inject(method = "teleportTo(Lnet/minecraft/server/level/ServerLevel;DDDFF)V", at = @At("HEAD"), cancellable = true)
    private void sbw$checkRemoteTeleport(ServerLevel destination, double x, double y, double z, float yaw, float pitch, CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        if (destination != player.level() && !RemoteControl.canTravel(player)) ci.cancel();
    }

    @Inject(method = "teleportTo(Lnet/minecraft/server/level/ServerLevel;DDDLjava/util/Set;FF)Z", at = @At("HEAD"), cancellable = true)
    private void sbw$checkRemoteTeleport(ServerLevel destination, double x, double y, double z, Set<RelativeMovement> relative,
                                         float yaw, float pitch, CallbackInfoReturnable<Boolean> cir) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        if (destination != player.level() && !RemoteControl.canTravel(player)) cir.setReturnValue(false);
    }

    @Inject(method = "openMenu", at = @At("RETURN"))
    private void superbwarfare$afterOpenMenu(MenuProvider provider, CallbackInfoReturnable<OptionalInt> cir) {
        if (cir.getReturnValue().isPresent()) {
            ServerPlayer player = (ServerPlayer) (Object) this;
            PlayerMenuOpenedCallback.EVENT.invoker().onOpened(player, player.containerMenu);
        }
    }
}
