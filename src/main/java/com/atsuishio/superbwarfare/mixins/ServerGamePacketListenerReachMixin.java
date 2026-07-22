package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.tools.PlayerReachTool;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ServerGamePacketListenerImpl.class, priority = 900)
public class ServerGamePacketListenerReachMixin {

    @Shadow
    public ServerPlayer player;

    @Redirect(
            method = "handleUseItemOn",
            require = 0,
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;MAX_INTERACTION_DISTANCE:D",
                    opcode = Opcodes.GETSTATIC
            )
    )
    private double superbwarfare$useBlockInteractionReach() {
        return PlayerReachTool.getBlockInteractionDistanceSqr(this.player);
    }

    @ModifyConstant(
            method = "handleUseItemOn",
            require = 0,
            constant = @Constant(doubleValue = 64.0D)
    )
    private double superbwarfare$removeRedundantVanillaDistanceCheck(double original) {
        return Double.POSITIVE_INFINITY;
    }
}
