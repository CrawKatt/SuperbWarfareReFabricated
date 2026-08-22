package com.atsuishio.superbwarfare.event

import com.atsuishio.superbwarfare.client.overlay.OverlayTraceHandler
import com.atsuishio.superbwarfare.client.screens.LoiterConfigScreen
import com.atsuishio.superbwarfare.client.screens.MissilePosInputScreen
import com.atsuishio.superbwarfare.client.screens.TacticalMapScreen
import com.atsuishio.superbwarfare.client.screens.WeaponEditScreen
import com.atsuishio.superbwarfare.compat.CompatHolder
import com.atsuishio.superbwarfare.compat.clothconfig.ClothConfigHelper
import com.atsuishio.superbwarfare.config.client.ReloadConfig
import com.atsuishio.superbwarfare.config.server.MapConfig
import com.atsuishio.superbwarfare.data.gun.FireMode
import com.atsuishio.superbwarfare.data.gun.GunData
import com.atsuishio.superbwarfare.data.gun.GunProp
import com.atsuishio.superbwarfare.data.gun.SeekType
import com.atsuishio.superbwarfare.data.vehicle.subdata.EngineType
import com.atsuishio.superbwarfare.entity.vehicle.MortarEntity
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import com.atsuishio.superbwarfare.init.*
import com.atsuishio.superbwarfare.item.ItemScreenProvider
import com.atsuishio.superbwarfare.item.trinket.TacticalTerminalItem
import com.atsuishio.superbwarfare.item.gun.GunItem
import com.atsuishio.superbwarfare.mixins.KeyMappingAccessor
import com.atsuishio.superbwarfare.mixins.MinecraftAccessor
import com.atsuishio.superbwarfare.network.message.send.*
import com.atsuishio.superbwarfare.resource.gun.GunResource
import com.atsuishio.superbwarfare.tools.*
import com.mojang.blaze3d.platform.InputConstants
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.ChatFormatting
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.ClipContext
import net.minecraft.world.phys.Vec3
import org.lwjgl.glfw.GLFW
import dev.emi.trinkets.api.TrinketsApi

object ClickEventHandler {
    @JvmField
    var switchZoom: Boolean = false

    @JvmStatic
    fun onButtonReleased(button: Int, action: Int, modifiers: Int) {
        if (notInGame) return
        if (action != InputConstants.RELEASE) return

        syncModMouseConflictState(button, action)

        val player = localPlayer ?: return
        if (player.hasEffect(ModMobEffects.SHOCK)) return

        if (ModKeyMappings.FIRE.matchesMouse(button)) {
            handleWeaponFireRelease()
        }

        if (ModKeyMappings.HOLD_ZOOM.matchesMouse(button)) {
            handleWeaponZoomRelease()
        } else if (ModKeyMappings.SWITCH_ZOOM.matchesMouse(button) && !switchZoom) {
            handleWeaponZoomRelease()
        }
    }

    private fun cancelFireKey(player: Player, stack: ItemStack): Boolean {
        val vehicle = player.vehicle
        return stack.item is GunItem || stack.`is`(ModItems.MONITOR) || stack.`is`(ModItems.LUNGE_MINE)
                || stack.`is`(ModItems.ARTILLERY_INDICATOR) || player.hasEffect(ModMobEffects.SHOCK)
                || (vehicle is VehicleEntity && vehicle.banHand(player))
    }

    private fun cancelZoomKey(player: Player, stack: ItemStack): Boolean {
        val vehicle = player.vehicle
        return stack.item is GunItem || (vehicle is VehicleEntity && vehicle.banHand(player) && !stack.isEdible)
    }

    @JvmStatic
    fun releaseVanillaMouseButton(button: Int) {
        val options = mc.options

        if (options.keyAttack.matchesMouse(button)) {
            options.keyAttack.setDown(false)
        }
        if (options.keyUse.matchesMouse(button)) {
            options.keyUse.setDown(false)
        }
        if (options.keyPickItem.matchesMouse(button)) {
            options.keyPickItem.setDown(false)
        }
    }

    @JvmStatic
    fun forwardVanillaMouseButtonIfNeeded(button: Int, action: Int) {
        if (notInGame || !hasModMouseConflict(button)) {
            return
        }

        val options = mc.options
        forwardVanillaMouseButton(options.keyAttack, button, action)
        forwardVanillaMouseButton(options.keyUse, button, action)
        forwardVanillaMouseButton(options.keyPickItem, button, action)
    }

