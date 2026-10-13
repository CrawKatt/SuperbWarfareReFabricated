package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.item.gun.GunItem;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.atsuishio.superbwarfare.event.ClientEventHandler.zoomTime;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow(aliases = "Lnet/minecraft/client/Camera;setRotation(FF)V")
    protected abstract void setRotation(float p_90573_, float p_90574_);

    @Shadow(aliases = "Lnet/minecraft/client/Camera;setPosition(DDD)V")
    protected abstract void setPosition(double p_90585_, double p_90586_, double p_90587_);

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setRotation(FF)V", ordinal = 0),
            method = "setup(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/world/entity/Entity;ZZF)V",
            cancellable = true)
    private void onSetup(BlockGetter level, Entity entity, boolean detached, boolean mirrored, float partialTicks, CallbackInfo info) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player != null) {
            if (player.getVehicle() instanceof VehicleEntity vehicle) {
                var rotation = vehicle.getCameraRotation(partialTicks, player, ClientEventHandler.zoomVehicle, Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON);
                if (rotation != null) {
                    setRotation(rotation.x, rotation.y);
                }
                var position = vehicle.getCameraPosition(partialTicks, player, ClientEventHandler.zoomVehicle, Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON);
                if (position != null) {
                    setPosition(position.x, position.y, position.z);
                }

                if (rotation != null || position != null) {
                    info.cancel();
                }

            }
        }
    }

    @Inject(method = "setup", at = @At("TAIL"))
    public void superbWarfare$setup(BlockGetter area, Entity entity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        if (Minecraft.getInstance().options.getCameraType() == CameraType.THIRD_PERSON_BACK
                && entity instanceof Player player
                && GunItem.isHeldWeapon(player.getMainHandItem())
                && zoomTime > 0
        ) {
            double zoom = ClientEventHandler.aimingProgress(zoomTime);
            move(-getMaxZoom(-2.9 * zoom), 0, -ClientEventHandler.cameraLocation * zoom);
            return;
        }

        if (!thirdPerson || !(entity.getVehicle() instanceof VehicleEntity vehicle)) return;

        var cameraPosition = vehicle.getThirdPersonCameraPosition();
        move(-getMaxZoom(cameraPosition.x()), cameraPosition.y(), cameraPosition.z());
    }

    @Shadow
    protected abstract void move(double x, double y, double z);

    @Shadow
    protected abstract double getMaxZoom(double desiredCameraDistance);
}