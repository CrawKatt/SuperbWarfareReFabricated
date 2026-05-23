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
import com.atsuishio.superbwarfare.item.gun.GunItem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class ClientRenderHandler {

    // TODO 正确赋值该变量
    public static Vec3 bulletRenderOffset = null;
    private static final ContainerItemDecorator CONTAINER_DECORATOR = new ContainerItemDecorator();
    private static final LuckyContainerItemDecorator LUCKY_CONTAINER_DECORATOR = new LuckyContainerItemDecorator();

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
            if (component instanceof GunImageComponent c && c.stack.getItem() instanceof GunItem) return new ClientGunImageTooltip(c);
            if (component instanceof BocekImageComponent c) return new ClientBocekImageTooltip(c);
            if (component instanceof CellImageComponent c) return new ClientCellImageTooltip(c);
            if (component instanceof SentinelImageComponent c) return new ClientSentinelImageTooltip(c);
            if (component instanceof ChargingStationImageComponent c) return new ClientChargingStationImageTooltip(c);
            if (component instanceof DogTagImageComponent c) return new ClientDogTagImageTooltip(c);
            return null;
        });
    }

    public static void registerRenderers() {
        BlockEntityRenderers.register(ModBlockEntities.CONTAINER, context -> new ContainerBlockEntityRenderer());
        BlockEntityRenderers.register(ModBlockEntities.FUMO_25, context -> new FuMO25BlockEntityRenderer());
        BlockEntityRenderers.register(ModBlockEntities.CHARGING_STATION, context -> new ChargingStationBlockEntityRenderer());
        BlockEntityRenderers.register(ModBlockEntities.SMALL_CONTAINER, context -> new SmallContainerBlockEntityRenderer());
        BlockEntityRenderers.register(ModBlockEntities.LUCKY_CONTAINER, context -> new LuckyContainerBlockEntityRenderer());
        BlockEntityRenderers.register(ModBlockEntities.VEHICLE_ASSEMBLING_TABLE, context -> new VehicleAssemblingTableBlockEntityRenderer());
    }

    public static void registerOverlays() {
        HudRenderCallback.EVENT.register((guiGraphics, deltaTracker) -> {
            new JavelinHudOverlay().render(guiGraphics, deltaTracker);
            new KillMessageOverlay().render(guiGraphics, deltaTracker);
            new ArmorPlateOverlay().render(guiGraphics, deltaTracker);
            new AmmoBarOverlay().render(guiGraphics, deltaTracker);
            new IFFOverlay().render(guiGraphics, deltaTracker);
            new VehicleTeamOverlay().render(guiGraphics, deltaTracker);
            new IglaHudOverlay().render(guiGraphics, deltaTracker);
            new VehicleMainWeaponHudOverlay().render(guiGraphics, deltaTracker);
            new VehicleHudOverlay().render(guiGraphics, deltaTracker);
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

    public static void renderItemDecorations(GuiGraphics guiGraphics, Font font, ItemStack stack, int x, int y) {
        if (CONTAINER_DECORATOR.render(guiGraphics, font, stack, x, y)) {
            return;
        }
        LUCKY_CONTAINER_DECORATOR.render(guiGraphics, font, stack, x, y);
    }

    public static void onClientSetup() {
        TrinketRendererRegistry.registerRenderer(ModItems.PARACHUTE, new ParachuteRenderer());
    }

    public static void registerLayer() {
        EntityModelLayerRegistry.registerModelLayer(ParachuteModel.LAYER_LOCATION, ParachuteModel::createBodyLayer);
    }
}
