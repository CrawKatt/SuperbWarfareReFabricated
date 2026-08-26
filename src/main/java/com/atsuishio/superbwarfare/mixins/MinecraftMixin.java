package com.atsuishio.superbwarfare.mixins;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.init.ModKeyMappings;
import com.atsuishio.superbwarfare.event.custom.InteractionKeyMappingTriggeredCallback;
import com.atsuishio.superbwarfare.event.custom.ClientLevelLifecycleCallback;
import com.atsuishio.superbwarfare.event.custom.ScreenOpeningCallback;
import com.atsuishio.superbwarfare.network.NetworkRegistry;
import com.atsuishio.superbwarfare.network.message.send.ChangeVehicleSeatMessage;
import com.atsuishio.superbwarfare.network.message.send.SwitchVehicleWeaponMessage;
import com.atsuishio.superbwarfare.tools.MinecraftUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow
    public Screen screen;

    @Shadow
    @Nullable
    public LocalPlayer player;

    @Shadow
    @Nullable
    public ClientLevel level;

    @Shadow
    @Final
    public Options options;

    /**
     * 在可切换座位的载具上，按下潜行键+数字键时切换座位
     * 在有武器的载具上，按下数字键时切换武器
     */
    @Inject(method = "handleKeybinds()V", at = @At("HEAD"), cancellable = true)
    private void handleKeybinds(CallbackInfo ci) {
        if (player == null || !(player.getVehicle() instanceof VehicleEntity vehicle)) return;

        var index = -1;
        for (int i = 0; i < 9; ++i) {
            if (options.keyHotbarSlots[i].isDown()) {
                index = i;
                break;
            }
        }
        if (index == -1) return;

        // shift+数字键 座位更改
        if (vehicle.getMaxPassengers() > 1
                && ModKeyMappings.CHANGE_SEAT.isDown()
                && index < vehicle.getMaxPassengers()
                && vehicle.getNthEntity(index) == null
        ) {
            ci.cancel();
            options.keyHotbarSlots[index].consumeClick();

            NetworkRegistry.sendToServer(new ChangeVehicleSeatMessage(index));
            vehicle.changeSeat(player, index);

            return;
        }

        var seatIndex = vehicle.getSeatIndex(player);

        if (vehicle.banHand(player)) {
            ci.cancel();
            options.keyHotbarSlots[index].consumeClick();

            // 数字键 武器切换
            if (!ModKeyMappings.CHANGE_SEAT.isDown()
                    && vehicle.hasWeapon(seatIndex)
                    && vehicle.getWeaponIndex(seatIndex) != index) {
                if (ClientEventHandler.switchVehicleWeaponCooldown <= 0) {
                    NetworkRegistry.sendToServer(new SwitchVehicleWeaponMessage(seatIndex, index, false));
                    ClientEventHandler.switchVehicleWeaponCooldown = 3;
                }
            }
        }
    }

    /// Event for Fabric
    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$onSetScreen(Screen newScreen, CallbackInfo ci) {
        Screen replacement = ScreenOpeningCallback.EVENT.invoker().onScreenOpening(this.screen, newScreen);

        if (replacement != newScreen) {
            ci.cancel();
            ((Minecraft) (Object) this).setScreen(replacement);
        }
    }

    @Inject(method = "setLevel", at = @At("HEAD"))
    private void superbwarfare$beforeSetLevel(ClientLevel newLevel, CallbackInfo ci) {
        if (this.level != null && this.level != newLevel) {
            ClientLevelLifecycleCallback.UNLOAD.invoker().onLevel((Minecraft) (Object) this, this.level);
        }
    }

    @Inject(method = "setLevel", at = @At("TAIL"))
    private void superbwarfare$afterSetLevel(ClientLevel newLevel, CallbackInfo ci) {
        ClientLevelLifecycleCallback.LOAD.invoker().onLevel((Minecraft) (Object) this, newLevel);
    }

    @Inject(method = "clearLevel(Lnet/minecraft/client/gui/screens/Screen;)V", at = @At("HEAD"))
    private void superbwarfare$beforeClearLevel(Screen screen, CallbackInfo ci) {
        if (this.level != null) {
            ClientLevelLifecycleCallback.UNLOAD.invoker().onLevel((Minecraft) (Object) this, this.level);
        }
    }

    @Unique
    private boolean superbwarfare$cancelSwing = false;

    @Inject(method = "startUseItem", at = @At("HEAD"))
    private void superbwarfare$onStartUseItem(CallbackInfo ci) {
        InteractionKeyMappingTriggeredCallback.Event event = new InteractionKeyMappingTriggeredCallback.Event(InteractionHand.MAIN_HAND);
        InteractionKeyMappingTriggeredCallback.EVENT.invoker().interact(event);
        superbwarfare$cancelSwing = !event.shouldSwingHand();
    }

    @Redirect(method = "startUseItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;swing(Lnet/minecraft/world/InteractionHand;)V"))
    private void superbwarfare$redirectSwing(LocalPlayer player, InteractionHand hand) {
        if (!superbwarfare$cancelSwing) {
            player.swing(hand);
        }
        superbwarfare$cancelSwing = false;
    }
}