    private fun hasModMouseConflict(button: Int): Boolean {
        return ModKeyMappings.FIRE.matchesMouse(button)
                || ModKeyMappings.HOLD_ZOOM.matchesMouse(button)
                || ModKeyMappings.SWITCH_ZOOM.matchesMouse(button)
                || ModKeyMappings.FIRE_MODE.matchesMouse(button)
                || ModKeyMappings.MELEE.matchesMouse(button)
                || ModKeyMappings.MARK.matchesMouse(button)
    }

    private fun syncModMouseConflictState(button: Int, action: Int) {
        if (action != InputConstants.PRESS && action != InputConstants.RELEASE) {
            return
        }

        val down = action == InputConstants.PRESS
        syncMouseMapping(ModKeyMappings.MELEE, button, down)
        syncMouseMapping(ModKeyMappings.RELEASE_DECOY, button, down)
    }

    private fun syncMouseMapping(keyMapping: KeyMapping, button: Int, down: Boolean) {
        if (keyMapping.matchesMouse(button)) {
            keyMapping.setDown(down)
        }
    }

    private fun forwardVanillaMouseButton(keyMapping: KeyMapping, button: Int, action: Int) {
        if (!keyMapping.matchesMouse(button) || !isShadowedMouseMapping(keyMapping, button)) {
            return
        }

        if (action == InputConstants.PRESS) {
            keyMapping.setDown(true)
            val accessor = keyMapping as KeyMappingAccessor
            accessor.`superbwarfare$setClickCount`(accessor.`superbwarfare$getClickCount`() + 1)
        } else if (action == InputConstants.RELEASE) {
            keyMapping.setDown(false)
        }
    }

    private fun isShadowedMouseMapping(keyMapping: KeyMapping, button: Int): Boolean {
        val key = InputConstants.Type.MOUSE.getOrCreate(button)
        return KeyMappingAccessor.`superbwarfare$getMap`()[key] != keyMapping
    }

    @JvmStatic
    fun onButtonPressed(button: Int, action: Int, modifiers: Int): Boolean {
        if (notInGame) return false
        if (action != InputConstants.PRESS) return false

        syncModMouseConflictState(button, action)

        val player = localPlayer ?: return false
        if (player.isSpectator) return false

        var canceled = false
        if (player.hasEffect(ModMobEffects.SHOCK)) {
            return true
        }

        val stack = player.mainHandItem

        if (ModKeyMappings.FIRE.matchesMouse(button) && cancelFireKey(player, stack)) {
            canceled = true
        }

        if (ModKeyMappings.HOLD_ZOOM.matchesMouse(button) && cancelZoomKey(player, stack)) {
            canceled = true
        }

        if (button == GLFW.GLFW_MOUSE_BUTTON_MIDDLE) {
            if (stack.`is`(ModItems.ARTILLERY_INDICATOR)) {
                canceled = true
            }
            if (stack.`is`(ModItems.MONITOR) && player.offhandItem.`is`(ModItems.ARTILLERY_INDICATOR)) {
                canceled = true
            }
        }

        if (ModKeyMappings.MARK.matchesMouse(button)) {
            if (stack.`is`(ModItems.ARTILLERY_INDICATOR)) {
                sendPacketToServer(SetFiringParametersMessage)
            }
            if (stack.`is`(ModItems.MONITOR) && player.offhandItem.`is`(ModItems.ARTILLERY_INDICATOR)) {
                droneLeftClick(stack, player)
            }
        }

        if (stack.item is GunItem
            || player.vehicle is VehicleEntity
            || stack.`is`(ModItems.MONITOR)
            || stack.`is`(ModItems.LUNGE_MINE)
            || (stack.`is`(Items.SPYGLASS) && player.isScoping && player.offhandItem.`is`(ModItems.FIRING_PARAMETERS))
            || stack.`is`(ModItems.ARTILLERY_INDICATOR)
        ) {
            if (ModKeyMappings.FIRE.matchesMouse(button)) {
                handleWeaponFirePress(player, stack)
            }

            if (ModKeyMappings.HOLD_ZOOM.matchesMouse(button)) {
                handleWeaponZoomPress(player, stack)
                switchZoom = false
            } else if (ModKeyMappings.SWITCH_ZOOM.matchesMouse(button)) {
                handleWeaponZoomPress(player, stack)
                switchZoom = !switchZoom
            }
        }

        if (ModKeyMappings.FIRE_MODE.matchesMouse(button)) {
            val vehicle = player.vehicle
            if (vehicle is VehicleEntity) {
                val data = vehicle.getGunData(player)
                if (data != null && data.get(GunProp.AMMO_CONSUMER).size > 1) {
                    sendPacketToServer(EditMessage(5, add = true, isVehicle = true))
                }
            } else {
                sendPacketToServer(FireModeMessage(false))
            }
            ClientEventHandler.burstFireAmount = 0
        }
        return canceled
    }

