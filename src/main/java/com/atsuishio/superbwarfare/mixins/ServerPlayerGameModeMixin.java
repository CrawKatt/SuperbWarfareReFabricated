package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.tools.PlayerReachTool;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ServerPlayerGameMode.class, priority = 900)
public class ServerPlayerGameModeMixin {

    @Shadow
    @Final
    protected ServerPlayer player;

    @Redirect(
            method = "handleBlockBreakAction",
            require = 0,
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;MAX_INTERACTION_DISTANCE:D",
                    opcode = Opcodes.GETSTATIC
            )
    )
    private double superbwarfare$useBlockBreakReach() {
        return PlayerReachTool.getBlockInteractionDistanceSqr(this.player);
    }
}
