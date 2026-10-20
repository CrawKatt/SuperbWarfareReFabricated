package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.event.custom.RenderLevelStageCallback;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Shadow
    @Final
    private EntityRenderDispatcher entityRenderDispatcher;

    @Shadow
    private int ticks;

    @Inject(method = "renderLevel", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/LevelRenderer;renderSky(Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;FLnet/minecraft/client/Camera;ZLjava/lang/Runnable;)V",
            shift = At.Shift.AFTER))
    private void superbwarfare$afterSky(DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera,
                                      GameRenderer gameRenderer, LightTexture lightTexture,
                                      Matrix4f modelViewMatrix, Matrix4f projectionMatrix, CallbackInfo ci,
                                      @Local Frustum frustum) {
        RenderLevelStageCallback.EVENT.invoker().onRenderLevelStage(new RenderLevelStageCallback.Context(
                RenderLevelStageCallback.Context.Stage.AFTER_SKY, (LevelRenderer) (Object) this, null,
                modelViewMatrix, projectionMatrix, ticks, Minecraft.getInstance().getTimer(), camera, frustum
        ));
    }

    // Hook the caller because Sodium replaces renderSectionLayer.
    @Inject(method = "renderLevel", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/LevelRenderer;renderSectionLayer(Lnet/minecraft/client/renderer/RenderType;DDDLorg/joml/Matrix4f;Lorg/joml/Matrix4f;)V",
            ordinal = 2, shift = At.Shift.AFTER))
    private void superbwarfare$afterCutoutBlocks(DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera,
                                               GameRenderer gameRenderer, LightTexture lightTexture,
                                               Matrix4f modelViewMatrix, Matrix4f projectionMatrix, CallbackInfo ci,
                                               @Local Frustum frustum) {
        RenderLevelStageCallback.EVENT.invoker().onRenderLevelStage(new RenderLevelStageCallback.Context(
                RenderLevelStageCallback.Context.Stage.AFTER_CUTOUT_BLOCKS, (LevelRenderer) (Object) this, null,
                modelViewMatrix, projectionMatrix, ticks, Minecraft.getInstance().getTimer(), camera, frustum
        ));
    }

    @Inject(method = "renderLevel", at = @At(value = "CONSTANT", args = "stringValue=destroyProgress"))
    private void superbwarfare$afterBlockEntities(DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera,
                                               GameRenderer gameRenderer, LightTexture lightTexture,
                                               Matrix4f modelViewMatrix, Matrix4f projectionMatrix, CallbackInfo ci,
                                               @Local PoseStack poseStack, @Local Frustum frustum) {
        RenderLevelStageCallback.EVENT.invoker().onRenderLevelStage(new RenderLevelStageCallback.Context(
                RenderLevelStageCallback.Context.Stage.AFTER_BLOCK_ENTITIES, (LevelRenderer) (Object) this, poseStack,
                modelViewMatrix, projectionMatrix, ticks, Minecraft.getInstance().getTimer(), camera, frustum
        ));
    }

    @Inject(method = "renderLevel", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/particle/ParticleEngine;render(Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/client/Camera;F)V",
            shift = At.Shift.AFTER))
    private void superbwarfare$afterParticles(DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera,
                                            GameRenderer gameRenderer, LightTexture lightTexture,
                                            Matrix4f modelViewMatrix, Matrix4f projectionMatrix, CallbackInfo ci,
                                            @Local PoseStack poseStack, @Local Frustum frustum) {
        RenderLevelStageCallback.EVENT.invoker().onRenderLevelStage(new RenderLevelStageCallback.Context(
                RenderLevelStageCallback.Context.Stage.AFTER_PARTICLES, (LevelRenderer) (Object) this, poseStack,
                modelViewMatrix, projectionMatrix, ticks, Minecraft.getInstance().getTimer(), camera, frustum
        ));
    }

    // 感谢 Minecraft-Ping-Wheel 开源
    // https://github.com/LukenSkyne/Minecraft-Ping-Wheel/blob/ede72b18f57bd9dfe55ef44afe61190421fbc084/common/src/main/java/nx/pingwheel/common/mixin/LevelRendererMixin.java

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;applyModelViewMatrix()V", ordinal = 0, shift = At.Shift.AFTER))
    private void onStartRenderLevel(DeltaTracker deltaTracker, boolean bl, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci) {
        ClientEventHandler.modelViewMatrix = RenderSystem.getModelViewMatrix();
        ClientEventHandler.projectionMatrix = RenderSystem.getProjectionMatrix();
    }

    // TODO 找到真正把实体渲染在世界中的位置进行mixin
    @Inject(method = "renderEntity(Lnet/minecraft/world/entity/Entity;DDDFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;)V",
            at = @At("HEAD"), cancellable = true)
    private void renderEntity(Entity pEntity, double pCamX, double pCamY, double pCamZ, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, CallbackInfo ci) {
        // 这里只改实体的亮度
        if (ClientEventHandler.activeThermalImaging) {
            ci.cancel();
            double d0 = Mth.lerp(pPartialTick, pEntity.xOld, pEntity.getX());
            double d1 = Mth.lerp(pPartialTick, pEntity.yOld, pEntity.getY());
            double d2 = Mth.lerp(pPartialTick, pEntity.zOld, pEntity.getZ());
            float f = Mth.lerp(pPartialTick, pEntity.yRotO, pEntity.getYRot());
            this.entityRenderDispatcher.render(pEntity, d0 - pCamX, d1 - pCamY, d2 - pCamZ, f, pPartialTick, pPoseStack,
                    pBufferSource, LightTexture.FULL_BRIGHT);
        }
    }
}
