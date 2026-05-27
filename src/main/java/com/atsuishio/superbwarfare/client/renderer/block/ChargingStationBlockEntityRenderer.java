package com.atsuishio.superbwarfare.client.renderer.block;

import com.atsuishio.superbwarfare.block.ChargingStationBlock;
import com.atsuishio.superbwarfare.block.entity.ChargingStationBlockEntity;
import com.atsuishio.superbwarfare.client.renderer.ModRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ChargingStationBlockEntityRenderer implements BlockEntityRenderer<ChargingStationBlockEntity> {

    @Override
    public void render(ChargingStationBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        if (!pBlockEntity.getBlockState().getValue(ChargingStationBlock.SHOW_RANGE)) return;

        pPoseStack.pushPose();
        var pos = pBlockEntity.getBlockPos();
        pPoseStack.translate(-pos.getX(), -pos.getY(), -pos.getZ());

        var aabb = new AABB(pos).inflate(ChargingStationBlockEntity.CHARGE_RADIUS);

        float startX = (float) aabb.minX - 0.001f;
        float startY = (float) aabb.minY - 0.001f;
        float startZ = (float) aabb.minZ - 0.001f;
        float endX = (float) aabb.maxX + 0.001f;
        float endY = (float) aabb.maxY + 0.001f;
        float endZ = (float) aabb.maxZ + 0.001f;

        var red = 0.0f;
        var green = 1.0f;
        var blue = 0.0f;
        var alpha = 0.2f;


        var builder = pBuffer.getBuffer(ModRenderTypes.BLOCK_OVERLAY);
        var m4f = pPoseStack.last().pose();

        // east
        builder.vertex(m4f, startX, startY, startZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(m4f, startX, endY, startZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(m4f, endX, endY, startZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(m4f, endX, startY, startZ).color(red, green, blue, alpha).endVertex();

        // west
        builder.vertex(m4f, startX, startY, endZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(m4f, endX, startY, endZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(m4f, endX, endY, endZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(m4f, startX, endY, endZ).color(red, green, blue, alpha).endVertex();

        // south
        builder.vertex(m4f, endX, startY, startZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(m4f, endX, endY, startZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(m4f, endX, endY, endZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(m4f, endX, startY, endZ).color(red, green, blue, alpha).endVertex();

        // north
        builder.vertex(m4f, startX, startY, startZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(m4f, startX, startY, endZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(m4f, startX, endY, endZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(m4f, startX, endY, startZ).color(red, green, blue, alpha).endVertex();

        // top
        builder.vertex(m4f, startX, endY, startZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(m4f, endX, endY, startZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(m4f, endX, endY, endZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(m4f, startX, endY, endZ).color(red, green, blue, alpha).endVertex();

        // bottom
        builder.vertex(m4f, startX, startY, startZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(m4f, endX, startY, startZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(m4f, endX, startY, endZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(m4f, startX, startY, endZ).color(red, green, blue, alpha).endVertex();


        pPoseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(ChargingStationBlockEntity pBlockEntity) {
        return true;
    }

    @Override
    public boolean shouldRender(ChargingStationBlockEntity pBlockEntity, Vec3 pCameraPos) {
        return true;
    }

}
