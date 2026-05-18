package com.atsuishio.superbwarfare.client;

import com.atsuishio.superbwarfare.client.animation.AnimationCurves;
import com.atsuishio.superbwarfare.client.decorator.ContainerItemDecorator;
import com.atsuishio.superbwarfare.client.decorator.LuckyContainerItemDecorator;
import com.atsuishio.superbwarfare.client.model.curio.ParachuteModel;
import com.atsuishio.superbwarfare.client.overlay.*;
import com.atsuishio.superbwarfare.client.renderer.block.*;
import com.atsuishio.superbwarfare.client.renderer.curio.ParachuteRenderer;
import com.atsuishio.superbwarfare.client.tooltip.*;
import com.atsuishio.superbwarfare.client.tooltip.component.*;
import com.atsuishio.superbwarfare.init.ModBlockEntities;
import com.atsuishio.superbwarfare.init.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.minecraft.client.Minecraft;
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
            if (component instanceof GunImageComponent c) return new ClientGunImageTooltip(c);
            if (component instanceof BocekImageComponent c) return new ClientBocekImageTooltip(c);
            if (component instanceof CellImageComponent c) return new ClientCellImageTooltip(c);
            if (component instanceof SentinelImageComponent c) return new ClientSentinelImageTooltip(c);
            if (component instanceof ChargingStationImageComponent c) return new ClientChargingStationImageTooltip(c);
            if (component instanceof DogTagImageComponent c) return new ClientDogTagImageTooltip(c);
            return null;
        });
    }

    public static void registerRenderers() {
        BlockEntityRendererRegistry.register(ModBlockEntities.CONTAINER, context -> new ContainerBlockEntityRenderer());
        BlockEntityRendererRegistry.register(ModBlockEntities.FUMO_25, context -> new FuMO25BlockEntityRenderer());
        BlockEntityRendererRegistry.register(ModBlockEntities.CHARGING_STATION, context -> new ChargingStationBlockEntityRenderer());
        BlockEntityRendererRegistry.register(ModBlockEntities.SMALL_CONTAINER, context -> new SmallContainerBlockEntityRenderer());
        BlockEntityRendererRegistry.register(ModBlockEntities.LUCKY_CONTAINER, context -> new LuckyContainerBlockEntityRenderer());
        BlockEntityRendererRegistry.register(ModBlockEntities.VEHICLE_ASSEMBLING_TABLE, context -> new VehicleAssemblingTableBlockEntityRenderer());
    }

    public static void registerOverlays() {
        HudRenderCallback.EVENT.register((guiGraphics, deltaTracker) -> {
            new KillMessageOverlay().render(guiGraphics, deltaTracker);
            new ArmorPlateOverlay().render(guiGraphics, deltaTracker);
            new AmmoBarOverlay().render(guiGraphics, deltaTracker);
            new IFFOverlay().render(guiGraphics, deltaTracker);
            new VehicleTeamOverlay().render(guiGraphics, deltaTracker);
            new JavelinHudOverlay().render(guiGraphics, deltaTracker);
            new IglaHudOverlay().render(guiGraphics, deltaTracker);
            new VehicleHudOverlay().render(guiGraphics, deltaTracker);
            new VehicleMainWeaponHudOverlay().render(guiGraphics, deltaTracker);
            new VehicleCrosshairOverlay().render(guiGraphics, deltaTracker);
            new StaminaOverlay().render(guiGraphics, deltaTracker);
            new AmmoCountOverlay().render(guiGraphics, deltaTracker);
            new ItemRendererFixOverlay().render(guiGraphics, deltaTracker);
            new CrossHairOverlay().render(guiGraphics, deltaTracker);
            new HeatBarOverlay().render(guiGraphics, deltaTracker);
            new DroneHudOverlay().render(guiGraphics, deltaTracker);
            new RedTriangleOverlay().render(guiGraphics, deltaTracker);
            new HandsomeFrameOverlay().render(guiGraphics, deltaTracker);
            new SpyglassRangeOverlay().render(guiGraphics, deltaTracker);
            new TowOverlay().render(guiGraphics, deltaTracker);
            new MortarInfoOverlay().render(guiGraphics, deltaTracker);
            new Type63InfoOverlay().render(guiGraphics, deltaTracker);
        });
    }

    public static void registerItemDecorations() {
    }

    public static void onClientSetup() {
        TrinketRendererRegistry.registerRenderer(ModItems.PARACHUTE, new ParachuteRenderer());
    }

    public static void registerLayer() {
        EntityModelLayerRegistry.registerModelLayer(ParachuteModel.LAYER_LOCATION, ParachuteModel::createBodyLayer);
    }
}
