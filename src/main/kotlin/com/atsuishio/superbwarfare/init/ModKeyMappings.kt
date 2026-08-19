package com.atsuishio.superbwarfare.init

import com.mojang.blaze3d.platform.InputConstants
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.KeyMapping
import org.lwjgl.glfw.GLFW

object ModKeyMappings {
    const val CATEGORY = "key.categories.superbwarfare"
    private val KEYS = mutableListOf<KeyMapping>()

    @JvmField
    val MOVE_FORWARD = registerKey("move_forward", GLFW.GLFW_KEY_W)

    @JvmField
    val MOVE_BACKWARD = registerKey("move_backward", GLFW.GLFW_KEY_S)

    @JvmField
    val MOVE_LEFT = registerKey("move_left", GLFW.GLFW_KEY_A)

    @JvmField
    val MOVE_RIGHT = registerKey("move_right", GLFW.GLFW_KEY_D)

    @JvmField
    val MOVE_SPACE = registerKey("move_space", GLFW.GLFW_KEY_SPACE)

    @JvmField
    val MOVE_SHIFT = registerKey("move_shift", GLFW.GLFW_KEY_LEFT_SHIFT)

    @JvmField
    val MOVE_CTRL = registerKey("move_ctrl", GLFW.GLFW_KEY_LEFT_CONTROL)

    @JvmField
    val RELOAD = registerKey("reload", GLFW.GLFW_KEY_R)

    @JvmField
    val FIRE_MODE = registerKey("fire_mode", GLFW.GLFW_KEY_N)

    @JvmField
    val SENSITIVITY_INCREASE = registerKey("sensitivity_increase", GLFW.GLFW_KEY_PAGE_UP)

    @JvmField
    val SENSITIVITY_REDUCE = registerKey("sensitivity_reduce", GLFW.GLFW_KEY_PAGE_DOWN)

    @JvmField
    val INTERACT = registerKey("interact", GLFW.GLFW_KEY_X)

    @JvmField
    val DISMOUNT = registerKey("dismount", GLFW.GLFW_KEY_LEFT_ALT)

    @JvmField
    val BREATH = registerKey("breath", GLFW.GLFW_KEY_LEFT_CONTROL)

    @JvmField
    val CONFIG = KeyBindingHelper.registerKeyBinding(KeyMapping(
        "key.superbwarfare.config",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_O,
        CATEGORY
    ))

    @JvmField
    val EDIT_MODE = registerKey("edit_mode", GLFW.GLFW_KEY_H)

    @JvmField
    val CHANGE_AMMO_FORWARD = registerKey("change_ammo_forward", GLFW.GLFW_KEY_LEFT)

    @JvmField
    val CHANGE_AMMO_BACKWARD = registerKey("change_ammo_backward", GLFW.GLFW_KEY_RIGHT)

    @JvmField
    val CHANGE_FIRE_MODE_FORWARD = registerKey("change_fire_mode_forward", GLFW.GLFW_KEY_UP)

    @JvmField
    val CHANGE_FIRE_MODE_BACKWARD = registerKey("change_fire_mode_backward", GLFW.GLFW_KEY_DOWN)

    @JvmField
    val UNLOAD = registerKey("unload", InputConstants.UNKNOWN.value)

    @JvmField
    val FIRE = registerKey("fire", GLFW.GLFW_MOUSE_BUTTON_LEFT, type = InputConstants.Type.MOUSE)

    @JvmField
    val HOLD_ZOOM = registerKey("hold_zoom", GLFW.GLFW_MOUSE_BUTTON_RIGHT, type = InputConstants.Type.MOUSE)

    @JvmField
    val SWITCH_ZOOM = registerKey("switch_zoom", GLFW.GLFW_KEY_UNKNOWN)

    @JvmField
    val RELEASE_DECOY = registerKey("release_decoy", GLFW.GLFW_KEY_V)

    @JvmField
    val FREE_CAMERA = registerKey("free_camera", GLFW.GLFW_KEY_C)

    @JvmField
    val MELEE = registerKey("melee", GLFW.GLFW_KEY_V)

    @JvmField
    val VEHICLE_SEEK = registerKey("vehicle_seek", GLFW.GLFW_KEY_X)

    @JvmField
    val MARK = registerKey("mark", GLFW.GLFW_MOUSE_BUTTON_MIDDLE, type = InputConstants.Type.MOUSE)

    @JvmField
    val ACTIVE_THERMAL_IMAGING = registerKey("active_thermal_imaging", GLFW.GLFW_KEY_K)

    private fun registerKey(
        name: String,
        code: Int,
        type: InputConstants.Type = InputConstants.Type.KEYSYM
    ): KeyMapping {
        val key = KeyMapping(
            "key.superbwarfare.$name",
            type,
            code,
            CATEGORY
        )
        KEYS.add(key)
        return key
    }

    @JvmStatic
    fun init() {
        KEYS.forEach { KeyBindingHelper.registerKeyBinding(it) }
    }
}