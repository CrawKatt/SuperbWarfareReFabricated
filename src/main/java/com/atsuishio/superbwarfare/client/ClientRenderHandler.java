package com.atsuishio.superbwarfare.client;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.client.animation.AnimationCurves;
import com.atsuishio.superbwarfare.client.model.curio.ParachuteModel;
import com.atsuishio.superbwarfare.client.renderer.block.*;
import com.atsuishio.superbwarfare.client.renderer.curio.ParachuteRenderer;
import com.atsuishio.superbwarfare.client.overlay.*;
import com.atsuishio.superbwarfare.client.tooltip.*;
import com.atsuishio.superbwarfare.client.tooltip.component.*;
import com.atsuishio.superbwarfare.init.ModBlockEntities;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;

public class ClientRenderHandler {

    // TODO 正确赋值该变量
    public static Vec3 bulletRenderOffset = null;

    /**
     * 修改子弹类实体的虚拟渲染位置
     */
    public static void transformVirtualRenderPosition(PoseStack stack, Projectile projectile, float partialTick) {
        if (ClientRenderHandler.bulletRenderOffset == null) return;

        var player = Minecraft.getInstance().player;
        if (player == null || projectile.getOwner() == null || !player.getUUID().equals(projectile.getOwner().getUUID()))
            return;

        var rate = 1 - AnimationCurves.EASE_OUT_CIRC.apply(Math.min(1, (projectile.tickCount + partialTick) / 5.0));
        var offset = ClientRenderHandler.bulletRenderOffset.subtract(projectile.position()).multiply(rate, rate, rate);
        stack.translate(offset.x, offset.y, offset.z);
    }

    public static void registerBlockRenderers() {
        BlockEntityRenderers.register(ModBlockEntities.CONTAINER.get(), ContainerBlockEntityRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.FUMO_25.get(), FuMO25BlockEntityRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.CHARGING_STATION.get(), ChargingStationBlockEntityRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.SMALL_CONTAINER.get(), SmallContainerBlockEntityRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.LUCKY_CONTAINER.get(), LuckyContainerBlockEntityRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.VEHICLE_ASSEMBLING_TABLE.get(), VehicleAssemblingTableBlockEntityRenderer::new);
    }

    public static void onClientSetup() {
        HudRenderCallback.EVENT.register((guiGraphics, tickDelta) -> {
            var mc = Minecraft.getInstance();
            if (mc.player == null) return;
            int w = mc.getWindow().getGuiScaledWidth();
            int h = mc.getWindow().getGuiScaledHeight();

            AmmoBarOverlay.render(guiGraphics, tickDelta, w, h);
            AmmoCountOverlay.render(guiGraphics, tickDelta, w, h);
            ArmorPlateOverlay.render(guiGraphics, tickDelta, w, h);
            CrossHairOverlay.render(guiGraphics, tickDelta, w, h);
            DroneHudOverlay.render(guiGraphics, tickDelta, w, h);
            HandsomeFrameOverlay.render(guiGraphics, tickDelta, w, h);
            HeatBarOverlay.render(guiGraphics, tickDelta, w, h);
            IFFOverlay.render(guiGraphics, tickDelta, w, h);
            IglaHudOverlay.render(guiGraphics, tickDelta, w, h);
            ItemRendererFixOverlay.render(guiGraphics, tickDelta, w, h);
            JavelinHudOverlay.render(guiGraphics, tickDelta, w, h);
            KillMessageOverlay.render(guiGraphics, tickDelta, w, h);
            MortarInfoOverlay.render(guiGraphics, tickDelta, w, h);
            RedTriangleOverlay.render(guiGraphics, tickDelta, w, h);
            SpyglassRangeOverlay.render(guiGraphics, tickDelta, w, h);
            StaminaOverlay.render(guiGraphics, tickDelta, w, h);
            TowOverlay.render(guiGraphics, tickDelta, w, h);
            Type63InfoOverlay.render(guiGraphics, tickDelta, w, h);
            VehicleCrosshairOverlay.render(guiGraphics, tickDelta, w, h);
            VehicleHudOverlay.render(guiGraphics, tickDelta, w, h);
            VehicleTeamOverlay.render(guiGraphics, tickDelta, w, h);
        });
    }

    public static void registerLayerDefinitions() {
        EntityModelLayerRegistry.registerModelLayer(ParachuteModel.LAYER_LOCATION, ParachuteModel::createBodyLayer);
    }
}