    /**
     * 枪械交互时禁止挥舞手臂
     */
    @JvmStatic
    fun stopSwing(hand: InteractionHand): Boolean {
        val player = localPlayer ?: return false
        if (player.getItemInHand(hand).item is GunItem) {
            return true
        }
        return false
    }

    @JvmStatic
    fun onMouseScrolling(horizontalAmount: Double, verticalAmount: Double): Boolean {
        val player = localPlayer ?: return false
        if (notInGame) return false
        if (player.hasEffect(ModMobEffects.SHOCK)) {
            return false
        }

        val stack = player.mainHandItem
        val scroll = verticalAmount
        val vehicle = player.vehicle
        var canceled = false

        // 按下自由视角键时，为载具调整相机距离
        if (vehicle is VehicleEntity && ModKeyMappings.FREE_CAMERA.isDown()) {
            ClientMouseHandler.custom3pDistance =
                (ClientMouseHandler.custom3pDistance - verticalAmount).coerceIn(-3.0, 8.0)
            return true
        }

        // 未按下shift时，为有武器的载具切换武器
        if (!Screen.hasShiftDown()
            && vehicle is VehicleEntity
            && vehicle.hasWeapon(vehicle.getSeatIndex(player))
            && vehicle.banHand(player)
        ) {
            if (ClientEventHandler.switchVehicleWeaponCooldown <= 0) {
                val index = vehicle.getSeatIndex(player)
                sendPacketToServer(SwitchVehicleWeaponMessage(index, -scroll, true))
                ClientEventHandler.switchVehicleWeaponCooldown = 3
            }
            canceled = true
        }

        if (stack.item is GunItem && ClientEventHandler.zoom) {
            val data = GunData.from(stack)
            if (data.canSwitchScope()) {
                sendPacketToServer(SwitchScopeMessage(scroll))
            } else if (data.canAdjustZoom() || stack.`is`(ModItems.MINIGUN)) {
                AdjustZoomFovMessage.apply(player, scroll, false)
                sendPacketToServer(AdjustZoomFovMessage(scroll))
            }
            canceled = true
        }

        val tag = NBTTool.getTag(stack)

        if (stack.`is`(ModItems.MONITOR) && tag.getBoolean("Using")
            && tag.getBoolean("Linked")
        ) {
            ClientEventHandler.droneFov = (ClientEventHandler.droneFov + 0.4 * scroll).coerceIn(1.0, 6.0)
            canceled = true
        }

        if (player.isUsingItem && player.useItem.`is`(ModItems.ARTILLERY_INDICATOR)) {
            ClientEventHandler.artilleryIndicatorCustomZoom =
                (ClientEventHandler.artilleryIndicatorCustomZoom + 0.4 * scroll).coerceIn(-2.0, 6.0)
            canceled = true
        }

        val looking = OverlayTraceHandler.playerReachEntity
        if (looking is MortarEntity && player.isShiftKeyDown) {
            sendPacketToServer(AdjustMortarAngleMessage(scroll))
            canceled = true
        }
        return canceled
    }

