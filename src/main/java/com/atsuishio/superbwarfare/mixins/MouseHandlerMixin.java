package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.event.ClickEventHandler;
import com.atsuishio.superbwarfare.event.ClientMouseHandler;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.tools.NBTTool;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.Minecraft;
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

    @ModifyVariable(method = "turnPlayer(D)V", at = @At(value = "STORE", opcode = Opcodes.DSTORE), ordinal = 1)
    private double superbwarfare$sensitivity(double original) {
        return ClientMouseHandler.changeSensitivity(original);
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
        ClickEventHandler.onButtonPressed(button, action, modifiers);

        if (ClickEventHandler.shouldCancelMouseButton(button)) {
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
    private void superbwarfare$onMouseScrolled(long window, double horizontalAmount, double verticalAmount, CallbackInfo ci) {
        if (ClickEventHandler.onMouseScrolling(horizontalAmount, verticalAmount)) {
            ci.cancel();
        }
    }

    @ModifyArgs(
            method = "turnPlayer(D)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V")
    )
    private void superbwarfare$blockPlayerTurnWhileControllingDrone(Args args) {
        if (superbwarfare$isControllingDrone()) {
            args.set(0, 0.0D);
            args.set(1, 0.0D);
        }
    }

    @Unique
    private static boolean superbwarfare$isControllingDrone() {
        var player = Minecraft.getInstance().player;
        if (player == null) return false;

        var stack = player.getMainHandItem();
        if (!stack.is(ModItems.MONITOR)) return false;

        var tag = NBTTool.getTag(stack);
        return tag.getBoolean("Using") && tag.getBoolean("Linked");
    }

//    @Unique
//    private static double sbw121$x;
//    @Unique
//    private static double sbw121$y;

    // TODO 正确实现视角计算
//    @ModifyVariable(method = "turnPlayer(D)V", at = @At(value = "STORE", opcode = Opcodes.DSTORE), ordinal = 3)
//    private double modifyD0(double d) {
//        Minecraft mc = Minecraft.getInstance();
//        Player player = mc.player;
//
//        if (player == null) return d;
//        if (mc.options.getCameraType() != CameraType.FIRST_PERSON) return d;
//
//        if (player.getVehicle() instanceof VehicleEntity vehicle) {
//            sbw121$x = d;
//
//            double i = 0;
//
//            if (vehicle.getRoll() < 0) {
//                i = 1;
//            } else if (vehicle.getRoll() > 0) {
//                i = -1;
//            }
//
//            if (Mth.abs(vehicle.getRoll()) > 90) {
//                i *= (1 - (Mth.abs(vehicle.getRoll()) - 90) / 90);
//            }
//
//            return (1 - (Mth.abs(vehicle.getRoll()) / 90)) * d + ((Mth.abs(vehicle.getRoll()) / 90)) * sbw121$y * i;
//        }
//        return d;
//    }
//
//    @ModifyVariable(method = "turnPlayer(D)V", at = @At(value = "STORE", opcode = Opcodes.DSTORE), ordinal = 4)
//    private double modifyD1(double d) {
//        Minecraft mc = Minecraft.getInstance();
//        Player player = mc.player;
//
//        if (player == null) return d;
//        if (mc.options.getCameraType() != CameraType.FIRST_PERSON) return d;
//
//        if (player.getVehicle() instanceof VehicleEntity vehicle) {
//            sbw121$y = d;
//            return (1 - (Mth.abs(vehicle.getRoll()) / 90)) * d + ((Mth.abs(vehicle.getRoll()) / 90)) * sbw121$x * (vehicle.getRoll() < 0 ? -1 : 1);
//        }
//
//        return d;
//    }

}
