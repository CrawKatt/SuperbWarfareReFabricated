package com.atsuishio.superbwarfare.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;

/**
 * 2D 纹理绘制与 GUI 辅助。
 *
 * <h2>blit 的唯一锚点是 {@link PoseStack}</h2>
 * GUI 里传 {@code guiGraphics.pose()}，自绘节点里直接用当前 pose（例如 AUI 的
 * {@code BodyRenderNodeProvider#drawContentOnly(PoseStack)}），不再需要 {@link GuiGraphics}，
 * 也不再需要 {@code BufferSource}。
 *
 * <h2>三种画法</h2>
 * <ul>
 *   <li>{@link #blit}：源矩形与目标矩形同尺寸，只需要给 (u, v) 偏移。绝大多数场合用它。</li>
 *   <li>{@link #blitRegion}：源矩形与目标矩形尺寸不同（拉伸 / 裁切）。</li>
 *   <li>{@code preciseBlit} / {@code preciseBlitWithColor}：旧的 {@link GuiGraphics} 适配器，
 *       已废弃，仅为一处不改就保留的老调用点存在。</li>
 * </ul>
 *
 * <p>颜色统一为 ARGB：{@code 0xAARRGGBB}，alpha 参与混合。旧 {@code preciseBlitWithColor}
 * 会把 alpha 强行改成 {@code 0xFF}（{@code color | 0xFF000000}），该行为只在那个废弃适配器里保留。
 *
 * <h2>跨版本</h2>
 * 本文件只有 {@link #emitQuad} 一个方法随版本不同（1.20 用 {@code Tesselator#getBuilder()} +
 * {@code vertex/uv/color/endVertex/end()}，1.21 用 {@code Tesselator#begin(mode, format)} +
 * {@code addVertex/setUv/setColor/buildOrThrow()}），其余部分与 1.20 分支（sbw）逐行一致，
 * 改动时请同步两边。
 */
public final class RenderHelper {
    private RenderHelper() {
    }

    // ==================================================================================
    //  新 API：与版本无关，只需要 PoseStack
    // ==================================================================================

    /**
     * 把 {@code texture} 上 (u, v) 起、与目标矩形同尺寸的一块画到 (x, y, width, height)。
     *
     * <pre>
     *   blit(pose, icon, x, y, 16, 16, 0, 0, 16, 16)        // 整张 16x16 图标，画在 (x, y)
     *   blit(pose, atlas, x, y, w, h, u, v, 512, 512)       // 图集里的一块，图集 512x512
     * </pre>
     *
     * @param pose          当前 PoseStack（GUI 里是 {@code guiGraphics.pose()}）
     * @param texture       纹理
     * @param x             目标左上角 X
     * @param y             目标左上角 Y
     * @param width         目标宽度，同时也是从纹理上截取出来的宽度
     * @param height        目标高度，同时也是从纹理上截取出来的高度
     * @param u             纹理内起点 X（单位为纹素）
     * @param v             纹理内起点 Y（单位为纹素）
     * @param textureWidth  整张纹理宽度，用于把 (u, v) 归一化成 UV
     * @param textureHeight 整张纹理高度
     */
    public static void blit(PoseStack pose, ResourceLocation texture,
                            float x, float y, float width, float height,
                            float u, float v, float textureWidth, float textureHeight) {
        blit(pose, texture, x, y, width, height, u, v, textureWidth, textureHeight, 0xFFFFFFFF);
    }

    /**
     * 同 {@link #blit(PoseStack, ResourceLocation, float, float, float, float, float, float, float, float)}，
     * 并乘上 ARGB 颜色。
     *
     * @param argb 顶点颜色 {@code 0xAARRGGBB}，会和纹理采样结果相乘；alpha 参与混合
     */
    public static void blit(PoseStack pose, ResourceLocation texture,
                            float x, float y, float width, float height,
                            float u, float v, float textureWidth, float textureHeight, int argb) {
        blitRegion(pose, texture, x, y, width, height, u, v, width, height, textureWidth, textureHeight, argb);
    }

