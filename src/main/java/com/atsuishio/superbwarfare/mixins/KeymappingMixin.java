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
import java.util.function.Consumer;

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
            var hotbarKey = ((KeyMappingAccessor) Minecraft.getInstance().options.keyHotbarSlots[i]).superbwarfare$getKey();
            if (hotbarKey.equals(this.key)) {
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

    @Inject(method = "set(Lcom/mojang/blaze3d/platform/InputConstants$Key;Z)V", at = @At("TAIL"))
    private static void superbwarfare$setVanillaMouseMappings(InputConstants.Key key, boolean isDown, CallbackInfo ci) {
        superbwarfare$forSharedVanillaMouseMapping(key, mapping -> mapping.setDown(isDown));
    }

    @Inject(method = "click(Lcom/mojang/blaze3d/platform/InputConstants$Key;)V", at = @At("TAIL"))
    private static void superbwarfare$clickVanillaMouseMappings(InputConstants.Key key, CallbackInfo ci) {
        superbwarfare$forSharedVanillaMouseMapping(key, mapping -> {
            KeyMappingAccessor accessor = (KeyMappingAccessor) mapping;
            accessor.superbwarfare$setClickCount(accessor.superbwarfare$getClickCount() + 1);
        });
    }

    @Unique
    private static void superbwarfare$forSharedVanillaMouseMapping(InputConstants.Key key, Consumer<KeyMapping> action) {
        if (key.getType() != InputConstants.Type.MOUSE) return;

        Minecraft mc = Minecraft.getInstance();

        superbwarfare$acceptIfShared(key, mc.options.keyAttack, action);
        superbwarfare$acceptIfShared(key, mc.options.keyUse, action);
        superbwarfare$acceptIfShared(key, mc.options.keyPickItem, action);
    }

    @Unique
    private static void superbwarfare$acceptIfShared(InputConstants.Key key, KeyMapping mapping, Consumer<KeyMapping> action) {
        if (MAP.get(key) == mapping) return;
        if (((KeyMappingAccessor) mapping).superbwarfare$getKey().equals(key)) {
            action.accept(mapping);
        }
    }
}
