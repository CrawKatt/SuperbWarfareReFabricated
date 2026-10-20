package com.atsuishio.superbwarfare.event.custom;

import com.atsuishio.superbwarfare.Mod;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

@Environment(EnvType.CLIENT)
@FunctionalInterface
public interface RenderLevelStageCallback {

    ResourceLocation LOWEST_PHASE = ResourceLocation.fromNamespaceAndPath(Mod.MODID, "render_level_lowest");

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

    final class Context {
        private final Stage stage;
        private final LevelRenderer levelRenderer;
        private final PoseStack poseStack;
        private final Matrix4f modelViewMatrix;
        private final Matrix4f projectionMatrix;
        private final int renderTick;
        private final DeltaTracker partialTick;
        private final Camera camera;
        private final Frustum frustum;

        public Context(Stage stage, LevelRenderer levelRenderer, @Nullable PoseStack poseStack,
                       Matrix4f modelViewMatrix, Matrix4f projectionMatrix, int renderTick,
                       DeltaTracker partialTick, Camera camera, Frustum frustum) {
            this.stage = stage;
            this.levelRenderer = levelRenderer;
            this.poseStack = poseStack != null ? poseStack : new PoseStack();
            this.modelViewMatrix = modelViewMatrix;
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

        public Matrix4f getModelViewMatrix() {
            return modelViewMatrix;
        }

        public Matrix4f getProjectionMatrix() {
            return projectionMatrix;
        }

        public int getRenderTick() {
            return renderTick;
        }

        public DeltaTracker getPartialTick() {
            return partialTick;
        }

        public Camera getCamera() {
            return camera;
        }

        public Frustum getFrustum() {
            return frustum;
        }

        public enum Stage {
            AFTER_SKY,
            AFTER_LEVEL
        }
    }
}
