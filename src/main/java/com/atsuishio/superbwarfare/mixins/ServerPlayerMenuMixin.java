package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.inventory.menu.EnergyMenu;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerMenuMixin {

    @Inject(method = "initMenu", at = @At("TAIL"))
    private void superbwarfare$onOpenEnergyMenu(AbstractContainerMenu containerMenu, CallbackInfo ci) {
        var player = (ServerPlayer) (Object) this;
        if (containerMenu instanceof EnergyMenu menu) {
            menu.onOpened(player);
        }
    }
}