    @JvmStatic
    fun onKeyPressed(keyCode: Int, scanCode: Int, action: Int, modifiers: Int): Boolean {
        if (notInGame) return false

        val player = localPlayer ?: return false

        val key = keyCode
        if (key < 0) return false

        syncModKeyConflictState(key, scanCode, action)

        if (player.isSpectator) return false
        if (player.hasEffect(ModMobEffects.SHOCK)) return false

        val stack = player.mainHandItem
        val vehicle = player.vehicle

        if (action == GLFW.GLFW_PRESS) {
            if (ModKeyMappings.ACTIVE_THERMAL_IMAGING.matches(key, scanCode)) {
                if (vehicle is VehicleEntity) {
                    val index = vehicle.getSeatIndex(player)
                    val seat = vehicle.computed().seats().getOrNull(index) ?: return false
                    if (seat.hasThermalImaging) {
                        ClientEventHandler.activeThermalImaging = !ClientEventHandler.activeThermalImaging
                        if (ClientEventHandler.activeThermalImaging) {
                            player.playSound(ModSounds.CANNON_ZOOM_IN)
                        } else {
                            player.playSound(ModSounds.CANNON_ZOOM_OUT)
                        }
                        return true
                    }
                    return true
                }

                TrinketsApi.getTrinketComponent(player).ifPresent {
                    if (it.isEquipped(ModItems.THERMAL_IMAGING_GOGGLES)) {
                        ClientEventHandler.activeThermalImaging = !ClientEventHandler.activeThermalImaging
                        if (ClientEventHandler.activeThermalImaging) {
                            player.playSound(ModSounds.NIGHT_VISION_ACTIVATE)
                        } else {
                            player.playSound(ModSounds.CANNON_ZOOM_OUT)
                        }
                    }
                }
                return true
            }

            if (key == ModKeyMappings.DISMOUNT.key.value) {
                handleDismountPress(player)
            }

            if (key == ModKeyMappings.TOGGLE_TACTICAL_MAP.key.value && MapConfig.ENABLE_TACTICAL_MAP.get()) {
                if (TacticalTerminalItem.isTerminalEquipped(player) && mc.screen == null) {
                    mc.setScreen(TacticalMapScreen())
                }
            }

            if (key == Minecraft.getInstance().options.keyJump.key.value) {
                handleDoubleJump(player)
                handleParachute()
            }

            if (ModKeyMappings.CONFIG.matches(key, scanCode)) {
                handleConfigScreen(player)
            }
            if (key == ModKeyMappings.RELOAD.key.value) {
                ClientEventHandler.burstFireAmount = 0
                ClientEventHandler.isEditing = false
                ClientEventHandler.seekingTime = 0
                ClientEventHandler.lockOn = false
                ClientEventHandler.lockingEntity = null
                ClientEventHandler.seekingEntity = null
                ClientEventHandler.lockingPos = null
                sendPacketToServer(ReloadMessage)
            }
            if (key == ModKeyMappings.FIRE_MODE.key.value || key == ModKeyMappings.CHANGE_FIRE_MODE_BACKWARD.key.value) {
                sendPacketToServer(FireModeMessage(false))
                ClientEventHandler.burstFireAmount = 0
            }
            if (key == ModKeyMappings.CHANGE_FIRE_MODE_FORWARD.key.value) {
                sendPacketToServer(FireModeMessage(true))
                ClientEventHandler.burstFireAmount = 0
            }
            if (key == ModKeyMappings.INTERACT.key.value) {
                if (stack.item is GunItem) {
                    (mc as MinecraftAccessor).`superbwarfare$startUseItem`()
                } else if (stack.`is`(ModItems.MONITOR)) {
                    sendPacketToServer(InteractMessage)
                }
            }

            // 玩家手持枪械时，处理卸弹/切换弹种
            if (stack.item is GunItem) {
                val data = GunData.from(stack)
                if (key == ModKeyMappings.UNLOAD.key.value) {
                    if (data.useBackpackAmmo() || data.ammo.get() + data.virtualAmmo.get() <= 0) return true
                    sendPacketToServer(UnloadMessage)
                    ClientEventHandler.burstFireAmount = 0
                }
                if (data.get(GunProp.AMMO_CONSUMER).size > 1) {
                    if (key == ModKeyMappings.CHANGE_AMMO_FORWARD.key.value) {
                        sendPacketToServer(EditMessage(5, add = false, isVehicle = false))
                        ClientEventHandler.burstFireAmount = 0
                    }
                    if (key == ModKeyMappings.CHANGE_AMMO_BACKWARD.key.value) {
                        sendPacketToServer(EditMessage(5, add = true, isVehicle = false))
                        ClientEventHandler.burstFireAmount = 0
                    }
                }
            }

            // 玩家位于载具上时，处理切换弹种
            if (vehicle is VehicleEntity) {
                val data = vehicle.getGunData(player)
                if (data != null && data.get(GunProp.AMMO_CONSUMER).size > 1) {
                    if (key == ModKeyMappings.CHANGE_AMMO_FORWARD.key.value) {
                        sendPacketToServer(EditMessage(5, add = false, isVehicle = true))
                        ClientEventHandler.burstFireAmount = 0
                    }
                    if (key == ModKeyMappings.CHANGE_AMMO_BACKWARD.key.value || key == ModKeyMappings.FIRE_MODE.key.value) {
                        sendPacketToServer(EditMessage(5, add = true, isVehicle = true))
                        ClientEventHandler.burstFireAmount = 0
                    }
                }

                if (key == ModKeyMappings.LOITER_CONFIG.key.value) {
                    if (vehicle.computed().engineType == EngineType.AIRCRAFT && mc.screen == null) {
                        mc.setScreen(LoiterConfigScreen(vehicle))
                    }
                }
            }

            if (key == ModKeyMappings.EDIT_MODE.key.value) {
                if (vehicle is VehicleEntity) {
                    val data = vehicle.getGunData(player)
                    if (data != null) {
                        val input = data.get(GunProp.SEEK_WEAPON_INFO)?.inputBlockPos
                        if (input == true) {
                            Minecraft.getInstance().setScreen(MissilePosInputScreen())
                            return true
                        }
                    }
                }

                val item = stack.item
                if (item is ItemScreenProvider) {
                    val screen = item.getItemScreen(stack, player, InteractionHand.MAIN_HAND)
                    if (screen != null) {
                        Minecraft.getInstance().setScreen(screen)
                        if (screen is WeaponEditScreen) {
                            ClientEventHandler.onOpenEditScreen()
                        }
                        return true
                    }
                }

                val offHand = player.offhandItem
                val offHandItem = offHand.item
                if (offHandItem is ItemScreenProvider) {
                    val screen = offHandItem.getItemScreen(offHand, player, InteractionHand.OFF_HAND)
                    if (screen != null) {
                        Minecraft.getInstance().setScreen(screen)
                        return true
                    }
                }
            }

            if (key == ModKeyMappings.BREATH.key.value && !ClientEventHandler.exhaustion && ClientEventHandler.zoom) {
                ClientEventHandler.breath = true
            }
            if (key == ModKeyMappings.SENSITIVITY_INCREASE.key.value) {
                sendPacketToServer(SensitivityMessage(true))
            }
            if (key == ModKeyMappings.SENSITIVITY_REDUCE.key.value) {
                sendPacketToServer(SensitivityMessage(false))
            }

            if (stack.item is GunItem
                || (vehicle is VehicleEntity && vehicle.firstPassenger == player)
                || stack.`is`(ModItems.MONITOR)
                || (stack.`is`(Items.SPYGLASS) && player.isScoping && player.offhandItem.`is`(ModItems.FIRING_PARAMETERS))
                || (stack.`is`(ModItems.ARTILLERY_INDICATOR))
            ) {
                if (key == ModKeyMappings.FIRE.key.value) {
                    handleWeaponFirePress(player, stack)
                }

                if (key == ModKeyMappings.HOLD_ZOOM.key.value) {
                    handleWeaponZoomPress(player, stack)
                    switchZoom = false
                    return true
                }

                if (key == ModKeyMappings.SWITCH_ZOOM.key.value) {
                    handleWeaponZoomPress(player, stack)
                    switchZoom = !switchZoom
                }
            }

            if (key == ModKeyMappings.MARK.key.value) {
                if (stack.`is`(ModItems.ARTILLERY_INDICATOR)) {
                    sendPacketToServer(SetFiringParametersMessage)
                }
                if (stack.`is`(ModItems.MONITOR) && player.offhandItem.`is`(ModItems.ARTILLERY_INDICATOR)) {
                    droneLeftClick(stack, player)
                }
            }
        } else {
            if (key == ModKeyMappings.FIRE.key.value) {
                handleWeaponFireRelease()
            }

            if (key == ModKeyMappings.HOLD_ZOOM.key.value) {
                handleWeaponZoomRelease()
            } else if (key == ModKeyMappings.SWITCH_ZOOM.key.value && !switchZoom) {
                handleWeaponZoomRelease()
            }

            if (action == GLFW.GLFW_RELEASE) {
                if (key == ModKeyMappings.BREATH.key.value) {
                    ClientEventHandler.breath = false
                    return true
                }
            }
        }
        return false
    }

