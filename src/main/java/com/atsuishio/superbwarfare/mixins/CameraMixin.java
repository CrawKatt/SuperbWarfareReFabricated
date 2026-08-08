package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.client.ICustomCamera;
import com.atsuishio.superbwarfare.entity.vehicle.DroneEntity;
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.event.ClientMouseHandler;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.item.gun.GunItem;
import com.atsuishio.superbwarfare.tools.EntityFindUtil;
import com.atsuishio.superbwarfare.tools.NBTTool;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import org.joml.*;
import org.joml.Math;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Camera.class)
public abstract class CameraMixin implements ICustomCamera {

    @Unique
    private boolean superbWarfare$anglesComputed;

    @Unique
    private float superbWarfare$computedYaw;

    @Unique
    private float superbWarfare$computedPitch;

    @Unique
    private float superbWarfare$partialTicks;

    @Shadow
    @Final
    private Quaternionf rotation;

    @Shadow
    @Deprecated
    protected abstract void setRotation(float yRot, float xRot);

    @Shadow
    protected abstract void setPosition(double x, double y, double z);

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

        superbWarfare$computeAngles(entity.getViewYRot(partialTicks), entity.getViewXRot(partialTicks));

        ItemStack stack = player.getMainHandItem();
        var tag = NBTTool.getTag(stack);

        if (stack.is(ModItems.MONITOR) && tag.getBoolean("Using") && tag.getBoolean("Linked")) {
            DroneEntity drone = EntityFindUtil.findDrone(player.level(), tag.getString("LinkedDrone"));
            if (drone != null) {
                boolean firstPerson = Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON || Minecraft.getInstance().options.getCameraType() == CameraType.THIRD_PERSON_BACK;
                if (firstPerson) {
                    Matrix4d transform = superbWarfare$getDroneTransform(drone, partialTicks);
                    double x0 = 0;
                    double y0 = 0.075;
                    double z0 = 0.18;

                    Vector4d worldPosition = superbWarfare$transformPosition(transform, x0, y0, z0);

                    setRotation(drone.getYaw(partialTicks), drone.getPitch(partialTicks));
                    setPosition(worldPosition.x, worldPosition.y, worldPosition.z);
                    info.cancel();
                } else {
                    var rotation = drone.getCameraRotation(partialTicks, player, false, false);
                    if (rotation != null) {
                        setRotation(rotation.x, rotation.y);
                    }
                    var position = drone.getCameraPosition(partialTicks, player, false, false);
                    if (position != null) {
                        setPosition(position.x, position.y, position.z);
                    }

                    if (rotation != null || position != null) {
                        info.cancel();
                    }
                }
            }
            return;
        }

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
    private static Matrix4d superbWarfare$getDroneTransform(DroneEntity vehicle, float ticks) {
        Matrix4d transform = new Matrix4d();
        transform.translate(Mth.lerp(ticks, vehicle.xo, vehicle.getX()), Mth.lerp(ticks, vehicle.yo, vehicle.getY()), Mth.lerp(ticks, vehicle.zo, vehicle.getZ()));
        transform.rotate(Axis.YP.rotationDegrees(-vehicle.getYaw(ticks)));
        transform.rotate(Axis.XP.rotationDegrees(vehicle.getBodyPitch(ticks)));
        transform.rotate(Axis.ZP.rotationDegrees(vehicle.getRoll(ticks)));
        return transform;
    }

    @Unique
    private static Vector4d superbWarfare$transformPosition(Matrix4d transform, double x, double y, double z) {
        return transform.transform(new Vector4d(x, y, z, 1));
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

    @Inject(method = "setup", at = @At("TAIL"))
    public void superbWarfare$setup(BlockGetter area, Entity entity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        if (Minecraft.getInstance().options.getCameraType() == CameraType.THIRD_PERSON_BACK
                && entity instanceof Player player
                && player.getMainHandItem().getItem() instanceof GunItem
                && Math.max(ClientEventHandler.bowPullPos, ClientEventHandler.zoomPos) > 0
        ) {
            move(-getMaxZoom((float) (-2.9 * Math.max(ClientEventHandler.bowPullPos, ClientEventHandler.zoomPos))), 0F, (float) (-ClientEventHandler.cameraLocation * Math.max(ClientEventHandler.bowPullPos, ClientEventHandler.zoomPos)));
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

    @Override
    public Quaternionf superbwarfare$getRotation() {
        return this.rotation;
    }
}
