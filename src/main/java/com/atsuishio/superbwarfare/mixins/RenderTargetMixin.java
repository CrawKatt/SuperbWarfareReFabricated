package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.accessor.RenderTargetStencilAccessor;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL30;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderTarget.class)
public abstract class RenderTargetMixin implements RenderTargetStencilAccessor {

    @Shadow
    public int viewWidth;

    @Shadow
    public int viewHeight;

    @Shadow
    public int frameBufferId;

    @Shadow
    @Final
    public boolean useDepth;

    @Shadow
    public abstract void resize(int width, int height, boolean clearError);

    @Unique
    private boolean superbwarfare$stencilEnabled = false;

    @Override
    public boolean superbwarfare$isStencilEnabled() {
        return this.superbwarfare$stencilEnabled;
    }

    @Override
    public void superbwarfare$enableStencil() {
        if (!this.useDepth || this.superbwarfare$stencilEnabled) {
            return;
        }

        this.superbwarfare$stencilEnabled = true;
        this.resize(this.viewWidth, this.viewHeight, Minecraft.ON_OSX);
    }

    @Redirect(
            method = "createBuffers",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/platform/GlStateManager;_texImage2D(IIIIIIIILjava/nio/IntBuffer;)V"
            )
    )
    private void superbwarfare$useDepthStencilTexture(
            int target,
            int level,
            int internalFormat,
            int width,
            int height,
            int border,
            int format,
            int type,
            java.nio.IntBuffer pixels
    ) {
        if (this.superbwarfare$stencilEnabled) {
            GlStateManager._texImage2D(
                    target,
                    level,
                    GL30.GL_DEPTH32F_STENCIL8,
                    width,
                    height,
                    border,
                    GL30.GL_DEPTH_STENCIL,
                    GL30.GL_FLOAT_32_UNSIGNED_INT_24_8_REV,
                    pixels
            );
        } else {
            GlStateManager._texImage2D(
                    target,
                    level,
                    internalFormat,
                    width,
                    height,
                    border,
                    format,
                    type,
                    pixels
            );
        }
    }

    @Redirect(
            method = "createBuffers",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/platform/GlStateManager;_glFramebufferTexture2D(IIIII)V"
            )
    )
    private void superbwarfare$attachStencilIfEnabled(
            int target,
            int attachment,
            int textureTarget,
            int texture,
            int level
    ) {
        GlStateManager._glFramebufferTexture2D(target, attachment, textureTarget, texture, level);

        if (this.superbwarfare$stencilEnabled && attachment == GL30.GL_DEPTH_ATTACHMENT) {
            GlStateManager._glFramebufferTexture2D(
                    target,
                    GL30.GL_STENCIL_ATTACHMENT,
                    textureTarget,
                    texture,
                    level
            );
        }
    }
}
