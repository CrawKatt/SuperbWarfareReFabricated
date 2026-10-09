package com.atsuishio.superbwarfare.client.shader

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.mobeffect.RadiationMobEffect
import com.atsuishio.superbwarfare.tools.clientLevel
import com.atsuishio.superbwarfare.tools.localPlayer
import com.atsuishio.superbwarfare.tools.mc
import com.mojang.blaze3d.systems.RenderSystem
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.client.CameraType
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
        private var activeDose = 0f
        private var lastWidth = 0
        private var lastHeight = 0

        @JvmStatic
        fun onRegisterReloadListeners() {
            ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(listener)
        }

        @JvmStatic
        fun setDosage(dose: Float) {
            val newDose = dose.coerceAtLeast(0f)
            if (activeDose != newDose) {
                activeDose = newDose
                if (newDose <= 0f) cleanup()
            }
        }

        @JvmStatic
        fun getStrength(): Float {
            return RadiationMobEffect.getSaturation(activeDose)
        }

        @JvmStatic
        fun render(event: WorldRenderContext) {
            if (activeDose <= 0f || localPlayer  == null || clientLevel == null ||
                mc.options.cameraType != CameraType.FIRST_PERSON ||
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
