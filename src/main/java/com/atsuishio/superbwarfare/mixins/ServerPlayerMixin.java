package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.event.custom.PlayerMenuOpenedCallback;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.OptionalInt;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
    @Inject(method = "openMenu", at = @At("RETURN"))
    private void superbwarfare$afterOpenMenu(MenuProvider provider, CallbackInfoReturnable<OptionalInt> cir) {
        if (cir.getReturnValue().isPresent()) {
            ServerPlayer player = (ServerPlayer) (Object) this;
            PlayerMenuOpenedCallback.EVENT.invoker().onOpened(player, player.containerMenu);
        }
    }
}