    /**
     * 把纹理上的源矩形 (u, v, uWidth, vHeight) 拉伸/裁切到目标矩形 (x, y, width, height)。
     *
     * <p>只有源矩形和目标矩形尺寸不同时才需要它；同尺寸请用 {@link #blit}。
     *
     * <p>需要 z 偏移时改 pose：{@code pose.pushPose(); pose.translate(0, 0, z); ...; pose.popPose()}，
     * 顶点 z 会随之平移，效果等同于旧 API 的 {@code blitOffset}。
     *
     * @param argb 顶点颜色 {@code 0xAARRGGBB}，alpha 参与混合
     */
    public static void blitRegion(PoseStack pose, ResourceLocation texture,
                                  float x, float y, float width, float height,
                                  float u, float v, float uWidth, float vHeight,
                                  float textureWidth, float textureHeight, int argb) {
        blitRegionAt(pose, texture, x, y, width, height, u, v, uWidth, vHeight, textureWidth, textureHeight, 0f, argb);
    }

    private static void blitRegionAt(PoseStack pose, ResourceLocation texture,
                                     float x, float y, float width, float height,
                                     float u, float v, float uWidth, float vHeight,
                                     float textureWidth, float textureHeight,
                                     float z, int argb) {
        // 空矩形直接跳过：HUD 里大量 0 高度/0 宽度（进度条、淡出中）的调用不该产生顶点。
        if (width == 0f || height == 0f || uWidth == 0f || vHeight == 0f) return;
        emitQuad(
                pose.last().pose(), texture,
                x, y, x + width, y + height, z,
                u / textureWidth, v / textureHeight,
                (u + uWidth) / textureWidth, (v + vHeight) / textureHeight,
                argb
        );
    }

    // ==================================================================================
    //  旧 API：GuiGraphics 适配器（语义与旧实现一致，新代码不要用）
    // ==================================================================================

    /**
     * @deprecated 只给已有调用点用。新代码用
     * {@link #blit(PoseStack, ResourceLocation, float, float, float, float, float, float, float, float)}。
     */
    @Deprecated
    public static void preciseBlit(GuiGraphics gui, ResourceLocation atlasLocation,
                                   float x, float y, float uOffset, float vOffset,
                                   float uWidth, float vHeight) {
        preciseBlit(gui, atlasLocation, x, y, uOffset, vOffset, uWidth, vHeight, 256f, 256f);
    }

    /**
     * @deprecated 只给已有调用点用。
     */
    @Deprecated
    public static void preciseBlit(GuiGraphics gui, ResourceLocation atlasLocation,
                                   float x, float y, float uOffset, float vOffset,
                                   float uWidth, float vHeight,
                                   float textureWidth, float textureHeight) {
        blit(gui.pose(), atlasLocation, x, y, uWidth, vHeight, uOffset, vOffset, textureWidth, textureHeight);
    }

    /**
     * @deprecated 只给已有调用点用。
     */
    @Deprecated
    public static void preciseBlit(GuiGraphics gui, ResourceLocation atlasLocation,
                                   float x, float y, float blitOffset,
                                   float uOffset, float vOffset,
                                   float uWidth, float vHeight,
                                   float textureWidth, float textureHeight) {
        blitRegionAt(gui.pose(), atlasLocation, x, y, uWidth, vHeight, uOffset, vOffset, uWidth, vHeight,
                textureWidth, textureHeight, blitOffset, 0xFFFFFFFF);
    }

    /**
     * @deprecated 只给已有调用点用。
     */
    @Deprecated
    public static void preciseBlit(GuiGraphics gui, ResourceLocation atlasLocation,
                                   float x, float y, float width, float height,
                                   float uOffset, float vOffset,
                                   float uWidth, float vHeight,
                                   float textureWidth, float textureHeight) {
        blitRegion(gui.pose(), atlasLocation, x, y, width, height, uOffset, vOffset, uWidth, vHeight,
                textureWidth, textureHeight, 0xFFFFFFFF);
    }

    /**
     * @deprecated 只给已有调用点用。注意旧实现会把 alpha 强行改成 {@code 0xFF}。
     */
    @Deprecated
    public static void preciseBlitWithColor(GuiGraphics gui, ResourceLocation atlasLocation,
                                            float x, float y, float uOffset, float vOffset,
                                            float width, float height,
                                            float textureWidth, float textureHeight,
                                            int color) {
        preciseBlitWithColor(gui, atlasLocation, x, y, 0f, uOffset, vOffset, width, height,
                textureWidth, textureHeight, color);
    }