    private fun syncModKeyConflictState(key: Int, scanCode: Int, action: Int) {
        if (action != GLFW.GLFW_PRESS && action != GLFW.GLFW_RELEASE) {
            return
        }

        val down = action == GLFW.GLFW_PRESS
        syncMovementKeyConflictState(key, scanCode, down)
        syncKeyMapping(ModKeyMappings.DISMOUNT, key, scanCode, down)
        syncKeyMapping(ModKeyMappings.MELEE, key, scanCode, down)
        syncKeyMapping(ModKeyMappings.RELEASE_DECOY, key, scanCode, down)
    }

    private fun syncMovementKeyConflictState(key: Int, scanCode: Int, down: Boolean) {
        val options = mc.options

        syncKeyMapping(options.keyUp, key, scanCode, down)
        syncKeyMapping(options.keyDown, key, scanCode, down)
        syncKeyMapping(options.keyLeft, key, scanCode, down)
        syncKeyMapping(options.keyRight, key, scanCode, down)
        syncKeyMapping(options.keyJump, key, scanCode, down)
        syncKeyMapping(options.keyShift, key, scanCode, down)
        syncKeyMapping(options.keySprint, key, scanCode, down)

        syncKeyMapping(ModKeyMappings.MOVE_FORWARD, key, scanCode, down)
        syncKeyMapping(ModKeyMappings.MOVE_BACKWARD, key, scanCode, down)
        syncKeyMapping(ModKeyMappings.MOVE_LEFT, key, scanCode, down)
        syncKeyMapping(ModKeyMappings.MOVE_RIGHT, key, scanCode, down)
        syncKeyMapping(ModKeyMappings.MOVE_SPACE, key, scanCode, down)
        syncKeyMapping(ModKeyMappings.MOVE_SHIFT, key, scanCode, down)
        syncKeyMapping(ModKeyMappings.MOVE_CTRL, key, scanCode, down)
    }

