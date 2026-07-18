package com.atsuishio.superbwarfare.client;

import com.atsuishio.superbwarfare.client.animation.AnimationCurves;
import com.atsuishio.superbwarfare.client.model.curio.ParachuteModel;
import com.atsuishio.superbwarfare.client.renderer.block.*;
import com.atsuishio.superbwarfare.client.overlay.*;
import com.atsuishio.superbwarfare.client.tooltip.*;
import com.atsuishio.superbwarfare.client.tooltip.component.*;
import com.atsuishio.superbwarfare.init.ModBlockEntities;
import com.atsuishio.superbwarfare.item.gun.GunItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
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

    public static void registerTooltip() {
        TooltipComponentCallback.EVENT.register(component -> {
            if (component instanceof BocekImageComponent c) return new ClientBocekImageTooltip(c);
            if (component instanceof SentinelImageComponent c) return new ClientSentinelImageTooltip(c);
            if (component instanceof ChargingStationImageComponent c) return new ClientChargingStationImageTooltip(c);
            if (component instanceof GunImageComponent c && c.stack.getItem() instanceof GunItem) return new ClientGunImageTooltip(c);
            if (component instanceof CellImageComponent c) return new ClientCellImageTooltip(c);
            if (component instanceof DogTagImageComponent c) return new ClientDogTagImageTooltip(c);
            return null;
        });
    }

    public static void registerBlockRenderers() {
        BlockEntityRenderers.register(ModBlockEntities.CONTAINER.get(), c -> new ContainerBlockEntityRenderer());
        BlockEntityRenderers.register(ModBlockEntities.FUMO_25.get(), c -> new FuMO25BlockEntityRenderer());
        BlockEntityRenderers.register(ModBlockEntities.CHARGING_STATION.get(), c -> new ChargingStationBlockEntityRenderer());
        BlockEntityRenderers.register(ModBlockEntities.SMALL_CONTAINER.get(), c -> new SmallContainerBlockEntityRenderer());
        BlockEntityRenderers.register(ModBlockEntities.LUCKY_CONTAINER.get(), c -> new LuckyContainerBlockEntityRenderer());
        BlockEntityRenderers.register(ModBlockEntities.VEHICLE_ASSEMBLING_TABLE.get(), c -> new VehicleAssemblingTableBlockEntityRenderer());
    }

    public static void renderOverlays(GuiGraphics guiGraphics, float partialTick) {
        var mc = Minecraft.getInstance();
        if (mc.player == null) return;

        int w = guiGraphics.guiWidth();
        int h = guiGraphics.guiHeight();

        Type63InfoOverlay.render(guiGraphics, partialTick, w, h);
        MortarInfoOverlay.render(guiGraphics, partialTick, w, h);
        TowOverlay.render(guiGraphics, partialTick, w, h);
        SpyglassRangeOverlay.render(guiGraphics, partialTick, w, h);
        HandsomeFrameOverlay.render(guiGraphics, partialTick, w, h);
        RedTriangleOverlay.render(guiGraphics, partialTick, w, h);
        DroneHudOverlay.render(guiGraphics, partialTick, w, h);
        HeatBarOverlay.render(guiGraphics, partialTick, w, h);
        CrossHairOverlay.render(guiGraphics, partialTick, w, h);
        ItemRendererFixOverlay.render(guiGraphics, partialTick, w, h);
        AmmoCountOverlay.render(guiGraphics, partialTick, w, h);
        StaminaOverlay.render(guiGraphics, partialTick, w, h);
        VehicleCrosshairOverlay.render(guiGraphics, partialTick, w, h);
        VehicleMainWeaponHudOverlay.render(guiGraphics, partialTick, w, h);
        VehicleHudOverlay.render(guiGraphics, partialTick, w, h);
        IglaHudOverlay.render(guiGraphics, partialTick, w, h);
        JavelinHudOverlay.render(guiGraphics, partialTick, w, h);
        VehicleTeamOverlay.render(guiGraphics, partialTick, w, h);
        IFFOverlay.render(guiGraphics, partialTick, w, h);
        AmmoBarOverlay.render(guiGraphics, partialTick, w, h);
        ArmorPlateOverlay.render(guiGraphics, partialTick, w, h);
        KillMessageOverlay.render(guiGraphics, partialTick, w, h);
    }

    public static void registerLayerDefinitions() {
        EntityModelLayerRegistry.registerModelLayer(ParachuteModel.LAYER_LOCATION, ParachuteModel::createBodyLayer);
    }
}
