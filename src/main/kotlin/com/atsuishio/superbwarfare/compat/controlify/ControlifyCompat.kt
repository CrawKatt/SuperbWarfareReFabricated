package com.atsuishio.superbwarfare.compat.controlify

import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import com.atsuishio.superbwarfare.event.ClickEventHandler
import com.atsuishio.superbwarfare.event.ClientEventHandler
import com.atsuishio.superbwarfare.event.ClientMouseHandler
import com.atsuishio.superbwarfare.init.ModKeyMappings
import com.atsuishio.superbwarfare.init.ModMobEffects
import com.atsuishio.superbwarfare.item.gun.GunItem
import com.atsuishio.superbwarfare.network.message.send.ChangeVehicleSeatMessage
import com.atsuishio.superbwarfare.tools.*
import dev.isxander.controlify.api.ControlifyApi
import dev.isxander.controlify.api.bind.ControlifyBindApi
import dev.isxander.controlify.api.bind.InputBindingSupplier
import dev.isxander.controlify.api.entrypoint.ControlifyEntrypoint
import dev.isxander.controlify.api.entrypoint.InitContext
import dev.isxander.controlify.api.entrypoint.PreInitContext
import dev.isxander.controlify.api.event.ControlifyEvents
import dev.isxander.controlify.api.ingameinput.LookInputModifier
import dev.isxander.controlify.bindings.BindContext
import dev.isxander.controlify.bindings.ControlifyBindings
import dev.isxander.controlify.bindings.RadialIcons
import dev.isxander.controlify.controller.ControllerEntity
import net.minecraft.client.CameraType
import net.minecraft.client.KeyMapping
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.phys.Vec2
import net.minecraftforge.client.event.InputEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.TickEvent
import org.lwjgl.glfw.GLFW
import kotlin.math.abs

class ControlifyCompat : ControlifyEntrypoint {
    private val bindings = linkedMapOf<KeyMapping, InputBindingSupplier>()
    private val scrollBindings = linkedMapOf<Double, InputBindingSupplier>()
    private lateinit var vehicleReload: InputBindingSupplier
    private var controller: ControllerEntity? = null
    private var toggledZoom = false

    override fun onControlifyPreInit(context: PreInitContext) {
        val api = ControlifyBindApi.get()
        val vehicleContext = BindContext(ResourceLocation("superbwarfare", "vehicle")) {
            !notInGame && localPlayer?.vehicle is VehicleEntity
        }

        val gunContext = BindContext(ResourceLocation("superbwarfare", "gun")) {
            !notInGame && localPlayer?.vehicle !is VehicleEntity &&
                    localPlayer?.let { GunItem.isHeldWeapon(it.mainHandItem) } == true
        }

        api.registerBindContext(vehicleContext)
        api.registerBindContext(gunContext)

        val vehicleKeys = setOf(
            ModKeyMappings.MOVE_FORWARD, ModKeyMappings.MOVE_BACKWARD,
            ModKeyMappings.MOVE_LEFT, ModKeyMappings.MOVE_RIGHT,
            ModKeyMappings.MOVE_SPACE, ModKeyMappings.MOVE_SHIFT, ModKeyMappings.MOVE_CTRL,
            ModKeyMappings.DISMOUNT, ModKeyMappings.CHANGE_SEAT, ModKeyMappings.FREE_CAMERA,
            ModKeyMappings.RELEASE_DECOY, ModKeyMappings.VEHICLE_SEEK,
            ModKeyMappings.UNLOAD_PASSENGERS, ModKeyMappings.DISCONNECT_TOWING, ModKeyMappings.LOITER_CONFIG
        )

        for (key in ModKeyMappings.keyMappings) {
            val bindContext = when (key) {
                in vehicleKeys -> vehicleContext
                ModKeyMappings.RELOAD -> gunContext
                else -> BindContext.IN_GAME
            }
            bindings[key] = api.registerBinding { builder ->
                builder.id("superbwarfare", key.name.removePrefix("key.superbwarfare."))
                    .name(Component.translatable(key.name))
                    .category(Component.translatable(ModKeyMappings.CATEGORY))
                    .allowedContexts(bindContext)
                    .addKeyCorrelation(key)
                    .radialCandidate(RadialIcons.EMPTY)
            }
        }

        for ((name, scroll) in listOf("next_weapon" to -1.0, "previous_weapon" to 1.0)) {
            scrollBindings[scroll] = api.registerBinding { builder ->
                builder.id("superbwarfare", name)
                    .category(Component.translatable(ModKeyMappings.CATEGORY))
                    .allowedContexts(BindContext.IN_GAME)
                    .radialCandidate(RadialIcons.EMPTY)
            }
        }

        vehicleReload = api.registerBinding { builder ->
            builder.id("superbwarfare", "vehicle_reload")
                .category(Component.translatable(ModKeyMappings.CATEGORY))
                .allowedContexts(vehicleContext)
                .radialCandidate(RadialIcons.EMPTY)
        }

        ControlifyEvents.ACTIVE_CONTROLLER_TICKED.register { updateInput(it.controller()) }
        ControlifyEvents.LOOK_INPUT_MODIFIER.register(::modifyLook)
        ControlifyEvents.CONTROLLER_DISCONNECTED.register {
            if (it.controller() === controller) resetInput()
        }

        ControlifyEvents.INPUT_MODE_CHANGED.register {
            if (!it.mode().isController) resetInput()
        }

        MinecraftForge.EVENT_BUS.addListener<TickEvent.ClientTickEvent> {
            if (it.phase == TickEvent.Phase.START && !canInput()) resetInput()
        }

        MinecraftForge.EVENT_BUS.addListener<InputEvent.InteractionKeyMappingTriggered> {
            val player = localPlayer
            val current = ControlifyApi.get().currentController.orElse(null)
            if (!notInGame && player != null && !player.isSpectator &&
                ControlifyApi.get().currentInputMode().isController && current != null &&
                ((it.isAttack && ControlifyBindings.ATTACK.on(current).digitalNow() &&
                        ClickEventHandler.cancelFireKey(player, player.mainHandItem)) ||
                        (it.isUseItem && ControlifyBindings.USE.on(current).digitalNow() &&
                                ClickEventHandler.cancelZoomKey(player, player.mainHandItem)))
            ) {
                it.isCanceled = true
                it.setSwingHand(false)
            }
        }
    }

