package com.atsuishio.superbwarfare.event.custom;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

@Environment(EnvType.CLIENT)
public interface RenderLevelStageCallback {
    ResourceLocation LOWEST_PHASE = new ResourceLocation("superbwarfare", "render_level_lowest");
    Event<RenderLevelStageCallback> EVENT = EventFactory.createWithPhases(
            RenderLevelStageCallback.class,
            callbacks -> event -> {
                for (RenderLevelStageCallback callback : callbacks) {
                    callback.onRenderLevelStage(event);
                }
            },
            Event.DEFAULT_PHASE, LOWEST_PHASE
    );

    void onRenderLevelStage(Context event);

    class Context {
        public enum Stage { AFTER_SKY, AFTER_CUTOUT_BLOCKS, AFTER_BLOCK_ENTITIES, AFTER_PARTICLES, AFTER_LEVEL }

        private final Stage stage;
        private final LevelRenderer levelRenderer;
        private final PoseStack poseStack;
        private final Matrix4f projectionMatrix;
        private final int renderTick;
        private final float partialTick;
        private final Camera camera;
        private final Frustum frustum;

        public Context(Stage stage, LevelRenderer levelRenderer, PoseStack poseStack, Matrix4f projectionMatrix,
                       int renderTick, float partialTick, Camera camera, Frustum frustum) {
            this.stage = stage;
            this.levelRenderer = levelRenderer;
            this.poseStack = poseStack;
            this.projectionMatrix = projectionMatrix;
            this.renderTick = renderTick;
            this.partialTick = partialTick;
            this.camera = camera;
            this.frustum = frustum;
        }

        public Stage getStage() {
            return stage;
        }

        public LevelRenderer getLevelRenderer() {
            return levelRenderer;
        }

        public PoseStack getPoseStack() {
            return poseStack;
        }

        public Matrix4f getProjectionMatrix() {
            return projectionMatrix;
        }

        public int getRenderTick() {
            return renderTick;
        }

        public float getPartialTick() {
            return partialTick;
        }

        public Camera getCamera() {
            return camera;
        }

        public Frustum getFrustum() {
            return frustum;
        }
    }
}
