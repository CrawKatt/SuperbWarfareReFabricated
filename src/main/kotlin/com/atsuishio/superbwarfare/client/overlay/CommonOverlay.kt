package com.atsuishio.superbwarfare.client.overlay

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.client.overlay.components.BaseComponent
import com.atsuishio.superbwarfare.tools.isNullOrSpector
import com.atsuishio.superbwarfare.tools.localPlayer
import com.atsuishio.superbwarfare.tools.options
import net.minecraft.client.Camera
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.world.phys.Vec3

class RenderContext(var guiGraphics: GuiGraphics, var partialTick: Float) {
    val screenWidth get() = guiGraphics.guiWidth()
    val screenHeight get() = guiGraphics.guiHeight()

    val w by ::screenWidth
    val h by ::screenHeight

    // Non-null local player, MUST BE USED AFTER NULL CHECK!
    val player get() = localPlayer!!

    val mc get() = com.atsuishio.superbwarfare.tools.mc

    val camera: Camera get() = mc.gameRenderer.mainCamera
    val cameraPos: Vec3 get() = camera.position

    val isFirstPerson get() = options.cameraType.isFirstPerson

    val deltaFrame by ::partialTick
}

abstract class CommonOverlay(id: String) {
    val ID = Mod.MODID + "_" + id

    val components = mutableListOf<BaseComponent>()

    fun registerComponents(vararg components: BaseComponent) {
        this.components.addAll(components)
    }

    open fun RenderContext.preRender() {}

    open fun RenderContext.render() {
        components.forEach {
            if (it.shouldRender()) {
                it.apply { renderComponent() }
            }
        }
    }

    open fun shouldRender() = !options.hideGui && !localPlayer.isNullOrSpector()

    private lateinit var context: RenderContext

    fun render(guiGraphics: GuiGraphics, partialTick: Float) {
        if (!shouldRender()) return

        if (!this::context.isInitialized) {
            context = RenderContext(guiGraphics, partialTick)
        } else {
            context.guiGraphics = guiGraphics
            context.partialTick = partialTick
        }

        with(context) {
            preRender()
            render()
        }
    }
}
