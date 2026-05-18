package com.atsuishio.superbwarfare.client.renderer.curio;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.client.model.curio.ParachuteModel;
import com.atsuishio.superbwarfare.item.trinket.ParachuteItem;
import com.atsuishio.superbwarfare.tools.NBTTool;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.client.TrinketRenderer;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

public class ParachuteRenderer implements TrinketRenderer {

    private static ParachuteModel firstPersonModel;
    private static final ResourceLocation TEXTURE = Mod.loc("textures/trinket/parachute.png");

    private ParachuteModel model;

    public ParachuteRenderer() {
    }

    private ParachuteModel getModel() {
        if (model == null) {
            model = new ParachuteModel(Minecraft.getInstance().getEntityModels().bakeLayer(ParachuteModel.LAYER_LOCATION));
        }
        return model;
    }

    @Override
    public void render(ItemStack stack, SlotReference slotReference, EntityModel<? extends LivingEntity> model, PoseStack matrices, MultiBufferSource multiBufferSource, int light, LivingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        matrices.pushPose();

        matrices.scale(0.5f, 0.5f, 0.5f);
        matrices.translate(0, 1.25, 0);

        if (NBTTool.getTag(stack).getBoolean(ParachuteItem.TAG_OPEN)) {
            var m = getModel();
            m.prepareMobModel(entity, limbAngle, limbDistance, tickDelta);
            m.setupAnim(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);

            VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(multiBufferSource, RenderType.armorCutoutNoCull(TEXTURE), stack.hasFoil());

            m.renderToBuffer(matrices, vertexconsumer, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        }

        matrices.popPose();
    }

    public static void onRenderLevelStage() {
        WorldRenderEvents.LAST.register(context -> {
            RenderBuffers buffers = Minecraft.getInstance().renderBuffers();
            var player = Minecraft.getInstance().player;
            if (player == null) return;
            if (!ParachuteItem.isParachuteOpen(player)) return;
            if (!ParachuteItem.isParachuteVisible(player)) return;
            PoseStack stack = context.matrixStack();

            if (Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON) {
                stack.pushPose();

                if (firstPersonModel == null) {
                    firstPersonModel = new ParachuteModel(Minecraft.getInstance().getEntityModels().bakeLayer(ParachuteModel.LAYER_LOCATION));
                }

                var tickDelta = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
                stack.mulPose(Axis.XP.rotationDegrees(180));
                stack.mulPose(Axis.YP.rotationDegrees(player.getViewYRot(tickDelta)));
                stack.translate(0, 1.5, 0);

                firstPersonModel.prepareMobModel(player, 0, 0, tickDelta);
                firstPersonModel.setupAnim(player, 0, 0, player.tickCount, 0, 0);
                firstPersonModel.renderToBuffer(stack, buffers.bufferSource().getBuffer(RenderType.armorCutoutNoCull(TEXTURE)), 0xFFFFFF, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);

                stack.popPose();
            }
        });
    }
}
