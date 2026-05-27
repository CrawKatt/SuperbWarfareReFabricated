package com.atsuishio.superbwarfare.client;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.client.animation.AnimationCurves;
import com.atsuishio.superbwarfare.client.model.curio.ParachuteModel;
import com.atsuishio.superbwarfare.client.renderer.block.*;
import com.atsuishio.superbwarfare.client.renderer.curio.ParachuteRenderer;
import com.atsuishio.superbwarfare.client.tooltip.*;
import com.atsuishio.superbwarfare.client.tooltip.component.*;
import com.atsuishio.superbwarfare.init.ModBlockEntities;
import com.atsuishio.superbwarfare.init.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

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
        BlockEntityRendererRegistry.register(ModBlockEntities.CONTAINER.get(), ContainerBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntities.FUMO_25.get(), FuMO25BlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntities.CHARGING_STATION.get(), ChargingStationBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntities.SMALL_CONTAINER.get(), SmallContainerBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntities.LUCKY_CONTAINER.get(), LuckyContainerBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntities.VEHICLE_ASSEMBLING_TABLE.get(), VehicleAssemblingTableBlockEntityRenderer::new);
    }

    public static void onClientSetup() {
        CuriosRendererRegistry.register(ModItems.PARACHUTE.get(), ParachuteRenderer::new);
    }

    public static void registerLayerDefinitions() {
        EntityModelLayerRegistry.registerModelLayer(ParachuteModel.LAYER_LOCATION, ParachuteModel::createBodyLayer);
    }
}
