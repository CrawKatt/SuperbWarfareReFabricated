package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.client.renderer.scope.ScopeThermalRenderer;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.event.custom.RenderLevelStageCallback;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.culling.Frustum;
import org.joml.Matrix4f;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
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

    @Shadow
    private Frustum cullingFrustum;

    @Shadow
    private Frustum capturedFrustum;

    @Inject(method = "renderLevel", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/LevelRenderer;renderSky(Lcom/mojang/blaze3d/vertex/PoseStack;Lorg/joml/Matrix4f;FLnet/minecraft/client/Camera;ZLjava/lang/Runnable;)V",
            shift = At.Shift.AFTER))
    private void superbwarfare$afterSky(PoseStack poseStack, float partialTick, long finishTimeNano,
                                      boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer,
                                      LightTexture lightTexture, Matrix4f projectionMatrix, CallbackInfo ci) {
        Frustum frustum = capturedFrustum != null ? capturedFrustum : cullingFrustum;
        RenderLevelStageCallback.EVENT.invoker().onRenderLevelStage(new RenderLevelStageCallback.Context(
                RenderLevelStageCallback.Context.Stage.AFTER_SKY, (LevelRenderer) (Object) this,
                poseStack, projectionMatrix, ticks, partialTick, camera, frustum));
    }

    // Hook the caller because Sodium replaces renderChunkLayer.
    @Inject(method = "renderLevel", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/LevelRenderer;renderChunkLayer(Lnet/minecraft/client/renderer/RenderType;Lcom/mojang/blaze3d/vertex/PoseStack;DDDLorg/joml/Matrix4f;)V",
            ordinal = 2, shift = At.Shift.AFTER))
    private void superbwarfare$afterCutoutBlocks(PoseStack poseStack, float partialTick, long finishTimeNano,
                                               boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer,
                                               LightTexture lightTexture, Matrix4f projectionMatrix,
                                               CallbackInfo ci, @Local Frustum frustum) {
        RenderLevelStageCallback.EVENT.invoker().onRenderLevelStage(new RenderLevelStageCallback.Context(
                RenderLevelStageCallback.Context.Stage.AFTER_CUTOUT_BLOCKS, (LevelRenderer) (Object) this,
                poseStack, projectionMatrix, ticks, partialTick, camera, frustum));
    }

    @Inject(method = "renderLevel", at = @At(value = "CONSTANT", args = "stringValue=destroyProgress"))
    private void superbwarfare$afterBlockEntities(PoseStack poseStack, float partialTick, long finishTimeNano,
                                               boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer,
                                               LightTexture lightTexture, Matrix4f projectionMatrix,
                                               CallbackInfo ci, @Local Frustum frustum) {
        RenderLevelStageCallback.EVENT.invoker().onRenderLevelStage(new RenderLevelStageCallback.Context(
                RenderLevelStageCallback.Context.Stage.AFTER_BLOCK_ENTITIES, (LevelRenderer) (Object) this,
                poseStack, projectionMatrix, ticks, partialTick, camera, frustum));
    }

    @Inject(method = "renderLevel", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/particle/ParticleEngine;render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/client/Camera;F)V",
            shift = At.Shift.AFTER))
    private void superbwarfare$afterParticles(PoseStack poseStack, float partialTick, long finishTimeNano,
                                            boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer,
                                            LightTexture lightTexture, Matrix4f projectionMatrix,
                                            CallbackInfo ci, @Local Frustum frustum) {
        RenderLevelStageCallback.EVENT.invoker().onRenderLevelStage(new RenderLevelStageCallback.Context(
                RenderLevelStageCallback.Context.Stage.AFTER_PARTICLES, (LevelRenderer) (Object) this,
                poseStack, projectionMatrix, ticks, partialTick, camera, frustum));
    }

    // 感谢 Minecraft-Ping-Wheel 开源
    // https://github.com/LukenSkyne/Minecraft-Ping-Wheel/blob/138295954dab9d2451ad19e16d8d413ef018a2d8/fabric/src/main/java/nx/pingwheel/fabric/mixin/LevelRendererMixin.java
    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;applyModelViewMatrix()V", ordinal = 1, shift = At.Shift.AFTER))
    private void onStartRenderLevel(PoseStack $$0, float tickDelta, long $$2, boolean $$3, Camera $$4, GameRenderer $$5, LightTexture $$6, Matrix4f $$7, CallbackInfo ci) {
        ClientEventHandler.modelViewMatrix = RenderSystem.getModelViewMatrix();
        ClientEventHandler.projectionMatrix = RenderSystem.getProjectionMatrix();
    }

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

    @WrapOperation(method = "renderEntity(Lnet/minecraft/world/entity/Entity;DDDFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;render(Lnet/minecraft/world/entity/Entity;DDDFFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"))
    private void superbWarfare$captureThermalEntity(EntityRenderDispatcher dispatcher, Entity entity,
                                                    double x, double y, double z, float yaw, float partialTick,
                                                    PoseStack poses, MultiBufferSource source, int light,
                                                    Operation<Void> original) {
        original.call(dispatcher, entity, x, y, z, yaw, partialTick, poses,
                ScopeThermalRenderer.capture(entity, source), light);
    }
}
