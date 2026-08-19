package com.atsuishio.superbwarfare.client.shader

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.tools.mc
import com.mojang.blaze3d.pipeline.RenderTarget
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.PoseStack
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.PostChain
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.ResourceManager

/**
 * Code based on YWZJ Team
 */
class ThermalShaderHandler : SimpleSynchronousResourceReloadListener {
    override fun getFabricId(): ResourceLocation = loc("thermal_shader_handler")

    override fun onResourceManagerReload(resourceManager: ResourceManager) {
        cleanup()
    }

    companion object {
        private val THERMAL_EFFECT = loc("shaders/post/thermal.json")
        private var isActive = false
        private var thermalChain: PostChain? = null
        private var lastWidth = 0
        private var lastHeight = 0
        private var seeThroughWalls = false

        @JvmStatic
        fun register() {
            ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(ThermalShaderHandler())

            WorldRenderEvents.AFTER_ENTITIES.register { context ->
                val partialTick = context.tickDelta()
                RenderSystem.setShaderGameTime(0, partialTick)
                if (isActive) prepareAndRenderEntities(context.matrixStack(), partialTick)
            }

            WorldRenderEvents.LAST.register { context ->
                val partialTick = context.tickDelta()
                RenderSystem.setShaderGameTime(0, partialTick)
                if (isActive) applyPostProcess(partialTick)
            }
        }

        @JvmStatic
        fun setSeeThroughWalls(seeThrough: Boolean) {
            seeThroughWalls = seeThrough
        }

        @JvmStatic
        fun setActive(active: Boolean) {
            if (isActive != active) {
                isActive = active
                if (!active) {
                    cleanup()
                }
            }
        }

        private fun cleanup() {
            if (thermalChain != null) {
                thermalChain!!.close()
                thermalChain = null
            }
        }

        @JvmStatic
        fun isActive(): Boolean {
            return isActive
        }

        private fun ensureChain(mc: Minecraft): Boolean {
            if (thermalChain == null) {
                try {
                    thermalChain = PostChain(
                        mc.textureManager,
                        mc.resourceManager,
                        mc.mainRenderTarget,
                        THERMAL_EFFECT
                    )
                    thermalChain!!.resize(mc.window.width, mc.window.height)
                    lastWidth = mc.window.width
                    lastHeight = mc.window.height
                } catch (e: Exception) {
                    e.printStackTrace()
                    isActive = false
                    return false
                }
            }

            if (lastWidth != mc.window.width || lastHeight != mc.window.height) {
                lastWidth = mc.window.width
                lastHeight = mc.window.height
                thermalChain!!.resize(lastWidth, lastHeight)
            }
            return true
        }

        private fun prepareAndRenderEntities(poseStack: PoseStack, partialTick: Float) {
            val mc = Minecraft.getInstance()
            if (mc.level == null) {
                return
            }

            if (!ensureChain(mc)) return

            val thermalBuffer: RenderTarget = thermalChain!!.getTempTarget("thermal_buffer") ?: return

            thermalBuffer.setClearColor(0.0f, 0.0f, 0.0f, 0.0f)
            thermalBuffer.clear(Minecraft.ON_OSX)
            if (!seeThroughWalls) {
                try {
                    thermalBuffer.copyDepthFrom(mc.mainRenderTarget)
                } catch (_: Throwable) {
                    seeThroughWalls = true
                }
            }
            thermalBuffer.bindWrite(true)

            poseStack.pushPose()
            val bufferSource = mc.renderBuffers().bufferSource()

            RenderSystem.enablePolygonOffset()
            RenderSystem.polygonOffset(-1.0f, -1.0f)
            mc.entityRenderDispatcher.setRenderShadow(false)

            bufferSource.endBatch()
            RenderSystem.disablePolygonOffset()
            poseStack.popPose()

            mc.mainRenderTarget.bindWrite(true)
        }

        private fun applyPostProcess(partialTick: Float) {
            if (thermalChain == null) return

            try {
                thermalChain!!.process(partialTick)
            } catch (_: Exception) {
                cleanup()
            }

            mc.mainRenderTarget.bindWrite(true)
        }
    }
}
