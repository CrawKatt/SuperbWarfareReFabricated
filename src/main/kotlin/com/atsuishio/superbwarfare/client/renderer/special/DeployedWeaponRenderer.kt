package com.atsuishio.superbwarfare.client.renderer.special

import com.atsuishio.superbwarfare.client.renderer.gun.GunMountContext
import com.atsuishio.superbwarfare.client.renderer.gun.GunRenderContext
import com.atsuishio.superbwarfare.event.DeployedWeaponHandler
import com.atsuishio.superbwarfare.item.gun.GunItem
import com.atsuishio.superbwarfare.tools.mc
import com.mojang.math.Axis
import net.minecraft.client.renderer.LevelRenderer
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.world.item.ItemDisplayContext
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents

@Environment(EnvType.CLIENT)
object DeployedWeaponRenderer {
    fun register() {
        WorldRenderEvents.AFTER_ENTITIES.register(::onRenderLevelStage)
    }

    fun onRenderLevelStage(event: WorldRenderContext) {

        val level = mc.level ?: return
        val camera = event.camera().position
        val poseStack = event.matrixStack()
        val bufferSource = mc.renderBuffers().bufferSource()
        val partialTick = event.tickDelta()

        for (player in level.players()) {
            val tripod = DeployedWeaponHandler.tripodOf(player) ?: continue
            if (player === mc.player && mc.options.cameraType.isFirstPerson) continue

            val stack = player.mainHandItem
            if (!GunItem.isHeldWeapon(stack)) continue

            val pivot = DeployedWeaponHandler.pivotAt(tripod)
            val light = LevelRenderer.getLightColor(level, tripod)

            poseStack.pushPose()
            poseStack.translate(pivot.x - camera.x, pivot.y - camera.y, pivot.z - camera.z)
            poseStack.mulPose(Axis.YP.rotationDegrees(180f - player.getViewYRot(partialTick)))
            poseStack.mulPose(Axis.XP.rotationDegrees(-player.getViewXRot(partialTick)))

            GunMountContext.enter()
            GunRenderContext.push(player, player.mainArm)
            try {
                mc.itemRenderer.renderStatic(
                    player,
                    stack,
                    ItemDisplayContext.FIXED,
                    false,
                    poseStack,
                    bufferSource,
                    level,
                    light,
                    OverlayTexture.NO_OVERLAY,
                    player.id
                )
            } finally {
                GunRenderContext.pop()
                GunMountContext.exit()
            }

            poseStack.popPose()
        }

        bufferSource.endBatch()
    }
}
