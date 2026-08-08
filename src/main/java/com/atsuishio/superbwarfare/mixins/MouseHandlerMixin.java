package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.event.ClickEventHandler;
import com.atsuishio.superbwarfare.event.ClientMouseHandler;
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.tools.NBTTool;
import net.minecraft.client.CameraType;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.Minecraft;
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
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || mc.options.getCameraType() != CameraType.FIRST_PERSON) return;
        if (!(player.getVehicle() instanceof VehicleEntity vehicle)) return;

        double x = args.get(0);
        double y = args.get(1);
        double roll = vehicle.getRoll();
        double absoluteRoll = Mth.abs((float) roll);
        double horizontalSign = roll < 0 ? 1 : roll > 0 ? -1 : 0;

        if (absoluteRoll > 90) {
            horizontalSign *= 1 - (absoluteRoll - 90) / 90;
        }

        args.set(0, (1 - absoluteRoll / 90) * x + (absoluteRoll / 90) * superbwarfare$previousTurnY * horizontalSign);
        args.set(1, (1 - absoluteRoll / 90) * y + (absoluteRoll / 90) * x * (roll < 0 ? -1 : 1));
        superbwarfare$previousTurnY = y;
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

    @Unique
    private static double superbwarfare$previousTurnY;

}