    /**
     * @deprecated 只给已有调用点用。注意旧实现会把 alpha 强行改成 {@code 0xFF}。
     */
    @Deprecated
    public static void preciseBlitWithColor(GuiGraphics gui, ResourceLocation atlasLocation,
                                            float x, float y, float blitOffset,
                                            float uOffset, float vOffset,
                                            float width, float height,
                                            float textureWidth, float textureHeight,
                                            int color) {
        blitRegionAt(gui.pose(), atlasLocation, x, y, width, height, uOffset, vOffset, width, height,
                textureWidth, textureHeight, blitOffset, color | 0xFF000000);
    }

    // ==================================================================================
    //  版本相关：1.21 的 Tesselator / VertexConsumer API
    //  1.20 分支（sbw）里这一段用 getBuilder()/vertex/uv/color/endVertex/end()，
    //  并且用 POSITION_COLOR_TEX + getPositionColorTexShader。其余部分两边一致。
    // ==================================================================================

    /**
     * 立即画一个 POSITION_TEX_COLOR 四边形。
     *
     * <p>这里只管顶点和着色器；混合函数由调用方负责（AUI 的 content node、GuiGraphics 的 HUD
     * 流程都会在调用前把 blend 打开并设好 blendFunc）。这里只保证 blend 是开着的，并且<b>绝不</b>
     * 在画完把它关掉 —— 旧 {@code innerBlit(color)} 结尾的 {@code disableBlend()} 会把同一批次里
     * 后续所有 untinted blit 的透明通道一起搞坏。
     */
    private static void emitQuad(Matrix4f matrix, ResourceLocation texture,
                                 float x1, float y1, float x2, float y2, float z,
                                 float minU, float minV, float maxU, float maxV,
                                 int argb) {
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.enableBlend();

        BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        buffer.addVertex(matrix, x1, y1, z).setUv(minU, minV).setColor(argb);
        buffer.addVertex(matrix, x1, y2, z).setUv(minU, maxV).setColor(argb);
        buffer.addVertex(matrix, x2, y2, z).setUv(maxU, maxV).setColor(argb);
        buffer.addVertex(matrix, x2, y1, z).setUv(maxU, minV).setColor(argb);
        BufferUploader.drawWithShader(buffer.buildOrThrow());
    }

    // ==================================================================================
    //  GUI 专用辅助（需要 GuiGraphics / BufferSource，与 blit 无关）
    // ==================================================================================

    /**
     * Fills a rectangle with the specified color and z-level using the given render type and coordinates as the boundaries.
     *
     * @param renderType the render type to use.
     * @param minX       the minimum x-coordinate of the rectangle.
     * @param minY       the minimum y-coordinate of the rectangle.
     * @param maxX       the maximum x-coordinate of the rectangle.
     * @param maxY       the maximum y-coordinate of the rectangle.
     * @param z          the z-level of the rectangle.
     * @param color      the color to fill the rectangle with.
     */
    public static void fill(GuiGraphics guiGraphics, RenderType renderType, float minX, float minY, float maxX, float maxY, float z, int color) {
        Matrix4f matrix4f = guiGraphics.pose().last().pose();
        if (minX < maxX) {
            float i = minX;
            minX = maxX;
            maxX = i;
        }

        if (minY < maxY) {
            float j = minY;
            minY = maxY;
            maxY = j;
        }

        VertexConsumer vertexconsumer = guiGraphics.bufferSource().getBuffer(renderType);
        vertexconsumer.addVertex(matrix4f, minX, minY, z).setColor(color);
        vertexconsumer.addVertex(matrix4f, minX, maxY, z).setColor(color);
        vertexconsumer.addVertex(matrix4f, maxX, maxY, z).setColor(color);
        vertexconsumer.addVertex(matrix4f, maxX, minY, z).setColor(color);

        guiGraphics.flush();
    }

