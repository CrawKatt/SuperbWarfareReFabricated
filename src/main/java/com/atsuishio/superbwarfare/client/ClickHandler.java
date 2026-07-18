package com.atsuishio.superbwarfare.client;

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
import com.atsuishio.superbwarfare.event.custom.InteractionKeyMappingTriggeredCallback;
import com.atsuishio.superbwarfare.event.custom.KeyInputCallback;
import com.atsuishio.superbwarfare.event.custom.MouseButtonCallback;
import com.atsuishio.superbwarfare.event.custom.MouseScrollCallback;
import com.atsuishio.superbwarfare.init.*;
import com.atsuishio.superbwarfare.item.ItemScreenProvider;
import com.atsuishio.superbwarfare.item.gun.GunItem;
import com.atsuishio.superbwarfare.mixins.accessor.KeyMappingAccessor;
import com.atsuishio.superbwarfare.network.NetworkRegistry;
import com.atsuishio.superbwarfare.network.message.send.*;
import com.atsuishio.superbwarfare.resource.gun.GunResource;
import com.atsuishio.superbwarfare.tools.EntityFindUtil;
import com.atsuishio.superbwarfare.tools.SeekTool;
import com.atsuishio.superbwarfare.tools.TraceTool;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
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

    public static void registerEvents() {
        MouseButtonCallback.EVENT.register(event -> {
            onButtonReleased(event);
            onButtonPressed(event);
        });

        MouseScrollCallback.EVENT.register(ClickHandler::onMouseScrolling);
        KeyInputCallback.EVENT.register(ClickHandler::onKeyPressed);
        InteractionKeyMappingTriggeredCallback.EVENT.register(ClickHandler::stopSwing);
    }

    private static boolean notInGame() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return true;
        if (mc.getOverlay() != null) return true;
        if (mc.screen != null) return true;
        if (!mc.mouseHandler.isMouseGrabbed()) return true;
        return !mc.isWindowActive();
    }

    public static void onButtonReleased(MouseButtonCallback.Event event) {
        if (notInGame()) return;
        if (event.getAction() != InputConstants.RELEASE) return;

        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        if (player.hasEffect(ModMobEffects.SHOCK.get())) {
            return;
        }

        int button = event.getButton();
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
        return stack.getItem() instanceof GunItem || stack.is(ModItems.MONITOR.get()) || stack.is(ModItems.LUNGE_MINE.get()) || stack.is(ModItems.ARTILLERY_INDICATOR.get()) || player.hasEffect(ModMobEffects.SHOCK.get())
                || (player.getVehicle() instanceof VehicleEntity vehicle && vehicle.banHand(player));
    }

    private static boolean cancelZoomKey(Player player, ItemStack stack) {
        return stack.getItem() instanceof GunItem
                || (player.getVehicle() instanceof VehicleEntity vehicle && vehicle.banHand(player) && !stack.getItem().isEdible());
    }

    public static void onButtonPressed(MouseButtonCallback.Event event) {
        if (notInGame()) return;
        if (event.getAction() != InputConstants.PRESS) return;

        var mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;
        if (player.isSpectator()) return;

        ItemStack stack = player.getMainHandItem();

        int button = event.getButton();

        if (ModKeyMappings.FIRE.matchesMouse(button)
                && cancelFireKey(player, stack)
        ) {
            event.setCanceled(true);
        }

        if (player.hasEffect(ModMobEffects.SHOCK.get())) {
            return;
        }

        if (ModKeyMappings.HOLD_ZOOM.matchesMouse(button)
                && cancelZoomKey(player, stack)
        ) {
            event.setCanceled(true);
        }

        if (button == GLFW.GLFW_MOUSE_BUTTON_MIDDLE) {
            if (player.hasEffect(ModMobEffects.SHOCK.get())) {
                event.setCanceled(true);
                return;
            }
            if (stack.is(ModItems.ARTILLERY_INDICATOR.get())) {
                event.setCanceled(true);
            }
            if (stack.is(ModItems.MONITOR.get()) && player.getOffhandItem().is(ModItems.ARTILLERY_INDICATOR.get())) {
                event.setCanceled(true);
            }
        }

        if (ModKeyMappings.MARK.matchesMouse(button)) {
            if (stack.is(ModItems.ARTILLERY_INDICATOR.get())) {
                NetworkRegistry.sendToServer(SetFiringParametersMessage.INSTANCE);
            }
            if (stack.is(ModItems.MONITOR.get()) && player.getOffhandItem().is(ModItems.ARTILLERY_INDICATOR.get())) {
                droneLeftClick(stack, player);
            }
        }

        if (stack.getItem() instanceof GunItem
                || stack.is(ModItems.MONITOR.get())
                || stack.is(ModItems.LUNGE_MINE.get())
                || player.getVehicle() instanceof VehicleEntity
                || (stack.is(Items.SPYGLASS) && player.isScoping() && player.getOffhandItem().is(ModItems.FIRING_PARAMETERS.get()))
                || (stack.is(ModItems.ARTILLERY_INDICATOR.get()))
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
            if (player.getVehicle() instanceof VehicleEntity vehicle) {
                var data = vehicle.getGunData(player);
                if (data != null && data.getDefault().getAmmoConsumers().size() > 1) {
                    NetworkRegistry.sendToServer(new EditMessage(5, true, true));
                }
            } else {
                NetworkRegistry.sendToServer(new FireModeMessage(false));
            }
            burstFireAmount = 0;
        }
    }

    // 枪械交互时禁止挥舞手臂
    public static void stopSwing(InteractionKeyMappingTriggeredCallback.Event event) {
        var player = Minecraft.getInstance().player;
        if (player != null && player.getItemInHand(event.getHand()).getItem() instanceof GunItem) {
            event.setSwingHand(false);
        }
    }

    public static void onMouseScrolling(MouseScrollCallback.Event event) {
        Player player = Minecraft.getInstance().player;

        if (notInGame()) return;
        if (player == null) return;

        ItemStack stack = player.getMainHandItem();

        if (player.hasEffect(ModMobEffects.SHOCK.get())) {
            return;
        }

        double scroll = event.getScrollDelta();

        // 按下自由视角键时，为载具调整相机距离
        if (player.getVehicle() instanceof VehicleEntity vehicle && player == vehicle.getFirstPassenger() && ModKeyMappings.FREE_CAMERA.isDown()) {
            ClientMouseHandler.custom3pDistance = Mth.clamp(ClientMouseHandler.custom3pDistance - event.getScrollDelta(), -3, 8);
            event.setCanceled(true);
            return;
        }

        // 未按下shift时，为有武器的载具切换武器
        if (!Screen.hasShiftDown()
                && player.getVehicle() instanceof VehicleEntity vehicle
                && vehicle.hasWeapon(vehicle.getSeatIndex(player))
                && vehicle.banHand(player)
        ) {
            if (switchVehicleWeaponCooldown <= 0) {
                int index = vehicle.getSeatIndex(player);
                NetworkRegistry.sendToServer(new SwitchVehicleWeaponMessage(index, -scroll, true));
                switchVehicleWeaponCooldown = 3;
            }
            event.setCanceled(true);
        }

        if (stack.getItem() instanceof GunItem && ClientEventHandler.zoom) {
            var data = GunData.from(stack);
            if (data.canSwitchScope()) {
                NetworkRegistry.sendToServer(new SwitchScopeMessage(scroll));
            } else if (data.canAdjustZoom() || stack.is(ModItems.MINIGUN.get())) {
                NetworkRegistry.sendToServer(new AdjustZoomFovMessage(scroll));
            }
            event.setCanceled(true);
        }

        if (stack.is(ModItems.MONITOR.get()) && stack.getOrCreateTag().getBoolean("Using") && stack.getOrCreateTag().getBoolean("Linked")) {
            ClientEventHandler.droneFov = Mth.clamp(ClientEventHandler.droneFov + 0.4 * scroll, 1, 6);
            event.setCanceled(true);
        }

        if (player.isUsingItem() && player.getUseItem().is(ModItems.ARTILLERY_INDICATOR.get())) {
            artilleryIndicatorCustomZoom = Mth.clamp(artilleryIndicatorCustomZoom + 0.4 * scroll, -2, 6);
            event.setCanceled(true);
        }

        Entity looking = TraceTool.findLookingEntity(player, 6);
        if (looking instanceof MortarEntity && player.isShiftKeyDown()) {
            NetworkRegistry.sendToServer(new AdjustMortarAngleMessage(scroll));
            event.setCanceled(true);
        }
    }

    public static void onKeyPressed(KeyInputCallback.Event event) {
        if (notInGame()) return;

        var mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;
        int key = event.getKey();
        if (key < 0) return;

        if (ModKeyMappings.DISMOUNT.matches(key, event.getScanCode())) {
            handleDismountPress(player);
        }

        if (player.isSpectator()) return;

        var stack = player.getMainHandItem();

        if (event.getAction() == GLFW.GLFW_PRESS) {
            if (player.hasEffect(ModMobEffects.SHOCK.get())) {
                return;
            }

            if (Minecraft.getInstance().options.keyJump.matches(key, event.getScanCode())) {
                handleDoubleJump(player);
                handleParachute();
            }

            if (ModKeyMappings.CONFIG.matches(key, event.getScanCode()) && Screen.hasAltDown()) {
                handleConfigScreen(player);
            }
            if (ModKeyMappings.RELOAD.matches(key, event.getScanCode())) {
                burstFireAmount = 0;
                isEditing = false;
                seekingTime = 0;
                lockOn = false;
                lockingEntity = null;
                seekingEntity = null;
                lockingPos = null;
                NetworkRegistry.sendToServer(ReloadMessage.INSTANCE);
            }
            if (ModKeyMappings.FIRE_MODE.matches(key, event.getScanCode()) || ModKeyMappings.CHANGE_FIRE_MODE_BACKWARD.matches(key, event.getScanCode())) {
                NetworkRegistry.sendToServer(new FireModeMessage(false));
                burstFireAmount = 0;
            }
            if (ModKeyMappings.CHANGE_FIRE_MODE_FORWARD.matches(key, event.getScanCode())) {
                NetworkRegistry.sendToServer(new FireModeMessage(true));
                burstFireAmount = 0;
            }
            if (ModKeyMappings.INTERACT.matches(key, event.getScanCode())) {
                if (stack.getItem() instanceof GunItem) {
                    KeyMapping.click(((KeyMappingAccessor) mc.options.keyUse).superbwarfare$getKey());
                } else if (stack.is(ModItems.MONITOR.get())) {
                    NetworkRegistry.sendToServer(InteractMessage.INSTANCE);
                }
            }

            // 玩家手持枪械时，处理卸弹/切换弹种
            if (stack.getItem() instanceof GunItem) {
                var data = GunData.from(stack);
                if (ModKeyMappings.UNLOAD.matches(key, event.getScanCode())) {
                    if (data.useBackpackAmmo() || data.ammo.get() + data.virtualAmmo.get() <= 0) return;
                    NetworkRegistry.sendToServer(UnloadMessage.INSTANCE);
                    burstFireAmount = 0;
                }
                if (data.compute().getAmmoConsumers().size() > 1) {
                    if (ModKeyMappings.CHANGE_AMMO_FORWARD.matches(key, event.getScanCode())) {
                        NetworkRegistry.sendToServer(new EditMessage(5, false));
                        burstFireAmount = 0;
                    }
                    if (ModKeyMappings.CHANGE_AMMO_BACKWARD.matches(key, event.getScanCode())) {
                        NetworkRegistry.sendToServer(new EditMessage(5, true));
                        burstFireAmount = 0;
                    }
                }
            }

            // 玩家位于载具上时，处理切换弹种
            if (player.getVehicle() instanceof VehicleEntity vehicle) {
                var data = vehicle.getGunData(player);
                if (data != null && data.getDefault().getAmmoConsumers().size() > 1) {
                    if (ModKeyMappings.CHANGE_AMMO_FORWARD.matches(key, event.getScanCode())) {
                        NetworkRegistry.sendToServer(new EditMessage(5, false, true));
                        burstFireAmount = 0;
                    }
                    if (ModKeyMappings.CHANGE_AMMO_BACKWARD.matches(key, event.getScanCode()) || ModKeyMappings.FIRE_MODE.matches(key, event.getScanCode())) {
                        NetworkRegistry.sendToServer(new EditMessage(5, true, true));
                        burstFireAmount = 0;
                    }
                }
            }

            if (ModKeyMappings.EDIT_MODE.matches(key, event.getScanCode())) {
                if (stack.getItem() instanceof ItemScreenProvider provider) {
                    var screen = provider.getItemScreen(stack, player, InteractionHand.MAIN_HAND);
                    if (screen != null) {
                        Minecraft.getInstance().setScreen(screen);
                        if (screen instanceof WeaponEditScreen) {
                            ClientEventHandler.onOpenEditScreen();
                        }
                        event.setCanceled(true);
                        return;
                    }
                }
                ItemStack offHand = player.getOffhandItem();
                if (offHand.getItem() instanceof ItemScreenProvider provider) {
                    var screen = provider.getItemScreen(offHand, player, InteractionHand.OFF_HAND);
                    if (screen != null) {
                        Minecraft.getInstance().setScreen(screen);
                        event.setCanceled(true);
                        return;
                    }
                }
            }

            if (ModKeyMappings.BREATH.matches(key, event.getScanCode()) && !exhaustion && zoom) {
                breath = true;
            }
            if (ModKeyMappings.SENSITIVITY_INCREASE.matches(key, event.getScanCode())) {
                NetworkRegistry.sendToServer(new SensitivityMessage(true));
            }
            if (ModKeyMappings.SENSITIVITY_REDUCE.matches(key, event.getScanCode())) {
                NetworkRegistry.sendToServer(new SensitivityMessage(false));
            }

            if (stack.getItem() instanceof GunItem
                    || stack.is(ModItems.MONITOR.get())
                    || (player.getVehicle() instanceof VehicleEntity vehicle && vehicle.getFirstPassenger() == player)
                    || (stack.is(Items.SPYGLASS) && player.isScoping() && player.getOffhandItem().is(ModItems.FIRING_PARAMETERS.get()))
                    || (stack.is(ModItems.ARTILLERY_INDICATOR.get()))
            ) {
                if (ModKeyMappings.FIRE.matches(key, event.getScanCode())) {
                    handleWeaponFirePress(player, stack);
                }

                if (ModKeyMappings.HOLD_ZOOM.matches(key, event.getScanCode())) {
                    handleWeaponZoomPress(player, stack);
                    switchZoom = false;
                    return;
                }

                if (ModKeyMappings.SWITCH_ZOOM.matches(key, event.getScanCode())) {
                    handleWeaponZoomPress(player, stack);
                    switchZoom = !switchZoom;
                }
            }

            if (ModKeyMappings.MARK.matches(key, event.getScanCode())) {
                if (stack.is(ModItems.ARTILLERY_INDICATOR.get())) {
                    NetworkRegistry.sendToServer(SetFiringParametersMessage.INSTANCE);
                }
                if (stack.is(ModItems.MONITOR.get()) && player.getOffhandItem().is(ModItems.ARTILLERY_INDICATOR.get())) {
                    droneLeftClick(stack, player);
                }
            }
        } else {
            if (player.hasEffect(ModMobEffects.SHOCK.get())) {
                return;
            }

            if (ModKeyMappings.FIRE.matches(key, event.getScanCode())) {
                handleWeaponFireRelease();
            }

            if (ModKeyMappings.HOLD_ZOOM.matches(key, event.getScanCode())) {
                handleWeaponZoomRelease();
                return;
            }

            if (ModKeyMappings.SWITCH_ZOOM.matches(key, event.getScanCode()) && !switchZoom) {
                handleWeaponZoomRelease();
            }

            if (event.getAction() == GLFW.GLFW_RELEASE) {
                if (ModKeyMappings.BREATH.matches(key, event.getScanCode())) {
                    breath = false;
                }
            }
        }
    }

    public static void handleWeaponFirePress(Player player, ItemStack stack) {
        isEditing = false;

        if (player.hasEffect(ModMobEffects.SHOCK.get())) return;

        if (player.getVehicle() instanceof VehicleEntity vehicle && vehicle.banHand(player)) {
            if (vehicle.hasWeapon(vehicle.getSeatIndex(player))) {
                ClientEventHandler.holdFireVehicle = true;
            }
            return;
        }

        if (stack.is(ModItems.ARTILLERY_INDICATOR.get())) {
            ClientEventHandler.holdingFireKey = true;
        }

        if (stack.is(Items.SPYGLASS) && player.isScoping() && player.getOffhandItem().is(ModItems.FIRING_PARAMETERS.get())) {
            NetworkRegistry.sendToServer(SetFiringParametersMessage.INSTANCE);
        }

        if (stack.is(ModItems.MONITOR.get())) {
            if (player.getOffhandItem().is(ModItems.ARTILLERY_INDICATOR.get())) {
                ClientEventHandler.holdingFireKey = true;
            } else {
                droneLeftClick(stack, player);
            }
        }

        if (stack.is(ModItems.LUNGE_MINE.get())) {
            ClientEventHandler.usingLunge = true;
        }

        if (stack.getItem() instanceof GunItem gunItem
                && clientTimer.getProgress() == 0
                && !notInGame()
        ) {
            var data = GunData.from(stack);
            var resource = GunResource.compute(stack);

            // TODO 整合特殊处理
            if (!(stack.is(ModItems.BOCEK.get()) || stack.is(ModItems.AURELIA_SCEPTRE.get()))) {
                if (!data.meleeOnly()) {
                    // 普通枪（？）
                    if (stack.is(ModItems.QL_1031.get()) && data.selectedFireModeInfo().name.equals("Hold") && gunItem.canShoot(data, player)) {
                        player.playSound(ModSounds.QL_1031_CHARGE.get(), 1, 1);
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

            if (!data.useBackpackAmmo() && !data.meleeOnly() && !data.hasEnoughAmmoToShoot(player) && data.reload.time() == 0) {
                if (ReloadConfig.LEFT_CLICK_RELOAD.get()) {
                    NetworkRegistry.sendToServer(ReloadMessage.INSTANCE);
                    burstFireAmount = 0;
                    seekingTime = 0;
                    lockOn = false;
                    lockingEntity = null;
                    seekingEntity = null;
                    lockingPos = null;
                }
            } else {
                NetworkRegistry.sendToServer(new FireKeyMessage(0, bowPower, zoom));
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
                            ClientEventHandler.burstFireAmount = data.compute().burstAmount;
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
        NetworkRegistry.sendToServer(new FireKeyMessage(1, bowPower, zoom));
        bowPull = false;
        holdingFireKey = false;
        holdFireVehicle = false;
        isEditing = false;
        customRpm = 0;

        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        if (player.isSpectator()) return;

        ItemStack stack = player.getMainHandItem();

        if (stack.is(ModItems.BOCEK.get())) {
            NetworkRegistry.sendToServer(ReloadMessage.INSTANCE);
        }

        if (stack.getItem() instanceof GunItem) {
            var data = GunData.from(stack);
            var computed = data.compute();
            if (computed.seekType == SeekType.HOLD_FIRE) {
                ClientEventHandler.stopWeaponSeekSound(Minecraft.getInstance().player);
            }
        }
    }

    public static void handleWeaponZoomPress(Player player, ItemStack stack) {
        NetworkRegistry.sendToServer(new ZoomMessage(0));

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
                if (data.perk.has(ModPerks.PHASE_PENETRATING_BULLET.get()) || data.perk.has(ModPerks.BEAST_BULLET.get())) {
                    ClientEventHandler.lockedEntity = SeekTool.seekEntityThroughWall(player, 32 + 8 * (level - 1), 20);
                } else {
                    ClientEventHandler.lockedEntity = SeekTool.seekLivingEntity(player, 32 + 8 * (level - 1), 20);
                }
            }
        }
    }

    public static void handleWeaponZoomRelease() {
        NetworkRegistry.sendToServer(new ZoomMessage(1));
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
            level.playLocalSound(x, y, z, ModSounds.DOUBLE_JUMP.get(), SoundSource.BLOCKS, 1, 1, false);
            NetworkRegistry.sendToServer(DoubleJumpMessage.INSTANCE);
            canDoubleJump = false;
        }
    }

    private static void handleParachute() {
        NetworkRegistry.sendToServer(ParachuteMessage.INSTANCE);
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
            NetworkRegistry.sendToServer(new PlayerStopRidingMessage(false));
            ClientEventHandler.stopVehicleReloadSound(player);
        }

    }

    public static void droneLeftClick(ItemStack stack, Player player) {
        if (stack.is(ModItems.MONITOR.get()) && stack.getOrCreateTag().getBoolean("Using") && stack.getOrCreateTag().getBoolean("Linked")) {
            DroneEntity drone = EntityFindUtil.findDrone(player.level(), stack.getOrCreateTag().getString("LinkedDrone"));
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

                NetworkRegistry.sendToServer(new DroneFireMessage(pos.toVector3f()));
            }
        }
    }
}
