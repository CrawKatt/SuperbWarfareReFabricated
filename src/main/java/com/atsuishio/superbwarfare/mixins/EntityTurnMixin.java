package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.item.gun.GunItem;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Math;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.atsuishio.superbwarfare.event.ClientEventHandler.isProne;

@Mixin(Entity.class)
public class EntityTurnMixin {

    @Inject(method = "turn(DD)V", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$turnWhileProne(double yRot, double xRot, CallbackInfo ci) {
        var entity = (Entity) (Object) this;
        if (!(entity instanceof Player player)
                || !(player.getMainHandItem().getItem() instanceof GunItem)
                || !isProne(player)
                || player.isSwimming()) {
            return;
        }

        ci.cancel();
        float pitchDelta = (float) xRot * 0.15F;
        float yawDelta = (float) yRot * 0.15F;
        player.setXRot(player.getXRot() + pitchDelta);
        player.setYRot(player.getYRot() + yawDelta);

        Vec3 forward = new Vec3(player.getLookAngle().x, 0, player.getLookAngle().z).normalize();
        BlockPos inFront = BlockPos.containing(
                player.getX() + 0.25 * forward.x,
                player.getY() - 0.1,
                player.getZ() + 0.25 * forward.z
        );
        float maxPitch = player.level().getBlockState(inFront).canOcclude() ? 30F : 89F;
        player.setXRot(Mth.clamp(player.getXRot(), -45F, maxPitch));

        player.xRotO = Mth.clamp(player.xRotO + pitchDelta, -90F, 90F);
        player.yRotO += yawDelta;

        float bodyDelta = Math.clamp(-90f, 90f, Mth.wrapDegrees(player.getYHeadRot() - player.yBodyRot));
        player.setYBodyRot(player.yBodyRot + 0.5f * bodyDelta);

        if (player.getVehicle() != null) {
            player.getVehicle().onPassengerTurned(player);
        }
    }
}
