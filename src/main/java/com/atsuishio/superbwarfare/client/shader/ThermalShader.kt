package com.atsuishio.superbwarfare.client.shader

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.accessor.RenderTargetStencilAccessor
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
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity

/**
 * Code based on YWZJ Team
 */
class ThermalShaderHandler : SimpleSynchronousResourceReloadListener {
    override fun getFabricId(): ResourceLocation {
        return loc("thermal_shader_handler")
    }

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
                val partialTick = context.tickCounter().getGameTimeDeltaPartialTick(true)
                RenderSystem.setShaderGameTime(0, partialTick)

                if (!isActive) return@register

                val poseStack = context.matrixStack() ?: return@register
                prepareAndRenderEntities(poseStack, partialTick)
            }

            WorldRenderEvents.LAST.register { context ->
                val partialTick = context.tickCounter().getGameTimeDeltaPartialTick(true)
                RenderSystem.setShaderGameTime(0, partialTick)

                if (!isActive) return@register

                applyPostProcess(partialTick)
            }
        }

        fun setSeeThroughWalls(seeThrough: Boolean) {
            seeThroughWalls = seeThrough
        }

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
            thermalBuffer.setClearColor(0.0f, 0.0f, 0.0f, 0.0f)
            thermalBuffer.clear(Minecraft.ON_OSX)

            if (!seeThroughWalls) {
                val mainStencil = (mc.mainRenderTarget as RenderTargetStencilAccessor).`superbwarfare$isStencilEnabled`()
                val thermalStencil = (thermalBuffer as RenderTargetStencilAccessor).`superbwarfare$isStencilEnabled`()

                if (mainStencil && !thermalStencil) {
                    (thermalBuffer as RenderTargetStencilAccessor).`superbwarfare$enableStencil`()
                }

                try {
                    thermalBuffer.copyDepthFrom(mc.mainRenderTarget)
                } catch (_: Throwable) {
                    seeThroughWalls = true
                }
            }

            thermalBuffer.bindWrite(true)

            val camera = mc.gameRenderer.mainCamera
            val cameraPos = camera.position

            poseStack.pushPose()
            val bufferSource = mc.renderBuffers().bufferSource()

            RenderSystem.enablePolygonOffset()
            RenderSystem.polygonOffset(-1.0f, -1.0f)
            mc.entityRenderDispatcher.setRenderShadow(false)

            for (entity in mc.level!!.entitiesForRendering()) {
                if (isHotEntity(entity)) {
                    val lerpX = Mth.lerp(partialTick.toDouble(), entity.xo, entity.x)
                    val lerpY = Mth.lerp(partialTick.toDouble(), entity.yo, entity.y)
                    val lerpZ = Mth.lerp(partialTick.toDouble(), entity.zo, entity.z)

                    mc.entityRenderDispatcher.render(
                        entity,
                        lerpX - cameraPos.x,
                        lerpY - cameraPos.y,
                        lerpZ - cameraPos.z,
                        entity.getViewYRot(partialTick),
                        partialTick,
                        poseStack,
                        bufferSource,
                        15728880
                    )
                }
            }

            bufferSource.endBatch()
            RenderSystem.disablePolygonOffset()
            poseStack.popPose()

            mc.mainRenderTarget.bindWrite(true)
        }

        private fun applyPostProcess(partialTick: Float) {
            if (thermalChain == null) return

            try {
                thermalChain!!.process(partialTick)
            } catch (e: Exception) {
                e.printStackTrace()
                cleanup()
            }

            Minecraft.getInstance().mainRenderTarget.bindWrite(true)
        }

        private fun isHotEntity(entity: Entity?): Boolean {
            return false
            //        return (entity != Minecraft.getInstance().player || !Minecraft.getInstance().options.getCameraType().isFirstPerson());
        }
    }
}