package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.atsuishio.superbwarfare.mixins.accessor.KeyMappingAccessor;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(KeyMapping.class)
public class KeymappingMixin {

    @Shadow
    @Final
    private static Map<InputConstants.Key, KeyMapping> MAP;

    @Shadow
    private InputConstants.Key key;

    @Shadow
    private int clickCount;

    @Inject(method = "consumeClick()Z", at = @At("HEAD"), cancellable = true)
    public void consumeClick(CallbackInfoReturnable<Boolean> cir) {
        Player player = Minecraft.getInstance().player;
        if (player == null || !(player.getVehicle() instanceof VehicleEntity vehicle)) return;

        for (int i = 0; i < 9; i++) {
            if (Minecraft.getInstance().options.keyHotbarSlots[i].getDefaultKey() == key) {
                if (vehicle.getMaxPassengers() > 1
                        && Screen.hasShiftDown()
                        && i < vehicle.getMaxPassengers()
                        && vehicle.getNthEntity(i) == null
                ) {
                    if (this.clickCount > 0) {
                        --this.clickCount;
                    }
                    cir.setReturnValue(false);
                }

                if (vehicle.banHand(player)) {
                    if (this.clickCount > 0) {
                        --this.clickCount;
                    }
                    cir.setReturnValue(false);
                }
            }
        }
    }

    @Inject(method = "resetMapping()V", at = @At("TAIL"))
    private static void superbwarfare$restoreVanillaMouseMappings(CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.options == null) return;

        superbwarfare$restoreMapping(mc.options.keyAttack);
        superbwarfare$restoreMapping(mc.options.keyUse);
        superbwarfare$restoreMapping(mc.options.keyPickItem);
    }

    @Unique
    private static void superbwarfare$restoreMapping(KeyMapping mapping) {
        InputConstants.Key key = ((KeyMappingAccessor) mapping).superbwarfare$getKey();
        if (key != InputConstants.UNKNOWN) {
            MAP.put(key, mapping);
        }
    }
}
