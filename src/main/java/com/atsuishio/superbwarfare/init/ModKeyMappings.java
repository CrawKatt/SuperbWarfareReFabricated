package com.atsuishio.superbwarfare.init;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class ModKeyMappings {

    public static final KeyMapping RELOAD = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.superbwarfare.reload", GLFW.GLFW_KEY_R, "key.categories.superbwarfare"));
    public static final KeyMapping FIRE_MODE = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.superbwarfare.fire_mode", GLFW.GLFW_KEY_N, "key.categories.superbwarfare"));
    public static final KeyMapping SENSITIVITY_INCREASE = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.superbwarfare.sensitivity_increase", GLFW.GLFW_KEY_PAGE_UP, "key.categories.superbwarfare"));
    public static final KeyMapping SENSITIVITY_REDUCE = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.superbwarfare.sensitivity_reduce", GLFW.GLFW_KEY_PAGE_DOWN, "key.categories.superbwarfare"));
    public static final KeyMapping INTERACT = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.superbwarfare.interact", GLFW.GLFW_KEY_X, "key.categories.superbwarfare"));
    public static final KeyMapping DISMOUNT = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.superbwarfare.dismount", GLFW.GLFW_KEY_LEFT_ALT, "key.categories.superbwarfare"));
    public static final KeyMapping BREATH = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.superbwarfare.breath", GLFW.GLFW_KEY_LEFT_CONTROL, "key.categories.superbwarfare"));

    public static final KeyMapping CONFIG = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.superbwarfare.config", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_O, "key.categories.superbwarfare"));

    public static final KeyMapping EDIT_MODE = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.superbwarfare.edit_mode", GLFW.GLFW_KEY_H, "key.categories.superbwarfare"));
    public static final KeyMapping CHANGE_AMMO_FORWARD = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.superbwarfare.change_ammo_forward", GLFW.GLFW_KEY_LEFT, "key.categories.superbwarfare"));
    public static final KeyMapping CHANGE_AMMO_BACKWARD = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.superbwarfare.change_ammo_backward", GLFW.GLFW_KEY_RIGHT, "key.categories.superbwarfare"));
    public static final KeyMapping CHANGE_FIRE_MODE_FORWARD = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.superbwarfare.change_fire_mode_forward", GLFW.GLFW_KEY_UP, "key.categories.superbwarfare"));
    public static final KeyMapping CHANGE_FIRE_MODE_BACKWARD = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.superbwarfare.change_fire_mode_backward", GLFW.GLFW_KEY_DOWN, "key.categories.superbwarfare"));
    public static final KeyMapping UNLOAD = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.superbwarfare.unload", InputConstants.UNKNOWN.getValue(), "key.categories.superbwarfare"));

    public static final KeyMapping FIRE = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.superbwarfare.fire", InputConstants.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_LEFT, "key.categories.superbwarfare"));
    public static final KeyMapping HOLD_ZOOM = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.superbwarfare.hold_zoom", InputConstants.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_RIGHT, "key.categories.superbwarfare"));
    public static final KeyMapping SWITCH_ZOOM = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.superbwarfare.switch_zoom", GLFW.GLFW_KEY_UNKNOWN, "key.categories.superbwarfare"));
    public static final KeyMapping RELEASE_DECOY = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.superbwarfare.release_decoy", GLFW.GLFW_KEY_V, "key.categories.superbwarfare"));
    public static final KeyMapping FREE_CAMERA = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.superbwarfare.free_camera", GLFW.GLFW_KEY_C, "key.categories.superbwarfare"));
    public static final KeyMapping MELEE = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.superbwarfare.melee", GLFW.GLFW_KEY_V, "key.categories.superbwarfare"));
    public static final KeyMapping VEHICLE_SEEK = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.superbwarfare.vehicle_seek", GLFW.GLFW_KEY_X, "key.categories.superbwarfare"));
    public static final KeyMapping MARK = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.superbwarfare.mark", InputConstants.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_MIDDLE, "key.categories.superbwarfare"));
}