    private fun syncKeyMapping(keyMapping: KeyMapping, key: Int, scanCode: Int, down: Boolean) {
        if (keyMapping.matches(key, scanCode)) {
            keyMapping.setDown(down)
        }
    }

    fun handleWeaponFirePress(player: Player, stack: ItemStack) {
        ClientEventHandler.isEditing = false

        if (player.hasEffect(ModMobEffects.SHOCK)) return

        val vehicle = player.vehicle

        if (vehicle is VehicleEntity && vehicle.banHand(player)) {
            if (vehicle.hasWeapon(vehicle.getSeatIndex(player))) {
                ClientEventHandler.holdFireVehicle = true
            }
            return
        }

        if (stack.`is`(ModItems.ARTILLERY_INDICATOR)) {
            ClientEventHandler.holdingFireKey = true
        }

        if (stack.`is`(Items.SPYGLASS) && player.isScoping && player.offhandItem.`is`(ModItems.FIRING_PARAMETERS)) {
            sendPacketToServer(SetFiringParametersMessage)
        }

        if (stack.`is`(ModItems.MONITOR)) {
            if (player.offhandItem.`is`(ModItems.ARTILLERY_INDICATOR)) {
                ClientEventHandler.holdingFireKey = true
            } else {
                droneLeftClick(stack, player)
            }
        }

        if (stack.`is`(ModItems.LUNGE_MINE)) {
            ClientEventHandler.usingLunge = true
        }

        val item = stack.item
        if (item is GunItem
            && ClientEventHandler.clientTimer.progress == 0L
            && !notInGame
        ) {
            val data = GunData.from(stack)
            val resource = GunResource.compute(stack)

            // TODO 整合特殊处理
            if (!(stack.`is`(ModItems.BOCEK))) {
                if (!data.meleeOnly()) {
                    // 普通枪（？）
                    if (stack.`is`(ModItems.QL_1031) && data.selectedFireModeInfo().name == "Hold"
                        && item.canShoot(data, player)
                    ) {
                        player.playSound(ModSounds.QL_1031_CHARGE, 1f, 1f)
                        ClientEventHandler.shouldPlayDischargeSound = true
                    }

                    val triggerSound = resource.triggerSound
                    if (triggerSound != null && !data.meleeOnly()) {
                        player.playSound(triggerSound, 1f, 1f)
                    }
                }
            } else {
                // 波塞克特殊处理
                ClientEventHandler.bowPower = 0.0
                ClientEventHandler.holdingFireKey = true
                player.isSprinting = false
                if (data.hasEnoughAmmoToShoot(player)) {
                    return
                }
            }

            if (!data.useBackpackAmmo() && !data.meleeOnly() && !data.hasEnoughAmmoToShoot(player) && data.reload.time() == 0) {
                if (ReloadConfig.LEFT_CLICK_RELOAD.get()) {
                    sendPacketToServer(ReloadMessage)
                    ClientEventHandler.burstFireAmount = 0
                    ClientEventHandler.seekingTime = 0
                    ClientEventHandler.lockOn = false
                    ClientEventHandler.lockingEntity = null
                    ClientEventHandler.seekingEntity = null
                    ClientEventHandler.lockingPos = null
                }
            } else {
                sendPacketToServer(FireKeyMessage(0, ClientEventHandler.bowPower, ClientEventHandler.zoom))
                if (ClientEventHandler.drawTime < 0.01) {
                    val fireMode = data.selectedFireModeInfo().mode
                    if (fireMode == FireMode.BURST) {
                        if (ClientEventHandler.burstFireAmount == 0) {
                            ClientEventHandler.noSprintTicks = 8f
                            player.isSprinting = false
                            ClientEventHandler.burstFireAmount = data.get(GunProp.BURST_AMOUNT)
                        }
                    } else if (fireMode == FireMode.SEMI) {
                        if (ClientEventHandler.burstFireAmount == 0) {
                            ClientEventHandler.noSprintTicks = 3f
                            player.isSprinting = false
                            ClientEventHandler.burstFireAmount = 1
                        }
                    }

                    ClientEventHandler.holdingFireKey = true
                    player.isSprinting = false
                }
            }
        }
    }

