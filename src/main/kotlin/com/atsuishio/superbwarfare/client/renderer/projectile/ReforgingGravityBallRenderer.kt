package com.atsuishio.superbwarfare.client.renderer.projectile

import com.atsuishio.superbwarfare.Mod.Companion.LOGGER
import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.client.ClientRenderHandler
import com.atsuishio.superbwarfare.client.renderer.ModRenderTypes
import com.atsuishio.superbwarfare.client.shader.ThermalShaderHandler
import com.atsuishio.superbwarfare.compat.oculus.OculusCompat
import com.atsuishio.superbwarfare.entity.projectile.ReforgingGravityBallEntity
import com.mojang.blaze3d.pipeline.RenderTarget
import com.mojang.blaze3d.pipeline.TextureTarget
import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.*
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.ShaderInstance
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.world.phys.Vec3
import net.minecraftforge.client.event.RegisterShadersEvent
import net.minecraftforge.client.event.RenderLevelStageEvent
import org.joml.Matrix4f
import org.joml.Vector4f
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL30
import kotlin.math.*

class ReforgingGravityBallRenderer(context: EntityRendererProvider.Context) :
    EntityRenderer<ReforgingGravityBallEntity>(context) {

    override fun render(
        entity: ReforgingGravityBallEntity, entityYaw: Float, partialTicks: Float,
        pose: PoseStack, buffers: MultiBufferSource, packedLight: Int,
    ) {
        pose.pushPose()
        ClientRenderHandler.transformVirtualRenderPosition(pose, entity, partialTicks)
        val expansion = entity.expansionProgress(partialTicks).toDouble()
        val time = entity.level().gameTime - entity.anchorTime + partialTicks.toDouble()
        queueLensing(entity, pose.last().pose(), expansion)

        // The opaque center masks the far side of the disk; additive black would be invisible.
        pose.pushPose()
        pose.mulPose(entityRenderDispatcher.cameraOrientation())
        val corePose = Matrix4f(pose.last().pose()).rotateZ((time * 0.008).toFloat())
        val coreRadius = 0.055 + expansion * 0.22
        annulus(buffers.getBuffer(ModRenderTypes.SINGULARITY), corePose, 0.0, coreRadius,
            6, 0.005f, 0.003f, 0.001f, 1f)
        pose.popPose()

        val buffer = buffers.getBuffer(ModRenderTypes.REFORGING_GLOW)
        annulus(buffer, corePose, coreRadius, coreRadius * 1.08, 6, 1f, 0.97f, 0.82f, 1f)
        annulus(buffer, corePose, coreRadius * 1.08, coreRadius * 1.25, 6, 1f, 0.72f, 0.22f, 0.85f)
        annulus(buffer, corePose, coreRadius * 1.25, coreRadius * 1.4, 6, 1f, 0.65f, 0.15f, 0.08f)

        if (entity.anchored && expansion > 0.001) {
            val diskRadius = entity.attackRadius * expansion
            accretionDisk(buffer, pose.last().pose(), diskRadius, expansion, time)
            orbitingSpheres(buffer, pose.last().pose(), entity.orbitProgress(partialTicks).toDouble(), expansion, time)
        }
        pose.popPose()
        super.render(entity, entityYaw, partialTicks, pose, buffers, packedLight)
    }

    private fun accretionDisk(
        buffer: VertexConsumer, pose: Matrix4f, outerRadius: Double, expansion: Double, time: Double,
    ) {
        if (outerRadius <= 0.0) return
        // All rings stay in the world XZ plane, independent of projectile and camera rotations.
        repeat(18) { ring ->
            val fraction = ring / 17.0
            val radius = outerRadius * (0.12 + 0.88 * fraction.pow(1.6))
            val rotation = time * (0.012 + (1.0 - fraction) * 0.045) + ring * 2.39996
            repeat(64) { segment ->
                val angle = segment * PI / 32
                val nextAngle = (segment + 1) * PI / 32
                val wave = 0.5 + 0.5 * sin(angle * 2.0 + rotation)
                val alpha = ((0.06 + 0.3 * wave.pow(3)) * (1.0 - fraction * 0.65) * expansion).toFloat()
                goldBeam(buffer, pose, diskPoint(radius, angle, rotation),
                    diskPoint(radius, nextAngle, rotation), (0.004 + wave * 0.004) * expansion, alpha)
            }
        }
        repeat(22) { index ->
            // Each mote spirals inward, then respawns at the edge.
            val travel = (time / (48.0 + index % 5 * 7) + index * 0.618034) % 1.0
            val radius = outerRadius * (1.0 - travel).pow(0.8)
            val angle = index * 2.39996 + travel * PI * 3 + time * 0.012
            val point = Vec3(cos(angle) * radius, sin(angle * 2 + index) * 0.025 * expansion, sin(angle) * radius)
            val fade = (sin(PI * travel) * expansion).toFloat()
            sphere(buffer, pose, point, (0.009 + index % 3 * 0.004) * expansion,
                1f, 0.82f, 0.38f, fade * 0.7f)
        }
    }

    private fun diskPoint(radius: Double, angle: Double, rotation: Double): Vec3 =
        Vec3(cos(angle + rotation) * radius, 0.0, sin(angle + rotation) * radius)

    private fun orbitingSpheres(
        buffer: VertexConsumer, pose: Matrix4f, progress: Double, expansion: Double, time: Double,
    ) {
        if (progress >= 1.0) return
        // The envelope reaches exactly zero at collapseStartTime, before the final implosion.
        val radius = 1.65 * (1.0 - progress).pow(0.7) * expansion
        val visibility = ((1.0 - progress) * 24.0).coerceIn(0.0, 1.0)
        val points = orbitPoints(time, radius)
        val tails = Array(9) { orbitPoints(time - (it + 1) * 0.7, radius) }
        for (index in points.indices) {
            goldBeam(buffer, pose, points[index], points[(index + 1) % 3], 0.008 * expansion,
                (0.65 * visibility).toFloat())
            var previous = points[index]
            for (tail in tails.indices) {
                val point = tails[tail][index]
                goldBeam(buffer, pose, previous, point, 0.012 * expansion,
                    ((1.0 - (tail + 1) / 10.0) * visibility * 0.3).toFloat())
                previous = point
            }
            val size = 0.07 * expansion * visibility
            sphere(buffer, pose, points[index], size * 1.6, 1f, 0.65f, 0.18f, 0.1f)
            sphere(buffer, pose, points[index], size, 1f, 0.8f, 0.35f, 0.9f)
        }
    }

    /** Equal radii and 120-degree spacing keep the rotating triangle equilateral as it contracts. */
    private fun orbitPoints(time: Double, radius: Double): Array<Vec3> {
        val rotation = time * 0.065 + 0.38 * sin(time * 0.043)
        return Array(3) { index ->
            val angle = rotation + index * PI * 2 / 3
            Vec3(cos(angle) * radius, 0.0, sin(angle) * radius)
        }
    }

    private fun goldBeam(
        buffer: VertexConsumer, pose: Matrix4f, start: Vec3, end: Vec3, width: Double, alpha: Float,
    ) = beam(buffer, pose, start, end, width, 1f, 0.77f, 0.3f, alpha)

    private fun annulus(
        buffer: VertexConsumer, pose: Matrix4f, inner: Double, outer: Double, sides: Int,
        red: Float, green: Float, blue: Float, alpha: Float,
    ) {
        repeat(sides) { index ->
            val angle = index * PI * 2 / sides
            val next = (index + 1) * PI * 2 / sides
            for ((radius, theta) in arrayOf(inner to angle, outer to angle, outer to next, inner to next)) {
                buffer.vertex(pose, (cos(theta) * radius).toFloat(), (sin(theta) * radius).toFloat(), 0f)
                    .color(red, green, blue, alpha).endVertex()
            }
        }
    }

    private fun beam(
        buffer: VertexConsumer, pose: Matrix4f, start: Vec3, end: Vec3,
        width: Double, red: Float, green: Float, blue: Float, alpha: Float,
    ) {
        if (alpha <= 0f || width <= 0.0 || start.distanceToSqr(end) < 1.0E-10) return
        val direction = end.subtract(start).normalize()
        val reference = if (abs(direction.y) > 0.9) Vec3(1.0, 0.0, 0.0) else Vec3(0.0, 1.0, 0.0)
        val side = direction.cross(reference).normalize().scale(width)
        val up = direction.cross(side).normalize().scale(width)
        val offsets = arrayOf(side.add(up), side.subtract(up), side.scale(-1.0).subtract(up), up.subtract(side))
        for (i in offsets.indices) {
            val next = (i + 1) % offsets.size
            vertex(buffer, pose, start.add(offsets[i]), red, green, blue, alpha)
            vertex(buffer, pose, end.add(offsets[i]), red, green, blue, alpha)
            vertex(buffer, pose, end.add(offsets[next]), red, green, blue, alpha)
            vertex(buffer, pose, start.add(offsets[next]), red, green, blue, alpha)
        }
    }

    // Cache unit sphere vertices instead of rebuilding the trigonometry every frame.
    private val sphere = buildList {
        fun point(latitude: Int, longitude: Int): Vec3 {
            val pitch = PI * latitude / 6
            val yaw = PI * longitude / 6
            return Vec3(sin(pitch) * cos(yaw), cos(pitch), sin(pitch) * sin(yaw))
        }
        for (latitude in 0 until 6) for (longitude in 0 until 12) {
            add(point(latitude, longitude))
            add(point(latitude, longitude + 1))
            add(point(latitude + 1, longitude + 1))
            add(point(latitude + 1, longitude))
        }
    }

    private fun sphere(
        buffer: VertexConsumer, pose: Matrix4f, center: Vec3, radius: Double,
        red: Float, green: Float, blue: Float, alpha: Float,
    ) {
        if (radius <= 0.0001 || alpha <= 0f) return
        for (point in sphere) vertex(buffer, pose, center.add(point.scale(radius)), red, green, blue, alpha)
    }

    private fun vertex(
        buffer: VertexConsumer, pose: Matrix4f, point: Vec3,
        red: Float, green: Float, blue: Float, alpha: Float,
    ) {
        buffer.vertex(pose, point.x.toFloat(), point.y.toFloat(), point.z.toFloat())
            .color(red, green, blue, alpha).endVertex()
    }

    override fun getTextureLocation(entity: ReforgingGravityBallEntity) = TEXTURE

    companion object {
        private val TEXTURE = loc("textures/bedrock/projectile/reforging_gravity_ball.png")
        private const val MAX_LENSES = 8
        private var lensShader: ShaderInstance? = null
        private var lensScene: RenderTarget? = null
        private var lensFailed = false
        private var canLensThisFrame = false
        private val lenses = linkedMapOf<Int, Vector4f>()
        private val lensStrengths = hashMapOf<Int, Float>()

        fun registerLensingShader(event: RegisterShadersEvent) {
            event.registerShader(
                ShaderInstance(event.resourceProvider, loc("reforging_gravity_lens"), DefaultVertexFormat.POSITION)
            ) {
                lensShader = it
                lensFailed = false
                clearLensing()
            }
        }

        fun clearLensing() {
            lenses.clear()
            lensStrengths.clear()
            canLensThisFrame = false
            lensScene?.destroyBuffers()
            lensScene = null
        }

        fun renderLensing(event: RenderLevelStageEvent) {
            if (event.stage === RenderLevelStageEvent.Stage.AFTER_SKY) {
                lenses.clear()
                lensStrengths.clear()
                canLensThisFrame = lensShader != null && !lensFailed &&
                    !OculusCompat.isShaderPackActive() && !OculusCompat.isRenderingShadowPass() &&
                    !ThermalShaderHandler.isActive()
                if (!canLensThisFrame && lensScene != null) clearLensing()
            } else if (event.stage === RenderLevelStageEvent.Stage.AFTER_LEVEL) {
                try {
                    if (canLensThisFrame && lenses.isNotEmpty()) applyLensing(event.projectionMatrix, event.partialTick)
                } finally {
                    lenses.clear()
                    lensStrengths.clear()
                    canLensThisFrame = false
                }
            }
        }

        private fun queueLensing(entity: ReforgingGravityBallEntity, pose: Matrix4f, expansion: Double) {
            if (!canLensThisFrame || !entity.anchored || expansion < 0.025) return
            // Capture the actual view-space render position, including the virtual muzzle offset.
            val center = pose.transform(Vector4f(0f, 0f, 0f, 1f))
            val radius = (entity.attackRadius * 0.3 * expansion).toFloat()
            if (radius <= 0f || center.z >= -radius * 0.25f) return
            val projection = RenderSystem.getProjectionMatrix()
            val clip = projection.transform(Vector4f(center))
            val extent = radius * maxOf(abs(projection.m00()), abs(projection.m11()))
            if (clip.w <= 0f || abs(clip.x) > clip.w + extent || abs(clip.y) > clip.w + extent) return
            val mc = Minecraft.getInstance()
            if (radius / -center.z * abs(projection.m11()) * mc.mainRenderTarget.height < 1.5f) return
            if (!lenses.containsKey(entity.id) && lenses.size >= MAX_LENSES) {
                val farthest = lenses.minBy { it.value.z }
                if (center.z <= farthest.value.z) return
                lenses.remove(farthest.key)
                lensStrengths.remove(farthest.key)
            }
            val nearFade = ((-center.z - radius * 0.25f) / (radius * 0.75f)).coerceIn(0f, 1f)
            val collapseBoost = if (entity.collapsing) 1f + sin(PI * (1.0 - expansion)).toFloat() * 0.6f else 1f
            lenses[entity.id] = center.apply { w = radius }
            lensStrengths[entity.id] = 0.2f * expansion.toFloat() * nearFade * collapseBoost
        }

        private fun applyLensing(projection: Matrix4f, partialTick: Float) {
            val mc = Minecraft.getInstance()
            val main = mc.mainRenderTarget
            val shader = lensShader ?: return
            if (main.width <= 0 || main.height <= 0 || main.depthTextureId < 0) return
            val previousShader = RenderSystem.getShader()
            val depthTest = GL11.glIsEnabled(GL11.GL_DEPTH_TEST)
            val depthWrite = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK)
            val blending = GL11.glIsEnabled(GL11.GL_BLEND)
            val culling = GL11.glIsEnabled(GL11.GL_CULL_FACE)
            val activeTexture = GL11.glGetInteger(GL13.GL_ACTIVE_TEXTURE)
            try {
                var scene = lensScene
                if (scene == null || scene.width != main.width || scene.height != main.height ||
                    scene.isStencilEnabled != main.isStencilEnabled
                ) {
                    scene?.destroyBuffers()
                    scene = TextureTarget(main.width, main.height, true, Minecraft.ON_OSX)
                    lensScene = scene
                    // Depth blits require matching formats, including the scope's stencil attachment.
                    if (main.isStencilEnabled) scene.enableStencil()
                    scene.setFilterMode(GL11.GL_LINEAR)
                }
                // Copy once for all lenses; never sample from an attachment being written to.
                GlStateManager._glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, main.frameBufferId)
                GlStateManager._glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, scene.frameBufferId)
                GL30.glBlitFramebuffer(0, 0, main.width, main.height, 0, 0, main.width, main.height,
                    GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT, GL11.GL_NEAREST)
                main.bindWrite(true)

                shader.setSampler("SceneColor", scene.colorTextureId)
                shader.setSampler("SceneDepth", scene.depthTextureId)
                shader.safeGetUniform("LensProjection").set(projection)
                shader.safeGetUniform("InverseProjection").set(Matrix4f(projection).invert())
                shader.safeGetUniform("LensCount").set(lenses.size)
                shader.safeGetUniform("LensTime").set(((mc.level!!.gameTime % 24000) + partialTick) * 0.05f)
                val strengths = FloatArray(MAX_LENSES)
                for ((index, entry) in lenses.entries.withIndex()) {
                    val center = entry.value
                    shader.safeGetUniform("Lens$index").set(center.x, center.y, center.z, center.w)
                    strengths[index] = lensStrengths[entry.key] ?: 0f
                }
                shader.safeGetUniform("LensStrengths0").set(strengths[0], strengths[1], strengths[2], strengths[3])
                shader.safeGetUniform("LensStrengths1").set(strengths[4], strengths[5], strengths[6], strengths[7])

                RenderSystem.disableDepthTest()
                RenderSystem.depthMask(false)
                RenderSystem.disableBlend()
                RenderSystem.disableCull()
                RenderSystem.setShader { shader }
                val builder = Tesselator.getInstance().builder
                builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION)
                builder.vertex(-1.0, -1.0, 0.0).endVertex()
                builder.vertex(1.0, -1.0, 0.0).endVertex()
                builder.vertex(1.0, 1.0, 0.0).endVertex()
                builder.vertex(-1.0, 1.0, 0.0).endVertex()
                BufferUploader.drawWithShader(builder.end())
            } catch (exception: Exception) {
                lensFailed = true
                clearLensing()
                LOGGER.warn("Gravity ball lensing disabled until the next resource reload", exception)
            } finally {
                main.bindWrite(true)
                RenderSystem.depthMask(depthWrite)
                if (depthTest) RenderSystem.enableDepthTest() else RenderSystem.disableDepthTest()
                if (blending) RenderSystem.enableBlend() else RenderSystem.disableBlend()
                if (culling) RenderSystem.enableCull() else RenderSystem.disableCull()
                RenderSystem.activeTexture(activeTexture)
                RenderSystem.setShader { previousShader }
            }
        }
    }
}
