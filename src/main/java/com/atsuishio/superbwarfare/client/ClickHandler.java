package com.atsuishio.superbwarfare.client;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import com.atsuishio.superbwarfare.client.screens.WeaponEditScreen;
import com.atsuishio.superbwarfare.compat.CompatHolder;
import com.atsuishio.superbwarfare.compat.clothconfig.ClothConfigHelper;
import com.atsuishio.superbwarfare.config.client.ReloadConfig;
import com.atsuishio.superbwarfare.data.gun.FireMode;
import com.atsuishio.superbwarfare.data.gun.GunData;
import com.atsuishio.superbwarfare.data.gun.SeekType;
import com.atsuishio.superbwarfare.entity.vehicle.DroneEntity;
import com.atsuishio.superbwarfare.entity.vehicle.MortarEntity;
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.event.ClientMouseHandler;
import com.atsuishio.superbwarfare.init.*;
import com.atsuishio.superbwarfare.item.ItemScreenProvider;
import com.atsuishio.superbwarfare.item.gun.GunItem;
import com.atsuishio.superbwarfare.mixins.KeyMappingAccessor;
import com.atsuishio.superbwarfare.mixins.MinecraftAccessor;
import com.atsuishio.superbwarfare.network.message.send.*;
import com.atsuishio.superbwarfare.resource.gun.GunResource;
import com.atsuishio.superbwarfare.tools.EntityFindUtil;
import com.atsuishio.superbwarfare.tools.NBTTool;
import com.atsuishio.superbwarfare.tools.SeekTool;
import com.atsuishio.superbwarfare.tools.TraceTool;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.glfw.GLFW;

import static com.atsuishio.superbwarfare.event.ClientEventHandler.*;


public class ClickHandler {
    public static boolean switchZoom = false;

