package com.atsuishio.superbwarfare.mixins;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.atsuishio.superbwarfare.event.ClickEventHandler;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.event.custom.InteractionKeyMappingTriggeredCallback;
import com.atsuishio.superbwarfare.init.ModKeyMappings;
import com.atsuishio.superbwarfare.network.message.send.ChangeVehicleSeatMessage;
import com.atsuishio.superbwarfare.network.message.send.SwitchVehicleWeaponMessage;
import com.atsuishio.superbwarfare.tools.MinecraftUtil;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow
    @Nullable
    public LocalPlayer player;

    @Shadow
    @Final
    public Options options;

    @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$onShelfAttack(CallbackInfoReturnable<Boolean> cir) {
        if (ClickEventHandler.onShelfAttack()) cir.setReturnValue(false);
    }

    @Inject(method = "continueAttack", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$onShelfContinueAttack(boolean leftClick, CallbackInfo ci) {
        if (leftClick && ClickEventHandler.onShelfAttack()) ci.cancel();
    }

    @Inject(method = "startAttack", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/phys/HitResult;getType()Lnet/minecraft/world/phys/HitResult$Type;"),
            cancellable = true)
    private void superbwarfare$onStartAttack(CallbackInfoReturnable<Boolean> cir,
                                           @Share("suppressAttackSwing") LocalBooleanRef suppressSwing) {
        var event = new InteractionKeyMappingTriggeredCallback.Event(InteractionHand.MAIN_HAND, true);
        InteractionKeyMappingTriggeredCallback.EVENT.invoker().interact(event);
        suppressSwing.set(!event.shouldSwingHand());
        if (event.isCanceled()) {
            if (event.shouldSwingHand()) player.swing(InteractionHand.MAIN_HAND);
            cir.setReturnValue(false);
        }
    }

    @WrapWithCondition(method = "startAttack", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/player/LocalPlayer;swing(Lnet/minecraft/world/InteractionHand;)V"))
    private boolean superbwarfare$shouldSwingOnAttack(LocalPlayer player, InteractionHand hand,
                                                     @Share("suppressAttackSwing") LocalBooleanRef suppressSwing) {
        return !suppressSwing.get();
    }

    @WrapOperation(method = "continueAttack", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;continueDestroyBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z"))
    private boolean superbwarfare$onContinueAttack(MultiPlayerGameMode gameMode, BlockPos pos, Direction face,
                                                  Operation<Boolean> original) {
        var event = new InteractionKeyMappingTriggeredCallback.Event(InteractionHand.MAIN_HAND, true);
        InteractionKeyMappingTriggeredCallback.EVENT.invoker().interact(event);
        return (event.isCanceled() || original.call(gameMode, pos, face)) && event.shouldSwingHand();
    }

    @Inject(method = "startUseItem", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/player/LocalPlayer;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;"),
            cancellable = true)
    private void superbwarfare$onStartUseItem(CallbackInfo ci, @Local InteractionHand hand,
                                            @Share("suppressUseSwing") LocalBooleanRef suppressSwing) {
        var event = new InteractionKeyMappingTriggeredCallback.Event(hand, false);
        InteractionKeyMappingTriggeredCallback.EVENT.invoker().interact(event);
        suppressSwing.set(!event.shouldSwingHand());
        if (event.isCanceled()) {
            if (event.shouldSwingHand()) player.swing(hand);
            ci.cancel();
        }
    }

    @ModifyExpressionValue(method = "startUseItem", at = {
            @At(value = "INVOKE", target = "Lnet/minecraft/world/InteractionResult;shouldSwing()Z", ordinal = 0),
            @At(value = "INVOKE", target = "Lnet/minecraft/world/InteractionResult;shouldSwing()Z", ordinal = 1)
    }, require = 2)
    private boolean superbwarfare$shouldSwingOnInteraction(boolean original,
                                                          @Share("suppressUseSwing") LocalBooleanRef suppressSwing) {
        return original && !suppressSwing.get();
    }

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

            MinecraftUtil.sendPacketToServer(new ChangeVehicleSeatMessage(index));
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
                    MinecraftUtil.sendPacketToServer(new SwitchVehicleWeaponMessage(seatIndex, index, false));
                    ClientEventHandler.switchVehicleWeaponCooldown = 3;
                }
            }
        }
    }
}
