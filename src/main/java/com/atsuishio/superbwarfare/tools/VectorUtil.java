package com.atsuishio.superbwarfare.tools;

import com.atsuishio.superbwarfare.event.custom.ComputeFovCallback;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector4d;

@Environment(EnvType.CLIENT)
public class VectorUtil {

    public static double fov = 70;
    public static Matrix4f modelViewMatrix;
    public static Matrix4f projectionMatrix;

    public static void registerEvents() {
        ComputeFovCallback.EVENT.register(VectorUtil::captureFov);
    }

    // 感谢 Minecraft-Ping-Wheel 开源
    // https://github.com/LukenSkyne/Minecraft-Ping-Wheel/blob/138295954dab9d2451ad19e16d8d413ef018a2d8/common/src/main/java/nx/pingwheel/common/helper/MathUtils.java#L15
    public static Vec3 worldToScreen(Vec3 pos) {
        var mc = Minecraft.getInstance();
        var window = mc.getWindow();
        var camera = mc.gameRenderer.getMainCamera();
        var worldPosRel = new Vector4d(camera.getPosition().reverse().add(pos).toVector3f(), 1f);
        worldPosRel.mul(modelViewMatrix);
        worldPosRel.mul(projectionMatrix);

        var depth = worldPosRel.w;

        if (depth != 0) {
            worldPosRel.div(depth);
        }

        return new Vec3(
                window.getGuiScaledWidth() * (0.5f + worldPosRel.x * 0.5f),
                window.getGuiScaledHeight() * (0.5f - worldPosRel.y * 0.5f),
                depth
        );
    }

    public static void captureFov(ComputeFovCallback.Event event) {
        if (event.usedConfiguredFov()) {
            fov = event.getFOV();
        }
    }

    public static boolean canSee(Vec3 pos) {
        Minecraft mc = Minecraft.getInstance();
        Camera camera = mc.gameRenderer.getMainCamera();
        Vec3 cameraPos = camera.getPosition();
        Vec3 viewVec = new Vec3(camera.getLookVector());
        Vec3 v1 = cameraPos.vectorTo(pos);
        return VectorTool.calculateAngle(v1, viewVec) < fov + 10;
    }
}