    fun handleWeaponFireRelease() {
        sendPacketToServer(FireKeyMessage(1, ClientEventHandler.bowPower, ClientEventHandler.zoom))
        ClientEventHandler.bowPull = false
        ClientEventHandler.holdingFireKey = false
        ClientEventHandler.holdFireVehicle = false
        ClientEventHandler.isEditing = false
        ClientEventHandler.customRpm = 0

        val player = localPlayer ?: return
        if (player.isSpectator) return

        val stack = player.mainHandItem

        if (stack.`is`(ModItems.BOCEK)) {
            sendPacketToServer(ReloadMessage)
        }

        if (stack.item is GunItem) {
            val data = GunData.from(stack)
            val fireMode = data.selectedFireModeInfo().mode

            if (fireMode != FireMode.BURST) {
                ClientEventHandler.burstFireAmount = 0
            }

            if (data.get(GunProp.SEEK_TYPE) == SeekType.HOLD_FIRE) {
                ClientEventHandler.stopWeaponSeekSound(Minecraft.getInstance().player)
            }
        }
    }

    fun handleWeaponZoomPress(player: Player, stack: ItemStack) {
        sendPacketToServer(ZoomMessage(0))

        ClientEventHandler.isEditing = false

        val vehicle = player.vehicle
        if (vehicle is VehicleEntity && vehicle.hasWeapon(vehicle.getSeatIndex(player)) && vehicle.banHand(player)) {
            val data = vehicle.getGunData(player)
            if (data != null) {
                val input = data.get(GunProp.SEEK_WEAPON_INFO)?.inputBlockPos
                if (input == true) {
                    Minecraft.getInstance().setScreen(MissilePosInputScreen())
                    return
                }
            }
            ClientEventHandler.zoomVehicle = true
            return
        }

        if (stack.item !is GunItem) return
        if (!GunResource.compute(stack).canZoom) return

        val data = GunData.from(stack)
        ClientEventHandler.zoom = true

        val level = data.perk.getLevel(ModPerks.INTELLIGENT_CHIP)
        if (level > 0) {
            if (ClientEventHandler.lockedEntity == null) {
                ClientEventHandler.lockedEntity =
                    if (data.perk.has(ModPerks.PHASE_PENETRATING_BULLET) || data.perk.has(ModPerks.BEAST_BULLET)) {
                        SeekTool.seekEntityThroughWall(player, 32 + 8 * (level - 1).toDouble(), 20.0)
                    } else {
                        SeekTool.seekLivingEntity(player, 32 + 8 * (level - 1).toDouble(), 20.0)
                    }
            }
        }
    }

