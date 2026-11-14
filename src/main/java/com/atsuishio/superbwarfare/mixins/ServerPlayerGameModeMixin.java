package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.event.custom.LeftClickBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {
    @Shadow @Final protected ServerPlayer player;

    @Inject(method = "handleBlockBreakAction", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$onLeftClickBlock(BlockPos pos, ServerboundPlayerActionPacket.Action action,
                                               Direction face, int maxBuildHeight, int sequence, CallbackInfo ci) {
        var event = new LeftClickBlockCallback.Event(player, pos, face, action);
        LeftClickBlockCallback.EVENT.invoker().onLeftClick(event);
        if (event.isCanceled()) ci.cancel();
    }
}
