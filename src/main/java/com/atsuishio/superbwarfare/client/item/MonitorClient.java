package com.atsuishio.superbwarfare.client.item;

import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.tools.FormatTool;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;

@Environment(EnvType.CLIENT)
public final class MonitorClient {

    private MonitorClient() {
    }

    public static void startUsing() {
        ClientEventHandler.lastCameraType = Minecraft.getInstance().options.getCameraType();
        Minecraft.getInstance().options.setCameraType(CameraType.THIRD_PERSON_BACK);
    }

    public static void stopUsing() {
        if (ClientEventHandler.lastCameraType != null) {
            Minecraft.getInstance().options.setCameraType(ClientEventHandler.lastCameraType);
        }
    }

    public static void appendHoverText(ItemStack stack, List<Component> tooltip) {
        var player = Minecraft.getInstance().player;
        if (player == null) return;

        if (!stack.getOrCreateTag().contains("PosX") || !stack.getOrCreateTag().contains("PosY") || !stack.getOrCreateTag().contains("PosZ")) {
            return;
        }

        Vec3 droneVec = new Vec3(
                stack.getOrCreateTag().getDouble("PosX"),
                stack.getOrCreateTag().getDouble("PosY"),
                stack.getOrCreateTag().getDouble("PosZ")
        );

        tooltip.add(Component.translatable("des.superbwarfare.monitor",
                FormatTool.format1D(player.position().distanceTo(droneVec), "m")).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("X: " + FormatTool.format1D(droneVec.x) +
                " Y: " + FormatTool.format1D(droneVec.y) +
                " Z: " + FormatTool.format1D(droneVec.z)
        ));
    }
}