    private static boolean notInGame() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return true;
        if (mc.getOverlay() != null) return true;
        if (mc.screen != null) return true;
        if (!mc.mouseHandler.isMouseGrabbed()) return true;
        return !mc.isWindowActive();
    }

    public static void onButtonReleased(int button, int action, int modifiers) {
        if (notInGame()) return;
        if (action != InputConstants.RELEASE) return;

        syncModMouseConflictState(button, action);

        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        if (player.hasEffect(ModMobEffects.SHOCK)) return;

        if (ModKeyMappings.FIRE.matchesMouse(button)) {
            handleWeaponFireRelease();
        }
        if (ModKeyMappings.HOLD_ZOOM.matchesMouse(button)) {
            handleWeaponZoomRelease();
            return;
        }

        if (ModKeyMappings.SWITCH_ZOOM.matchesMouse(button) && !switchZoom) {
            handleWeaponZoomRelease();
        }
    }

    private static boolean cancelFireKey(Player player, ItemStack stack) {
        return stack.getItem() instanceof GunItem || stack.is(ModItems.MONITOR) || stack.is(ModItems.LUNGE_MINE) || stack.is(ModItems.ARTILLERY_INDICATOR) || player.hasEffect(ModMobEffects.SHOCK)
                || (player.getVehicle() instanceof VehicleEntity vehicle && vehicle.banHand(player));
    }

    private static boolean cancelZoomKey(Player player, ItemStack stack) {
        return stack.getItem() instanceof GunItem
                || (player.getVehicle() instanceof VehicleEntity vehicle && vehicle.banHand(player) && !stack.has(DataComponents.FOOD));
    }

    public static boolean shouldCancelMouseButton(int button) {
        if (notInGame()) return false;

        var mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || player.isSpectator()) return false;

        ItemStack stack = player.getMainHandItem();

        if (ModKeyMappings.FIRE.matchesMouse(button) && cancelFireKey(player, stack)) {
            return true;
        }

        if ((ModKeyMappings.HOLD_ZOOM.matchesMouse(button) || ModKeyMappings.SWITCH_ZOOM.matchesMouse(button))
                && cancelZoomKey(player, stack)) {
            return true;
        }

        return button == GLFW.GLFW_MOUSE_BUTTON_MIDDLE
                && (player.hasEffect(ModMobEffects.SHOCK)
                || stack.is(ModItems.ARTILLERY_INDICATOR)
                || (stack.is(ModItems.MONITOR) && player.getOffhandItem().is(ModItems.ARTILLERY_INDICATOR)));
    }

    public static void releaseVanillaMouseButton(int button) {
        var options = Minecraft.getInstance().options;

        if (options.keyAttack.matchesMouse(button)) {
            options.keyAttack.setDown(false);
        }
        if (options.keyUse.matchesMouse(button)) {
            options.keyUse.setDown(false);
        }
        if (options.keyPickItem.matchesMouse(button)) {
            options.keyPickItem.setDown(false);
        }
    }

    public static void forwardVanillaMouseButtonIfNeeded(int button, int action) {
        if (notInGame() || !hasModMouseConflict(button)) {
            return;
        }

        var options = Minecraft.getInstance().options;
        forwardVanillaMouseButton(options.keyAttack, button, action);
        forwardVanillaMouseButton(options.keyUse, button, action);
        forwardVanillaMouseButton(options.keyPickItem, button, action);
    }

    private static boolean hasModMouseConflict(int button) {
        return ModKeyMappings.FIRE.matchesMouse(button)
                || ModKeyMappings.HOLD_ZOOM.matchesMouse(button)
                || ModKeyMappings.SWITCH_ZOOM.matchesMouse(button)
                || ModKeyMappings.FIRE_MODE.matchesMouse(button)
                || ModKeyMappings.MELEE.matchesMouse(button)
                || ModKeyMappings.MARK.matchesMouse(button);
    }

    private static void syncModMouseConflictState(int button, int action) {
        if (action != InputConstants.PRESS && action != InputConstants.RELEASE) {
            return;
        }

        boolean down = action == InputConstants.PRESS;
        syncMouseMapping(ModKeyMappings.MELEE, button, down);
        syncMouseMapping(ModKeyMappings.RELEASE_DECOY, button, down);
    }

    private static void syncMouseMapping(KeyMapping keyMapping, int button, boolean down) {
        if (keyMapping.matchesMouse(button)) {
            keyMapping.setDown(down);
        }
    }

    private static void forwardVanillaMouseButton(KeyMapping keyMapping, int button, int action) {
        if (!keyMapping.matchesMouse(button) || !isShadowedMouseMapping(keyMapping, button)) {
            return;
        }

        if (action == InputConstants.PRESS) {
            keyMapping.setDown(true);
            var accessor = (KeyMappingAccessor) (Object) keyMapping;
            accessor.superbwarfare$setClickCount(accessor.superbwarfare$getClickCount() + 1);
        } else if (action == InputConstants.RELEASE) {
            keyMapping.setDown(false);
        }
    }

    private static boolean isShadowedMouseMapping(KeyMapping keyMapping, int button) {
        var key = InputConstants.Type.MOUSE.getOrCreate(button);
        return KeyMappingAccessor.superbwarfare$getMap().get(key) != keyMapping;
    }

    public static void onButtonPressed(int button, int action, int modifiers) {
        if (notInGame()) return;
        if (action != InputConstants.PRESS) return;

        syncModMouseConflictState(button, action);

        var mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;
        if (player.isSpectator()) return;

        ItemStack stack = player.getMainHandItem();

        if (player.hasEffect(ModMobEffects.SHOCK)) return;

        if (ModKeyMappings.MARK.matchesMouse(button)) {
            if (stack.is(ModItems.ARTILLERY_INDICATOR)) {
                ClientPlayNetworking.send(SetFiringParametersMessage.INSTANCE);
            }
            if (stack.is(ModItems.MONITOR) && player.getOffhandItem().is(ModItems.ARTILLERY_INDICATOR)) {
                droneLeftClick(stack, player);
            }
        }

        if (stack.getItem() instanceof GunItem
                || stack.is(ModItems.MONITOR)
                || stack.is(ModItems.LUNGE_MINE)
                || player.getVehicle() instanceof VehicleEntity
                || (stack.is(Items.SPYGLASS) && player.isScoping() && player.getOffhandItem().is(ModItems.FIRING_PARAMETERS))
                || (stack.is(ModItems.ARTILLERY_INDICATOR))
        ) {
            if (ModKeyMappings.FIRE.matchesMouse(button)) {
                handleWeaponFirePress(player, stack);
            }

            if (ModKeyMappings.HOLD_ZOOM.matchesMouse(button)) {
                handleWeaponZoomPress(player, stack);
                switchZoom = false;
                return;
            }

            if (ModKeyMappings.SWITCH_ZOOM.matchesMouse(button)) {
                handleWeaponZoomPress(player, stack);
                switchZoom = !switchZoom;
            }
        }

        if (ModKeyMappings.FIRE_MODE.matchesMouse(button)) {
            handleFireModePress(player, false);
        }
    }

    // 枪械交互时禁止挥舞手臂
    public static void stopSwing() {
        var player = Minecraft.getInstance().player;
        if (player != null && player.getMainHandItem().getItem() instanceof GunItem) {
            // requires mixin to cancel hand swing
        }
    }

    public static boolean onMouseScrolling(double horizontalAmount, double verticalAmount) {
        Player player = Minecraft.getInstance().player;

        if (notInGame()) return false;
        if (player == null) return false;

        ItemStack stack = player.getMainHandItem();

        if (player.hasEffect(ModMobEffects.SHOCK)) return false;

        double scroll = verticalAmount;
        boolean consumed = false;

        // 按下自由视角键时，为载具调整相机距离
        if (player.getVehicle() instanceof VehicleEntity vehicle && player == vehicle.getFirstPassenger() && ModKeyMappings.FREE_CAMERA.isDown()) {
            ClientMouseHandler.custom3pDistance = Mth.clamp(ClientMouseHandler.custom3pDistance - verticalAmount, -3, 8);
            return true;
        }

        // 未按下shift时，为有武器的载具切换武器
        if (!Screen.hasShiftDown()
                && player.getVehicle() instanceof VehicleEntity vehicle
                && vehicle.hasWeapon(vehicle.getSeatIndex(player))
                && vehicle.banHand(player)
        ) {
            consumed = true;
            if (switchVehicleWeaponCooldown <= 0) {
                int index = vehicle.getSeatIndex(player);
                ClientPlayNetworking.send(new SwitchVehicleWeaponMessage(index, -scroll, true));
                switchVehicleWeaponCooldown = 3;
            }
        }

        var tag = NBTTool.getTag(stack);

        if (stack.getItem() instanceof GunItem && ClientEventHandler.zoom) {
            var data = GunData.from(stack);
            if (data.canSwitchScope()) {
                ClientPlayNetworking.send(new SwitchScopeMessage(scroll));
                consumed = true;
            } else if (data.canAdjustZoom() || stack.is(ModItems.MINIGUN)) {
                ClientPlayNetworking.send(new AdjustZoomFovMessage(scroll));
                consumed = true;
            }
        }

        if (stack.is(ModItems.MONITOR) && tag.getBoolean("Using") && tag.getBoolean("Linked")) {
            ClientEventHandler.droneFov = Mth.clamp(ClientEventHandler.droneFov + 0.4 * scroll, 1, 6);
            consumed = true;
        }

        if (player.isUsingItem() && player.getUseItem().is(ModItems.ARTILLERY_INDICATOR)) {
            artilleryIndicatorCustomZoom = Mth.clamp(artilleryIndicatorCustomZoom + 0.4 * scroll, -2, 6);
            consumed = true;
        }

        Entity looking = TraceTool.findLookingEntity(player, 6);
        if (looking instanceof MortarEntity && player.isShiftKeyDown()) {
            ClientPlayNetworking.send(new AdjustMortarAngleMessage(scroll));
            consumed = true;
        }

        return consumed;
    }

    public static boolean onKeyPressed(int keyCode, int scanCode, int action, int modifiers) {
        if (notInGame()) return false;

        var mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return false;
        int key = keyCode;
        if (key < 0) return false;

        syncModKeyConflictState(key, scanCode, action);

        if (action == GLFW.GLFW_PRESS && ModKeyMappings.DISMOUNT.matches(key, scanCode)) {
            handleDismountPress(player);
            return true;
        }

        if (player.isSpectator()) return false;

        var stack = player.getMainHandItem();

        if (action == GLFW.GLFW_PRESS) {
            if (player.hasEffect(ModMobEffects.SHOCK)) return false;

            if (Minecraft.getInstance().options.keyJump.matches(key, scanCode)) {
                handleDoubleJump(player);
                handleParachute();
            }

            if (ModKeyMappings.CONFIG.matches(key, scanCode)) {
                handleConfigScreen(player);
                return true;
            }

            if (ModKeyMappings.RELOAD.matches(key, scanCode)) {
                burstFireAmount = 0;
                isEditing = false;
                seekingTime = 0;
                lockOn = false;
                lockingEntity = null;
                seekingEntity = null;
                lockingPos = null;
                ClientPlayNetworking.send(ReloadMessage.INSTANCE);
                return true;
            }
            if (ModKeyMappings.FIRE_MODE.matches(key, scanCode) || ModKeyMappings.CHANGE_FIRE_MODE_BACKWARD.matches(key, scanCode)) {
                handleFireModePress(player, false);
                return true;
            }
            if (ModKeyMappings.CHANGE_FIRE_MODE_FORWARD.matches(key, scanCode)) {
                handleFireModePress(player, true);
                return true;
            }
            if (ModKeyMappings.INTERACT.matches(key, scanCode)) {
                if (stack.getItem() instanceof GunItem) {
                    ((MinecraftAccessor) mc).superbwarfare$startUseItem();
                    return true;
                } else if (stack.is(ModItems.MONITOR)) {
                    ClientPlayNetworking.send(InteractMessage.INSTANCE);
                    return true;
                }
                return false;
            }

            // 玩家手持枪械时，处理卸弹/切换弹种
            if (stack.getItem() instanceof GunItem) {
                var data = GunData.from(stack);
                if (ModKeyMappings.UNLOAD.matches(key, scanCode)) {
                    if (data.useBackpackAmmo() || data.ammo.get() + data.virtualAmmo.get() <= 0) return true;
                    ClientPlayNetworking.send(UnloadMessage.INSTANCE);
                    burstFireAmount = 0;
                    return true;
                }
                if (data.compute().getAmmoConsumers().size() > 1) {
                    if (ModKeyMappings.CHANGE_AMMO_FORWARD.matches(key, scanCode)) {
                        ClientPlayNetworking.send(new EditMessage(5, false, false));
                        burstFireAmount = 0;
                        return true;
                    } else if (ModKeyMappings.CHANGE_AMMO_BACKWARD.matches(key, scanCode)) {
                        ClientPlayNetworking.send(new EditMessage(5, true, false));
                        burstFireAmount = 0;
                        return true;
                    }
                }
            }

            // 玩家位于载具上时，处理切换弹种
            if (player.getVehicle() instanceof VehicleEntity vehicle) {
                var data = vehicle.getGunData(player);
                if (data != null && data.getDefault().getAmmoConsumers().size() > 1) {
                    if (ModKeyMappings.CHANGE_AMMO_FORWARD.matches(key, scanCode)) {
                        ClientPlayNetworking.send(new EditMessage(5, false, true));
                        burstFireAmount = 0;
                        return true;
                    }
                    if (ModKeyMappings.CHANGE_AMMO_BACKWARD.matches(key, scanCode) ||
                            ModKeyMappings.FIRE_MODE.matches(key, scanCode)) {
                        ClientPlayNetworking.send(new EditMessage(5, true, true));
                        burstFireAmount = 0;
                        return true;
                    }
                }
            }

            if (ModKeyMappings.EDIT_MODE.matches(key, scanCode)) {
                if (stack.getItem() instanceof ItemScreenProvider provider) {
                    var screen = provider.getItemScreen(stack, player, InteractionHand.MAIN_HAND);
                    if (screen != null) {
                        Minecraft.getInstance().setScreen(screen);
                        if (screen instanceof WeaponEditScreen) {
                            ClientEventHandler.onOpenEditScreen();
                        }
                        return true;
                    }
                }
                ItemStack offHand = player.getOffhandItem();
                if (offHand.getItem() instanceof ItemScreenProvider provider) {
                    var screen = provider.getItemScreen(offHand, player, InteractionHand.OFF_HAND);
                    if (screen != null) {
                        Minecraft.getInstance().setScreen(screen);
                        return true;
                    }
                }
                return true;
            }

            if (ModKeyMappings.BREATH.matches(key, scanCode) && !exhaustion && zoom) {
                breath = true;
                return true;
            }
            if (ModKeyMappings.SENSITIVITY_INCREASE.matches(key, scanCode)) {
                ClientPlayNetworking.send(new SensitivityMessage(true));
                return true;
            }
            if (ModKeyMappings.SENSITIVITY_REDUCE.matches(key, scanCode)) {
                ClientPlayNetworking.send(new SensitivityMessage(false));
                return true;
            }

            if (stack.getItem() instanceof GunItem
                    || stack.is(ModItems.MONITOR)
                    || (player.getVehicle() instanceof VehicleEntity vehicle && vehicle.getFirstPassenger() == player)
                    || (stack.is(Items.SPYGLASS) && player.isScoping() && player.getOffhandItem().is(ModItems.FIRING_PARAMETERS))
                    || (stack.is(ModItems.ARTILLERY_INDICATOR))
            ) {
                if (ModKeyMappings.FIRE.matches(key, scanCode)) {
                    handleWeaponFirePress(player, stack);
                    return true;
                }

                if (ModKeyMappings.HOLD_ZOOM.matches(key, scanCode)) {
                    handleWeaponZoomPress(player, stack);
                    switchZoom = false;
                    return true;
                }

                if (ModKeyMappings.SWITCH_ZOOM.matches(key, scanCode)) {
                    handleWeaponZoomPress(player, stack);
                    switchZoom = !switchZoom;
                    return true;
                }
            }

            if (ModKeyMappings.MARK.matches(key, scanCode)) {
                if (stack.is(ModItems.ARTILLERY_INDICATOR)) {
                    ClientPlayNetworking.send(SetFiringParametersMessage.INSTANCE);
                }
                if (stack.is(ModItems.MONITOR) && player.getOffhandItem().is(ModItems.ARTILLERY_INDICATOR)) {
                    droneLeftClick(stack, player);
                }
                return true;
            }
        } else if (action == GLFW.GLFW_RELEASE) {
            if (player.hasEffect(ModMobEffects.SHOCK)) return false;

            if (ModKeyMappings.BREATH.matches(key, scanCode)) {
                breath = false;
                return true;
            }

            if (ModKeyMappings.FIRE.matches(key, scanCode)) {
                handleWeaponFireRelease();
                return true;
            }
            if (ModKeyMappings.HOLD_ZOOM.matches(key, scanCode)) {
                handleWeaponZoomRelease();
                return true;
            }

            if (ModKeyMappings.SWITCH_ZOOM.matches(key, scanCode) && !switchZoom) {
                handleWeaponZoomRelease();
                return true;
            }
        }

        return false;
    }

    private static void syncModKeyConflictState(int key, int scanCode, int action) {
        if (action != GLFW.GLFW_PRESS && action != GLFW.GLFW_RELEASE) {
            return;
        }

        boolean down = action == GLFW.GLFW_PRESS;
        syncKeyMapping(ModKeyMappings.DISMOUNT, key, scanCode, down);
        syncKeyMapping(ModKeyMappings.MELEE, key, scanCode, down);
        syncKeyMapping(ModKeyMappings.RELEASE_DECOY, key, scanCode, down);
    }

    private static void syncKeyMapping(KeyMapping keyMapping, int key, int scanCode, boolean down) {
        if (keyMapping.matches(key, scanCode)) {
            keyMapping.setDown(down);
        }
    }

    private static void handleFireModePress(Player player, boolean forward) {
        if (player.getVehicle() instanceof VehicleEntity vehicle) {
            var data = vehicle.getGunData(player);
            if (data != null && data.getDefault().getAmmoConsumers().size() > 1) {
                ClientPlayNetworking.send(new EditMessage(5, !forward, true));
                burstFireAmount = 0;
                return;
            }
        }

        ClientPlayNetworking.send(new FireModeMessage(forward));
        burstFireAmount = 0;
    }

    public static void handleWeaponFirePress(Player player, ItemStack stack) {
        isEditing = false;
        if (player.hasEffect(ModMobEffects.SHOCK)) return;

        if (player.getVehicle() instanceof VehicleEntity vehicle && vehicle.banHand(player)) {
            if (vehicle.hasWeapon(vehicle.getSeatIndex(player))) {
                ClientEventHandler.holdFireVehicle = true;
            }
            return;
        }

        if (stack.is(ModItems.ARTILLERY_INDICATOR)) {
            ClientEventHandler.holdingFireKey = true;
        }

        if (stack.is(Items.SPYGLASS) && player.isScoping() && player.getOffhandItem().is(ModItems.FIRING_PARAMETERS)) {
            ClientPlayNetworking.send(SetFiringParametersMessage.INSTANCE);
        }

        if (stack.is(ModItems.MONITOR)) {
            if (player.getOffhandItem().is(ModItems.ARTILLERY_INDICATOR)) {
                ClientEventHandler.holdingFireKey = true;
            } else {
                droneLeftClick(stack, player);
            }
        }

        if (stack.is(ModItems.LUNGE_MINE)) {
            ClientEventHandler.usingLunge = true;
        }

        if (stack.getItem() instanceof GunItem gunItem
                && clientTimer.getProgress() == 0
                && !notInGame()
        ) {
            var data = GunData.from(stack);
            var resource = GunResource.compute(stack);

            /*
            // TODO 整合特殊处理
            if (!(stack.is(ModItems.BOCEK) || stack.is(ModItems.AURELIA_SCEPTRE))) {
                if (!data.meleeOnly()) {
                    // 普通枪（？）
                    if (stack.is(ModItems.QL_1031) && data.selectedFireModeInfo().name.equals("Hold") && gunItem.canShoot(data, player)) {
                        player.playSound(ModSounds.QL_1031_CHARGE, 1, 1);
                        shouldPlayDischargeSound = true;
                    }

                    var triggerSound = resource.triggerSound;
                    if (triggerSound != null && !data.meleeOnly()) {
                        player.playSound(triggerSound, 1, 1);
                    }
                }
            } else {
                // 波塞克、海月权杖特殊处理
                bowPower = 0;
                holdingFireKey = true;
                player.setSprinting(false);
                if (data.hasEnoughAmmoToShoot(player)) {
                    return;
                }
            }
            */

            if (!data.useBackpackAmmo() && !data.meleeOnly() && !data.hasEnoughAmmoToShoot(player) && data.reload.time() == 0) {
                if (ReloadConfig.LEFT_CLICK_RELOAD.get()) {
                    ClientPlayNetworking.send(ReloadMessage.INSTANCE);
                    burstFireAmount = 0;
                    seekingTime = 0;
                    lockOn = false;
                    lockingEntity = null;
                    seekingEntity = null;
                    lockingPos = null;
                }
            } else {
                ClientPlayNetworking.send(new FireKeyMessage(0, bowPower, zoom));
                if ((!data.reloading()
                        && !data.charging()
                        && !data.bolt.needed.get())
                        && drawTime < 0.01
                ) {
                    var fireMode = data.selectedFireModeInfo().mode;

                    if (fireMode == FireMode.BURST) {
                        if (ClientEventHandler.burstFireAmount == 0) {
                            noSprintTicks = 8;
                            player.setSprinting(false);
                            ClientEventHandler.burstFireAmount = data.compute().getBurstAmount();
                        }
                    } else if (fireMode == FireMode.SEMI) {
                        if (ClientEventHandler.burstFireAmount == 0) {
                            noSprintTicks = 3;
                            player.setSprinting(false);
                            ClientEventHandler.burstFireAmount = 1;
                        }
                    }

                    ClientEventHandler.holdingFireKey = true;
                    player.setSprinting(false);
                }
            }
        }
    }

    public static void handleWeaponFireRelease() {
        ClientPlayNetworking.send(new FireKeyMessage(1, bowPower, zoom));
        bowPull = false;
        holdingFireKey = false;
        holdFireVehicle = false;
        isEditing = false;
        customRpm = 0;

        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        if (player.isSpectator()) return;

        ItemStack stack = player.getMainHandItem();

        if (stack.is(ModItems.BOCEK)) {
            ClientPlayNetworking.send(ReloadMessage.INSTANCE);
        }

        if (stack.getItem() instanceof GunItem) {
            var data = GunData.from(stack);
            var computed = data.compute();
            if (computed.getSeekType() == SeekType.HOLD_FIRE) {
                ClientEventHandler.stopWeaponSeekSound(Minecraft.getInstance().player);
            }
        }
    }

    public static void handleWeaponZoomPress(Player player, ItemStack stack) {
        ClientPlayNetworking.send(new ZoomMessage(0));

        isEditing = false;

        if (player.getVehicle() instanceof VehicleEntity vehicle && vehicle.hasWeapon(vehicle.getSeatIndex(player)) && vehicle.banHand(player)) {
            ClientEventHandler.zoomVehicle = true;
            return;
        }

        if (!(stack.getItem() instanceof GunItem)) return;

        if (!GunResource.compute(stack).canZoom) return;

        var data = GunData.from(stack);
        ClientEventHandler.zoom = true;

        int level = data.perk.getLevel(ModPerks.INTELLIGENT_CHIP);
        if (level > 0) {
            if (ClientEventHandler.lockedEntity == null) {
                if (data.perk.has(ModPerks.PHASE_PENETRATING_BULLET) || data.perk.has(ModPerks.BEAST_BULLET)) {
                    ClientEventHandler.lockedEntity = SeekTool.seekEntityThroughWall(player, 32 + 8 * (level - 1), 20);
                } else {
                    ClientEventHandler.lockedEntity = SeekTool.seekLivingEntity(player, 32 + 8 * (level - 1), 20);
                }
            }
        }
    }

    public static void handleWeaponZoomRelease() {
        ClientPlayNetworking.send(new ZoomMessage(1));
        ClientEventHandler.zoom = false;
        ClientEventHandler.zoomVehicle = false;
        ClientEventHandler.lockedEntity = null;
        ClientEventHandler.stopWeaponSeekSound(Minecraft.getInstance().player);
        breath = false;
    }

    private static void handleDoubleJump(Player player) {
        Level level = player.level();
        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();

        if (!level.isLoaded(player.blockPosition())) {
            return;
        }

        if (canDoubleJump) {
            player.setDeltaMovement(new Vec3(player.getLookAngle().x, 0.8, player.getLookAngle().z));
            level.playLocalSound(x, y, z, ModSounds.DOUBLE_JUMP, SoundSource.BLOCKS, 1, 1, false);
            ClientPlayNetworking.send(DoubleJumpMessage.INSTANCE);
            canDoubleJump = false;
        }
    }

    private static void handleParachute() {
        ClientPlayNetworking.send(ParachuteMessage.INSTANCE);
    }

    private static void handleConfigScreen(Player player) {
        if (FabricLoader.getInstance().isModLoaded(CompatHolder.CLOTH_CONFIG)) {
            CompatHolder.hasMod(CompatHolder.CLOTH_CONFIG, () -> Minecraft.getInstance().setScreen(ClothConfigHelper.getConfigScreen(null)));
        } else {
            player.displayClientMessage(Component.translatable("tips.superbwarfare.no_cloth_config").withStyle(ChatFormatting.RED), true);
        }
    }

    private static void handleDismountPress(Player player) {
        if (player.getVehicle() instanceof VehicleEntity vehicle) {
            if ((!vehicle.onGround() || vehicle.getDeltaMovement().length() >= 0.1) && ClientEventHandler.dismountCountdown <= 0) {
                if (vehicle.allowEjection(vehicle.getSeatIndex(player))) {
                    player.displayClientMessage(Component.translatable("tips.superbwarfare.mount.onboard", ModKeyMappings.DISMOUNT.getTranslatedKeyMessage()), true);
                } else {
                    player.displayClientMessage(Component.translatable("mount.onboard", ModKeyMappings.DISMOUNT.getTranslatedKeyMessage()), true);
                }

                ClientEventHandler.dismountCountdown = 20;
                return;
            }
            ClientPlayNetworking.send(new PlayerStopRidingMessage(false));
            ClientEventHandler.stopVehicleReloadSound(player);
        }
    }

    public static void droneLeftClick(ItemStack stack, Player player) {
        var tag = NBTTool.getTag(stack);
        if (stack.is(ModItems.MONITOR) && tag.getBoolean("Using") && tag.getBoolean("Linked")) {
            DroneEntity drone = EntityFindUtil.findDrone(player.level(), tag.getString("LinkedDrone"));
            if (drone != null) {
                boolean lookAtEntity = false;

                Entity lookingEntity = SeekTool.seekLivingEntity(drone, 512, 2 / droneFovLerp);

                BlockHitResult result = player.level().clip(new ClipContext(drone.getEyePosition(), drone.getEyePosition().add(drone.getLookAngle().scale(512)),
                        ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, drone));
                Vec3 pos = result.getLocation();

                if (lookingEntity != null && !player.isShiftKeyDown()) {
                    lookAtEntity = true;
                }

                if (lookAtEntity) {
                    pos = lookingEntity.position();
                }

                ClientPlayNetworking.send(new DroneFireMessage(pos.toVector3f()));
            }
        }
    }
}