    fun handleWeaponZoomRelease() {
        sendPacketToServer(ZoomMessage(1))
        ClientEventHandler.zoom = false
        ClientEventHandler.zoomVehicle = false
        ClientEventHandler.lockedEntity = null
        ClientEventHandler.stopWeaponSeekSound(Minecraft.getInstance().player)
        ClientEventHandler.breath = false
    }

    fun handleDoubleJump(player: Player) {
        val level = player.level()
        val x = player.x
        val y = player.y
        val z = player.z

        if (!level.isLoaded(player.blockPosition())) {
            return
        }

        if (ClientEventHandler.canDoubleJump) {
            player.deltaMovement = Vec3(player.lookAngle.x, 0.8, player.lookAngle.z)
            level.playLocalSound(x, y, z, ModSounds.DOUBLE_JUMP, SoundSource.BLOCKS, 1f, 1f, false)
            sendPacketToServer(DoubleJumpMessage)
            ClientEventHandler.canDoubleJump = false
        }
    }

    fun handleParachute() {
        sendPacketToServer(ParachuteMessage)
    }

    fun handleConfigScreen(player: Player) {
        if (FabricLoader.getInstance().isModLoaded(CompatHolder.CLOTH_CONFIG)) {
            CompatHolder.hasMod(
                CompatHolder.CLOTH_CONFIG
            ) { mc.setScreen(ClothConfigHelper.getConfigScreen(null)) }
        } else {
            player.displayClientMessage(
                Component.translatable("tips.superbwarfare.no_cloth_config").withStyle(ChatFormatting.RED), true
            )
        }
    }

    private fun handleDismountPress(player: Player) {
        val vehicle = player.vehicle as? VehicleEntity ?: return

        if ((!vehicle.onGround() || vehicle.deltaMovement.length() >= 0.1) && ClientEventHandler.dismountCountdown <= 0) {
            if (vehicle.allowEjection(vehicle.getSeatIndex(player))) {
                player.displayClientMessage(
                    Component.translatable(
                        "tips.superbwarfare.mount.onboard",
                        ModKeyMappings.DISMOUNT.translatedKeyMessage
                    ), true
                )
            } else {
                player.displayClientMessage(
                    Component.translatable(
                        "mount.onboard",
                        ModKeyMappings.DISMOUNT.translatedKeyMessage
                    ), true
                )
            }

            ClientEventHandler.dismountCountdown = 20
            return
        }
        sendPacketToServer(PlayerStopRidingMessage(false))
        ClientEventHandler.stopVehicleReloadSound(player)
    }

    fun droneLeftClick(stack: ItemStack, player: Player) {
        if (stack.`is`(ModItems.MONITOR) && stack.getOrCreateTag().getBoolean("Using")
            && stack.getOrCreateTag().getBoolean("Linked")
        ) {
            val drone =
                EntityFindUtil.findDrone(player.level(), stack.getOrCreateTag().getString("LinkedDrone")) ?: return
            val lookingEntity = SeekTool.seekLivingEntity(drone, 512.0, 2 / ClientEventHandler.droneFovLerp)

            val result = player.level().clip(
                ClipContext(
                    drone.eyePosition,
                    drone.eyePosition.add(drone.lookAngle.scale(512.0)),
                    ClipContext.Block.OUTLINE,
                    ClipContext.Fluid.NONE,
                    drone
                )
            )

            var pos = result.location
            if (lookingEntity != null && !player.isShiftKeyDown) {
                pos = lookingEntity.position()
            }

            sendPacketToServer(DroneFireMessage(pos.toVector3f()))
        }
    }
}
