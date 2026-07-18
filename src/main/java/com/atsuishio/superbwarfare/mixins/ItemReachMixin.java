package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.tools.PlayerReachTool;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemReachMixin {
    @Inject(method = "getPlayerPOVHitResult", at = @At("HEAD"), cancellable = true)
    private static void superbwarfare$usePlayerBlockReach(Level level, Player player, ClipContext.Fluid fluidMode, CallbackInfoReturnable<BlockHitResult> cir) {
        float xRot = player.getXRot();
        float yRot = player.getYRot();
        Vec3 eyePosition = player.getEyePosition();
        float yawCos = Mth.cos(-yRot * ((float) Math.PI / 180F) - (float) Math.PI);
        float yawSin = Mth.sin(-yRot * ((float) Math.PI / 180F) - (float) Math.PI);
        float pitchCos = -Mth.cos(-xRot * ((float) Math.PI / 180F));
        float pitchSin = Mth.sin(-xRot * ((float) Math.PI / 180F));
        float xDirection = yawSin * pitchCos;
        float zDirection = yawCos * pitchCos;
        double reach = PlayerReachTool.getBlockReach(player);
        Vec3 end = eyePosition.add(xDirection * reach, pitchSin * reach, zDirection * reach);
        cir.setReturnValue(level.clip(new ClipContext(eyePosition, end, ClipContext.Block.OUTLINE, fluidMode, player)));
    }
}