    override fun onControlifyInit(context: InitContext) {}

    override fun onControllersDiscovered(controlify: ControlifyApi) {}

    private fun canInput(): Boolean {
        val player = localPlayer ?: return false
        return !notInGame && !player.isSpectator && !player.hasEffect(ModMobEffects.SHOCK.get()) &&
                ControlifyApi.get().currentInputMode().isController &&
                ControlifyApi.get().currentController.isPresent
    }

    private fun updateInput(current: ControllerEntity) {
        if (!canInput()) {
            resetInput()
            return
        }

        if (controller !== current) resetInput()
        controller = current

        val player = localPlayer ?: return
        val vehicle = player.vehicle

        for ((key, supplier) in bindings) {
            val down = supplier.on(current).digitalNow()
            if (!ModKeyMappings.updateControllerKey(key, down)) continue
            if (key == ModKeyMappings.CHANGE_SEAT) {
                if (down) changeSeat()
            } else if (down && vehicle is VehicleEntity && vehicle.firstPassenger != player &&
                (key == ModKeyMappings.FIRE || key == ModKeyMappings.HOLD_ZOOM || key == ModKeyMappings.SWITCH_ZOOM)
            ) {
                if (key == ModKeyMappings.FIRE) {
                    ClickEventHandler.handleWeaponFirePress(player, player.mainHandItem)
                } else {
                    ClickEventHandler.handleWeaponUsePress(player, player.mainHandItem)
                    ClickEventHandler.switchZoom =
                        key == ModKeyMappings.SWITCH_ZOOM && !ClickEventHandler.switchZoom
                }
            } else {
                ClickEventHandler.handleKeyInput(key, if (down) GLFW.GLFW_PRESS else GLFW.GLFW_RELEASE)
            }

            if (key == ModKeyMappings.SWITCH_ZOOM) toggledZoom = ClickEventHandler.switchZoom
        }

        for ((scroll, supplier) in scrollBindings) {
            if (supplier.on(current).justPressed()) ClickEventHandler.handleScroll(scroll, shiftDown = false)
        }

        if (vehicleReload.on(current).justPressed()) {
            ClickEventHandler.handleKeyInput(ModKeyMappings.RELOAD, GLFW.GLFW_PRESS)
        }

        if (ControlifyBindings.JUMP.on(current).justPressed()) {
            ClickEventHandler.handleKeyInput(mc.options.keyJump, GLFW.GLFW_PRESS)
        }

        if (ControlifyBindings.SNEAK.on(current).justPressed()) {
            ClickEventHandler.handleKeyInput(mc.options.keyShift, GLFW.GLFW_PRESS)
        }

        if (!canInput()) resetInput()
    }

    private fun resetInput() {
        ClientMouseHandler.controllerMovement = Vec2.ZERO
        for (key in bindings.keys) {
            if (!ModKeyMappings.updateControllerKey(key, false)) continue
            when (key) {
                ModKeyMappings.FIRE -> ClickEventHandler.cancelWeaponFire()
                ModKeyMappings.HOLD_ZOOM -> ClickEventHandler.handleWeaponUseRelease()
                ModKeyMappings.BREATH -> ClientEventHandler.breath = false
            }
        }

        if (toggledZoom) {
            ClickEventHandler.switchZoom = false
            ClickEventHandler.handleWeaponUseRelease()
            toggledZoom = false
        }
        controller = null
    }

    private fun changeSeat() {
        val player = localPlayer ?: return
        val vehicle = player.vehicle as? VehicleEntity ?: return
        val current = vehicle.getSeatIndex(player)

        for (offset in 1 until vehicle.maxPassengers) {
            val index = (current + offset) % vehicle.maxPassengers
            if (vehicle.getNthEntity(index) != null) continue

            sendPacketToServer(ChangeVehicleSeatMessage(index))
            vehicle.changeSeat(player, index)
            return
        }
    }

    private fun modifyLook(event: LookInputModifier) {
        if (!canInput()) {
            ClientMouseHandler.controllerMovement = Vec2.ZERO
            val player = localPlayer
            if (player != null && !player.isSpectator && player.hasEffect(ModMobEffects.SHOCK.get())) {
                event.lookInput().zero()
            }
            return
        }

        val look = event.lookInput()
        // Entity.turn uses 0.15 degrees per mouse delta; aircraft controls consume those deltas.
        ClientMouseHandler.controllerMovement = Vec2(look.x / 0.15f, look.y / 0.15f)
        look.mul(ClientMouseHandler.changeSensitivity(1.0).toFloat())
        look.y *= ClientMouseHandler.invertY()

        val vehicle = localPlayer?.vehicle as? VehicleEntity ?: return
        if (mc.options.cameraType != CameraType.FIRST_PERSON) return

        val roll = vehicle.roll
        val weight = abs(roll) / 90f
        val direction = if (roll < 0) 1f else -1f

        val bank = direction * (if (abs(roll) > 90) 1 - (abs(roll) - 90) / 90 else 1f)
        val x = look.x

        look.x = (1 - weight) * x + weight * look.y * bank
        look.y = (1 - weight) * look.y - weight * x * direction
    }
}
