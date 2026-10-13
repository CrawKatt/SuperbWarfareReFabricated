package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.event.ClientMouseHandler;
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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import static com.atsuishio.superbwarfare.event.ClientEventHandler.zoomTime;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Unique
    private boolean superbWarfare$anglesComputed;

    @Unique
    private float superbWarfare$computedYaw;

    @Unique
    private float superbWarfare$computedPitch;

    @Unique
    private float superbWarfare$partialTicks;

    @Shadow
    @Deprecated
    protected abstract void setRotation(float yRot, float xRot);

    @Shadow
    protected abstract void setPosition(double x, double y, double z);

    @Shadow
    private float xRot;

    @Shadow
    private float yRot;

    @Inject(method = "setup", at = @At("HEAD"))
    private void superbWarfare$capturePartialTicks(BlockGetter level, Entity entity, boolean detached,
                                                    boolean thirdPersonReverse, float partialTicks, CallbackInfo ci) {
        superbWarfare$anglesComputed = false;
        superbWarfare$partialTicks = partialTicks;
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setRotation(FF)V", ordinal = 0),
            method = "setup",
            cancellable = true)
    private void onSetup(BlockGetter level, Entity entity, boolean detached, boolean thirdPersonReverse, float partialTicks, CallbackInfo info) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;

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
                superbWarfare$applyComputedAnglesToCustomCamera();
                info.cancel();
            }

        }
    }

    @ModifyArgs(
            method = "setup",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Camera;setRotation(FF)V",
                    ordinal = 0
            )
    )
    private void superbWarfare$applyComputedAngles(Args args) {
        superbWarfare$computeAngles(args.get(0), args.get(1));
        args.set(0, superbWarfare$computedYaw);
        args.set(1, superbWarfare$computedPitch);
    }

    @Unique
    private void superbWarfare$computeAngles(float yaw, float pitch) {
        if (superbWarfare$anglesComputed) return;
        superbWarfare$anglesComputed = true;

        Camera camera = (Camera) (Object) this;
        ClientMouseHandler.handleClientTick(camera, superbWarfare$partialTicks);

        ClientEventHandler.CameraAnglesContext context = new ClientEventHandler.CameraAnglesContext(
                camera,
                superbWarfare$partialTicks,
                yaw,
                pitch,
                0f
        );

        ClientEventHandler.computeCameraAngles(context);
        superbWarfare$computedYaw = context.getYaw();
        superbWarfare$computedPitch = context.getPitch();
    }

    @Unique
    private void superbWarfare$applyComputedAnglesToCustomCamera() {
        superbWarfare$computeAngles(yRot, xRot);
        setRotation(superbWarfare$computedYaw, superbWarfare$computedPitch);
    }

    @Inject(method = "setup", at = @At("TAIL"))
    public void superbWarfare$setup(BlockGetter area, Entity entity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        if (Minecraft.getInstance().options.getCameraType() == CameraType.THIRD_PERSON_BACK
                && entity instanceof Player player
                && GunItem.isHeldWeapon(player.getMainHandItem())
                && zoomTime > 0
        ) {
            double zoom = ClientEventHandler.aimingProgress(zoomTime);
            move(-getMaxZoom((float) (-2.9 * zoom)), 0f, (float) (-ClientEventHandler.cameraLocation * zoom));
            return;
        }

        if (!thirdPerson || !(entity.getVehicle() instanceof VehicleEntity vehicle)) return;

        var cameraPosition = vehicle.getThirdPersonCameraPosition();
        move(-getMaxZoom((float) cameraPosition.x()), (float) cameraPosition.y(), (float) cameraPosition.z());
    }

    @Shadow
    protected abstract void move(float x, float y, float z);

    @Shadow
    protected abstract float getMaxZoom(float maxZoom);
}
