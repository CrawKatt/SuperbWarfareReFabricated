package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.atsuishio.superbwarfare.event.ClickEventHandler;
import com.atsuishio.superbwarfare.event.ClientMouseHandler;
import com.atsuishio.superbwarfare.event.custom.MouseButtonCallback;
import com.atsuishio.superbwarfare.event.custom.MouseScrollCallback;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.tools.NBTTool;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.objectweb.asm.Opcodes;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

/**
 * Author: MrCrayfish
 */
@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

    @Unique
    private static double sbw$x;
    @Unique
    private static double sbw$y;

    @ModifyVariable(method = "turnPlayer()V", at = @At(value = "STORE", opcode = Opcodes.DSTORE), ordinal = 2)
    private double superbwarfare$sensitivity(double original) {
        return ClientMouseHandler.changeSensitivity(original);
    }

    @ModifyVariable(method = "turnPlayer()V", at = @At(value = "STORE", opcode = Opcodes.ISTORE))
    private int superbwarfare$invertY(int value) {
        return value * ClientMouseHandler.invertY();
    }

    @ModifyVariable(method = "turnPlayer()V", at = @At(value = "STORE", opcode = Opcodes.DSTORE), ordinal = 5)
    private double superbwarfare$rollX(double value) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null || minecraft.options.getCameraType() != CameraType.FIRST_PERSON) return value;

        if (player.getVehicle() instanceof VehicleEntity vehicle) {
            sbw$x = value;
            double direction = vehicle.getRoll() < 0 ? 1 : vehicle.getRoll() > 0 ? -1 : 0;
            if (Mth.abs(vehicle.getRoll()) > 90) {
                direction *= 1 - (Mth.abs(vehicle.getRoll()) - 90) / 90;
            }
            return (1 - Mth.abs(vehicle.getRoll()) / 90) * value
                    + (Mth.abs(vehicle.getRoll()) / 90) * sbw$y * direction;
        }
        return value;
    }

    @ModifyVariable(method = "turnPlayer()V", at = @At(value = "STORE", opcode = Opcodes.DSTORE), ordinal = 6)
    private double superbwarfare$rollY(double value) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null || minecraft.options.getCameraType() != CameraType.FIRST_PERSON) return value;

        if (player.getVehicle() instanceof VehicleEntity vehicle) {
            sbw$y = value;
            return (1 - Mth.abs(vehicle.getRoll()) / 90) * value
                    + (Mth.abs(vehicle.getRoll()) / 90) * sbw$x * (vehicle.getRoll() < 0 ? -1 : 1);
        }
        return value;
    }

    @ModifyArgs(
            method = "turnPlayer()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V")
    )
    private void superbwarfare$blockPlayerTurnWhileControllingDrone(Args args) {
        if (superbwarfare$isControllingDrone()) {
            args.set(0, 0.0D);
            args.set(1, 0.0D);
        }
    }

    @Inject(method = "onPress(JIII)V", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$onMouseButton(long window, int button, int action, int modifiers, CallbackInfo ci) {
        MouseButtonCallback.Event event = new MouseButtonCallback.Event(window, button, action, modifiers);
        MouseButtonCallback.EVENT.invoker().interact(event);
        if (event.isCanceled()) ci.cancel();
    }

    @Inject(
            method = "onPress(JIII)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/KeyMapping;click(Lcom/mojang/blaze3d/platform/InputConstants$Key;)V"
            ),
            cancellable = true
    )
    private void superbwarfare$onMousePressed(long window, int button, int action, int modifiers, CallbackInfo ci) {
        if (ClickEventHandler.onButtonPressed(button, action, modifiers)) {
            ClickEventHandler.releaseVanillaMouseButton(button);
            ci.cancel();
            return;
        }
        ClickEventHandler.forwardVanillaMouseButtonIfNeeded(button, action);
    }

    @Inject(method = "onPress(JIII)V", at = @At("TAIL"))
    private void superbwarfare$onMouseReleased(long window, int button, int action, int modifiers, CallbackInfo ci) {
        if (action == GLFW.GLFW_RELEASE) {
            ClickEventHandler.onButtonReleased(button, action, modifiers);
            ClickEventHandler.forwardVanillaMouseButtonIfNeeded(button, action);
        }
    }

    @Inject(method = "onScroll(JDD)V", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$onMouseScrolled(long window, double horizontal, double vertical, CallbackInfo ci) {
        MouseScrollCallback.Event event = new MouseScrollCallback.Event(vertical);
        MouseScrollCallback.EVENT.invoker().interact(event);
        if (event.isCanceled() || ClickEventHandler.onMouseScrolling(horizontal, vertical)) ci.cancel();
    }

    @Unique
    private static boolean superbwarfare$isControllingDrone() {
        Player player = Minecraft.getInstance().player;
        if (player == null) return false;

        var stack = player.getMainHandItem();
        if (!stack.is(ModItems.MONITOR)) return false;

        var tag = NBTTool.getTag(stack);
        return tag.getBoolean("Using") && tag.getBoolean("Linked");
    }
}
