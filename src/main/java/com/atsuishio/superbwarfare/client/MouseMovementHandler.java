package com.atsuishio.superbwarfare.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec2;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class MouseMovementHandler {

    private static Vec2 delta = null;
    private static Vec2 lastPos = null;
    private static Vec2 vel = null;
    private static MouseHandler mouseHandler = null;
    private static boolean mouseLockActive = false;
    private static final Vector3f savedRot = new Vector3f();
    private static boolean initialized = false;

    private static void ensureInitialized() {
        if (!initialized) {
            initialized = true;
            delta = new Vec2(0, 0);
            vel = new Vec2(0, 0);
            mouseHandler = Minecraft.getInstance().mouseHandler;
            if (mouseHandler != null) {
                lastPos = getMousePos();
            }
        }
    }

    public static Vec2 getMousePos() {
        ensureInitialized();
        if (mouseHandler == null) return new Vec2(0, 0);
        if (mouseHandler.isMouseGrabbed()) {
            return new Vec2((float) mouseHandler.xpos(), (float) mouseHandler.ypos());
        } else {
            double[] x, y;
            x = new double[1];
            y = new double[1];
            GLFW.glfwGetCursorPos(Minecraft.getInstance().getWindow().getWindow(), x, y);
            return new Vec2((float) x[0], (float) y[0]);
        }
    }

    public static void resetCenter() {
        ensureInitialized();
        delta = new Vec2(0, 0);
        vel = new Vec2(0, 0);
        lastPos = getMousePos();
    }

    public static void init() {
    }

    public static float getX(boolean useVelocity) {
        ensureInitialized();
        if (useVelocity) {
            return vel.x;
        } else {
            return delta.x;
        }
    }

    public static float getY(boolean useVelocity) {
        ensureInitialized();
        if (useVelocity) {
            return vel.y;
        } else {
            return delta.y;
        }
    }

    public static void activateMouseLock() {
        ensureInitialized();
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        savedRot.x = player.getXRot();
        savedRot.y = player.getYRot();
        savedRot.z = 0;
        mouseLockActive = true;
        lastPos = getMousePos();
    }

    public static void deactivateMouseLock() {
        mouseLockActive = false;
    }

    public static void cancelPlayerTurn() {
        if (!mouseLockActive) return;
        ensureInitialized();
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        player.turn((savedRot.y - player.getYRot()) / 0.15, (savedRot.x - player.getXRot()) / 0.15);
        player.xBob = savedRot.x;
        player.yBob = savedRot.y;
        player.xBobO = savedRot.x;
        player.yBobO = savedRot.y;
    }
}