    public static void renderScrollingString(GuiGraphics pGuiGraphics, Font pFont, Component pText, float scale, int pMinX, int pMinY, int pMaxX, int pMaxY, int pColor) {
        int width = pFont.width(pText);
        int borderWidth = pMaxX - pMinX;
        if (width > borderWidth) {
            int l = width - borderWidth;
            double rate = (double) Util.getMillis() / 1000;
            double d1 = Math.max((double) l * 0.5, 3);
            double d2 = Math.sin((Math.PI / 2) * Math.cos((Math.PI * 2) * rate / d1)) / 2 + 0.5;
            double d3 = Mth.lerp(d2, 0, l);
            pGuiGraphics.enableScissor((int) (pMinX * scale), (int) (pMinY * scale), (int) (pMaxX * scale), (int) (pMaxY * scale));
            pGuiGraphics.drawString(pFont, pText, pMinX - (int) d3, pMinY, pColor);
            pGuiGraphics.disableScissor();
        } else {
            pGuiGraphics.drawString(pFont, pText, pMinX, pMinY, pColor);
        }
    }

    public static void renderCenteredScrollingString(GuiGraphics pGuiGraphics, Font pFont, Component pText, float scale, int pMinX, int pMinY, int pMaxX, int pMaxY, int pColor) {
        int width = pFont.width(pText);
        int borderWidth = pMaxX - pMinX;
        if (width > borderWidth) {
            int l = width - borderWidth;
            double rate = (double) Util.getMillis() / 1000;
            double d1 = Math.max((double) l * 0.5, 3);
            double d2 = Math.sin((Math.PI / 2) * Math.cos((Math.PI * 2) * rate / d1)) / 2 + 0.5;
            double d3 = Mth.lerp(d2, 0, l);
            pGuiGraphics.enableScissor((int) (pMinX * scale), (int) (pMinY * scale), (int) (pMaxX * scale), (int) (pMaxY * scale));
            pGuiGraphics.drawString(pFont, pText, pMinX - (int) d3, pMinY, pColor);
            pGuiGraphics.disableScissor();
        } else {
            pGuiGraphics.drawCenteredString(pFont, pText, (pMinX + pMaxX) / 2, pMinY, pColor);
        }
    }

    /**
     * 渲染一个圆环
     *
     * @param guiGraphics     gui
     * @param centerX         渲染中心X坐标
     * @param centerY         渲染中心Y坐标
     * @param outerRadius     外环半径
     * @param innerRadius     内环半径
     * @param backgroundColor 背景颜色
     * @param progressColor   进度颜色
     * @param progress        进度
     * @param useRate         是否使用占据屏幕百分比形式的半径
     */
    public static void renderCircularRing(GuiGraphics guiGraphics, float centerX, float centerY, float outerRadius, float innerRadius, float[] backgroundColor, float[] progressColor, float progress, boolean useRate) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        poseStack.rotateAround(Axis.ZP.rotationDegrees(-90), centerX, centerY, 0);

        var window = Minecraft.getInstance().getWindow();
        float scale = useRate ? Math.min(window.getGuiScaledWidth(), window.getGuiScaledHeight()) : 1;

        // 绘制背景圆环
        drawCircularRing(guiGraphics, centerX, centerY, outerRadius * scale, innerRadius * scale, backgroundColor, 1.0f);

        // 绘制进度圆环
        drawCircularRing(guiGraphics, centerX, centerY, outerRadius * scale, innerRadius * scale, progressColor, progress);

        poseStack.popPose();
        RenderSystem.disableBlend();
    }

    public static void drawCircularRing(GuiGraphics guiGraphics, float centerX, float centerY, float outerRadius, float innerRadius, float[] color, float progressAngle) {
        BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);

        PoseStack.Pose pose = guiGraphics.pose().last();
        Matrix4f matrix = pose.pose();
        float angleStep = (float) (2 * Math.PI / 180);
        float maxAngle = (float) (2 * Math.PI * progressAngle);

        for (int i = 0; i <= 180 * progressAngle; i++) {
            float angle = i * angleStep;
            if (angle > maxAngle) {
                angle = maxAngle;
            }

            float cos = (float) Math.cos(angle);
            float sin = (float) Math.sin(angle);

            // 外圆点
            float outerX = centerX + outerRadius * cos;
            float outerY = centerY + outerRadius * sin;
            buffer.addVertex(matrix, outerX, outerY, 0)
                    .setColor(color[0], color[1], color[2], color[3]);

            // 内圆点
            float innerX = centerX + innerRadius * cos;
            float innerY = centerY + innerRadius * sin;
            buffer.addVertex(matrix, innerX, innerY, 0)
                    .setColor(color[0], color[1], color[2], color[3]);

            if (angle >= maxAngle) break;
        }

        BufferUploader.drawWithShader(buffer.buildOrThrow());
    }
}
