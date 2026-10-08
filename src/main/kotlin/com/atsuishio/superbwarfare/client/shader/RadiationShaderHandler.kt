package com.atsuishio.superbwarfare.client.shader

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.mojang.blaze3d.systems.RenderSystem
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.PostChain
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.ResourceManager

class RadiationShaderHandler : SimpleSynchronousResourceReloadListener {
    override fun getFabricId(): ResourceLocation = loc("radiation_shader_handler")

    override fun onResourceManagerReload(resourceManager: ResourceManager) {
        cleanup()
    }

    companion object {
        private val RADIATION_EFFECT = loc("shaders/post/radiation.json")
        private val listener = RadiationShaderHandler()
        private var radiationChain: PostChain? = null
        private var activeLevel = 0
        private var lastWidth = 0
        private var lastHeight = 0

        @JvmStatic
        fun onRegisterReloadListeners() {
            ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(listener)
        }

        @JvmStatic
        fun setLevel(level: Int) {
            val newLevel = level.coerceIn(0, 20)
            if (activeLevel != newLevel) {
                activeLevel = newLevel
                if (newLevel == 0) cleanup()
            }
        }

        @JvmStatic
        fun getStrength(): Float {
            if (activeLevel <= 0) return 0.0f
            val normalized = (activeLevel - 1) / 19.0f
            return 0.12f + 0.88f * normalized
        }

        @JvmStatic
        fun render(event: WorldRenderContext) {
            val mc = Minecraft.getInstance()
            if (activeLevel <= 0 || mc.player == null || mc.level == null ||
                mc.options.cameraType != net.minecraft.client.CameraType.FIRST_PERSON ||
                mc.gameRenderer.currentEffect() != null || ThermalShaderHandler.isActive()
            ) {
                return
            }

            RenderSystem.setShaderGameTime(0, event.tickDelta())
            if (!ensureChain(mc)) return
            try {
                radiationChain?.process(event.tickDelta())
            } catch (_: Exception) {
                cleanup()
            }
            mc.mainRenderTarget.bindWrite(true)
        }

        private fun ensureChain(mc: Minecraft): Boolean {
            if (radiationChain == null) {
                try {
                    radiationChain = PostChain(
                        mc.textureManager,
                        mc.resourceManager,
                        mc.mainRenderTarget,
                        RADIATION_EFFECT
                    )
                    radiationChain!!.resize(mc.window.width, mc.window.height)
                    lastWidth = mc.window.width
                    lastHeight = mc.window.height
                } catch (e: Exception) {
                    e.printStackTrace()
                    cleanup()
                    return false
                }
            }

            if (lastWidth != mc.window.width || lastHeight != mc.window.height) {
                lastWidth = mc.window.width
                lastHeight = mc.window.height
                radiationChain!!.resize(lastWidth, lastHeight)
            }
            return true
        }

        private fun cleanup() {
            radiationChain?.close()
            radiationChain = null
            lastWidth = 0
            lastHeight = 0
        }
    }
}
