package com.atsuishio.superbwarfare.client.renderer.scope

import com.atsuishio.superbwarfare.accessor.RenderTargetStencilAccessor
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.Minecraft
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import org.lwjgl.opengl.GL11

@Environment(EnvType.CLIENT)
object ScopeStencilRenderHelper {

    fun enableItemEntityStencilTest() {
        RenderSystem.assertOnRenderThread()
        (Minecraft.getInstance().mainRenderTarget as RenderTargetStencilAccessor).`superbwarfare$enableStencil`()
        GL11.glEnable(GL11.GL_STENCIL_TEST)
    }

    fun disableItemEntityStencilTest() {
        RenderSystem.assertOnRenderThread()
        GL11.glDisable(GL11.GL_STENCIL_TEST)
    }
}
