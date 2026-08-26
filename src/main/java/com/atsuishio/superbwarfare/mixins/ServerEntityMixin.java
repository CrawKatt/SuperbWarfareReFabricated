package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.event.custom.EntityPairingCallback;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerEntity.class)
public class ServerEntityMixin {
    @Shadow
    @Final
    private Entity entity;

    @Inject(method = "addPairing", at = @At("TAIL"))
    private void superbwarfare$afterAddPairing(ServerPlayer player, CallbackInfo ci) {
        EntityPairingCallback.EVENT.invoker().onPairing(this.entity, player);
    }
}
